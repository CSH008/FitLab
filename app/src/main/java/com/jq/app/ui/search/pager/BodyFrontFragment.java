package com.jq.app.ui.search.pager;

import com.jq.app.R;
import com.jq.app.data.model.BodyPoint;

public class BodyFrontFragment extends BodyPartPagerViewFragment {

    public BodyFrontFragment() {
        setImageResource(R.mipmap.body_front);
        setBodyPoints();
    }

    public void setBodyPoints() {
        bodyPoints.clear();
        bodyPoints.add(new BodyPoint(396, 434, 1, "chest", "fv001"));
        bodyPoints.add(new BodyPoint(504, 434, 1, "chest", "fv001"));
        bodyPoints.add(new BodyPoint(320, 408, 2, "shoulder", "fv002"));
        bodyPoints.add(new BodyPoint(590, 408, 2, "shoulder", "fv002"));
        bodyPoints.add(new BodyPoint(312, 512, 3, "bicep", "fv003"));
        bodyPoints.add(new BodyPoint(600, 527, 3, "bicep", "fv003"));
        bodyPoints.add(new BodyPoint(294, 656, 4, "forearm", "fv004"));
        bodyPoints.add(new BodyPoint(612, 660, 4, "forearm", "fv004"));
        bodyPoints.add(new BodyPoint(278, 752, 5, "wrist", "fv005"));
        bodyPoints.add(new BodyPoint(623, 767, 5, "wrist", "fv006"));
        bodyPoints.add(new BodyPoint(425, 612, 6, "abs", "fv006"));
        bodyPoints.add(new BodyPoint(488, 612, 6, "abs", "fv006"));
        bodyPoints.add(new BodyPoint(414, 704, 6, "abs", "fv006"));
        bodyPoints.add(new BodyPoint(490, 705, 6, "abs", "fv006"));
        bodyPoints.add(new BodyPoint(383, 884, 7, "quad", "fv007"));
        bodyPoints.add(new BodyPoint(530, 884, 7, "quad", "fv007"));
        bodyPoints.add(new BodyPoint(404, 1076, 8, "knee", "fv008"));
        bodyPoints.add(new BodyPoint(504, 1076, 8, "knee", "fv008"));
        bodyPoints.add(new BodyPoint(409, 1222, 9, "shin", "fv009"));
        bodyPoints.add(new BodyPoint(508, 1222, 9, "shin", "fv009"));
        bodyPoints.add(new BodyPoint(424, 1345, 10, "ankle", "fv010"));
        bodyPoints.add(new BodyPoint(498, 1345, 10, "ankle", "fv010"));
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

// 1: (396, 434)
// 1: (504, 434)
// 2: (320, 408)
// 2: (590, 408)
// 3: (312, 512)
// 3: (600, 626)
// 4: (294, 656)
// 4: (612, 660)
// 5: (274, 760)
// 5: (618, 765)
// 6: (425, 612)
// 6: (488, 612)
// 6: (414, 704)
// 6: (488, 700)
// 7: (383, 884)
// 7: (530, 884)
// 8: (404, 1076)
// 8: (504, 1076)
// 9: (409, 1222)
// 9: (508, 1222)
// 10: (424, 1345)
// 10: (498, 1345)
