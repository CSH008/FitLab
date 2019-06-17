package com.jq.app.ui.search.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.daimajia.swipe.SwipeLayout;
import com.jq.app.R;
import com.jq.app.data.model.BaseModel;
import com.jq.app.data.model.Favorite;
import com.jq.app.data.model.VideoModel;
import com.jq.app.util.Common;
import com.jq.app.util.Config;
import com.jq.app.util.Constants;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link BaseModel} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private Context context;
    protected List<BaseModel> mValues;
    protected OnListFragmentInteractionListener mListener;

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(VideoModel item);
        void updateLikeStatus(final VideoModel item);
        void onDeleteFavorite(final VideoModel item);
    }

    public VideoAdapter(List<BaseModel> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final VideoModel item = (VideoModel) mValues.get(position);
        holder.mItem = item;

        //holder.video_thumb.setImageResource(item.image_resource);
        if (item.thumbnail_url != null && !item.thumbnail_url.isEmpty()) {
            Picasso.with(context).load(item.thumbnail_url).into(holder.video_thumb);
        }
        holder.title.setText(item.title);
        holder.description.setText(item.description);

        if (item.is_like) {
            holder.icon_like.setImageResource(R.drawable.ic_favorite);
        } else {
            holder.icon_like.setImageResource(R.drawable.ic_favorite_border);
        }

        holder.icon_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.updateLikeStatus(holder.mItem);
                }
            }
        });

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
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
        public final TextView title;
        public final TextView description;
        public final ImageView icon_like;
        public VideoModel mItem;

        public final SwipeLayout swipeContainer;
        public final ImageView mRemoveView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            card_view = (CardView) view.findViewById(R.id.card_view);
            video_thumb = (ImageView) view.findViewById(R.id.video_thumb);
            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.description);
            icon_like = view.findViewById(R.id.icon_like);

            swipeContainer = (SwipeLayout)view.findViewById(R.id.swipeContainer);
            swipeContainer.setSwipeEnabled(false);
            mRemoveView = (ImageView) view.findViewById(R.id.trash);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mItem.title + "'";
        }

    }

}
