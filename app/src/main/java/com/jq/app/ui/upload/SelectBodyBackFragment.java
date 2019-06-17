package com.jq.app.ui.upload;

import com.jq.app.R;
import com.jq.app.data.model.BodyPoint;
import com.jq.app.ui.search.pager.BodyPartPagerViewFragment;

public class SelectBodyBackFragment extends SelectBodyPartPagerViewFragment {

    public SelectBodyBackFragment() {
        setImageResource(R.mipmap.body_back);
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
        bodyPoints.add(new BodyPoint(439,285, 1, "neck", "bv001"));
        bodyPoints.add(new BodyPoint(487,356, 2, "mid back", "bv002"));
        bodyPoints.add(new BodyPoint(388,357, 2, "mid back", "bv002"));
        bodyPoints.add(new BodyPoint(479,434, 3, "mid back", "bv003"));
        bodyPoints.add(new BodyPoint(408,434, 3, "mid back", "bv003"));
        bodyPoints.add(new BodyPoint(533,446, 3, "mid back", "bv003"));
        bodyPoints.add(new BodyPoint(353,450, 3, "mid back", "bv003"));
        bodyPoints.add(new BodyPoint(499,559, 4, "lower back", "bv004"));
        bodyPoints.add(new BodyPoint(507,644, 4, "lower back", "bv004"));
        bodyPoints.add(new BodyPoint(451,674, 4, "lower back", "bv004"));
        bodyPoints.add(new BodyPoint(383,645, 4, "lower back", "bv004"));
        bodyPoints.add(new BodyPoint(387,559, 4, "lower back", "bv004"));
        bodyPoints.add(new BodyPoint(570,406, 5, "shoulder", "bv005"));
        bodyPoints.add(new BodyPoint(305,401, 5, "shoulder", "bv005"));
        bodyPoints.add(new BodyPoint(583,523, 6, "tricep ", "bv006"));
        bodyPoints.add(new BodyPoint(317,524, 6, "tricep ", "bv006"));
        bodyPoints.add(new BodyPoint(598,595, 7, "elbow", "bv007"));
        bodyPoints.add(new BodyPoint(306,605, 7, "elbow", "bv007"));
        bodyPoints.add(new BodyPoint(602,679, 8, "fore form", "bv008"));
        bodyPoints.add(new BodyPoint(295,684, 8, "fore form", "bv008"));
        bodyPoints.add(new BodyPoint(620,748, 9, "wrist", "bv009"));
        bodyPoints.add(new BodyPoint(284,763, 9, "wrist", "bv009"));
        bodyPoints.add(new BodyPoint(507,765, 10, "glute", "bv010"));
        bodyPoints.add(new BodyPoint(400,764, 10, "glute", "bv010"));
        bodyPoints.add(new BodyPoint(504,878, 11, "hamstring", "bv011"));
        bodyPoints.add(new BodyPoint(407,876, 11, "hamstring", "bv011"));
        bodyPoints.add(new BodyPoint(501,1018, 12, "hamstring", "bv012"));
        bodyPoints.add(new BodyPoint(391,1017, 12, "hamstring", "bv012"));
        bodyPoints.add(new BodyPoint(497,1172, 13, "calf", "bv013"));
        bodyPoints.add(new BodyPoint(398,1173, 13, "calf", "bv013"));
        bodyPoints.add(new BodyPoint(479,1340, 14, "achilles", "bv014"));
        bodyPoints.add(new BodyPoint(416,1340, 14, "achilles", "bv014"));
    }


}

/*
1-439,285
2-487,356
3-479,434
4.1-499,559
4.2-507,644
4.3-451,674
5-570,406
6-533,446
7-583,523
8-598,595
9-602,679
10-620,748
11-507,765
12-504,878
13-501,1018
14-497,1172
15-479,1340
16-416,1340
17-398,1173
18-391,1017
19-407,876
20-400,764
21-383,645
22-387,559
23-353,450
23.1-408,434
24-388,357
25-305,401
26-317,524
27-306,605
28-295,684
29-284,763 */