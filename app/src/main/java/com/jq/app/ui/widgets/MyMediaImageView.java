package com.jq.app.ui.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 6/20/2016.
 */
public class MyMediaImageView extends ImageView {

    public MyMediaImageView(Context context) {
        super(context);
    }

    public MyMediaImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()*9/16);
    }

}