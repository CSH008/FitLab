package com.jq.app.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.FileOutputStream;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Administrator on 6/30/2016.
 */
public class MediaHelper extends AppCompatActivity{

    public static String LOG = "MediaHelper";
    Activity mContext;
 Bitmap bitmap;
     ImageView ImageViewHolder;

    private String type;
    public static final int IMAGE_PICK = 1;
    public static final int IMAGE_CAPTURE = 2;
    public static final int IMAGE_CROP = 3;
    public static final int REQUEST_TAKE_GALLERY_VIDEO = 4;
    public static final int REQUEST_VIDEO_CAPTURED = 5;
    public  static final int RequestPermissionCode  = 1 ;

    public MediaHelper(Activity context){
        mContext = context;
    }

    public MediaHelper(Activity context, String type){
        mContext = context;
        this.type = type;
    }

    public String filePath = null;
    public void showSelectImageDialog() {

        final MaterialDialog alert = new MaterialDialog(mContext).setTitle(
                "Select Image From ...");

        final ArrayAdapter<String> arrayAdapter
                = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_list_item_1);
        if(type!=null){
            if(type.equals("..image")) {
                arrayAdapter.add("..From Image Gallery");
                arrayAdapter.add("..Capture Image");
            }

        } else {
            arrayAdapter.add("....From Image Gallery");
            arrayAdapter.add("..Capture Image");
            arrayAdapter.add("..From Video Gallery");
            arrayAdapter.add("...Capture Video");
        }

        ListView listView = new ListView(mContext);
        listView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        float scale = mContext.getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (8 * scale + 0.5f);
        listView.setPadding(0, dpAsPixels, 0, dpAsPixels);
        listView.setDividerHeight(0);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                alert.dismiss();

                switch (position){
                    case 0:
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        mContext.startActivityForResult(intent, IMAGE_PICK);
                        break;

                    case 1:
						//try{
						 EnableRuntimePermissionToAccessCamera();
                        intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        File imageFile = new File(android.os.Environment.getExternalStorageDirectory(), "photo" + String.valueOf(System.currentTimeMillis()) + ".jpg");
                        filePath = imageFile.getAbsolutePath();
                       // intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
                        mContext.startActivityForResult(intent, 7);
						//}catch(IOException e){}
                        break;

                    case 2:

                        intent = new Intent();
                        intent.setType("video/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        mContext.startActivityForResult(Intent.createChooser(intent,"Select Video"), REQUEST_TAKE_GALLERY_VIDEO);
                        break;

                    case 3:
						//try{
						// intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
						  EnableRuntimePermissionToAccessCamera();
                        intent=new Intent("android.media.action.VIDEO_CAPTURE");
                      
						//intent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
                        intent.putExtra("android.intent.extra.durationLimit", 5);
                        mContext.startActivityForResult(intent, 7);
					//}catch(IOException e){}
                        break;
                }

            }
        });

        alert.setContentView(listView);

        alert.setPositiveButton("Cancel", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        alert.show();
    }


 public void EnableRuntimePermissionToAccessCamera()
 {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA))
        {

            // Printing toast message after enabling runtime permission.
            Toast.makeText(this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, RequestPermissionCode);
            Toast.makeText(this,"Request for Camera Permission----"+RequestPermissionCode, Toast.LENGTH_LONG).show();

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 7 && resultCode == RESULT_OK && data != null && data.getData() != null) {
/*
           Uri uri = data.getData();

 try {

                imageFromCamera(resultCode,  data);
               //  Adding captured image in bitmap.
             // bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                // adding captured image in imageview.
         //  ImageViewHolder.setImageBitmap(bitmap);
			   } catch (IOException e) {

                e.printStackTrace();

            }
            */


        }

    }

    public void imageFromGallery(int resultCode, Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = mContext.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);

        File file = new File(filePath);
        Uri uri = Uri.fromFile(file);
        performCrop(uri);

        cursor.close();
    }

    public void imageFromCamera(int resultCode, Intent data) {
        Bitmap bmp = BitmapFactory.decodeFile(filePath);
        File file = new File(filePath);
        Uri uri = Uri.fromFile(file);
        performCrop(uri);
    }

    private void performCrop(Uri picUri) {
        // start picker to get image for cropping and then use the image in cropping activity
        /*CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(mContext);*/

        // start cropping activity for pre-acquired image saved on the device
        CropImage.activity(picUri)
                .start(mContext);

        /*try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            mContext.startActivityForResult(cropIntent, IMAGE_CROP);
            int i = 1;
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }*/
    }

    public void saveBitmapUsingPath(String path, Bitmap bitmapImage) {

        File file = new File(path);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String saveToInternalStorageTemporarily(Bitmap bitmapImage) {

        ContextWrapper cw = new ContextWrapper(mContext);
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

        long time= System.currentTimeMillis();
        String path = time + ".png";
        File mypath = new File(directory, path);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mypath.getAbsolutePath();
    }

    @Override
	public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(MediaHelper.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(MediaHelper.this,"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

}
