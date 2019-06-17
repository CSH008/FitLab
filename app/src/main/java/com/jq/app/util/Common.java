package com.jq.app.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 6/17/2016.
 */
public class Common {
    public static String TAG = "Common";
    public static ProgressDialog progressDialog;

    public static void showToast(Context context, String text){
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static String getStringWithValueKey(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
        return pref.getString(key, "");
    }

    public static void saveStringWithKeyValue(Context context, String key, String value) {

        SharedPreferences pref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static int getIntWithValueKey(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
        return pref.getInt(key, 0);
    }

    public static void saveIntWithKeyValue(Context context, String key, int value) {

        SharedPreferences pref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static boolean getBooleanWithValueKey(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
        return pref.getBoolean(key, false);
    }

    public static void saveBooleanWithKeyValue(Context context, String key, boolean value) {

        SharedPreferences pref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void showProgressDialog(Context context, boolean flag) {

        if (flag) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();

        } else {
            if (progressDialog != null)
                progressDialog.dismiss();
        }
    }

    public static String getJSONStringWithKey(JSONObject obj, String key){
        String result;
        try{
            result = obj.getString(key);

        } catch (JSONException e){
            Log.e(TAG, e.toString());
            return "";
        }

        return result;
    }

    public static boolean getJSONBooleanWithKey(JSONObject obj, String key){
        boolean result = false;
        try{
            result = obj.getBoolean(key);

        } catch (JSONException e){
            Log.e(TAG, e.toString());
        }

        return result;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static JSONObject getJSONObjectWithKey(JSONObject obj, String key){
        JSONObject result;
        try{
            result = obj.getJSONObject(key);

        } catch (JSONException e){
            Log.e(TAG, e.toString());
            return null;
        }

        return result;
    }

    public static JSONArray getJSONArrayWithKey(JSONObject obj, String key){
        JSONArray result;
        try{
            result = obj.getJSONArray(key);

        } catch (JSONException e){
            Log.e(TAG, e.toString());
            return null;
        }

        return result;
    }

    public static int getDeviceWidth(Activity activity) {
        Point point;
        WindowManager wm;

        wm = activity.getWindowManager();
        point = new Point();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            wm.getDefaultDisplay().getSize(point);
            return point.x;
        } else {
            return wm.getDefaultDisplay().getWidth();
        }
    }

    public static int getDeviceHeight(Activity activity) {
        Point point;
        WindowManager wm;

        wm = activity.getWindowManager();
        point = new Point();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            wm.getDefaultDisplay().getSize(point);
            return point.y;
        } else {
            return wm.getDefaultDisplay().getHeight();
        }
    }

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            if(params.height>300){
                params.height = 300;
            }
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }
    }

    public static Date getDateFromString(String value, String format) {
        if(format==null) {
            format = "yyyy-MM-dd'T'HH:mm:ssZ";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(value);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertedDate;
    }

    public static String getStringFromDate(Date value, String format) {
        if(format==null) {
            format = "yyyy-MM-dd'T'HH:mm:ssZ";
        }
        DateFormat dateFormat = new SimpleDateFormat(format);
        String newDateString = dateFormat.format(value);
        return newDateString;
    }

    public static String getStringDate(Calendar calendar) {
        int month = calendar.get(Calendar.MONTH);
        String strMonth = month>8?""+(month+1):"0"+(month+1);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String strDay = day>9?""+day:"0"+day;
        return "" + strMonth + "/" + strDay + "/" + calendar.get(Calendar.YEAR);
    }

}
