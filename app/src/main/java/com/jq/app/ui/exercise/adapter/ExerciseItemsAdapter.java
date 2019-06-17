package com.jq.app.ui.exercise.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.Builder;
import com.afollestad.materialdialogs.MaterialDialog.InputCallback;
import com.github.mikephil.charting.data.LineRadarDataSet;
import com.jq.app.R;
import com.jq.app.ui.exercise.adapter.ItemMoveCallbackChild.ItemTouchHelperContract;
import com.jq.app.ui.exercise.pojo.ExerciseResponse.Data.Video;
import com.jq.app.ui.timer.SelectTime;
import com.jq.app.ui.timer.tabata.TabataActivity;

import java.util.ArrayList;
import java.util.Collections;

public class ExerciseItemsAdapter extends Adapter<ExerciseItemsAdapter.ViewHolder> implements ItemTouchHelperContract {
    String TAG = ExerciseItemsAdapter.class.getName();
    private Context context;
    private String headerName;
    private ArrayList<Video> mValues;
    boolean select = false;

    public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        public final LinearLayout llMainView;
        public final View lineView1;
        public final TextView c_1;
        public final TextView c_2;
        public final TextView c_3;
        public final TextView c_4;
        public final TextView c_5;
        public final View selected_view;
        public final ImageView timer_icon;

