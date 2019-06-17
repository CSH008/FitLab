package com.jq.app.ui.time_calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.google.gson.JsonObject;
import com.jq.app.App;
import com.jq.app.R;
import com.jq.app.network.ApiCallback;
import com.jq.app.network.ApiClient;
import com.jq.app.network.ApiStores;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * This is a base activity which contains week view and all the codes necessary to initialize the
 * week view.
 * Created by Raquib-ul-Alam Kanak on 1/3/2014.
 * Website: http://alamkanak.github.io
 */
public abstract class BaseActivity extends AppCompatActivity implements WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener {
    private static final String TAG = BaseActivity.class.getSimpleName();
    private String selectedDate = "";

    //    private static final int TYPE_DAY_VIEW = 1;
//    private static final int TYPE_THREE_DAY_VIEW = 2;
//    private static final int TYPE_WEEK_VIEW = 3;
//    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    private Activity mActivity;
    private WeekView mWeekView;
    private boolean isStateExpanded;
    CoordinatorLayout coordinatorLayout;
    BottomSheetBehavior behavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mActivity = this;


        Drawable drawable= getResources().getDrawable(R.drawable.back);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Drawable newdrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 150, 150, true));
        newdrawable.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(newdrawable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) findViewById(R.id.weekView);

        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(this);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);

        // Set long press listener for empty view
        mWeekView.setEmptyViewLongPressListener(this);

        // Set up a date time interpreter to interpret how the date and time will be formatted in
        // the week view. This is optional.
        setupDateTimeInterpreter(false);


        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);


        View bottomSheet = findViewById(R.id.bottom_sheet);
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);


        findViewById(R.id.ll_cl1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mActivity, "New Appointment", Toast.LENGTH_SHORT).show();
                if (isStateExpanded) {
                    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    isStateExpanded = false;

                }

                String colorCode = Integer.toHexString(ContextCompat.getColor(mActivity, R.color.new_appointment_color) & 0x00ffffff);
                createEventDialog("New Appointment", 0, colorCode);

//                addEvent("appointment","#000");
            }
        });

        findViewById(R.id.ll_cl2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mActivity, "Add to Waitlist", Toast.LENGTH_SHORT).show();
                if (isStateExpanded) {
                    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    isStateExpanded = false;

                }

                String colorCode = Integer.toHexString(ContextCompat.getColor(mActivity, R.color.add_to_waitlist_color) & 0x00ffffff);
                createEventDialog("Add to Waitlist", 1, colorCode);

//                addEvent("appointment","#000");
            }
        });

        findViewById(R.id.ll_cl3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mActivity, "Personal Task", Toast.LENGTH_SHORT).show();
                if (isStateExpanded) {
                    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    isStateExpanded = false;

                }
                String colorCode = Integer.toHexString(ContextCompat.getColor(mActivity, R.color.personal_task_color) & 0x00ffffff);
                createEventDialog("Personal Task", 2, colorCode);

//                addEvent("appointment","#000");
            }
        });

        findViewById(R.id.ll_cl4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mActivity, "Edit working Hours", Toast.LENGTH_SHORT).show();
                if (isStateExpanded) {
                    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    isStateExpanded = false;

                }

                String colorCode = Integer.toHexString(ContextCompat.getColor(mActivity, R.color.edit_working_hour_color) & 0x00ffffff);
                createEventDialog("Edit working Hours", 3, colorCode);

//                addEvent("appointment","#000");
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_time, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        setupDateTimeInterpreter(id == R.id.action_week_view);
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_today:
                mWeekView.goToToday();
                return true;
//            case R.id.action_day_view:
//                if (mWeekViewType != TYPE_DAY_VIEW) {
//                    item.setChecked(!item.isChecked());
//                    mWeekViewType = TYPE_DAY_VIEW;
//                    mWeekView.setNumberOfVisibleDays(1);
//
//                    // Lets change some dimensions to best fit the view.
//                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
//                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
//                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
//                }
//                return true;
//            case R.id.action_three_day_view:
//                if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
//                    item.setChecked(!item.isChecked());
//                    mWeekViewType = TYPE_THREE_DAY_VIEW;
//                    mWeekView.setNumberOfVisibleDays(3);
//
//                    // Lets change some dimensions to best fit the view.
//                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
//                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
//                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
//                }
//                return true;
//            case R.id.action_week_view:
//                if (mWeekViewType != TYPE_WEEK_VIEW) {
//                    item.setChecked(!item.isChecked());
//                    mWeekViewType = TYPE_WEEK_VIEW;
//                    mWeekView.setNumberOfVisibleDays(7);
//
//                    // Lets change some dimensions to best fit the view.
//                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
//                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
//                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
//                }
//                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Set up a date time interpreter which will show short date values when in week view and long
     * date values otherwise.
     *
     * @param shortDate True if the date values should be short.
     */
    private void setupDateTimeInterpreter(final boolean shortDate) {
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
//                SimpleDateFormat format = new SimpleDateFormat(" DD-MMM-YYYY", Locale.getDefault());
                SimpleDateFormat format = new SimpleDateFormat(" MMM, dd yyyy", Locale.getDefault());

                // All android api level do not have a standard way of getting the first letter of
                // the week day name. Hence we get the first char programmatically.
                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0));
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
                return hour > 11 ? (hour - 12) + " PM" : (hour == 0 ? "12 AM" : hour + " AM");
            }
        });
    }

    protected String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH) + 1, time.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        //Toast.makeText(this, "Clicked " + event.getName(), Toast.LENGTH_SHORT).show();
        showEventDetails( event.getName());
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        //Toast.makeText(this, "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEmptyViewLongPress(Calendar calendar) {
        if (isStateExpanded) {
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            Toast.makeText(this, "STATE_HIDDEN", Toast.LENGTH_SHORT).show();
            isStateExpanded = false;
        } else {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);


            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM, dd yyyy");
            selectedDate = dateFormat.format(calendar.getTime());

