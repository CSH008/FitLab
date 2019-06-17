package com.jq.app.network;

import android.util.Log;

import cz.msebera.android.httpclient.HttpStatus;
import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

public abstract class ApiCallback<M> extends DisposableObserver<M> {
    public abstract void onFailure(String str);

    public abstract void onFinish();

    public abstract void onSuccess(M m);

    public abstract void onTokenExpire();

    public void onComplete() {
        onFinish();
    }

    public void onError(Throwable e) {
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            int code = httpException.code();
            onFailure(httpException.getMessage());
            if (code == HttpStatus.SC_UNAUTHORIZED) {
                onTokenExpire();
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(code);
            stringBuilder.append("  ");
            Log.e("code : ", stringBuilder.toString());
        } else {
            onFailure(e.getMessage());
        }
        onFinish();
    }

    public void onNext(M m) {
        try {
            onSuccess(m);
        } catch (Exception e) {
            onFailure(e.getMessage());
            e.printStackTrace();
        }
    }
}
