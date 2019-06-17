package com.jq.app.ui.save_video;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jq.app.R;
import com.jq.app.ui.PlannerNew.model.Planner;
import com.jq.app.ui.widgets.MyMediaImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Hasnain on 22-Feb-18.
 */

public class SaveVideoAdapter extends RecyclerView.Adapter<SaveVideoAdapter.ViewHolder> {
    private ArrayList<Planner> arrayList;
    private VideoClick videoClick;
    private Context mContext;

    public interface VideoClick{
        void onVideoClick(Planner planner);
    }

    public SaveVideoAdapter(ArrayList<Planner> arrayList, VideoClick videoClick) {
        this.arrayList = arrayList;
        this.videoClick = videoClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        mContext = parent.getContext();
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.row_save_video, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.bind(arrayList.get(i),videoClick);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon_play;
        private MyMediaImageView video_thumb;

        public ViewHolder(View itemView) {
            super(itemView);

            icon_play = itemView.findViewById(R.id.icon_play);
            video_thumb = itemView.findViewById(R.id.video_thumb);
        }

        public void bind(final Planner planner, final VideoClick videoClick) {

            if (planner.getThumbnail_url() != null && !planner.getThumbnail_url().isEmpty()) {
                Picasso.with(mContext).load(planner.getThumbnail_url()).into(video_thumb);
            }


            icon_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    videoClick.onVideoClick(planner);

                }
            });

            video_thumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    videoClick.onVideoClick(planner);
                }
            });

        }
    }
}
