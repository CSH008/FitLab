package com.jq.app.ui.base;

import android.app.Activity;
import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.jq.app.R;
import com.jq.app.ui.auth.BaseAuthActivity;
import com.jq.app.util.Common;
import com.jq.app.util.MediaHelper;
import com.jq.fitlab.chatsdk.BuildConfig;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenVideo;
import com.kbeanie.imagechooser.api.ChosenVideos;
import com.kbeanie.imagechooser.api.IntentUtils;
import com.kbeanie.imagechooser.api.VideoChooserListener;
import com.kbeanie.imagechooser.api.VideoChooserManager;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BaseMediaActivity extends BaseAuthActivity implements
        VideoChooserListener {

    public Context mContext;
    public static String TAG = "BaseMediaActivity";
    String mCurrentPhotoPath;
    public ImageView ivAttach;
    public String attachmentImage = "";
    static final int REQUEST_TAKE_PHOTO = 1;
    public static final int REQUEST_IMAGE = 100;
    public static final int REQUEST_PERMISSION = 200;
    private String imageFilePath = "";
    public LinearLayout attachmentVideoContainer;
    public View attachmentVideoLayout;
    public ImageView ivAttachmentVideoThumbnail;
    public String attachmentVideo = "";
    public String attachmentVideoThumbnail = "";
    public MediaHelper imageHelper;
    public static final int RequestPermissionCode = 1;

    private String AUTHORITY = BuildConfig.APPLICATION_ID+".fileprovider";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        mContext = this;
        imageHelper = new MediaHelper(this);
        checkForSharedVideo(getIntent());
    }

    private void checkForSharedVideo(Intent intent) {
        if (intent != null) {
            if (intent.getAction() != null && intent.getType() != null && intent.getExtras() != null) {
                VideoChooserManager m = new VideoChooserManager(this, ChooserType.REQUEST_PICK_VIDEO);
                m.setVideoChooserListener(this);
                m.submit(ChooserType.REQUEST_PICK_VIDEO, IntentUtils.getIntentForMultipleSelection(intent));

            }
        }
    }

    private VideoChooserManager videoChooserManager;
    private String videoFilePath;
    private int chooserType;
