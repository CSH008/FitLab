package com.jq.app.util;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.jq.app.data.local_helpers.LocalImageHelper;
import com.jq.app.data.local_helpers.LocalVideoHelper;
import com.jq.app.util.base.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 10/19/2017.
 */

public class UploadFileToServer extends AsyncTask<String, String, String> {

    private final BaseActivity activity;
    //    String FILE_UPLOAD_URL = "http://198.27.98.210:8080/1fitlab/jsp/video-upload.jsp";
    String FILE_UPLOAD_URL = Config.API_URL + "video-upload.jsp";
    //  String FILE_UPLOAD_URL = "http://173.8.145.131:8080/1fitlab/jsp/video-upload.jsp";
    String filePath;
    CompletionListener mListener;
    File sourceFile;
    int totalSize;
    String params;
    String type;

    public interface CompletionListener {
        void onCompleted(String message);

        void onFailed(String msg);
    }

    public UploadFileToServer(BaseActivity activity, String type, String filePath, String params, CompletionListener listener) {
        this.type = type;
        this.filePath = filePath;
        this.mListener = listener;
        this.params = params;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        // setting progress bar to zero
        sourceFile = new File(filePath);
        totalSize = (int) sourceFile.length();
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(String... progress) {
        Log.d("PROG", progress[0]);
        //((BaseActivity) mListener).setProgressDialogProgress(Integer.parseInt(progress[0]));
        activity.setProgressDialogProgress(Integer.parseInt(progress[0]));
    }

    @Override
    protected String doInBackground(String... args) {
        HttpURLConnection.setFollowRedirects(false);
        HttpURLConnection connection = null;
        String fileName = sourceFile.getName();

        try {
            connection = (HttpURLConnection) new URL(FILE_UPLOAD_URL + params).openConnection();
            connection.setRequestMethod("POST");
            Log.v("URL", connection.getURL().toString());
            String boundary = "---------------------------boundary";
            String tail = "\r\n--" + boundary + "--\r\n";
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            connection.setDoOutput(true);

            String metadataPart = "--" + boundary + "\r\n"
                    + "Content-Disposition: form-data; name=\"metadata\"\r\n\r\n"
                    + "" + "\r\n";

            String fileHeader1 = "--" + boundary + "\r\n"
                    + "Content-Disposition: form-data; name=\"fileToUpload\"; filename=\""
                    + fileName + "\"\r\n"
                    + "Content-Type: application/octet-stream\r\n"
                    + "Content-Transfer-Encoding: binary\r\n";

            long fileLength = sourceFile.length() + tail.length();
            String fileHeader2 = "Content-length: " + fileLength + "\r\n";
            String fileHeader = fileHeader1 + fileHeader2 + "\r\n";
            String stringData = metadataPart + fileHeader;

            long requestLength = stringData.length() + fileLength;
            connection.setRequestProperty("Content-length", "" + requestLength);
            connection.setFixedLengthStreamingMode((int) requestLength);
            connection.connect();

            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes(stringData);
            out.flush();

            int progress = 0;
            int bytesRead = 0;
            int maxBufferSize = 10 * 1024 * 1024;
            byte buf[] = new byte[maxBufferSize];
            BufferedInputStream bufInput = new BufferedInputStream(new FileInputStream(sourceFile));
            while ((bytesRead = bufInput.read(buf)) != -1) {
                // write output
                out.write(buf, 0, bytesRead);
                out.flush();
                progress += bytesRead; // Here progress is total uploaded bytes

                publishProgress("" + (int) ((progress * 100) / totalSize)); // sending progress percent to publishProgress
            }

            // Write closing boundary and close stream
            out.writeBytes(tail);
            out.flush();
            out.close();

            // Get server response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();

        } catch (Exception e) {
            // Exception
            e.printStackTrace();
        } finally {
            if (connection != null) connection.disconnect();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.e("Response", "Response from server: " + result);
        super.onPostExecute(result);
        if (result != null) {
            try {
                JSONObject response = new JSONObject(result);
                String status = response.getString("status");
                if (status.equals("1")) {
                    JSONObject data = response.getJSONObject("data");
                    if (type.equals("image")) {
                        LocalImageHelper.getInstance().createItem(filePath, data);

                    } else {
                        LocalVideoHelper.getInstance().createItem(filePath, data);
                    }
                } else {
                    String message = response.getString("message");
                    mListener.onFailed(message);
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                mListener.onFailed("Network error");
                return;
            }

            mListener.onCompleted("Successfully uploaded");
        } else {
            mListener.onFailed("Failure");
        }

    }

}
