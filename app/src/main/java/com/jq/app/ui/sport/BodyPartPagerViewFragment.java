package com.jq.app.ui.sport;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jq.app.R;
import com.jq.app.data.model.BodyPoint;

import java.util.ArrayList;

public class BodyPartPagerViewFragment extends Fragment {

    public static float BODY_IMAGE_WIDTH = 707;
    public static float BODY_IMAGE_HEIGHT = 1683;
    public static float CLICKED_POINT_DEFAULT_SIZE = 100;

    private int imageResource;

    protected RelativeLayout imageContainer;
    protected ArrayList<BodyPoint> bodyPoints = new ArrayList<>();

    public BodyPartPagerViewFragment() {}

    public static BodyPartPagerViewFragment newInstance(int imageResource) {
        BodyPartPagerViewFragment fragment = new BodyPartPagerViewFragment();
        fragment.setImageResource(imageResource);
        return fragment;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        View view = inflater.inflate(R.layout.baseball_pager_view, container, false);

        imageContainer = view.findViewById(R.id.baseball_possition_container);
        ImageView bodyImage = view.findViewById(R.id.img_baseball);
        bodyImage.setImageResource(imageResource);
        bodyImage.setOnTouchListener(image_Listener);

        return view;
    }

    private View.OnTouchListener image_Listener = new View.OnTouchListener(){
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            float screenX = event.getX();
            float screenY = event.getY();
            float viewX = screenX - v.getLeft();
            float viewY = screenY - v.getTop();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.d("DEBUG", "On touch (down)" + String.valueOf(viewX) +"-"+ String.valueOf(viewY));
                    processTouchDownEvent(v.getWidth(), v.getHeight(), viewX, viewY);
                    break;

                case MotionEvent.ACTION_UP:
                    imageContainer.removeAllViews();
                    /*if(clickedPoint!=null && imageContainer!=null) {
                        imageContainer.removeView(clickedPoint);
                    }*/
                    Log.d("DEBUG", "On touch (up)" + String.valueOf(viewX) +"-"+ String.valueOf(viewY));
                    processTouchUpEvent(v.getWidth(), v.getHeight(), viewX, viewY);
                    break;

                case MotionEvent.ACTION_MOVE:
                    //imageContainer.removeAllViews();
                    Log.d("DEBUG", "On touch (move)" + String.valueOf(viewX) +"-"+ String.valueOf(viewY));
                    break;
            }

            return true;
        }

