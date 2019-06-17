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
import com.jq.app.data.model.LocalImage;
import com.jq.app.ui.share.model.Image;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link BaseModel} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyShareImageAdapter extends RecyclerView.Adapter<MyShareImageAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Image> mValues;
    protected OnListFragmentInteractionListener mListener;

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(int position);
        void onDeleteInteraction(int position);
    }

    public MyShareImageAdapter(ArrayList<Image> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    public void setData(ArrayList<Image> data) {
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
        final Image item = mValues.get(position);

        if (item.getUrl() != null && !item.getUrl().isEmpty()) {
            Picasso.with(context).load(item.getUrl()).into(holder.imageView);
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
    public int getItemCount()
    {
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
