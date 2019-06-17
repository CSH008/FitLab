package com.jq.app.ui.search.pager;

import android.content.Intent;

import com.jq.app.R;
import com.jq.app.data.model.BodyPoint;
import com.jq.app.ui.search.VideosActivity;

public class BodyAngleFrontFragment extends BodyPartPagerViewFragment {

    public BodyAngleFrontFragment() {
        setImageResource(R.mipmap.body_angle_front);
        setBodyPoints();
    }

    @Override
    public void processTouchDownEvent(int viewWidth, int viewHeight, float viewX, float viewY) {
        super.processTouchDownEvent(viewWidth, viewHeight, viewX, viewY);
    }

    @Override
    public void processTouchUpEvent(int viewWidth, int viewHeight, float viewX, float viewY) {
        super.processTouchUpEvent(viewWidth, viewHeight, viewX, viewY);
    }

    public void setBodyPoints() {
        bodyPoints.clear();
        bodyPoints.add(new BodyPoint(393,236, 1));
        bodyPoints.add(new BodyPoint(309,296, 2));
        bodyPoints.add(new BodyPoint(317,390, 3));
        bodyPoints.add(new BodyPoint(311,471, 4));
        bodyPoints.add(new BodyPoint(293,562, 5));
        bodyPoints.add(new BodyPoint(273,683, 6));
        bodyPoints.add(new BodyPoint(459,314, 7));
        bodyPoints.add(new BodyPoint(551,344, 8));
        bodyPoints.add(new BodyPoint(494,529, 9));
        bodyPoints.add(new BodyPoint(486,631, 10));
        bodyPoints.add(new BodyPoint(586,602, 11));
        bodyPoints.add(new BodyPoint(628,706, 12));
        bodyPoints.add(new BodyPoint(399,827, 13));
        bodyPoints.add(new BodyPoint(497,836, 14));
        bodyPoints.add(new BodyPoint(344,1001, 15));
        bodyPoints.add(new BodyPoint(519,1029, 16));
        bodyPoints.add(new BodyPoint(250,1113, 17));
        bodyPoints.add(new BodyPoint(484,1154, 18));
        bodyPoints.add(new BodyPoint(206,1226, 19));
        bodyPoints.add(new BodyPoint(513,1328, 20));
    }

}

/*
1-393,236
2-309,296
3-317,390
4-311,471
5-293,562
6-273,683
7-459,314
8-551,344
9-494,529
10-486,631
11-586,602
12-628,706
13-399,827
14-497,836
15-344,1001
16-519,1029
17-250,1113
18-484,1154
19-206,1226
20-513,1328
 */