package com.jq.app.ui.exercise.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.Builder;
import com.afollestad.materialdialogs.MaterialDialog.InputCallback;
import com.jq.app.R;
import com.jq.app.ui.exercise.adapter.ItemMoveCallback.ItemTouchHelperContract;
import com.jq.app.ui.exercise.pojo.ExerciseResponse.Data;
import com.jq.app.ui.exercise.pojo.ExerciseResponse.Data.Video;

import java.util.ArrayList;
import java.util.Collections;

public class ExerciseAdapter extends Adapter<ExerciseAdapter.ViewHolder> implements ItemTouchHelperContract {
    private Context context;
    private ExerciseItemsAdapter exerciseItemsAdapter;
    private ArrayList<Data> mValues;
    boolean select = false;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView h_1;
        public final TextView h_2;
        public final TextView h_3;
        public final TextView h_4;
        public final TextView h_5;
        public final TextView header_name;
        public final View rowView;
        public final LinearLayout llBase;
        public final RecyclerView item_list;

        public ViewHolder(View view) {
            super(view);
            this.header_name = (TextView) view.findViewById(R.id.header_name);
            this.llBase = (LinearLayout) view.findViewById(R.id.llBase);
            this.rowView = (View) view.findViewById(R.id.rowView);
            this.h_1 = (TextView) view.findViewById(R.id.h_1);
            this.h_2 = (TextView) view.findViewById(R.id.h_2);
            this.h_3 = (TextView) view.findViewById(R.id.h_3);
            this.h_4 = (TextView) view.findViewById(R.id.h_4);
            this.h_5 = (TextView) view.findViewById(R.id.h_5);
            this.item_list = (RecyclerView) view.findViewById(R.id.item_list);
        }
    }

    public ExerciseAdapter(ArrayList<Data> items) {
        this.mValues = items;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_exercise_header_view, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        Data data = (Data) this.mValues.get(position);

        holder.header_name.setText(getName(data.getWork_out_code()).toUpperCase());
        if (getName(data.getWork_out_code()).equalsIgnoreCase("Mobility")) {

            holder.header_name.setBackgroundColor(context.getResources().getColor(R.color.colorMobility));
            holder.rowView.setBackgroundColor(context.getResources().getColor(R.color.colorMobility));
            holder.llBase.setBackgroundColor(context.getResources().getColor(R.color.colorMobility));

            /**
             * set Header
             */
            holder.h_2.setText("Sets");
            holder.h_3.setText("Both");
            holder.h_4.setText("R/L");

        } else if (getName(data.getWork_out_code()).equalsIgnoreCase("Exercise")) {

            holder.header_name.setBackgroundColor(context.getResources().getColor(R.color.colorExercise));
            holder.rowView.setBackgroundColor(context.getResources().getColor(R.color.colorExercise));
            holder.llBase.setBackgroundColor(context.getResources().getColor(R.color.colorExercise));

            /**
             * set Header
             */
            holder.h_2.setText("Sets");
            holder.h_3.setText("Reps");
            holder.h_4.setText("Lbs");

        } else if (getName(data.getWork_out_code()).equalsIgnoreCase("Cardio")) {

            holder.header_name.setBackgroundColor(context.getResources().getColor(R.color.colorCardio));
            holder.rowView.setBackgroundColor(context.getResources().getColor(R.color.colorCardio));
            holder.llBase.setBackgroundColor(context.getResources().getColor(R.color.colorCardio));

            /**
             * set Header
             */
            holder.h_2.setText("Sets");
            holder.h_3.setText("Reps");
            holder.h_4.setText("Distance");

        } else if (getName(data.getWork_out_code()).equalsIgnoreCase("Stretching")) {

            holder.header_name.setBackgroundColor(context.getResources().getColor(R.color.colorStretching));
            holder.rowView.setBackgroundColor(context.getResources().getColor(R.color.colorStretching));
            holder.llBase.setBackgroundColor(context.getResources().getColor(R.color.colorStretching));

            /**
             * set Header
             */
            holder.h_2.setText("Sets");
            holder.h_3.setText("Both");
            holder.h_4.setText("R/L");

        }

        holder.item_list.setLayoutManager(new LinearLayoutManager(this.context));
        this.exerciseItemsAdapter = new ExerciseItemsAdapter(getName(data.getWork_out_code()),data.getVideo());

        new ItemTouchHelper(new ItemMoveCallbackChild(this.exerciseItemsAdapter)).attachToRecyclerView(holder.item_list);
        holder.item_list.setAdapter(this.exerciseItemsAdapter);

        holder.h_2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ExerciseAdapter.this.setValues(position, 0, "");
            }
        });
        holder.h_3.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ExerciseAdapter.this.setValues(position, 1, "");
            }
        });
        holder.h_4.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ExerciseAdapter.this.setValues(position, 2, "");
            }
        });
        holder.h_5.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ExerciseAdapter.this.setValues(position, 3, "");
            }
        });
    }

    public String getName(int name) {
        switch (name) {
            case 1:
                return "Mobility";
            case 2:
                return "Stretching";
            case 3:
                return "Exercise";
//            case 4:
//                return "Baseball";
//            case 5:
//                return "Sport";
            default:
                return "Cardio";
        }
    }

    public int getItemCount() {
        return this.mValues.size();
    }

    public void onRowMoved(int fromPosition, int toPosition) {
        this.select = true;
        int i;
        if (fromPosition < toPosition) {
            for (i = fromPosition; i < toPosition; i++) {
                Collections.swap(this.mValues, i, i + 1);
            }
        } else {
            for (i = fromPosition; i > toPosition; i--) {
                Collections.swap(this.mValues, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    public void onRowSelected(ViewHolder myViewHolder) {
        myViewHolder.header_name.setBackgroundColor(-7829368);
    }

    public void onRowClear(ViewHolder myViewHolder) {
        myViewHolder.header_name.setBackgroundColor(this.context.getResources().getColor(R.color.green_header));
        if (this.select) {
            notifyDataSetChanged();
        }
        this.select = false;
    }

    private void setValues(final int posi, final int column, String lastValues) {
        new Builder(this.context).title("Enter Number").inputType(2).input("", "", new InputCallback() {
            public void onInput(MaterialDialog dialog, CharSequence input) {
                if (input != null && !input.toString().isEmpty()) {
                    ArrayList<Video> arrayList = ((Data) ExerciseAdapter.this.mValues.get(posi)).getVideo();
                    for (int i = 0; i < arrayList.size(); i++) {
                        Video video = (Video) arrayList.get(i);
                        if (column == 0) {
                            video.setHeader_values(new String[]{input.toString(), video.getHeader_values()[1], video.getHeader_values()[2], video.getHeader_values()[3]});
                        } else if (column == 1) {
                            video.setHeader_values(new String[]{video.getHeader_values()[0], input.toString(), video.getHeader_values()[2], video.getHeader_values()[3]});
                        } else if (column == 2) {
                            video.setHeader_values(new String[]{video.getHeader_values()[0], video.getHeader_values()[1], input.toString(), video.getHeader_values()[3]});
                        } else if (column == 3) {
                            video.setHeader_values(new String[]{video.getHeader_values()[0], video.getHeader_values()[1], video.getHeader_values()[2], input.toString()});
                        }
                    }
                    ExerciseAdapter.this.notifyItemChanged(posi);
                }
            }
        }).cancelable(false).show();
    }
}
