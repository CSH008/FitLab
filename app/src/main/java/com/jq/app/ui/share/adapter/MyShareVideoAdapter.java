package com.jq.app.ui.share.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jq.app.R;
import com.jq.app.data.model.BaseModel;
import com.jq.app.data.model.LocalVideo;
import com.jq.app.ui.share.model.Video;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link BaseModel} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyShareVideoAdapter extends RecyclerView.Adapter<MyShareVideoAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Video> mValues;
    protected OnListFragmentInteractionListener mListener;

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Video item);
        void onDeleteInteraction(Video item);
    }

    public MyShareVideoAdapter(ArrayList<Video> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    public void setData(ArrayList<Video> data) {
        this.mValues = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Video item = (Video) mValues.get(position);

        //holder.video_thumb.setImageResource(item.image_resource);
        if (item.getThumbnail_url() != null && !item.getThumbnail_url().isEmpty()) {
            Picasso.with(context).load(item.getThumbnail_url()).into(holder.video_thumb);
        }

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(mValues.get(position));
                }
            }
        });

        holder.card_view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != mListener) {
                    mListener.onDeleteInteraction(mValues.get(position));
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final CardView card_view;
        public final ImageView video_thumb;
        public LocalVideo mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            card_view = (CardView) view.findViewById(R.id.card_view);
            video_thumb = (ImageView) view.findViewById(R.id.video_thumb);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mItem.title + "'";
        }
    }

}
