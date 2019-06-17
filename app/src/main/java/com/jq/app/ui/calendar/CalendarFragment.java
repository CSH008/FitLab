package com.jq.app.ui.calendar;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.jq.app.App;
import com.jq.app.R;
import com.jq.app.util.Config;
import com.jq.app.util.base.BaseFragment;
import com.riontech.calendar.CustomCalendar;
import com.riontech.calendar.dao.EventData;
import com.riontech.calendar.dao.dataAboutDate;
import com.riontech.calendar.fragment.addEventListener;
import com.riontech.calendar.utils.CalendarUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Time;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

/**
 * Created by Hasnain on 16-Feb-18.
 */

public class CalendarFragment extends BaseFragment implements addEventListener {
    private static final String TAG = CalendarFragment.class.getSimpleName();

    // TODO: Rename parameter arguments, choo se names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CustomCalendar customCalendar;
    //    private MyLocalDB myLocalDB;
    private View mView;
    private ImageView iv_next, iv_previous;
    private static final String DATE_FORMATTER = "yyyy-MM-dd";
    private static final String TIME_FORMATTER = "hh:mm a";
    private static final String DATE_TIME_FORMATTER = "yyyy-MM-dd HH:mm";
    private ProgressDialog progressDialog;

    public CalendarFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        //setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         * Inflate the layout for this fragment
         */
        if (mView == null)
            mView = inflater.inflate(R.layout.fragment_calendar, container, false);

        iv_next = mView.findViewById(R.id.iv_next);
        iv_previous = mView.findViewById(R.id.iv_previous);

        customCalendar = mView.findViewById(R.id.customCalendar);
        customCalendar.initListener(this);

        /**
         * get database object
         */
//        myLocalDB = MyLocalDB.getInstance(getActivity());

        /**
         * setup progressbar
         */
        setupProgressBar();

        /**
         * get saved event
         */
        getEventFromServer(false, "");


        /**
         * set page next
         */
        iv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customCalendar.nextMonth();
            }
        });

        /**
         * set page previous
         */
        iv_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customCalendar.previousMonth();
            }
        });


        return mView;
    }


    public void setupProgressBar() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait....");
        progressDialog.setCancelable(false);
    }