//    public void showSelectImageDialog(boolean showVideoPicker) {
//        final MaterialDialog alert = new MaterialDialog(mContext).setTitle(
//                "Select Image From ...");
//
//        final ArrayAdapter<String> arrayAdapter
//                = new ArrayAdapter<String>(mContext,
//                android.R.layout.simple_list_item_1);
//        arrayAdapter.add("From Image Gallery");
//        arrayAdapter.add("Capture Image");
//        if (showVideoPicker) {
//            arrayAdapter.add("From Video Gallery");
//            arrayAdapter.add("Capture Video");
//        }
//        ListView listView = new ListView(mContext);
//        listView.setLayoutParams(new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT));
//        float scale = mContext.getResources().getDisplayMetrics().density;
//        int dpAsPixels = (int) (8 * scale + 0.5f);
//        listView.setPadding(0, dpAsPixels, 0, dpAsPixels);
//        listView.setDividerHeight(0);
//        listView.setAdapter(arrayAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                alert.dismiss();
//                switch (position) {
//                    case 0:
//                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                        startActivityForResult(intent, MediaHelper.IMAGE_PICK);
//                        break;
//
//                    case 1:
//                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        File imageFile = new File(android.os.Environment.getExternalStorageDirectory(), "photo" + String.valueOf(System.currentTimeMillis()) + ".jpg");
//                        attachmentImageFilePath = imageFile.getAbsolutePath();
//                        imageHelper.filePath = attachmentImageFilePath;
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(BaseMediaActivity.this,
//                                BuildConfig.APPLICATION_ID + ".provider",
//                                imageFile));
//                        startActivityForResult(intent, MediaHelper.IMAGE_CAPTURE);
//                        break;
//
//                    case 2:
//                        pickVideo();
//                        break;
//
//                    case 3:
//                        captureVideo();
//                        break;
//                }
//            }
//        });
//
//        alert.setContentView(listView);
//
//        alert.setPositiveButton("Cancel", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alert.dismiss();
//            }
//        });
//
//        alert.show();
//    }

    int i=5;
    public void showSelectImageDialog(boolean showVideoPicker) {

        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_camera_sharing);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        final FrameLayout linear_image_gallery = (FrameLayout) dialog.findViewById(R.id.linear_image_gallery);
        final FrameLayout linear_capture_image = (FrameLayout) dialog.findViewById(R.id.linear_capture_image);
        final FrameLayout linear_video_gallery = (FrameLayout) dialog.findViewById(R.id.linear_video_gallery);
        final FrameLayout linear_capture_video = (FrameLayout) dialog.findViewById(R.id.linear_capture_video);
        TextView tv_ok = (TextView) dialog.findViewById(R.id.tv_ok);
        TextView tv_cancel = (TextView) dialog.findViewById(R.id.tv_cancel);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (i!=5){
                    selecterCase(0);
                }else {
                    Toast.makeText(mContext, "Please select any option.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        if (showVideoPicker) {
            linear_video_gallery.setVisibility(View.VISIBLE);
            linear_capture_video.setVisibility(View.VISIBLE);
        } else {
            linear_video_gallery.setVisibility(View.GONE);
            linear_capture_video.setVisibility(View.GONE);
        }

        linear_image_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = 0;
                linear_image_gallery.setForeground(mContext.getResources().getDrawable(R.drawable.video_image_selecter));
                linear_capture_image.setForeground(null);
                linear_video_gallery.setForeground(null);
                linear_capture_video.setForeground(null);
                selecterCase(0);
                dialog.dismiss();
            }
        });
        linear_capture_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = 1;

                linear_image_gallery.setForeground(null);
                linear_capture_image.setForeground(mContext.getResources().getDrawable(R.drawable.video_image_selecter));
                linear_video_gallery.setForeground(null);
                linear_capture_video.setForeground(null);
                selecterCase(1);
                dialog.dismiss();
            }
        });
        linear_video_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = 2;

                linear_image_gallery.setForeground(null);
                linear_capture_image.setForeground(null);
                linear_video_gallery.setForeground(mContext.getResources().getDrawable(R.drawable.video_image_selecter));
                linear_capture_video.setForeground(null);
                selecterCase(2);
                dialog.dismiss();
            }
        });

        linear_capture_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = 3;
                linear_image_gallery.setForeground(null);
                linear_capture_image.setForeground(null);
                linear_video_gallery.setForeground(null);
                linear_capture_video.setForeground(mContext.getResources().getDrawable(R.drawable.video_image_selecter));
                selecterCase(3);
                dialog.dismiss();
            }
        });
