package com.jq.app.network;

import android.content.Context;

import com.jq.app.util.Config;
import com.jq.fitlab.chatsdk.BuildConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit mRetrofit;

    /* renamed from: com.jq.app.network.ApiClient$1 */
    static class C13991 implements Interceptor {
        C13991() {
        }

        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            return chain.proceed(original.newBuilder().method(original.method(), original.body()).build());
        }
    }

    public static Retrofit retrofit(Context context) {
        if (mRetrofit == null) {
            Builder builder = new Builder();
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(Level.BODY);
                builder.addInterceptor(loggingInterceptor);
            }
            builder.addInterceptor(new C13991());
            mRetrofit = new Retrofit.Builder().baseUrl(Config.API_URL).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).client(builder.writeTimeout(80, TimeUnit.SECONDS).readTimeout(80, TimeUnit.SECONDS).connectTimeout(80, TimeUnit.SECONDS).build()).build();
        }
        return mRetrofit;
    }
}
