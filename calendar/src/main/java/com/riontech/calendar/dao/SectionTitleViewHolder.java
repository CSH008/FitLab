package com.riontech.calendar.dao;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.riontech.calendar.R;
import com.riontech.calendar.fragment.addEventListener;

/**
 * Created by Dhaval Soneji on 4/4/16.
 */
public class SectionTitleViewHolder extends RecyclerView.ViewHolder {
    private TextView txtSection;
    private TextView txtSectionTime;
    private ImageView btnAdd,btnEdit;

    public TextView getTxtSection() {
        return txtSection;
    }

    public TextView getTxtSectionTime() {
        return txtSectionTime;
    }

    public ImageView getButtonSection() {
        return btnAdd;
    }

    public ImageView getButtonEdit() {
        return btnEdit;
    }

    public void setTxtSection(TextView txtSection) {
        this.txtSection = txtSection;
    }

    public SectionTitleViewHolder(View v) {
        super(v);
        txtSection = (TextView) v.findViewById(R.id.txtSection);
        txtSectionTime = (TextView) v.findViewById(R.id.txtSectionTime);
        btnAdd = (ImageView) v.findViewById(R.id.btnCheck);
        btnEdit = (ImageView) v.findViewById(R.id.btn_edit);



    }
}