//    private void setEventInCalendar() {
////        ArrayList<CountModel> countArrayList = myLocalDB.getCount();
////        for (int i = 0; i < countArrayList.size(); i++) {
////            CountModel countModel = countArrayList.get(i);
////            customCalendar.addAnEvent(countModel.getDate(), 1, getSavedEvent(countModel.getDate()));
////
////        }
//
//
//        getEventFromServer();
//
//    }

    @Override
    public void onTapToAddClick(String selectedDate) {
        /**
         * convert date form
         */
        String eventDate = new SimpleDateFormat(DATE_FORMATTER).format(new Date(selectedDate));
        openPopupForEventDetails(eventDate);
    }

    @Override
    public void onTapAddOneMoreClick(String selectedDate) {
        /**
         * convert date form
         */
        String eventDate = new SimpleDateFormat(DATE_FORMATTER).format(new Date(selectedDate));
        openPopupForEventDetails(eventDate);
        Toast.makeText(getActivity(), selectedDate, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTapDelete(final String date, final String event_time) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage("Are you sure you want to delete?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                        deleteEventFromServer(date, event_time);
//                        if (myLocalDB.delete(event_id)) {
//                            setEventInCalendar();
//                            Toast.makeText(getActivity(), "Deleted!", Toast.LENGTH_SHORT).show();
//
//
//                            /**
//                             * reload event
//                             */
//                            String eventDate = new SimpleDateFormat(DATE_FORMATTER).format(new Date(date));
//                            customCalendar.refreshCalendar(eventDate);
//
//                        } else {

//                        }


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        builder.create().show();

    }

    @Override
    public void onTapToEdit(String event_type, String event_title, String event_about, String date, String event_time) {
        final String event_date = (new SimpleDateFormat(DATE_FORMATTER).format(new Date(date))).trim();
//        Log.e(TAG, "onTapToEdit");
//        Log.e(TAG, "event_type : " + event_type);
//        Log.e(TAG, "event_title : " + event_title);
//        Log.e(TAG, "event_about : " + event_about);
//        Log.e(TAG, "event_date : " + event_date);
//        Log.e(TAG, "event_time : " + event_time);

        openEditEventPopup(event_type, event_title, event_about, event_date, event_time);

    }

    public void openPopupForEventDetails(final String selected_date) {
        final Dialog dialog = new Dialog(getActivity());
        try {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(R.layout.dialog_get_event);
            dialog.getWindow().setLayout(-1, -2);
            dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false);

            final AutoCompleteTextView eventTypeCompleteTextView = dialog.findViewById(R.id.act_event_type);
            final AutoCompleteTextView locationCompleteTextView = dialog.findViewById(R.id.act_location);
            final AutoCompleteTextView aboutCompleteTextView = dialog.findViewById(R.id.act_about);
            final TextView tv_select_time = dialog.findViewById(R.id.tv_select_time);

            /**
             * select date from picker
             */
            tv_select_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(dialog.getContext(), R.style.datepicker, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                            String AM_PM;
//                            if(selectedHour < 12){
//                                AM_PM = "AM";
//                            }else{
//                                AM_PM = "PM";
//                            }
//                            if(selectedHour > 12){
//                                selectedHour = selectedHour - 12;
//                            }
//
//                            String hour, minute;
//                            if (selectedHour < 10)
//                                hour = "0" + selectedHour;
//                            else
//                                hour = "" + selectedHour + "";
//
//                            if (selectedMinute < 10)
//                                minute = "0" + selectedMinute;
//                            else
//                                minute = "" + selectedMinute + "";
//                            tv_select_time.setText(hour + ":" + minute+" "+AM_PM);

                            tv_select_time.setText(getTime(selectedHour, selectedMinute));


                        }
                    }, hour, minute, false);
                    mTimePicker.setTitle("Select Event Time");
                    mTimePicker.show();


                }
            });

            /**
             * Close button
             */
            dialog.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            /**
             * Check button
             */
            dialog.findViewById(R.id.btn_check).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String type = eventTypeCompleteTextView.getText().toString().trim();
                    String location = locationCompleteTextView.getText().toString().trim();
                    String about = aboutCompleteTextView.getText().toString().trim();
                    String selected_time = tv_select_time.getText().toString().trim();

                    if (type.equalsIgnoreCase("")) {
                        Toast.makeText(mActivity, "Please set event type!", Toast.LENGTH_SHORT).show();

                    } else if (location.equalsIgnoreCase("")) {
                        Toast.makeText(mActivity, "Enter Location!", Toast.LENGTH_SHORT).show();

                    } else if (about.equalsIgnoreCase("")) {
                        Toast.makeText(mActivity, "Write something about event!", Toast.LENGTH_SHORT).show();

                    } else if (selected_time.equalsIgnoreCase("")) {
                        Toast.makeText(mActivity, "Select time for event!", Toast.LENGTH_SHORT).show();

                    } else {
                        /**
                         * check for valid event
                         */

//                        Log.e(TAG, "Selected date: " + selected_date + " Selected time: " + selected_time);
//                        String currentDateTimeString[] = (new SimpleDateFormat("yyyy-MM-dd$HH:mm").format(new Date())).split("\\$");
//                        String current_date = currentDateTimeString[0];
//                        String current_time = currentDateTimeString[1];
//                        Log.e(TAG, "Current date: " + current_date + " Current time: " + current_time);

                        /**
                         * compare date and time
                         */
                        try {
                            SimpleDateFormat selected_sdf = new SimpleDateFormat(DATE_TIME_FORMATTER);
                            Date selectedDate = selected_sdf.parse(selected_date + " " + selected_time);

                            if ((System.currentTimeMillis() + (60 * 60 * 1000)) > selectedDate.getTime()) {
                                Toast.makeText(getActivity(), "Add event at least an hour before!", Toast.LENGTH_SHORT).show();

                            } else {
                                /**
                                 * save event
                                 */
                                saveEventToServer(type, location, about, selected_date, selected_time);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        dialog.dismiss();
                    }
                }
            });
            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void openEditEventPopup(String event_type, String event_title, String event_about, final String selected_date, final String old_event_time) {
        final Dialog dialog = new Dialog(getActivity());
        try {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(R.layout.dialog_get_event);
            dialog.getWindow().setLayout(-1, -2);
            dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false);

            final AutoCompleteTextView eventTypeCompleteTextView = dialog.findViewById(R.id.act_event_type);
            eventTypeCompleteTextView.setText(event_type);

            final AutoCompleteTextView locationCompleteTextView = dialog.findViewById(R.id.act_location);
            locationCompleteTextView.setText(event_title);

            final AutoCompleteTextView aboutCompleteTextView = dialog.findViewById(R.id.act_about);
            aboutCompleteTextView.setText(event_about);

            final TextView tv_select_time = dialog.findViewById(R.id.tv_select_time);
            tv_select_time.setText(old_event_time);

            /**
             * select date from picker
             */
            tv_select_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(dialog.getContext(), R.style.datepicker, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            tv_select_time.setText(getTime(selectedHour, selectedMinute));
                        }
                    }, hour, minute, false);
                    mTimePicker.setTitle("Select Event Time");
                    mTimePicker.show();


                }
            });

            /**
             * Close button
             */
            dialog.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            /**
             * Check button
             */
            dialog.findViewById(R.id.btn_check).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String type = eventTypeCompleteTextView.getText().toString().trim();
                    String location = locationCompleteTextView.getText().toString().trim();
                    String about = aboutCompleteTextView.getText().toString().trim();
                    String selected_time = tv_select_time.getText().toString().trim();

                    if (type.equalsIgnoreCase("")) {
                        Toast.makeText(mActivity, "Please set event type!", Toast.LENGTH_SHORT).show();

                    } else if (location.equalsIgnoreCase("")) {
                        Toast.makeText(mActivity, "Enter Location!", Toast.LENGTH_SHORT).show();

                    } else if (about.equalsIgnoreCase("")) {
                        Toast.makeText(mActivity, "Write something about event!", Toast.LENGTH_SHORT).show();

                    } else if (selected_time.equalsIgnoreCase("")) {
                        Toast.makeText(mActivity, "Select time for event!", Toast.LENGTH_SHORT).show();

                    } else {
                        /**
                         * compare date and time
                         */
                        try {
                            SimpleDateFormat selected_sdf = new SimpleDateFormat(DATE_TIME_FORMATTER);
                            Date selectedDate = selected_sdf.parse(selected_date + " " + selected_time);

                            if ((System.currentTimeMillis() + (60 * 60 * 1000)) > selectedDate.getTime()) {
                                Toast.makeText(getActivity(), "Add event at least an hour before!", Toast.LENGTH_SHORT).show();

                            } else {
                                /**
                                 * save event
                                 */
                                updateEventOnServer(type, location, about, selected_date, old_event_time,selected_time);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        dialog.dismiss();
                    }
                }
            });
            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void updateEventOnServer(final String event_type, final String event_title, final String event_about, final String event_date, final String event_time,String event_time_updated) {
        progressDialog.show();

        Log.e(TAG, "event_type : " + event_type);
        Log.e(TAG, "event_title : " + event_title);
        Log.e(TAG, "event_about : " + event_about);
        Log.e(TAG, "event_date : " + event_date);
        Log.e(TAG, "event_time : " + event_time);
        Log.e(TAG, "event_time_updated : " + event_time_updated);

        AndroidNetworking.post(Config.EDIT_EVENT)
                .addBodyParameter("email", App.my_email)
                .addBodyParameter("event_type", event_type)
                .addBodyParameter("event_title", event_title)
                .addBodyParameter("event_about", event_about)
                .addBodyParameter("event_date", event_date)
                .addBodyParameter("event_time", event_time)
                .addBodyParameter("event_time_updated", event_time_updated)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "Response : " + response);
                            int status = response.getInt("status");
                            if (status == 1) {


                                getEventFromServer(true, event_date);

                                Toast.makeText(mActivity, response.getString("message"), Toast.LENGTH_LONG).show();

                                /**
                                 * reload event
                                 */
                                customCalendar.refreshCalendar(event_date);

                            } else {
                                Toast.makeText(mActivity, response.getString("message"), Toast.LENGTH_LONG).show();
                            }


                            progressDialog.dismiss();

                        } catch (Exception e) {
                            progressDialog.dismiss();

                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(mActivity, R.string.msg_network_error, Toast.LENGTH_LONG).show();
                    }
                });

    }

    public ArrayList<EventData> getSavedEvent(JSONArray eventJsonArray) {
        ArrayList<EventData> eventDataList = new ArrayList();
        try {

            for (int j = 0; j < eventJsonArray.length(); j++) {
                JSONObject jsonObject = eventJsonArray.getJSONObject(j);
                ArrayList<dataAboutDate> dataAboutDates = new ArrayList();
                EventData dateData = new EventData();

                String event_type = jsonObject.getString("event_type");
                String event_time = jsonObject.getString("event_time");
                String event_title = jsonObject.getString("event_title");
                String event_about = jsonObject.getString("event_about");

                dateData.setSection(event_type + "$" + "404" + "$" + event_time);


                dataAboutDate dataAboutDate = new dataAboutDate();
                dataAboutDate.setTitle(event_title);
                dataAboutDate.setSubject(event_about);
                dataAboutDates.add(dataAboutDate);

                dateData.setData(dataAboutDates);
                eventDataList.add(dateData);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return eventDataList;
    }

//    public ArrayList<EventData> getSavedEvent(String date) {
//
//        ArrayList<EventData> eventDataList = new ArrayList();
//
//        ArrayList<EventModel> dataArrayList = myLocalDB.getEventByDate(date);
//        for (int i = 0; i < dataArrayList.size(); i++) {
//            EventModel eventModel = dataArrayList.get(i);
//            EventData dateData = new EventData();
//            ArrayList<dataAboutDate> dataAboutDates = new ArrayList();
//
//            dateData.setSection(eventModel.getTitle() + "$" + eventModel.getId() + "$" + eventModel.getTime());
//
//            dataAboutDate dataAboutDate = new dataAboutDate();
//            dataAboutDate.setTitle(eventModel.getSubject());
//            dataAboutDate.setSubject(eventModel.getDescription());
//            dataAboutDates.add(dataAboutDate);
//
//            dateData.setData(dataAboutDates);
//            eventDataList.add(dateData);
//        }
//
//
//        return eventDataList;
//    }

//    private String getCalendarUriBase(Activity act) {
//        String calendarUriBase = null;
//        Uri calendars = getCalendarURI(false);//Uri.parse("content://calendar/calendars");
//        calendarUriBase = calendars.toString();
////        Cursor managedCursor = null;
////        try {
////            managedCursor = act.managedQuery(calendars, null, null, null, null);
////        } catch (Exception e) {
////        }
////        if (managedCursor != null) {
////            calendarUriBase = "content://calendar/";
////        } else {
////            calendars = Uri.parse("content://com.android.calendar/calendars");
////            try {
////                managedCursor = act.managedQuery(calendars, null, null, null, null);
////            } catch (Exception e) {
////            }
////            if (managedCursor != null) {
////                calendarUriBase = "content://com.android.calendar/";
////            }
////        }
//        return calendarUriBase;
//    }


//    private Uri getCalendarURI(boolean eventUri) {
//        Uri calendarURI = null;
//
//        if (android.os.Build.VERSION.SDK_INT <= 18) {
//            calendarURI = (eventUri) ? Uri.parse("content://calendar/events") : Uri.parse("content://calendar/calendars");
//        } else {
//            calendarURI = (eventUri) ? Uri.parse("content://com.android.calendar/events") : Uri.parse("content://com.android.calendar/calendars");
//        }
//        return calendarURI;
//    }

    private String getTime(int hr, int min) {
        Time tme = new Time(hr, min, 0);//seconds by default set to zero
        Format formatter;
        formatter = new SimpleDateFormat(TIME_FORMATTER);
        return formatter.format(tme);
    }

    private void saveEventToServer(final String event_type, final String event_title, final String event_about, final String event_date, final String event_time) {
        progressDialog.show();

        AndroidNetworking.post(Config.SAVE_EVENT)
                .addBodyParameter("event_type", event_type)
                .addBodyParameter("event_title", event_title)
                .addBodyParameter("event_about", event_about)
                .addBodyParameter("event_date", event_date) //02/27/2018
                .addBodyParameter("event_time", event_time) //12:12:12
                .addBodyParameter("email", App.my_email)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            int status = response.getInt("status");
                            if (status == 1) {


//                                long result = myLocalDB.insert(event_type, event_title, event_about, event_date, event_time);
//                                if (result > 0) {
                                getEventFromServer(true, event_date);

                                Toast.makeText(getActivity(), "Event added : ", Toast.LENGTH_SHORT).show();
//                                    Log.e(TAG, "Result : " + result);

                                /**
                                 * reload event
                                 */
                                customCalendar.refreshCalendar(event_date);

                                /**
                                 * add reminder for this
                                 */
//                                    Calendar cal = Calendar.getInstance();
//                                    Uri EVENTS_URI = Uri.parse(getCalendarUriBase(getActivity()) + "/events");
//                                    ContentResolver cr = getActivity().getContentResolver();
//                                    ContentValues values = new ContentValues();
//                                    values.put("calendar_id", 1);
//                                    values.put("title", event_type);
//                                    values.put("allDay", 0);
//                                    values.put("dtstart", cal.getTimeInMillis() + 60 * 1000); // event starts at 11 minutes from now
////                                    values.put("dtstart", selectedDate.getTime()); // event starts at 11 minutes from now
////                                    values.put("dtend", ( cal.getTimeInMillis() + 1*60*1000); // ends 60 minutes from now
//
//
//                                    SimpleDateFormat selected_sdf = new SimpleDateFormat(DATE_TIME_FORMATTER);
//                                    Date selectedDate = selected_sdf.parse(event_date + " " + event_time);
//
//                                    values.put("dtend", (selectedDate.getTime()) + 60 * 60 * 1000); // ends 60 minutes from now
//                                    values.put("description", event_title + "\n" + event_about);
//                                    values.put("visibility", 0);
//                                    values.put("hasAlarm", 1);
//                                    Uri event = cr.insert(EVENTS_URI, values);
//
//                                    /**
//                                     * reminder insert
//                                     */
//                                    Uri REMINDERS_URI = Uri.parse(getCalendarUriBase(getActivity()) + "/reminders");
//                                    values = new ContentValues();
//                                    values.put("event_id", Long.parseLong(event.getLastPathSegment()));
//                                    values.put("method", 1);
//                                    values.put("minutes", 10);
//                                    cr.insert(REMINDERS_URI, values);


//                                Calendar cal = Calendar.getInstance();
//
//                                long l = cal.getTimeInMillis();
//
//                                long cal_Id = 1;
//
//                                // Also Here Use Cal_Id = 1 not parse another value**
//
//                                ContentResolver CR = getActivity().getContentResolver();
//                                ContentValues calEvent = new ContentValues();
//
//                                calEvent.put(CalendarContract.Events.CALENDAR_ID, cal_Id); // XXX pick)
//                                calEvent.put(CalendarContract.Events.TITLE, event_type + "\n" + event_title + "\n" + event_about);
//
//                                SimpleDateFormat selected_sdf = new SimpleDateFormat(DATE_TIME_FORMATTER);
//                                Date selectedDate = selected_sdf.parse(event_date + " " + event_time);
//                                calEvent.put(CalendarContract.Events.DTSTART, (selectedDate.getTime()) + 60 * 60 * 1000); // ends 60 minutes from selected date and time);
//                                calEvent.put(CalendarContract.Events.DTEND, (cal.getTimeInMillis() + 1 * 60 * 1000)); // ends 60 minutes from selected time
//
//                                String mTimeZone = TimeZone.getDefault().getDisplayName();
//
//                                Log.e(TAG, "TimeZone : " + mTimeZone);
//                                calEvent.put(CalendarContract.Events.EVENT_TIMEZONE, mTimeZone);
//
//                                Uri uri = CR.insert(getCalendarURI(true), calEvent);
//                                int id = Integer.parseInt(uri.getLastPathSegment());
//                               // Toast.makeText(getActivity(), "Event Created" + id, Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(getActivity(), "opps! Fail to Create Event!", Toast.LENGTH_SHORT).show();
                            }

//                            } else {
//                                Toast.makeText(mActivity, response.getString("message"), Toast.LENGTH_LONG).show();
//                            }


                            progressDialog.dismiss();

                        } catch (Exception e) {
                            progressDialog.dismiss();

                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(mActivity, R.string.msg_network_error, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void deleteEventFromServer(String eventDate, String event_time) {
        final String event_date = (new SimpleDateFormat(DATE_FORMATTER).format(new Date(eventDate))).trim();
//        Log.e(TAG, "event_date : " + event_date);
//        Log.e(TAG, "event_time : " + event_time);

        progressDialog.show();

        AndroidNetworking.post(Config.DELETE_EVENT)
                .addBodyParameter("email", App.my_email)
                .addBodyParameter("event_date", event_date)
                .addBodyParameter("event_time", event_time.trim())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "Response : " + response);
                            int status = response.getInt("status");
                            if (status == 1) {


                                getEventFromServer(true, event_date);

                                Toast.makeText(mActivity, response.getString("message"), Toast.LENGTH_LONG).show();

                                /**
                                 * reload event
                                 */
                                customCalendar.refreshCalendar(event_date);

                            } else {
                                Toast.makeText(mActivity, response.getString("message"), Toast.LENGTH_LONG).show();
                            }


                            progressDialog.dismiss();

                        } catch (Exception e) {
                            progressDialog.dismiss();

                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(mActivity, R.string.msg_network_error, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void getEventFromServer(final boolean isAnyChangeInEvent, final String changeDate) {
        progressDialog.show();

        AndroidNetworking.post(Config.GET_EVENT)
                .addBodyParameter("email", App.my_email)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            int status = response.getInt("status");
                            if (status == 1) {
//                                Toast.makeText(mActivity, response.getString("message"), Toast.LENGTH_LONG).show();
                                JSONArray mJsonArray = response.getJSONArray("data");
                                for (int i = 0; i < mJsonArray.length(); i++) {
                                    JSONObject jsonObject = mJsonArray.getJSONObject(i);

                                    String event_date = jsonObject.getString("event_date");
                                    JSONArray eventJsonArray = jsonObject.getJSONArray("event");

                                    /**
                                     *set event into calendar
                                     */
//                                    customCalendar.addAnEvent(event_date, eventJsonArray.length(), getSavedEvent(eventJsonArray));
                                    customCalendar.addAnEvent(event_date, 1, getSavedEvent(eventJsonArray));

                                    /**
                                     * use to refresh event count
                                     */
                                    customCalendar.nextMonth();
                                    customCalendar.previousMonth();

                                    if (isAnyChangeInEvent) {
                                        customCalendar.refreshCalendar(changeDate);
                                    }

                                }

                            } else {
                                Toast.makeText(mActivity, response.getString("message"), Toast.LENGTH_LONG).show();
                            }


                            progressDialog.dismiss();

                        } catch (Exception e) {
                            progressDialog.dismiss();

                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(mActivity, R.string.msg_network_error, Toast.LENGTH_LONG).show();
                    }
                });
    }
}

