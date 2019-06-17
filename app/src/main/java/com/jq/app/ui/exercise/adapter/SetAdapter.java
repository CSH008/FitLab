package com.jq.app.ui.exercise.adapter;

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
import com.jq.app.data.model.SetModel;
import com.jq.app.data.model.VideoModel;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link BaseModel} and makes a call to the
 * specified {@link OnListItemInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class SetAdapter extends RecyclerView.Adapter<SetAdapter.ViewHolder> {

    private Context context;
    private List<BaseModel> mValues;
    private final OnListItemInteractionListener mListener;

    public interface OnListItemInteractionListener {
        // TODO: Update argument type and name
        void onClickedTimer(int position);
        void onClickedDelete(int position);
    }

    public SetAdapter(List<BaseModel> items, OnListItemInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    public void updateTime(int position, int time) {
        if(position>=mValues.size()) return;

        SetModel item = (SetModel) mValues.get(position);
        item.time = time;
        notifyDataSetChanged();
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
                .inflate(R.layout.item_layout_set, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SetModel item = (SetModel) mValues.get(position);
        holder.mItem = item;

        holder.txtNumber.setText("" + (position+1));
        item.order_number = position+1;
        if(item.reps>0) {
            holder.txtReps.setText("" + item.reps);
        }
        if(item.sets>0) {
            holder.txtSets.setText("" + item.sets);
        }
        if(item.time>0) {
            holder.txtTime.setText(getTimeStringFromSeconds(item.time));
        }

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


        holder.txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickedTimer(position);
            }
        });

        holder.btn_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickedTimer(position);
            }
        });

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickedDelete(position);
            }
        });

    }

    public String getTimeStringFromSeconds(int value) {
        /*String result = "";
        int mins = value / 60;
        int secs = value - mins * 60;

        if(mins>1) {
            result += mins + "mins";
        } else if(mins==1) {
            result += mins + "min";
        }

        if(secs>1) {
            result += " " + secs + "secs";
        } else if(secs==1) {
            result += " " + secs + "sec";
        }

        return result;*/
        return "" + value;
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
        public final TextView txtTime;
        public final ImageView btn_timer;
        public final ImageView btn_delete;
        public SetModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            txtNumber = view.findViewById(R.id.txtNumber);
            txtReps = view.findViewById(R.id.txtReps);
            txtSets = view.findViewById(R.id.txtSets);
            txtTime = view.findViewById(R.id.txtTime);
            btn_timer = view.findViewById(R.id.btn_timer);
            btn_delete = view.findViewById(R.id.btn_delete);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mItem.order_number + "'";
        }
    }

}
