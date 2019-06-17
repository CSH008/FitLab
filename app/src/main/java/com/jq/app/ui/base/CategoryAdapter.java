package com.jq.app.ui.base;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jq.app.R;
import com.jq.app.ui.my_workout.Workout;

import java.util.ArrayList;

/**
 * Created by Hasnain on 23-Feb-18.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.WorkoutViewHolder> {
    ArrayList<Category> categoryArrayList;
    Context mContext;
    WorkoutFragment.onCategorySelectionListner onCategorySelectionListner;

    public CategoryAdapter(Context mContext, ArrayList<Category> categoryArrayList, WorkoutFragment.onCategorySelectionListner onCategorySelectionListner) {
        this.mContext = mContext;
        this.categoryArrayList = categoryArrayList;
        this.onCategorySelectionListner = onCategorySelectionListner;
    }

    @Override
    public WorkoutViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new WorkoutViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_category, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(WorkoutViewHolder workoutViewHolder, final int i) {
        workoutViewHolder.tv_title.setText("" + categoryArrayList.get(i).getCat_name());
        if (categoryArrayList.get(i).isSelected_status()) {
            workoutViewHolder.tv_title.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.light_gray));
        } else {
            workoutViewHolder.tv_title.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.primary));
        }
        workoutViewHolder.tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCategorySelectionListner.onSelect(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    class WorkoutViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title;

        public WorkoutViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);

        }
    }
}
