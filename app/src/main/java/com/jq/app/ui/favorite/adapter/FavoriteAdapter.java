package com.jq.app.ui.favorite.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.daimajia.swipe.SwipeLayout;
import com.jq.app.R;
import com.jq.app.data.model.BaseModel;
import com.jq.app.data.model.VideoModel;
import com.jq.app.ui.search.adapter.VideoAdapter;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link BaseModel} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class FavoriteAdapter extends VideoAdapter {

    public FavoriteAdapter(OnListFragmentInteractionListener listener) {
        super(new ArrayList<BaseModel>(), listener);
    }

    public void setData(ArrayList<BaseModel> data) {
        this.mValues = data;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);

        final VideoModel item = (VideoModel) mValues.get(position);
        holder.mItem = item;

        holder.icon_like.setVisibility(View.GONE);

        holder.swipeContainer.addDrag(SwipeLayout.DragEdge.Right, holder.swipeContainer.findViewById(R.id.bottom_wrapper));
        holder.swipeContainer.setSwipeEnabled(true);

        holder.mRemoveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null) {
                    mListener.onDeleteFavorite(item);
                }
            }
        });
    }

}