/*
		 Display display = getWindowManager().getDefaultDisplay();
    DisplayMetrics metrics = new DisplayMetrics();
    display.getMetrics(metrics);

    Log.d("DEBUG", "density :" +  metrics.density);

    // density interms of dpi
    Log.d("DEBUG", "D density :" +  metrics.densityDpi);

    // horizontal pixel resolution
    Log.d("DEBUG", "width pix :" +  metrics.widthPixels);

     // actual horizontal dpi
    Log.d("DEBUG", "xdpi :" +  metrics.xdpi);

    // actual vertical dpi
    Log.d("DEBUG", "ydpi :" +  metrics.ydpi);
	*/
    };

    public float getBodyPointSize(int viewWidth, int viewHeight) {
        float viewRatio = (float)viewHeight / (float)viewWidth;
        float imageRatio = BODY_IMAGE_HEIGHT / BODY_IMAGE_WIDTH;
        if( viewRatio>= imageRatio) {
            return CLICKED_POINT_DEFAULT_SIZE * (float)viewWidth / BODY_IMAGE_WIDTH;
        } else {
           // return CLICKED_POINT_DEFAULT_SIZE * (float)viewHeight / BODY_IMAGE_HEIGHT;
		   return CLICKED_POINT_DEFAULT_SIZE * (float)viewWidth / BODY_IMAGE_WIDTH;
        }
    }

    public int getClickedPointIndex(int viewWidth, int viewHeight, float viewX, float viewY) {
        float bodyPointSize = getBodyPointSize(viewWidth, viewHeight);
        float viewRatio = (float)viewHeight / (float)viewWidth;
        float imageRatio = BODY_IMAGE_HEIGHT / BODY_IMAGE_WIDTH;

Log.d("DEBUG", "getClickedPointIndex" + String.valueOf(viewRatio) +"-"+ String.valueOf(imageRatio));
        if( viewRatio>= imageRatio) {
            for (int i=0; i<bodyPoints.size(); i++) {
                BodyPoint bodyPoint = bodyPoints.get(i);
                float minX = bodyPoint.x * viewWidth / BODY_IMAGE_WIDTH - bodyPointSize/2;
                float maxX = bodyPoint.x * viewWidth / BODY_IMAGE_WIDTH + bodyPointSize/2;
                float minY = bodyPoint.y * viewWidth / BODY_IMAGE_WIDTH - bodyPointSize/2 + (viewHeight - viewWidth * BODY_IMAGE_HEIGHT / BODY_IMAGE_WIDTH)/2;
                float maxY = bodyPoint.y * viewWidth / BODY_IMAGE_WIDTH + bodyPointSize/2 + (viewHeight - viewWidth * BODY_IMAGE_HEIGHT / BODY_IMAGE_WIDTH)/2;
Log.d("DEBUG", "getClickedPointIndex-1 " + String.valueOf(minX) +"-"+ String.valueOf(maxX)+"-"+String.valueOf(minY)+"-"+String.valueOf(maxY));

                if(viewX>=minX && viewX<=maxX && viewY>=minY && viewY<=maxY) {
                    return i;
                }
            }
        } else {
            for (int i=0; i<bodyPoints.size(); i++) {
                BodyPoint bodyPoint = bodyPoints.get(i);
                float minX = bodyPoint.x * viewHeight / BODY_IMAGE_HEIGHT - bodyPointSize/2 + (viewWidth - viewHeight * BODY_IMAGE_WIDTH / BODY_IMAGE_HEIGHT)/2;
                float maxX = bodyPoint.x * viewHeight / BODY_IMAGE_HEIGHT + bodyPointSize/2 + (viewWidth - viewHeight * BODY_IMAGE_WIDTH / BODY_IMAGE_HEIGHT)/2;
                float minY = bodyPoint.y * viewHeight / BODY_IMAGE_HEIGHT - bodyPointSize/2;
                float maxY = bodyPoint.y * viewHeight / BODY_IMAGE_HEIGHT + bodyPointSize/2;
Log.d("DEBUG", "getClickedPointIndex-3 " + String.valueOf(minX) +"-"+ String.valueOf(maxX)+"-"+String.valueOf(minY)+"-"+String.valueOf(maxY));

                if(viewX>=minX && viewX<=maxX && viewY>=minY && viewY<=maxY) {
                    return i;
                }
            }
        }

        return -1;
    }

    public int calculateMarginLeft(int bodyPointIndex, int viewWidth, int viewHeight) {
        BodyPoint bodyPoint = bodyPoints.get(bodyPointIndex);
        float bodyPointSize = getBodyPointSize(viewWidth, viewHeight);
        float viewRatio = (float)viewHeight / (float)viewWidth;
        float imageRatio = BODY_IMAGE_HEIGHT / BODY_IMAGE_WIDTH;
        if( viewRatio>= imageRatio) {
            float minX = bodyPoint.x * viewWidth / BODY_IMAGE_WIDTH - bodyPointSize;
			Log.d("DEBUG", "calculateMarginLeft " + String.valueOf(minX));

            return (int)minX;

        } else {
            float minX = bodyPoint.x * viewHeight / BODY_IMAGE_HEIGHT - bodyPointSize + (viewWidth - viewHeight * BODY_IMAGE_WIDTH / BODY_IMAGE_HEIGHT)/2;
			Log.d("DEBUG", "calculateMarginLeft -1" + String.valueOf(minX));
            return (int)minX;
        }
    }

    public int calculateMarginTop(int bodyPointIndex, int viewWidth, int viewHeight) {
        BodyPoint bodyPoint = bodyPoints.get(bodyPointIndex);

        float bodyPointSize = getBodyPointSize(viewWidth, viewHeight);
        float viewRatio = (float)viewHeight / (float)viewWidth;
        float imageRatio = BODY_IMAGE_HEIGHT / BODY_IMAGE_WIDTH;
        if( viewRatio>= imageRatio) {

            float minY = bodyPoint.y * viewWidth / BODY_IMAGE_WIDTH - bodyPointSize + (viewHeight - viewWidth * BODY_IMAGE_HEIGHT / BODY_IMAGE_WIDTH)/2;
			Log.d("DEBUG", "calculateMarginTop" + String.valueOf(minY));
            return (int)minY;
        } else {
            float minY = bodyPoint.y * viewHeight / BODY_IMAGE_HEIGHT - bodyPointSize;
						Log.d("DEBUG", "calculateMarginTop-1" + String.valueOf(minY));
            return (int)minY;
        }
    }

    public void processTouchDownEvent(int viewWidth, int viewHeight, float viewX, float viewY) {
        ImageView clicked_point = new ImageView(getActivity());
        int clickedEffectPointSize = (int) getBodyPointSize(viewWidth, viewHeight) *2;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                clickedEffectPointSize, clickedEffectPointSize);
        int pointAddressIndex = getClickedPointIndex(viewWidth, viewHeight, viewX, viewY);
        if(pointAddressIndex<0) return;

        int marginLeft = calculateMarginLeft(pointAddressIndex, viewWidth, viewHeight);
        int marginTop = calculateMarginTop(pointAddressIndex, viewWidth, viewHeight);
        layoutParams.setMargins(marginLeft, marginTop, 0, 0);
        clicked_point.setLayoutParams(layoutParams);
        clicked_point.setImageResource(R.mipmap.clicked_point);

        imageContainer.addView(clicked_point);
        //clickedPoint = clicked_point;
    }

    public void processTouchUpEvent(int viewWidth, int viewHeight, float viewX, float viewY) {
        int pointAddressIndex = getClickedPointIndex(viewWidth, viewHeight, viewX, viewY);
        if(pointAddressIndex<0) return;

        String bodyPartCode = getBodyPartCode(pointAddressIndex);
        Toast.makeText(getActivity(), bodyPartCode, Toast.LENGTH_SHORT).show();
        moveVideosActivity(bodyPartCode);
    }

    public void moveVideosActivity(String bodyPartCode) {
        if(!bodyPartCode.isEmpty()) {
            Intent i = new Intent(getActivity(), SportVideosActivity.class);
            i.putExtra(SportVideosActivity.KEY_SPORT_CODE_2, ((SportSearchActivity)getActivity()).mWorkoutCode);
            i.putExtra(SportVideosActivity.KEY_SPORT_CODE_1, bodyPartCode);
            startActivity(i);
        }
    }

    public String getBodyPartCode(int index) {
        if(index>-1 && index<bodyPoints.size()) {
            BodyPoint bodyPoint = bodyPoints.get(index);
            return bodyPoint.code;
        }

        return "";
    }

}
