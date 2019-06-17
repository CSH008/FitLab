package com.jq.app.ui.home;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jq.app.R;
import com.jq.app.data.model.MainCategoryModel;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link MainCategoryModel} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MainCategoryAdapter extends RecyclerView.Adapter<MainCategoryAdapter.ViewHolder> {

    private Context context;
    private final List<MainCategoryModel> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Typeface Caviar_Dreams_Regular;

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(MainCategoryModel item);
    }

    public MainCategoryAdapter(Typeface Caviar_Dreams_Regular,List<MainCategoryModel> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        this.Caviar_Dreams_Regular = Caviar_Dreams_Regular;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MainCategoryModel item = (MainCategoryModel) mValues.get(position);
        holder.mItem = item;

        holder.category_image.setImageResource(item.image_resource);
        holder.category_title.setText(item.title);

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
        public final ImageView category_image;
        public final TextView category_title;
        public MainCategoryModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            card_view = (CardView) view.findViewById(R.id.card_view);
            category_image = (ImageView) view.findViewById(R.id.category_image);
            category_title = (TextView) view.findViewById(R.id.category_title);
            category_title.setTypeface(Caviar_Dreams_Regular);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mItem.title + "'";
        }
    }

}
