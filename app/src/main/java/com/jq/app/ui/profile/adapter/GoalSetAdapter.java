package com.jq.app.ui.profile.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
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
import com.jq.app.data.model.SetModel;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link BaseModel} and makes a call to the
 * specified {@link OnListItemInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class GoalSetAdapter extends RecyclerView.Adapter<GoalSetAdapter.ViewHolder> {

    private Context context;
    private List<SetModel> mValues;
    private final OnListItemInteractionListener mListener;

    public interface OnListItemInteractionListener {
        // TODO: Update argument type and name
        void onChooseVideo(SetModel item);
        void onClickedDelete(int position);
    }

    public GoalSetAdapter(List<SetModel> items, OnListItemInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    public void setData(List<SetModel> list) {
        mValues = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout_set_goal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SetModel item = (SetModel) mValues.get(position);
        holder.mItem = item;

        holder.txtNumber.setText("" + (position+1));
        if(item.reps>0) {
            holder.txtReps.setText("" + item.reps);
        }
        if(item.sets>0) {
            holder.txtSets.setText("" + item.sets);
        }
        if(item.time>0) {
            holder.txtTime.setText("" + item.time);
        }

        if(item.video_id != null && !item.video_id.isEmpty() && Integer.parseInt(item.video_id)>0) {
            holder.btnVideoChooser.setColorFilter(ContextCompat.getColor(context, R.color.primary), android.graphics.PorterDuff.Mode.MULTIPLY);
            holder.btnVideoChooser.setImageResource(R.mipmap.ic_video);
        } else {
            holder.btnVideoChooser.setColorFilter(ContextCompat.getColor(context, R.color.red), android.graphics.PorterDuff.Mode.MULTIPLY);
            holder.btnVideoChooser.setImageResource(R.mipmap.ic_video);
        }
        holder.btnVideoChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onChooseVideo(item);
            }
        });

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickedDelete(position);
            }
        });

        holder.txtReps.addTextChangedListener(new TextWatcher() {

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
                    item.reps = Integer.parseInt(s.toString());
            }
        });

        holder.txtSets.addTextChangedListener(new TextWatcher() {

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
                    item.sets = Integer.parseInt(s.toString());
            }
        });

        holder.txtTime.addTextChangedListener(new TextWatcher() {

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
                    item.time = Integer.parseInt(s.toString());
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
        public final EditText txtReps;
        public final EditText txtSets;
        public final EditText txtTime;
        public final ImageView btnVideoChooser;
        public final ImageView btn_delete;
        public SetModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            txtNumber = view.findViewById(R.id.txtNumber);
            txtReps = view.findViewById(R.id.txtReps);
            txtSets = view.findViewById(R.id.txtSets);
            txtTime = view.findViewById(R.id.txtTime);
            btnVideoChooser = view.findViewById(R.id.btnVideoChooser);
            btn_delete = view.findViewById(R.id.btn_delete);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mItem.order_number + "'";
        }
    }

}
