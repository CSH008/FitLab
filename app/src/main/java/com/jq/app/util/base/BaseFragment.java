package com.jq.app.util.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jq.app.data.content_helpers.BaseContentHelper;
import com.jq.app.data.local_helpers.BaseLocalHelper;
import com.jq.app.network.ApiCallback;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Administrator on 6/29/2016.
 */
public class BaseFragment extends Fragment implements BaseLocalHelper.LoadCompletionListener, BaseContentHelper.OnDataLoadListener {

    private static final String TAG = "BaseFragment";
    public AppCompatActivity mActivity;

    public BaseFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (AppCompatActivity)getActivity();
    }

    public View mProgressView;
    public void showProgressView(boolean value) {
        if(mProgressView!=null) {
            if(value) {
                mProgressView.setVisibility(View.VISIBLE);

            } else {
                mProgressView.setVisibility(View.GONE);
            }
        }
    }

    private ProgressDialog mProgressDialog;
    private ProgressDialog mUploadingProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
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
            mUploadingProgressDialog = new ProgressDialog(getActivity());
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
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
            if(getActivity()!=null) {
                onFinishedAction(action, index, getString(errMsg));
            }
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