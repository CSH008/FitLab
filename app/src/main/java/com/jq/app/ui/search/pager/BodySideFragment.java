package com.jq.app.ui.search.pager;

import android.content.Intent;

import com.jq.app.R;
import com.jq.app.data.model.BodyPoint;

public class BodySideFragment extends BodyPartPagerViewFragment {

    public BodySideFragment() {
        setImageResource(R.mipmap.body_side);
        setBodyPoints();
    }

    public void setBodyPoints() {
        bodyPoints.clear();
        bodyPoints.add(new BodyPoint(413, 341, 1, "neck", "sv001"));
        bodyPoints.add(new BodyPoint(440, 430, 2, "shoulder", "sv002"));
        bodyPoints.add(new BodyPoint(446, 515, 3, "tricep", "sv003"));
        bodyPoints.add(new BodyPoint(440, 586, 4, "elbow", "sv004"));
        bodyPoints.add(new BodyPoint(460, 694, 5, "fore arm", "sv005"));
        bodyPoints.add(new BodyPoint(475, 772, 6, "wrist", "sv006"));
        bodyPoints.add(new BodyPoint(457, 886, 7, "hamstring", "sv007"));
        bodyPoints.add(new BodyPoint(517, 945, 8, "quad", "sv008"));
        bodyPoints.add(new BodyPoint(465, 1058, 9, "knee", "sv009"));
        bodyPoints.add(new BodyPoint(409, 1187, 10, "calf", "sv0010"));
        bodyPoints.add(new BodyPoint(418, 1346, 11, "ankle", "sv0011"));
    }

    @Override
    public void processTouchDownEvent(int viewWidth, int viewHeight, float viewX, float viewY) {
        super.processTouchDownEvent(viewWidth, viewHeight, viewX, viewY);
    }

    @Override
    public void processTouchUpEvent(int viewWidth, int viewHeight, float viewX, float viewY) {
        super.processTouchUpEvent(viewWidth, viewHeight, viewX, viewY);
    }

}

/*
1-413,341
2-440.430
3-446,515
4-440,586
5-460,694
6-475,772
7-457,886
8-517,945
9-465,1058
10-409,1187
11-418,1346
 */