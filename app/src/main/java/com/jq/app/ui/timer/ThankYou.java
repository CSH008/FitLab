package com.jq.app.ui.timer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.jq.app.App;
import com.jq.app.R;
import com.jq.app.network.ApiCallback;
import com.jq.app.network.ApiClient;
import com.jq.app.network.ApiStores;
import com.jq.app.ui.noteList.NoteFragment;
import com.jq.app.ui.noteList.Response;
import com.jq.app.util.base.BaseActivity;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ThankYou extends BaseActivity {
    private static final String TAG = ThankYou.class.getSimpleName();
    private String name = "";
    private ImageView iv_thumb;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

        name = getIntent().getExtras().getString("name");
        TextView textView = findViewById(R.id.header_title);
        iv_thumb = findViewById(R.id.iv_thumb);

        ((TextView) findViewById(R.id.header_name)).setText(name);


        if (name.equalsIgnoreCase("AMRAP")) {
            iv_thumb.setBackgroundResource(R.drawable.ic_thumb_amrap);

        } else if (name.equalsIgnoreCase("EMOM")) {

            iv_thumb.setBackgroundResource(R.drawable.ic_thumb_emom);

        } else if (name.equalsIgnoreCase("STOPWATCH")) {

            iv_thumb.setBackgroundResource(R.drawable.ic_thumb_stop_watch);

        } else if (name.equalsIgnoreCase("TABATA")) {

            iv_thumb.setBackgroundResource(R.drawable.ic_thumb_tabata);

        } else if (name.equalsIgnoreCase("TIMER")) {

            iv_thumb.setBackgroundResource(R.drawable.ic_thumb_timer);

        } else {

        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Time Cap : ");
        stringBuilder.append(getIntent().getExtras().getString("time"));
        stringBuilder.append(" minutes");
        textView.setText(stringBuilder.toString());


        /**
         * get data from api
         */
        try {
            String video_id = getIntent().getExtras().getString("video_id");
            if (video_id != null && !video_id.equalsIgnoreCase("")) {
                saveWorkoutAPI(getIntent().getExtras());
            }
        } catch (Exception e) {

        }

    }

    public void onClick(View v) {
        if (v.getId() == R.id.btn_back) {
            onBackPressed();
        }
    }


    private void saveWorkoutAPI(Bundle bundle) {
        showProgressView(false);
        ApiCallback apiCallback = new C16232();
        Map<String, String> map = new HashMap();
        map.put("email", App.my_email);
        map.put("timer_name", bundle.getString("name"));
        map.put("video_id", bundle.getString("video_id"));
        map.put("before_pain", bundle.getString("before_pain"));
        map.put("after_pain", bundle.getString("after_pain"));
        map.put("order_number", "1");
        map.put("sets", bundle.getString("sets"));
        map.put("reps", bundle.getString("reps"));
        map.put("time", bundle.getString("time"));
        /**
         * set current date
         */
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        map.put("date", date);

        addCall(((ApiStores) ApiClient.retrofit(this).create(ApiStores.class)).saveWorkout(map), apiCallback);
    }


    class C16232 extends ApiCallback<JsonObject> {
        C16232() {
        }

        public void onSuccess(JsonObject response) {
            try {
                JSONObject jsonObject = new JSONObject(response.toString());
                Log.e(TAG, "Response : " + jsonObject);

                if (jsonObject.getString("status").equalsIgnoreCase("1")) {

                    Toast.makeText(ThankYou.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    onBackPressed();

                } else if (jsonObject.getString("status").equalsIgnoreCase("0")) {

                    Toast.makeText(ThankYou.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(ThankYou.this, "Someting went wrong", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                hideProgressDialog();
                e.printStackTrace();
            }
        }

        public void onFailure(String msg) {
            Log.e("onFailure", msg);
        }

        public void onFinish() {
            hideProgressDialog();
        }

        public void onTokenExpire() {
        }
    }
}
