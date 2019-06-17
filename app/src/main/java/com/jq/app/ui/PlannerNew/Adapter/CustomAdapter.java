package com.jq.app.ui.PlannerNew.Adapter;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.jq.app.R;
import com.jq.app.data.local_helpers.BaseLocalHelper;
import com.jq.app.data.model.LocalVideo;
import com.jq.app.ui.PlannerNew.model.Planner;
import com.jq.app.ui.PlannerNew.model.Video;
import com.jq.chatsdk.activities.VideoActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by station13 on 12-02-2018.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> implements Filterable {

    Context mContext;
    ArrayList<Planner> mCustomList;
    ArrayList<Planner> tempList;
    RecyclerView recyclerView;
    CustomFilter filter;
    //Listener mListener;

    public CustomAdapter(Context mContext, ArrayList<Planner> mCustomList, RecyclerView recyclerView/*,Listener mListener*/) {
        this.mContext = mContext;
        this.mCustomList = mCustomList;
        this.recyclerView = recyclerView;
        tempList = new ArrayList<>();
        //  this.mListener = mListener;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_video, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int position) {

        final Planner videoList = tempList.get(position);
        customViewHolder.card_view.setBackgroundColor(Color.parseColor(BaseLocalHelper.getCalegoryColor(videoList.getWork_out_code())));

        if (videoList.getThumbnail_url() != null && !videoList.getThumbnail_url().isEmpty()) {
            Picasso.with(mContext).load(videoList.getThumbnail_url()).into(customViewHolder.video_thumb);
        }
        customViewHolder.textViewTitle.setText(videoList.getWorkout_name());
        customViewHolder.card_view.setTag(position);

        customViewHolder.card_view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.VISIBLE);
                return true;
            }
        });


        customViewHolder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!videoList.getUrl().isEmpty() && !videoList.getUrl().equalsIgnoreCase("")) {
                    Intent intent = new Intent(mContext, VideoActivity.class);
                    intent.putExtra(VideoActivity.FILE_PATH, videoList.getUrl());
                    mContext.startActivity(intent);
                }
            }
        });

    }

  /*  public DragListener getDragInstance() {
        if (mListener != null) {
            return new DragListener(mListener);
        } else {
            Log.e("Route Adapter: ", "Initialize listener first!");
            return null;
        }
    }*/

    @Override
    public int getItemCount() {
        return tempList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new CustomFilter();
        return filter;
    }

    public void setTempList(ArrayList<Planner> dataOneList) {
        tempList.clear();
        tempList.addAll(dataOneList);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final CardView card_view;
        public final ImageView video_thumb;
        public LocalVideo mItem;
        TextView textViewTitle;

        public CustomViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            card_view = mView.findViewById(R.id.card_view);
            video_thumb = mView.findViewById(R.id.video_thumb);
            textViewTitle = mView.findViewById(R.id.text_view_title);
        }
    }


   /* public interface Listener {
        void setEmptyList(boolean visibility);
    }*/

   

   /* public class DragListener implements View.OnDragListener {

        boolean isDropped = false;
        Listener mListener;

        public DragListener(Listener listener) {
            this.mListener = listener;
        }

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    break;

                case DragEvent.ACTION_DRAG_ENTERED:
                    //v.setBackgroundColor(Color.LTGRAY);
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    //v.setBackgroundColor(Color.YELLOW);
                    break;

                case DragEvent.ACTION_DROP:

                    isDropped = true;
                    int positionSource = -1;
                    int positionTarget = -1;

                    View viewSource = (View) event.getLocalState();

                    if (v.getId() == R.id.card_view || v.getId() == R.id.textempty) {
                        //RecyclerView target = (RecyclerView) v.getParent();
                        RecyclerView target;
                        if (v.getId() == R.id.textempty) {
                            target = (RecyclerView)
                                    v.getRootView().findViewById(R.id.recyclerview_child);
                        } else {
                            target = (RecyclerView) v.getParent();
                            positionTarget = (int) v.getTag();
                        }

                        RecyclerView source = (RecyclerView) viewSource.getParent();
                        CustomAdapter adapterSource = (CustomAdapter) source.getAdapter();
                        positionSource = (int) viewSource.getTag();

                        Video customList = (Video) adapterSource.getCustomList().get(positionSource);
                        ArrayList<Video> customListSource = adapterSource.getCustomList();

//                        customListSource.remove(positionSource);
                        adapterSource.updateCustomList(customListSource);
                        adapterSource.notifyDataSetChanged();

                        CustomAdapter adapterTarget = (CustomAdapter) target.getAdapter();
                        ArrayList<Video> customListTarget = adapterTarget.getCustomList();
                        if (positionTarget >= 0) {
                            customListTarget.add(positionTarget, customList);
                        } else {
                            customListTarget.add(customList);
                        }
                        adapterTarget.updateCustomList(customListTarget);
                        adapterTarget.notifyDataSetChanged();
                        v.setVisibility(View.VISIBLE);

                        if (source.getId() == R.id.recyclerview_child
                                && adapterSource.getItemCount() < 1) {
                            mListener.setEmptyList(true);
                        }

                        if (v.getId() == R.id.textempty) {
                            mListener.setEmptyList(false);
                        }
                    }

                    break;

                case DragEvent.ACTION_DRAG_ENDED:
                    //v.setBackgroundColor(0);
                    break;

                default:
                    break;
            }

            if (!isDropped) {
                View vw = (View) event.getLocalState();
                vw.setVisibility(View.VISIBLE);
            }

            return true;
        }

    }

    public ArrayList<Video> getCustomList() {
        return mCustomList;
    }

    public void updateCustomList(ArrayList<Video> customList) {
        this.mCustomList = customList;
    }*/


    public class CustomFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            tempList.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                tempList.addAll(mCustomList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final Planner planner : mCustomList) {
                    if (planner.getTitle().toLowerCase().contains(filterPattern) || planner.getWorkout_name().contains(filterPattern)) {
                        tempList.add(planner);
                    }
                }
            }
            results.values = tempList;
            results.count = tempList.size();
            return null;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notifyDataSetChanged();
        }
    }
}