        public ViewHolder(View view) {
            super(view);
            this.llMainView = (LinearLayout) view.findViewById(R.id.llMainView);
            this.lineView1 = (View) view.findViewById(R.id.lineView1);
            this.c_1 = (TextView) view.findViewById(R.id.c_1);
            this.c_2 = (TextView) view.findViewById(R.id.c_2);
            this.c_3 = (TextView) view.findViewById(R.id.c_3);
            this.c_4 = (TextView) view.findViewById(R.id.c_4);
            this.c_5 = (TextView) view.findViewById(R.id.c_5);
            this.selected_view = view.findViewById(R.id.selected_view);
            this.timer_icon = view.findViewById(R.id.timer_icon);
            this.timer_icon.setVisibility(View.GONE);
        }
    }

    public ExerciseItemsAdapter(String headerName,ArrayList<Video> items) {
        this.headerName = headerName;
        this.mValues = items;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_exercise_item_view, parent, false));
    }

    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Video data = (Video) this.mValues.get(position);
        try {
            if (headerName.equalsIgnoreCase("Mobility")) {

                holder.llMainView.setBackgroundColor(context.getResources().getColor(R.color.colorMobility));
                holder.lineView1.setBackgroundColor(context.getResources().getColor(R.color.colorMobility));

            } else if (headerName.equalsIgnoreCase("Exercise")) {

                holder.llMainView.setBackgroundColor(context.getResources().getColor(R.color.colorExercise));
                holder.lineView1.setBackgroundColor(context.getResources().getColor(R.color.colorExercise));

            } else if (headerName.equalsIgnoreCase("Cardio")) {

                holder.llMainView.setBackgroundColor(context.getResources().getColor(R.color.colorCardio));
                holder.lineView1.setBackgroundColor(context.getResources().getColor(R.color.colorCardio));

            } else if (headerName.equalsIgnoreCase("Stretching")) {

                holder.llMainView.setBackgroundColor(context.getResources().getColor(R.color.colorStretching));
                holder.lineView1.setBackgroundColor(context.getResources().getColor(R.color.colorStretching));

            }

            holder.c_1.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
            holder.c_2.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
            holder.c_3.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
            holder.c_4.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
            holder.c_5.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));


            holder.c_1.setText(data.getTitle());

            holder.c_2.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    ExerciseItemsAdapter.this.setValues(position, 0, data.getHeader_values()[0]);
                }
            });
            holder.c_3.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    ExerciseItemsAdapter.this.setValues(position, 1, data.getHeader_values()[0]);
                }
            });
            holder.c_4.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    ExerciseItemsAdapter.this.setValues(position, 2, data.getHeader_values()[0]);
                }
            });
            holder.c_5.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    ExerciseItemsAdapter.this.setValues(position, 3, data.getHeader_values()[0]);
                }
            });

            holder.timer_icon.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {


                    final Dialog dialog = new Dialog(context);
                    try {

                        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        dialog.setContentView(R.layout.dialog_select_timer);
                        dialog.getWindow().setLayout(-1, -2);
                        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        dialog.setCancelable(false);

                        dialog.findViewById(R.id.amrap).setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                data.setHeader_values(new String[]{data.getHeader_values()[0], data.getHeader_values()[1], data.getHeader_values()[2], data.getHeader_values()[3], "1"});
                            }
                        });
                        dialog.findViewById(R.id.emom).setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                data.setHeader_values(new String[]{data.getHeader_values()[0], data.getHeader_values()[1], data.getHeader_values()[2], data.getHeader_values()[3], "2"});
                            }
                        });
                        dialog.findViewById(R.id.stop_watch).setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                data.setHeader_values(new String[]{data.getHeader_values()[0], data.getHeader_values()[1], data.getHeader_values()[2], data.getHeader_values()[3], "3"});
                            }
                        });
                        dialog.findViewById(R.id.tabata).setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                data.setHeader_values(new String[]{data.getHeader_values()[0], data.getHeader_values()[1], data.getHeader_values()[2], data.getHeader_values()[3], "4"});
                            }
                        });
                        dialog.findViewById(R.id.timer).setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                data.setHeader_values(new String[]{data.getHeader_values()[0], data.getHeader_values()[1], data.getHeader_values()[2], data.getHeader_values()[3], "5"});
                            }
                        });

                        dialog.show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });
            holder.c_2.setText(data.getHeader_values()[0]);
            holder.c_3.setText(data.getHeader_values()[1]);
            holder.c_4.setText(data.getHeader_values()[2]);
            holder.c_5.setText(data.getHeader_values()[3]);

            holder.c_1.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    String timerName = data.getHeader_values()[4].equalsIgnoreCase("1") ? "AMRAP" : data.getHeader_values()[4].equalsIgnoreCase("2") ? "EMOM" : data.getHeader_values()[4].equalsIgnoreCase("3") ? "STOPWATCH" : data.getHeader_values()[4].equalsIgnoreCase("4") ? "TABATA" : "TIMER";
                    Log.e(TAG, timerName);
                    Intent intent;
                    if (timerName.equalsIgnoreCase("TABATA")) {
                        intent = new Intent(ExerciseItemsAdapter.this.context, TabataActivity.class);
                    } else {
                        intent = new Intent(ExerciseItemsAdapter.this.context, SelectTime.class);
                    }
                    intent.putExtra("video_url", data.getUrl());
                    intent.putExtra("video_id", data.getVideo_id() + "");
                    intent.putExtra("sets", holder.c_2.getText().toString().trim());
                    intent.putExtra("reps", holder.c_4.getText().toString().trim());
                    intent.putExtra("thumbnail", data.getThumbnail_url());
                    intent.putExtra("name", timerName);
                    ExerciseItemsAdapter.this.context.startActivity(intent);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
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
        myViewHolder.selected_view.setBackgroundColor(this.context.getResources().getColor(R.color.selected));
    }

    public void onRowClear(ViewHolder myViewHolder) {
        myViewHolder.selected_view.setBackgroundColor(this.context.getResources().getColor(R.color.unselected));
        if (this.select) {
            notifyDataSetChanged();
        }
        this.select = false;
    }

    private void setValues(final int posi, final int column, String lastValues) {
        Builder builder = new Builder(this.context);

        if(column == 1 ){
            builder.title("Enter data");
            builder.inputType(1);

        }else  if(column == 2){
            builder.title("Enter data");
            builder.inputType(1);

        }else{
            builder.title("Enter Number");
            builder.inputType(2);
        }

        builder.input("", "", new InputCallback() {
            public void onInput(MaterialDialog dialog, CharSequence input) {
                if (input != null && !input.toString().isEmpty()) {
                    if (column == 0) {

                        ((Video) ExerciseItemsAdapter.this.mValues.get(posi)).setHeader_values(new String[]{input.toString(), ((Video) ExerciseItemsAdapter.this.mValues.get(posi)).getHeader_values()[1], ((Video) ExerciseItemsAdapter.this.mValues.get(posi)).getHeader_values()[2], ((Video) ExerciseItemsAdapter.this.mValues.get(posi)).getHeader_values()[3], ((Video) ExerciseItemsAdapter.this.mValues.get(posi)).getHeader_values()[4]});

                    } else if (column == 1) {
                        ((Video) ExerciseItemsAdapter.this.mValues.get(posi)).setHeader_values(new String[]{((Video) ExerciseItemsAdapter.this.mValues.get(posi)).getHeader_values()[0], input.toString(), ((Video) ExerciseItemsAdapter.this.mValues.get(posi)).getHeader_values()[2], ((Video) ExerciseItemsAdapter.this.mValues.get(posi)).getHeader_values()[3], ((Video) ExerciseItemsAdapter.this.mValues.get(posi)).getHeader_values()[4]});
                    } else if (column == 2) {
                        ((Video) ExerciseItemsAdapter.this.mValues.get(posi)).setHeader_values(new String[]{((Video) ExerciseItemsAdapter.this.mValues.get(posi)).getHeader_values()[0], ((Video) ExerciseItemsAdapter.this.mValues.get(posi)).getHeader_values()[1], input.toString(), ((Video) ExerciseItemsAdapter.this.mValues.get(posi)).getHeader_values()[3], ((Video) ExerciseItemsAdapter.this.mValues.get(posi)).getHeader_values()[4]});
                    } else if (column == 3) {
                        ((Video) ExerciseItemsAdapter.this.mValues.get(posi)).setHeader_values(new String[]{((Video) ExerciseItemsAdapter.this.mValues.get(posi)).getHeader_values()[0], ((Video) ExerciseItemsAdapter.this.mValues.get(posi)).getHeader_values()[1], ((Video) ExerciseItemsAdapter.this.mValues.get(posi)).getHeader_values()[2], input.toString(), ((Video) ExerciseItemsAdapter.this.mValues.get(posi)).getHeader_values()[4]});
                    }
                    ExerciseItemsAdapter.this.notifyItemChanged(posi);
                }
            }
        }).cancelable(false).show();
    }
}
