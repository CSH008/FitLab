package com.jq.app.ui.my_media.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jq.app.R;
import com.jq.app.data.model.BaseModel;
import com.jq.app.data.model.LocalImage;
import com.jq.app.data.model.VideoModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link BaseModel} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyImageAdapter extends RecyclerView.Adapter<MyImageAdapter.ViewHolder> {

    private Context context;
    private List<LocalImage> mValues;
    protected OnListFragmentInteractionListener mListener;

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(int position);
        void onDeleteInteraction(int position);
    }

    public MyImageAdapter(List<LocalImage> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    public void setData(List<LocalImage> data) {
        this.mValues = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final LocalImage item = mValues.get(position);
        holder.mItem = item;

        //holder.video_thumb.setImageResource(item.image_resource);
        if(item.localPath!=null && !item.localPath.isEmpty()) {
            holder.imageView.setImageURI(Uri.parse(item.localPath));

        } else if (item.url != null && !item.url.isEmpty()) {
            Picasso.with(context).load(item.url).into(holder.imageView);
        }

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(position);
                }
            }
        });

        holder.card_view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != mListener) {
                    mListener.onDeleteInteraction(position);
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
        public final ImageView imageView;
        public LocalImage mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            card_view = view.findViewById(R.id.card_view);
            imageView = view.findViewById(R.id.imageView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mItem.title + "'";
        }
    }

}
