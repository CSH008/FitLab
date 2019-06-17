package com.jq.app.ui.my_workout;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jq.app.R;

import java.util.ArrayList;

/**
 * Created by Hasnain on 23-Feb-18.
 */

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {
    private ArrayList<Workout> arrayList;
    private ListSelect mListSelect;

    public interface ListSelect {
        void onListSelected(Workout workout);
        void onLongPress(Workout workout);
    }

    public WorkoutAdapter(ArrayList<Workout> arrayList, ListSelect mListSelect) {
        this.arrayList = arrayList;
        this.mListSelect = mListSelect;
    }

    @Override
    public WorkoutViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new WorkoutViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_my_workout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(WorkoutViewHolder workoutViewHolder, int i) {
        workoutViewHolder.bind(arrayList.get(i),mListSelect);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class WorkoutViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rl_row;
        private TextView tv_title;
        private TextView tv_no_of_video;

        public WorkoutViewHolder(View itemView) {
            super(itemView);

            rl_row = itemView.findViewById(R.id.rl_row);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_no_of_video = itemView.findViewById(R.id.tv_no_of_video);
        }

        public void bind(final Workout workout, final ListSelect mListSelect) {
            tv_title.setText(workout.getTitle());
            tv_no_of_video.setText(workout.getNoOfVideo());

            rl_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListSelect.onListSelected(workout);
                }
            });

            rl_row.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mListSelect.onLongPress(workout);
                    return true;
                }
            });
        }
    }
}
