package com.jq.app.ui.PlannerNew.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jq.app.R;
import com.jq.app.data.local_helpers.BaseLocalHelper;
import com.jq.app.data.model.LocalVideo;
import com.jq.app.ui.PlannerNew.interfaces.ItemTouchHelperAdapter;
import com.jq.app.ui.PlannerNew.interfaces.ItemTouchHelperViewHolder;
import com.jq.app.ui.PlannerNew.interfaces.OnStartDragListener;
import com.jq.app.ui.PlannerNew.model.Planner;
import com.jq.app.ui.PlannerNew.model.Video;
import com.jq.chatsdk.activities.VideoActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

public class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter {
    private static final String TAG = ItemAdapter.class.getSimpleName();
    private ArrayList<Planner> data;
    OnItemClickListener mItemClickListener;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private final LayoutInflater mInflater;
    private final OnStartDragListener mDragStartListener;
    private Context mContext;

    public ItemAdapter(Context context, ArrayList<Planner> list, OnStartDragListener dragListner) {
        this.data = list;
        this.mInflater = LayoutInflater.from(context);
        mDragStartListener = dragListner;
        mContext = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        if (viewType == TYPE_ITEM) {
            //inflate your layout and pass it to view holder
            View v = mInflater.inflate(R.layout.item_my_video, viewGroup, false);
            return new VHItem(v);
        } else if (viewType == TYPE_FOOTER) {

            return new FooterViewHolder(mInflater.inflate(R.layout.drag_n_drop_footer, viewGroup, false));
        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");

    }

    @Override
    public int getItemViewType(int position) {
//        Log.e(TAG,"position : "+position);
//        Log.e(TAG,"data.size() : "+data.size());
        if (position == data.size() - 1) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final int i = position + 1;
        if (viewHolder instanceof VHItem) {

            final VHItem holder = (VHItem) viewHolder;

            if (!(data.get(i).getThumbnail_url()).equalsIgnoreCase("")) {
                Picasso.with(mContext)
                        .load(data.get(i).getThumbnail_url())
                        .placeholder(R.drawable.ic_profile)
                        .into(((VHItem) viewHolder).video_thumb);
            }
            holder.textViewTitle.setText(data.get(i).getWorkout_name());
            holder.card_view.setBackgroundColor(Color.parseColor(BaseLocalHelper.getCalegoryColor(data.get(i).getWork_out_code())));

//
//            ((VHItem) viewHolder).video_thumb.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
//                        mDragStartListener.onStartDrag(holder);
//                    }
//                    return false;
//                }
//            });

            ((VHItem) viewHolder).video_thumb.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    mDragStartListener.onStartDrag(holder);

                    return false;
                }
            });


            ((VHItem) viewHolder).video_thumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!data.get(i).getUrl().isEmpty() && !data.get(i).getUrl().equalsIgnoreCase("")) {
                        Intent intent = new Intent(mContext, VideoActivity.class);
                        intent.putExtra(VideoActivity.FILE_PATH, data.get(i).getUrl());
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }


    public class VHItem extends RecyclerView.ViewHolder implements View.OnClickListener, ItemTouchHelperViewHolder {
        public final View mView;
        public final CardView card_view;
        public final ImageView video_thumb;
        private TextView textViewTitle;
        public LocalVideo mItem;


        public VHItem(View itemView) {
            super(itemView);
            mView = itemView;
            card_view = mView.findViewById(R.id.card_view);
            video_thumb = mView.findViewById(R.id.video_thumb);
            textViewTitle = mView.findViewById(R.id.text_view_title);
            video_thumb.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }


    }

    @Override
    public void onItemDismiss(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        //Log.v("", "Log position" + fromPosition + " " + toPosition);
        if (fromPosition < data.size() && toPosition < data.size()) {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(data, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(data, i, i - 1);
                }
            }
            notifyItemMoved(fromPosition, toPosition);
        }
        return true;
    }

    public void updateList(ArrayList<Planner> list) {
        data = list;
        notifyDataSetChanged();
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}