//        final MaterialDialog alert = new MaterialDialog(mContext).setTitle(
//                "Select Image From ...");
//
//        final ArrayAdapter<String> arrayAdapter
//                = new ArrayAdapter<String>(mContext,
//                android.R.layout.simple_list_item_1);
//        arrayAdapter.add("From Image Gallery");
//        arrayAdapter.add("Capture Image");
//        if (showVideoPicker) {
//            arrayAdapter.add("From Video Gallery");
//            arrayAdapter.add("Capture Video");
//        }
//
//        ListView listView = new ListView(mContext);
//        listView.setLayoutParams(new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT));
//        float scale = mContext.getResources().getDisplayMetrics().density;
//        int dpAsPixels = (int) (8 * scale + 0.5f);
//        listView.setPadding(0, dpAsPixels, 0, dpAsPixels);
//        listView.setDividerHeight(0);
//        listView.setAdapter(arrayAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                alert.dismiss();
//                switch (position) {
//                    case 0:
//                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                        startActivityForResult(intent, MediaHelper.IMAGE_PICK);
//                        break;
//
//                    case 1:
//                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        File imageFile = new File(android.os.Environment.getExternalStorageDirectory(), "photo" + String.valueOf(System.currentTimeMillis()) + ".jpg");
//                        attachmentImageFilePath = imageFile.getAbsolutePath();
//                        imageHelper.filePath = attachmentImageFilePath;
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(BaseMediaActivity.this,
//                                BuildConfig.APPLICATION_ID + ".provider",
//                                imageFile));
//                        startActivityForResult(intent, MediaHelper.IMAGE_CAPTURE);
//                        break;
//
//                    case 2:
//                        pickVideo();
//                        break;
//
//                    case 3:
//                        captureVideo();
//                        break;
//                }
//            }
//        });
//
//        alert.setContentView(listView);
//
//        alert.setPositiveButton("Cancel", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alert.dismiss();
//            }
//        });
//
//        alert.show();
    }

    public void selecterCase(int pos) {
        switch (pos) {
            case 0:
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, MediaHelper.IMAGE_PICK);
                break;
            case 1:

               dispatchTakePictureIntent();
//                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent, MediaHelper.IMAGE_CAPTURE);

               /* intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                File imageFile = new File(android.os.Environment.getExternalStorageDirectory(), "photo" + String.valueOf(System.currentTimeMillis()) + ".jpg");
                attachmentImageFilePath = imageFile.getAbsolutePath();
                imageHelper.filePath = attachmentImageFilePath;
                intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(BaseMediaActivity.this,
                        BuildConfig.APPLICATION_ID *//*+ ".provider"*//*,
                        imageFile));
                startActivityForResult(intent, MediaHelper.IMAGE_CAPTURE);*/
                break;
            case 2:
                pickVideo();
                break;
            case 3:
                captureVideo();
                break;
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public File getPrivateAlbumStorageDir(Context context, String albumName) {
        // Get the directory for the app's private pictures directory.
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e(TAG, "Directory not created");
        }
        return file;
    }

    public void EnableRuntimePermissionToAccessCamera() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            // Printing toast message after enabling runtime permission.
            Toast.makeText(this, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, RequestPermissionCode);
            Toast.makeText(this, "Request for Camera Permission" + RequestPermissionCode, Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Thanks for granting Permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String attachmentImageFilePath = "";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case MediaHelper.IMAGE_PICK:
                    imageHelper.imageFromGallery(resultCode, data);
                    break;
                case MediaHelper.IMAGE_CAPTURE:
                        imageHelper.imageFromCamera(resultCode, data);
                    break;

                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    if (data != null) {
                        CropImage.ActivityResult result = CropImage.getActivityResult(data);
                        if (resultCode == RESULT_OK) {
                            Uri resultUri = result.getUri();
                            updateImageViewWithUri(resultUri);
                        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                            Exception error = result.getError();
                        }

                        /*Bundle extras = data.getExtras();
                        Bitmap bmp = extras.getParcelable("data");
                        attachmentImageFilePath = imageHelper.saveToInternalStorageTemporarily(bmp);
                        if(attachmentImageFilePath==null) {
                            attachmentImageFilePath = "";
                        }
                        updateImageView(bmp);*/
                    }
                    break;

                case ChooserType.REQUEST_CAPTURE_VIDEO:
                    if (videoChooserManager == null) {
                        reinitializeVideoChooser();
                    }
                    videoChooserManager.submit(requestCode, data);
                    break;

                case ChooserType.REQUEST_PICK_VIDEO:
                    if (videoChooserManager == null) {
                        reinitializeVideoChooser();
                    }
                    videoChooserManager.submit(requestCode, data);
                    break;

                default:
                    break;
            }
        }
    }

    public String attachmentVideoThumbnailLocalPath = "";

    public void setVideo(final String filePath, String thumbnailPath) {
        if (filePath == null) {
            Common.showToast(mContext, "Can not import the video");
            return;
        }


        /*
        attachmentVideoContainer.removeAllViews();
        if(!filePath.contains("http")) {
            Bitmap thumb = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Video.Thumbnails.MINI_KIND);
            MediaHelper mediaHelper = new MediaHelper(BaseMediaActivity.this);
            attachmentVideoThumbnailLocalPath = mediaHelper.saveToInternalStorageTemporarily(thumb);
            if(attachmentVideoThumbnailLocalPath==null) {
                attachmentVideoThumbnailLocalPath = "";
            }
            ivAttachmentVideoThumbnail.setImageBitmap(thumb);
            ivAttachmentVideoThumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goVideoPage(filePath);
                }
            });
        } else {
            if(!thumbnailPath.isEmpty()) {
                Picasso.with(mContext).load(thumbnailPath).into(ivAttachmentVideoThumbnail);
            }

            ivAttachmentVideoThumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goVideoPage(filePath);
                }
            });
        }

        attachmentVideoContainer.addView(attachmentVideoLayout);
        */
    }

    public void goVideoPage(String filePath) {

    }

    public String getRealPathFromURI(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void updateImageViewWithUri(Uri uri) {
    }

    public void updateImageView(Bitmap bmp) {
        if (!attachmentImageFilePath.isEmpty() && ivAttach != null) {
            Picasso.with(mContext).load(new File(attachmentImageFilePath)).into(ivAttach);
        }
    }

    public void post() {
    }

    public void captureVideo() {
        try {
            chooserType = ChooserType.REQUEST_CAPTURE_VIDEO;
            videoChooserManager = new VideoChooserManager(this,
                    ChooserType.REQUEST_CAPTURE_VIDEO);
            videoChooserManager.setVideoChooserListener(this);

            videoFilePath = videoChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pickVideo() {
        chooserType = ChooserType.REQUEST_PICK_VIDEO;
        videoChooserManager = new VideoChooserManager(this,
                ChooserType.REQUEST_PICK_VIDEO);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Intent.EXTRA_ALLOW_MULTIPLE, true);
        videoChooserManager.setExtras(bundle);
        videoChooserManager.setVideoChooserListener(this);
        try {
            videoChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Uri selectedVideoUri;

    @Override
    public void onVideoChosen(final ChosenVideo video) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (video != null) {
                    setVideo(video
                            .getVideoFilePath(), "1.png");
                    selectedVideoUri = Uri.fromFile(new File(video.getVideoFilePath()));
                }
            }
        });
    }

    @Override
    public void onError(final String reason) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(mContext, reason,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onVideosChosen(final ChosenVideos videos) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(getClass().getName(), "run: Videos Chosen: " + videos.size());
                onVideoChosen(videos.getImage(0));
            }
        });
    }

    private void reinitializeVideoChooser() {
        videoChooserManager = new VideoChooserManager(this, chooserType, true);
        videoChooserManager.setVideoChooserListener(this);
        videoChooserManager.reinitialize(videoFilePath);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("chooser_type", chooserType);
        outState.putString("media_path", videoFilePath);
        outState.putString("image_path", attachmentImageFilePath);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("chooser_type")) {
                chooserType = savedInstanceState.getInt("chooser_type");
            }
            if (savedInstanceState.containsKey("media_path")) {
                videoFilePath = savedInstanceState.getString("media_path");
            }
            if (savedInstanceState.containsKey("image_path")) {
                attachmentImageFilePath = savedInstanceState.getString("image_path");
                if (!attachmentImageFilePath.isEmpty() && ivAttach != null) {
                    Picasso.with(mContext).load(new File(attachmentImageFilePath)).into(ivAttach);
                }
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void showMediaCategoryDialog() {
        new com.afollestad.materialdialogs.MaterialDialog.Builder(this)
                .title(R.string.media_category)
                .items(R.array.dot_categories)
                .itemsCallback(new com.afollestad.materialdialogs.MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(com.afollestad.materialdialogs.MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        chosenMediaCategory(which, text);
                    }
                })
                .show();
    }

    public void showDotsCategoryDialog(final String category, final int position, final String userStatusLevel) {
        new com.afollestad.materialdialogs.MaterialDialog.Builder(this)
                .title(R.string.dots_category)
                .items(R.array.dot_sub_categories)
                .itemsCallback(new com.afollestad.materialdialogs.MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(com.afollestad.materialdialogs.MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        chosenMediaCategory(position, category);
                        chosenBodyPartCategory(which, text,userStatusLevel);
                        dialog.dismiss();
                    }
                })
                .show();
    }



    public void chosenMediaCategory(int which, CharSequence text) {

    }

    public void chosenBodyPartCategory(int which, CharSequence text, String userStatusLevel) {

    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                attachmentImageFilePath = photoFile.getAbsolutePath();
                imageHelper.filePath = attachmentImageFilePath;
                Uri photoURI = FileProvider.getUriForFile(this,
                        AUTHORITY,
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, MediaHelper.IMAGE_CAPTURE);
            }
        }
    }
}
