package com.riontech.calendar.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riontech.calendar.Singleton;
import com.riontech.calendar.dao.SectionDataViewHolder;
import com.riontech.calendar.dao.SectionTitleViewHolder;
import com.riontech.calendar.fragment.addEventListener;
import com.riontech.calendar.utils.CalendarUtils;
import com.riontech.calendar.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import static android.view.View.GONE;

/**
 * Created by Dhaval Soneji on 4/4/16.
 */
public class CalendarDataAdapter extends RecyclerView
        .Adapter<RecyclerView
        .ViewHolder> {
    private static final String TAG = CalendarDataAdapter.class.getSimpleName();

    ArrayList<Object> mItems;
    private addEventListener mAddEventListener;
    private final int SECTION = 0, DATA = 1;

    public CalendarDataAdapter(addEventListener mAddEventListener, ArrayList<Object> items) {
        this.mItems = items;
        this.mAddEventListener = mAddEventListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case SECTION:
                View viewSection = inflater.inflate(R.layout.row_event_header, viewGroup, false);
                viewHolder = new SectionTitleViewHolder(viewSection);
                break;

            case DATA:
                View viewData = inflater.inflate(R.layout.row_event_desc, viewGroup, false);
                viewHolder = new SectionDataViewHolder(viewData);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case SECTION:
                SectionTitleViewHolder vh1 = (SectionTitleViewHolder) viewHolder;
                configureViewHolderSectionTitle(vh1, position);
                break;

            case DATA:
                SectionDataViewHolder vh2 = (SectionDataViewHolder) viewHolder;
                configureViewHolderSectionData(vh2, position);
                break;
        }
    }

    private void configureViewHolderSectionData(SectionDataViewHolder vh2, int position) {
        ArrayList<String> obj = (ArrayList<String>) mItems.get(position);
        if (obj.get(0) != "") {
            vh2.getTxtRemark().setText(obj.get(0));
            vh2.getTxtRemark().setVisibility(View.VISIBLE);
        }
        if (obj.get(1) != "") {
            vh2.getTxtSubject().setText(obj.get(1));
            vh2.getTxtSubject().setVisibility(View.VISIBLE);
        }
        if (obj.get(2) != "") {

            try {
                Date dateTemp = CalendarUtils.getCalendarDBFormat().parse(obj.get(2).toString());
                vh2.getTxtSubmissionDate().setText("Submission date: " + CalendarUtils.getCalendarDateFormat().format(dateTemp));

            } catch (ParseException e) {
                Log.e(TAG, e.getMessage(), e);
            }

            vh2.getTxtSubmissionDate().setVisibility(View.VISIBLE);
        }
        if (obj.get(3) != "") {
            vh2.getTxtTitle().setText(obj.get(3));
            vh2.getTxtTitle().setVisibility(View.VISIBLE);
        }
    }

    private void configureViewHolderSectionTitle(SectionTitleViewHolder vh1, final int position) {
        try {
            final Date dateTemp = CalendarUtils.getCalendarDBFormat().parse(Singleton.getInstance().getCurrentDate());
            if (position == 0) {
                final String meta_data[] = (mItems.get(position).toString()).split("\\$");
                vh1.getTxtSection().setText(meta_data[0] + " (" + CalendarUtils.getCalendarDateFormat().format(dateTemp) + ")");
                vh1.getTxtSectionTime().setText(meta_data[2]);
                vh1.getButtonSection().setImageResource(R.drawable.ic_exposure_plus_1);
                vh1.getButtonEdit().setImageResource(android.R.color.transparent);
                vh1.getButtonSection().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAddEventListener.onTapAddOneMoreClick(CalendarUtils.getCalendarDateFormat().format(dateTemp));
                    }
                });

//                vh1.getButtonSection().setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View view) {
//
//                        mAddEventListener.onTapDelete(CalendarUtils.getCalendarDateFormat().format(dateTemp),meta_data[2]);
//
//                        return true;
//                    }
//                });


                vh1.getButtonEdit().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /**
                         * do nothing
                         */
                    }
                });

            } else {
                try {
                    final String meta_data[] = (mItems.get(position).toString()).split("\\$");
                    vh1.getTxtSection().setText(meta_data[0]);
                    vh1.getTxtSectionTime().setText(meta_data[2]);
                    vh1.getButtonSection().setImageResource(R.drawable.ic_delete_forever);
                    vh1.getButtonEdit().setImageResource(R.drawable. ic_edit);
                    vh1.getButtonSection().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mAddEventListener.onTapDelete(CalendarUtils.getCalendarDateFormat().format(dateTemp),meta_data[2]);
                        }
                    });


                    vh1.getButtonEdit().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String event_type = meta_data[0];
                            String event_time= meta_data[2];
                            String event_date = CalendarUtils.getCalendarDateFormat().format(dateTemp);
                            mAddEventListener.onTapToEdit(event_type,"","",event_date,event_time);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

//            final String meta_data[] = (mItems.get(position).toString()).split("\\$");
//            vh1.getButtonEdit().setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    String event_type = meta_data[0];
////                    String event_title = mItems.get(position).toString();
////                    String event_about = mItems.get(position).toString();;
//                    String event_date = CalendarUtils.getCalendarDateFormat().format(dateTemp);
//                    String event_time= meta_data[2];
//
//                    mAddEventListener.onTapToEdit(event_type,"","",event_date,event_time);
//                }
//            });


        } catch (ParseException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    public int getItemCount() {
        return this.mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mItems.get(position) instanceof String) {
            return SECTION;
        } else if (mItems.get(position) instanceof ArrayList) {
            return DATA;
        }
        return -1;
    }
}
