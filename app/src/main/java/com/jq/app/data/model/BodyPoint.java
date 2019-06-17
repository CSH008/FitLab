package com.jq.app.data.model;

import android.view.View;

/**
 * Created by Administrator on 9/21/2017.
 */

public class BodyPoint {
    public int x;
    public int y;
    public int bodyPartNumber;
    public String title;
    public String code;
    private View view;

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public BodyPoint(int x, int y, int bodyPartNumber) {
        this.x = x;
        this.y = y;
        this.bodyPartNumber = bodyPartNumber;
    }

    public BodyPoint(int x, int y, int bodyPartNumber, String title, String code) {
        this.x = x;
        this.y = y;
        this.bodyPartNumber = bodyPartNumber;
        this.title = title;
        this.code = code;
    }

}
