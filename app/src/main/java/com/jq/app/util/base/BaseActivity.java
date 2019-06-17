package com.jq.app.util.base;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Toast;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.jq.app.data.content_helpers.BaseContentHelper;
import com.jq.app.data.local_helpers.BaseLocalHelper;
import com.jq.app.network.ApiCallback;
import com.jq.app.ui.auth.LoginActivity;
import com.jq.app.util.Common;
import com.jq.app.util.Constants;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BaseActivity extends LocalizationActivity implements BaseLocalHelper.LoadCompletionListener, BaseContentHelper.OnDataLoadListener {

    public Context mContext;
    private ProgressDialog mProgressDialog;
    private ProgressDialog mUploadingProgressDialog;
    public View mProgressView;

    protected static final int RC_CAMERA = 3000;
    protected static final int RC_STORAGE = 4000;

    @Override
    public void onResume() {
        super.onResume();

        mContext = this;

        /*if(Common.getValueWithKey(this, Constants.KEY_LANGUAGE).equals("en")) {
            setLanguage("en");

        } else {
            setLanguage("it");
        }*/
    }

    public void showProgressView(boolean value) {
        if(mProgressView!=null) {
            if(value) {
                mProgressView.setVisibility(View.VISIBLE);

            } else {
                mProgressView.setVisibility(View.GONE);
            }
        }
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void showUploadingProgressDialog(String msg) {
        if (mUploadingProgressDialog == null) {
            mUploadingProgressDialog = new ProgressDialog(this);
            mUploadingProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mUploadingProgressDialog.setCancelable(false);
            mUploadingProgressDialog.setMessage(msg);
        }

        mUploadingProgressDialog.show();
    }

    public void setProgressDialogProgress(int value) {
        if (mUploadingProgressDialog != null) {
            mUploadingProgressDialog.setProgress(value);
        }
    }

    public void hideUploadingProgressDialog() {
        if (mUploadingProgressDialog != null && mUploadingProgressDialog.isShowing()) {
            mUploadingProgressDialog.dismiss();
        }
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showDialog(String title, String content) {
        new AlertDialog.Builder(mContext)
                .setTitle(title)
                .setMessage(content)
                .setPositiveButton(android.R.string.ok, null).show();
    }

    public boolean requestCameraPermission() {
        final Activity activity = this;
        final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissions, RC_CAMERA);
            return false;
        }

        return true;
    }

    public void requestStoragePermission() {
        final Activity activity = this;
        final String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(activity, permissions, RC_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RC_CAMERA) {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionGranted();
            }
        } else if(requestCode == RC_STORAGE) {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionGranted();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void permissionGranted() {}

    public void logout() {
        Common.saveStringWithKeyValue(this, Constants.KEY_REMEMBER_LOGIN, "false");
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void finishedAction(int method, int message) {
        if(message!=0) {
            finishedAction(method, getString(message));
        } else {
            finishedAction(method, "");
        }
    }

    @Override
    public void finishedAction(int method, String message) {

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

    }


    public void addCall(Observable observable, final ApiCallback apiCallback) {
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new Observer() {
            public void onSubscribe(Disposable d) {
            }

            public void onNext(Object o) {
                apiCallback.onNext(o);
            }

            public void onError(Throwable e) {
                apiCallback.onError(e);
            }

            public void onComplete() {
                apiCallback.onComplete();
            }
        });
    }
}