//            Toast.makeText(this, "Empty view long pressed: " + getEventTitle(time), Toast.LENGTH_SHORT).show();

            isStateExpanded = true;
        }


    }

    public WeekView getWeekView() {
        return mWeekView;
    }

    public void addCall(Observable observable, final ApiCallback apiCallback) {
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new Observer() {
            public void onSubscribe(Disposable d) {
            }

            public void onNext(Object o) {
                apiCallback.onNext(o);
            }

            public void onError(Throwable e) {
                apiCallback.onError(e);
            }

            public void onComplete() {
                apiCallback.onComplete();
            }
        });
    }

    public void addEvent(String selectedTitle, String event_title, String event_description, String event_date, String start_time, String end_time, String colorCode) {
        try {

            event_date = parseDateToddMMyyyy(event_date);
            Log.e(TAG, "event_date : " + event_date); //23-02-2019-6
            String[] date = event_date.split("-");

            Map<String, String> map = new HashMap();
            map.put("name", selectedTitle);
            map.put("day", date[3]);
            map.put("dayOfMonth", date[0]);
            map.put("startTime", start_time);
            map.put("endTime", end_time);
            map.put("month", date[1]);
            map.put("year", date[2]);//Fri Feb, 22 2019
            map.put("title", event_title);
            map.put("description", event_description);
            map.put("email", App.my_email);
            map.put("color", colorCode);

            Log.e(TAG, "input : " + map);


            addCall(((ApiStores) ApiClient.retrofit(getApplicationContext()).create(ApiStores.class)).addCalendarEvent(map), new ApiCallback<JsonObject>() {

                @Override
                public void onSuccess(JsonObject response) {
                    //{"status":"1","message":"Success","user_id":"hasnain.ali@gmail.com"}
                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());
                        Log.e(TAG, "Response : " + jsonObject);
                        if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                            /**
                             * now add event in calender and refresh
                             */
                            addEventIntoLocalCalendar(jsonObject.getString("message"));

                            /**
                             * refresh view
                             */
                            recreate();

                        } else {

                            Toast.makeText(mActivity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


                @Override
                public void onFailure(String str) {
                    Log.e(TAG, "onFailure : " + str);

                }

                @Override
                public void onFinish() {
                    Log.e(TAG, "onFinish");

                }


                @Override
                public void onTokenExpire() {
                    Log.e(TAG, "onTokenExpire");

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


//  http://198.27.98.210:8080/fitlab/jsp/add_cal_event.jsp?
// name=appoitment
// &dayOfMonth=1
// &startTime=11:11
// &endTime=12:00
// &month=02
// &year=19
// &description=arvinda%20meeting%20with%20Ashe
// &email=arvinda@betaar3.com
// &color=FFFF


    }

    private void addEventIntoLocalCalendar(String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();

    }


    public void createEventDialog(final String selectedTitle, final int dialogPosition, final String color) {

        final Dialog dialog = new Dialog(mActivity);
        try {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(R.layout.dialog_add_event);
            dialog.getWindow().setLayout(-1, -2);
            dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false);

            ((TextView) dialog.findViewById(R.id.tv_title)).setText(selectedTitle);

            RelativeLayout title_layout = dialog.findViewById(R.id.title_layout);
            switch (dialogPosition) {
                case 0:

                    title_layout.setBackgroundColor(getResources().getColor(R.color.new_appointment_color));

                    break;

                case 1:

                    title_layout.setBackgroundColor(getResources().getColor(R.color.add_to_waitlist_color));

                    break;

                case 2:

                    title_layout.setBackgroundColor(getResources().getColor(R.color.personal_task_color));

                    break;

                case 3:

                    title_layout.setBackgroundColor(getResources().getColor(R.color.edit_working_hour_color));

                    break;

            }


            final EditText et_event_title = dialog.findViewById(R.id.et_event_title);
            final EditText et_event_description = dialog.findViewById(R.id.et_event_description);
            final TextView tv_event_date = dialog.findViewById(R.id.tv_event_date);
            tv_event_date.setText(selectedDate);
            final TextView tv_start_time = dialog.findViewById(R.id.tv_start_time);
            final TextView tv_end_time = dialog.findViewById(R.id.tv_end_time);


            final Calendar myCalendar = Calendar.getInstance();

//            final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
//
//                @Override
//                public void onDateSet(DatePicker view, int year, int monthOfYear,
//                                      int dayOfMonth) {
//                    // TODO Auto-generated method stub
//                    myCalendar.set(Calendar.YEAR, year);
//                    myCalendar.set(Calendar.MONTH, monthOfYear);
//                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//
//                    /**
//                     * set selected date
//                     */
//                    String myFormat = "MM/dd/yy"; //In which you need put here
//                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
//                    tv_event_date.setText(sdf.format(myCalendar.getTime()));
//
//                }
//
//            };
//            tv_event_date.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    new DatePickerDialog(dialog.getContext(), date, myCalendar
//                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//
//                }
//            });

            tv_start_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                    int minute = myCalendar.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;

                    mTimePicker = new TimePickerDialog(dialog.getContext(), AlertDialog.THEME_HOLO_LIGHT,new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                            final String _24HourTime = formatTime(selectedHour) + ":" + formatTime(selectedMinute);
                            try {
                                SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm",Locale.getDefault());
                                SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a",Locale.getDefault());

                                System.out.println(_12HourSDF.format(_24HourSDF.parse(_24HourTime)));
                                tv_start_time.setText(_12HourSDF.format(_24HourSDF.parse(_24HourTime)));

                            } catch (final ParseException e) {
                                e.printStackTrace();
                            }


                           // tv_start_time.setText(formatTime(selectedHour) + ":" + formatTime(selectedMinute));
                        }
                    }, hour, minute, false);//Yes 24 hour time
                    mTimePicker.setTitle("Select Start Time");
                    mTimePicker.show();
                }
            });


            tv_end_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                    int minute = myCalendar.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(dialog.getContext(),AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                            final String _24HourTime = formatTime(selectedHour) + ":" + formatTime(selectedMinute);
                            try {
                                SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm",Locale.getDefault());
                                SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a",Locale.getDefault());

                                System.out.println(_12HourSDF.format(_24HourSDF.parse(_24HourTime)));
                                tv_end_time.setText(_12HourSDF.format(_24HourSDF.parse(_24HourTime)));

                            } catch (final ParseException e) {
                                e.printStackTrace();
                            }


//                            tv_end_time.setText(formatTime(selectedHour) + ":" + formatTime(selectedMinute));
                        }
                    }, hour, minute, false);//Yes 24 hour time
                    mTimePicker.setTitle("Select End Time");
                    mTimePicker.show();
                }
            });


            dialog.findViewById(R.id.btn_save_event).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String event_title = et_event_title.getText().toString().trim();
                    String event_description = et_event_description.getText().toString().trim();
                    String event_date = tv_event_date.getText().toString().trim();
                    String start_time = tv_start_time.getText().toString().trim();
                    String end_time = tv_end_time.getText().toString().trim();

                    if (event_title.equalsIgnoreCase("")) {
                        Toast.makeText(dialog.getContext(), "Title should not be blank", Toast.LENGTH_LONG).show();

                    } else if (event_description.equalsIgnoreCase("")) {

                        Toast.makeText(dialog.getContext(), "Description should not be blank", Toast.LENGTH_LONG).show();

                    } else if (event_date.equalsIgnoreCase("")) {

                        Toast.makeText(dialog.getContext(), "Select date for event", Toast.LENGTH_LONG).show();

                    } else if (start_time.equalsIgnoreCase("")) {

                        Toast.makeText(dialog.getContext(), "Select start time for event", Toast.LENGTH_LONG).show();

                    } else if (end_time.equalsIgnoreCase("")) {

                        Toast.makeText(dialog.getContext(), "Select end time for event", Toast.LENGTH_LONG).show();

                    } else {
                        /**
                         * Add event details into db
                         */
                        addEvent(selectedTitle, event_title, event_description, event_date, start_time, end_time, color);

                        dialog.dismiss();

                    }


                }
            });

            dialog.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });


            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void showEventDetails(final String details) {
        final Dialog dialog = new Dialog(mActivity);
        try {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(R.layout.dialog_event_details);
            dialog.getWindow().setLayout(-1, -2);
            dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false);
            ((TextView) dialog.findViewById(R.id.tv_details)).setText(details);

            dialog.findViewById(R.id.tv_got_it).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String formatTime(int time) {

        String formattedTime;
        if (time < 10) {
            formattedTime = "0" + time;
        } else {
            formattedTime = "" + time + "";
        }

        return formattedTime;

    }

    public String parseDateToddMMyyyy(String time) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM, dd yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str + "-" + getDay(time.trim().substring(0, 3));
    }

    private int getDay(String eee) {
        int day = 0;
        switch (eee.toUpperCase()) {
            case "SUN":
                day = 0;
                break;

            case "MON":
                day = 1;
                break;

            case "TUE":
                day = 2;
                break;

            case "WED":
                day = 3;
                break;

            case "THU":
                day = 4;
                break;

            case "FRI":
                day = 5;
                break;

            case "SAT":
                day = 6;
                break;

        }

        return day;
    }
}
