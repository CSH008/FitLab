package com.jq.app.ui.createplanner;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.jq.app.R;

public class InputNoteDialog extends Dialog {
    private final String curretValue;
    Callback callback;

    public InputNoteDialog(@NonNull Context context, String currentValue, Callback callback) {
        super(context);
        this.callback = callback;
        this.curretValue = currentValue;
        init();
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_input_note);
        final EditText editText = findViewById(R.id.edit_text_note);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        editText.append(curretValue);
        findViewById(R.id.text_view_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                callback.onSave(editText.getText().toString().trim());
            }
        });

        findViewById(R.id.image_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public interface Callback {
        void onSave(String text);
    }
}
