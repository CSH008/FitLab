package com.jq.app.ui.profile.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jq.app.R;
import com.jq.app.data.model.BaseModel;
import com.jq.app.data.model.DayGoalModel;
import com.jq.app.data.model.SetModel;

import java.util.List;

public class DayGoalAdapter extends RecyclerView.Adapter<DayGoalAdapter.ViewHolder> {

    private Context context;
    private List<BaseModel> mValues;

    public DayGoalAdapter(List<BaseModel> items) {
        mValues = items;
    }

    public void updateTime(int position, int time) {
        SetModel item = (SetModel) mValues.get(position);
        item.time = time;
        notifyDataSetChanged();
    }

    public List<BaseModel> getData() {
        return mValues;
    }

    public void setData(List<BaseModel> list) {
        mValues = list;
        notifyDataSetChanged();
    }

    public void addItem(SetModel item) {
        mValues.add(item);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout_set_goal_week_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final DayGoalModel item = (DayGoalModel) mValues.get(position);
        holder.mItem = item;

        holder.txtNumber.setText("" + (position+1));
        if(item.exercise>0) {
            holder.txtExercise.setText("" + item.exercise);
        }
        if(item.mobility>0) {
            holder.txtMobility.setText("" + item.mobility);
        }
        if(item.stretching>0) {
            holder.txtStretching.setText("" + item.stretching);
        }

        holder.txtExercise.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0)
                    item.exercise = Integer.parseInt(s.toString());
            }
        });

        holder.txtMobility.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0)
                    item.mobility = Integer.parseInt(s.toString());
            }
        });

        holder.txtStretching.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0)
                        item.stretching = Integer.parseInt(s.toString());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView txtNumber;
        public final EditText txtExercise;
        public final EditText txtMobility;
        public final EditText txtStretching;
        public DayGoalModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            txtNumber = view.findViewById(R.id.txtNumber);
            txtExercise = view.findViewById(R.id.txtExercise);
            txtMobility = view.findViewById(R.id.txtMobility);
            txtStretching = view.findViewById(R.id.txtStretching);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mItem.date + "'";
        }

    }

}
