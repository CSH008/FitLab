package com.jq.app.ui.sport;

import com.jq.app.R;
import com.jq.app.data.model.BodyPoint;

public class BodyFrontFragment extends BodyPartPagerViewFragment {

    public BodyFrontFragment() {
        setImageResource(R.mipmap.baseball);
        setBodyPoints();
    }

    public void setBodyPoints() {
        bodyPoints.clear();
        bodyPoints.add(new BodyPoint(458, 757, 1, "1", "beba1"));// 423, 304=35,97
		bodyPoints.add(new BodyPoint(450, 757, 1, "1", "beba1"));
		bodyPoints.add(new BodyPoint(455, 932, 1, "1", "beba1"));// new

        bodyPoints.add(new BodyPoint(740, 880, 2, "2", "beba2"));//new
        bodyPoints.add(new BodyPoint(750, 880, 2, "2", "beba2"));//new point 1.9
		//bodyPoints.add(new BodyPoint(498, 523, 2, "2", "Possition 2-3"));

		bodyPoints.add(new BodyPoint(760, 875, 2, "2", "beba2"));//new
        bodyPoints.add(new BodyPoint(770, 870, 2, "2", "beba2"));//new point 1.9

		bodyPoints.add(new BodyPoint(750, 875, 2, "2", "beba2"));//new
        bodyPoints.add(new BodyPoint(740, 870, 2, "2", "beba2"));//new point 1.9

        bodyPoints.add(new BodyPoint(434, 133, 3, "3", "beba3"));//412,44
      // bodyPoints.add(new BodyPoint(431, 180, 3, "3", "Possition 3-2"));//new
		//bodyPoints.add(new BodyPoint(437, 106, 3, "3", "Possition 3-3"));

		bodyPoints.add(new BodyPoint(346, 696, 4, "4", "beba4"));//issue
		bodyPoints.add(new BodyPoint(321, 730, 4, "4", "321, 730"));
		//bodyPoints.add(new BodyPoint(327, 726, 4, "4", "327, 726"));

		bodyPoints.add(new BodyPoint(445, 1704, 5, "5", "beba5"));//
		bodyPoints.add(new BodyPoint(450, 1700, 5, "5", "beba5"));//

		bodyPoints.add(new BodyPoint(455, 1695, 5, "5", "beba5"));//
		bodyPoints.add(new BodyPoint(460, 1690, 5, "5", "beba5"));//

		bodyPoints.add(new BodyPoint(450, 1685, 5, "5", "beba5"));//
		bodyPoints.add(new BodyPoint(445, 1680, 5, "5", "beba5"));//
	//	bodyPoints.add(new BodyPoint(416, 1243, 5, "5", "416, 1243"));
		//bodyPoints.add(new BodyPoint(420, 1247, 5, "5", "420, 1247"));

		bodyPoints.add(new BodyPoint(345, 1200, 6, "6", "beba6"));//1220
		bodyPoints.add(new BodyPoint(317, 1150, 6, "6", "317, 983"));//983

		bodyPoints.add(new BodyPoint(317, 1170, 6, "6", "317, 983"));//983
		//bodyPoints.add(new BodyPoint(321, 987, 6, "6", "321, 987"));

		bodyPoints.add(new BodyPoint(280, 1713, 7, "7", "beba7"));// issue
		bodyPoints.add(new BodyPoint(260, 1695, 7, "7", "beba7"));
		bodyPoints.add(new BodyPoint(270, 1695, 7, "7", "beba7"));

		bodyPoints.add(new BodyPoint(255, 1685, 7, "7", "beba7"));
		bodyPoints.add(new BodyPoint(250, 1675, 7, "7", "beba7"));

		bodyPoints.add(new BodyPoint(240, 1670, 7, "7", "beba7"));
		bodyPoints.add(new BodyPoint(235, 1665, 7, "7", "beba7"));

		bodyPoints.add(new BodyPoint(206, 844, 8, "8", "beba8"));
	    bodyPoints.add(new BodyPoint(204, 842, 8, "8", "204, 842"));
		//bodyPoints.add(new BodyPoint(208, 846, 8, "8", "208, 846"));

		bodyPoints.add(new BodyPoint(271, 110, 9, "9", "beba9"));//
		bodyPoints.add(new BodyPoint(271, 210, 9, "9", "beba9"));
		//bodyPoints.add(new BodyPoint(249, 474, 9, "9", "249, 474"));

		bodyPoints.add(new BodyPoint(650, 1080, 10, "B", "bebab"));
		bodyPoints.add(new BodyPoint(670, 1075, 10, "B", "bebab"));

		bodyPoints.add(new BodyPoint(680, 1075, 10, "B", "bebab"));
		bodyPoints.add(new BodyPoint(690, 1070, 10, "B", "bebab"));
	//	  bodyPoints.add(new BodyPoint(609, 605, 10, "B", "583, 962"));//
		//bodyPoints.add(new BodyPoint(579, 958, 10, "B", "579, 958"));
       
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
