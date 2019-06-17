package com.jq.app.ui.createplanner;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.telecom.Call;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.jq.app.R;
import com.jq.app.data.local_helpers.LocalVideoHelper;
import com.jq.app.ui.upload.SelectBodyPartActivity;

import java.util.ArrayList;
import java.util.List;


public class FilterDialog extends Dialog implements View.OnClickListener {

    private final Callback callback;
    private Context context;
    private TextView textViewCategory;
    private TextView textViewBodyPart;
    private TextView textViewEquipment;
    private String category, bodypart, equipment;
    private String[] categories;
    private String[] euipments;
    private boolean[] checkEquiments;

    public FilterDialog(@NonNull Context context, Callback callback) {
        super(context);
        this.context = context;
        categories = context.getResources().getStringArray(R.array.categories);
        euipments = context.getResources().getStringArray(R.array.equipments);
        checkEquiments = new boolean[]{false, false, false, false};
        this.callback = callback;
        init();
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_filter);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        textViewCategory = findViewById(R.id.text_view_category);
        textViewBodyPart = findViewById(R.id.text_view_body_part);
        textViewEquipment = findViewById(R.id.text_view_equipment);
        textViewCategory.setOnClickListener(this);
        textViewBodyPart.setOnClickListener(this);
        textViewEquipment.setOnClickListener(this);
        findViewById(R.id.image_back).setOnClickListener(this);
        findViewById(R.id.text_view_save).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_view_category:
                showSingleItemDialog(categories);
                break;
            case R.id.text_view_body_part:
                Intent intent = new Intent(context, SelectBodyPartActivity.class);
                intent.putExtra("from_filter", true);
                ((Activity) context).startActivityForResult(intent, 11);
                break;
            case R.id.text_view_equipment:
                showMultiChoiceItemDialog(euipments);
                break;
            case R.id.text_view_save:
                dismiss();
                callback.onApply(category, bodypart, equipment);
                break;
            case R.id.image_back:
                dismiss();
                break;
        }
    }

    private void showMultiChoiceItemDialog(final String[] equipment) {
        new AlertDialog.Builder(context).setTitle("Select equipments").setMultiChoiceItems(equipment, checkEquiments, new OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                checkEquiments[which] = isChecked;
            }
        }).setPositiveButton("Ok", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<String> checkedEquipment = new ArrayList<>();
                for (int i = 0; i < checkEquiments.length; i++) {
                    boolean checked = checkEquiments[i];
                    if (checked) {
                        checkedEquipment.add(equipment[i]);
                    }
                }
                dialog.dismiss();
                String join = TextUtils.join(",", checkedEquipment);
                FilterDialog.this.equipment = join.toLowerCase();
                textViewEquipment.setText(join);

            }
        }).setNegativeButton("Cancel", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    private void showSingleItemDialog(final String[] categories) {
        new AlertDialog.Builder(context).setTitle("Select category").setSingleChoiceItems(categories, -1, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                textViewCategory.setText(categories[which]);
                category = LocalVideoHelper.getCalegoryType(categories[which].toLowerCase());
                dialog.dismiss();
            }
        }).show();
    }

    public void setSelecedParts(String stringExtra) {
        textViewBodyPart.setText(stringExtra);
        bodypart = stringExtra.toLowerCase();
    }

    public interface Callback {
        void onApply(String category, String parts, String equipments);
    }

}
