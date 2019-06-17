package com.jq.app.ui.profile;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jq.app.R;
import com.jq.app.data.content_helpers.ProfileHelper;
import com.jq.app.ui.base.BaseMediaActivity;
import com.jq.chatsdk.Utils.helper.ChatSDKProfileHelper;
import com.jq.chatsdk.Utils.helper.ChatSDKUiHelper;
import com.jq.chatsdk.dao.BUser;
import com.jq.chatsdk.network.BDefines;
import com.jq.chatsdk.network.BNetworkManager;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;
import org.jdeferred.DoneCallback;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class EditProfileActivity extends BaseMediaActivity implements ProfileHelper.OnDataLoadListener {

    private CircleImageView profileImage;
    //private User currentUser;
    private TextView tvName, txtAge, txtWeight, txtHeight;

    BUser currentUser;
    private HashMap<String, Object> userMeta;
    ProfileHelper profileHelper;

    protected ChatSDKUiHelper chatSDKUiHelper;
    protected ChatSDKProfileHelper chatSDKProfileHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ActionBar ab = getSupportActionBar();
        if (ab!=null)
        {
            ab.setHomeButtonEnabled(true);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        currentUser = BNetworkManager.sharedManager().getNetworkAdapter().currentUserModel();

        profileImage = findViewById(R.id.profileImage);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectImageDialog(false);
            }
        });

        tvName = findViewById(R.id.txtName);
        txtAge = findViewById(R.id.txtAge);
        txtWeight = findViewById(R.id.txtWeight);
        txtHeight = findViewById(R.id.txtHeight);

        profileHelper = new ProfileHelper(this);
        txtAge.setText(profileHelper.age);
        txtWeight.setText(profileHelper.weight);
        txtHeight.setText(profileHelper.height);

        ProgressBar progressBar = findViewById(com.jq.chatsdk.R.id.chat_sdk_progressbar);
        chatSDKUiHelper = ChatSDKUiHelper.getInstance().get(this);
        chatSDKProfileHelper = new ChatSDKProfileHelper(this, profileImage, progressBar, chatSDKUiHelper, this.findViewById(android.R.id.content));
        chatSDKProfileHelper.loadProfilePic();
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_save:
                save();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(currentUser!=null) {
            tvName.setText(currentUser.getMetaName());
            userMeta = new HashMap<>(currentUser.metaMap());

        } else {
            logout();
        }
    }

    boolean imageChanged = false;
    Uri updatedImageUri;
    @Override
    public void updateImageViewWithUri(Uri uri) {
        try{
            //profileImage.setImageURI(uri);
            updatedImageUri = uri;
            chatSDKProfileHelper.setProfilePicFromPath(uri.getPath());
            imageChanged = true;
            //Picasso.with(this).load(new File(uri.getPath())).placeholder(R.drawable.icn_100_profile).into(profileImage);
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_profile, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }
        else if(item.getItemId() == R.id.action_save) {
            save();
        }
        return true;
    }

    public void save() {
        profileHelper.saveProfile(txtAge.getText().toString(), txtWeight.getText().toString(), txtHeight.getText().toString());

        if(!StringUtils.isEmpty(tvName.getText().toString())) {
            currentUser.setMetadataString(BDefines.Keys.BName, tvName.getText().toString());

        } else {
            showToast("Input username");
            return;
        }

        if(imageChanged) {
            showProgressDialog();
            Bitmap bitmap = ((BitmapDrawable) profileImage.getDrawable()).getBitmap();
            chatSDKProfileHelper.setProfilePic(bitmap);
            chatSDKProfileHelper.saveProfilePicToServer(updatedImageUri.getPath(), true)
                    .done(new DoneCallback<String[]>() {
                        @Override
                        public void onDone(String[] strings) {
                            // Resetting the selected path when done saving the image
                            hideProgressDialog();
                        }
                    });
        }
    }

    private boolean valueChanged (Map<String, Object> h1, Map<String, Object> h2, String key) {
        Object o1 = h1.get(key);
        Object o2 = h2.get(key);
        if (o1 == null) {
            if (o2 != null) {
                return true;
            } else {
                return false;
            }
        } else {
            return !o1.equals(o2);
        }
    }

    @Override
    public void onFinishedAction(int action, int index, int errMsg) {
        if(errMsg>0) {
            onFinishedAction(action, index, getString(errMsg));
        } else {
            onFinishedAction(action, index, "");
        }
    }

    @Override
    public void onFinishedAction(int action, int index, String errMsg) {
        hideProgressDialog();
        if(errMsg!=null && !errMsg.isEmpty()) {
            showToast(errMsg);
        }

        switch (action) {
            case ProfileHelper.REQUEST_EDIT:
                showToast("Success");
                break;
        }
    }

}
