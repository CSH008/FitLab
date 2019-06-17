package com.jq.app.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jq.app.R;
import com.jq.app.data.content_helpers.BaseContentHelper;
import com.jq.app.data.content_helpers.GoalContentHelper;
import com.jq.app.data.content_helpers.SetContentHelper;
import com.jq.app.data.model.BaseModel;
import com.jq.app.data.model.DayGoalModel;
import com.jq.app.data.model.SetModel;
import com.jq.app.ui.base.BaseMainActivity;
import com.jq.app.ui.profile.adapter.DayGoalAdapter;
import com.jq.app.ui.profile.sets_pager.GoalSetsPagerViewFragment;
import com.jq.app.ui.profile.sets_pager.GoalSetsViewPagerAdapter;
import com.jq.app.util.Common;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SetGoalActivity extends BaseMainActivity implements DatePickerDialog.OnDateSetListener, BaseContentHelper.OnDataLoadListener {

    TextView textStartDate, textEndDate, textSelectedDate, textPlanWeight;
    RecyclerView weekDays;

    DayGoalAdapter dayGoalAdapter;

    SetContentHelper mHelper;
    ArrayList<BaseModel> sets = new ArrayList<>();

    final int START_DATE = 0;
    final int END_DATE = 1;
    int chooseDay = START_DATE; // 0: start date, 1: end date

    String startDate;
    String endDate;
    String selectedDate;

    ViewPager pager;
    GoalSetsViewPagerAdapter adapter;
    Button btnPrevious, btnNext;

    GoalContentHelper goalContentHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_goal);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(" ");

        goalContentHelper = new GoalContentHelper(this);

        textStartDate = findViewById(R.id.textStartDate);
        textEndDate = findViewById(R.id.textEndDate);
        textSelectedDate = findViewById(R.id.textSelectedDate);
        textPlanWeight = findViewById(R.id.textPlanWeight);
        weekDays = findViewById(R.id.weekDays);
        weekDays.setNestedScrollingEnabled(false);

        pager = findViewById(R.id.sets_viewpager);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);

        weekDays.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<BaseModel> goals = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            goals.add(new DayGoalModel());
        }
        dayGoalAdapter = new DayGoalAdapter(goals);
        weekDays.setAdapter(dayGoalAdapter);

        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); //0: Sunday
        calendar.add(Calendar.DAY_OF_YEAR, 7 - dayOfWeek); // start date
        updateDates(calendar);

    }

    public void onClick(View view) {
        Calendar calendar = Calendar.getInstance();

        int id = view.getId();
        switch (id) {
            case R.id.btn_back:
                finish();
                break;

            case R.id.viewStartDate:
                chooseDay = START_DATE;
                DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
                break;

            case R.id.viewEndDate:
                chooseDay = END_DATE;
                DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
                break;

            case R.id.btnPrevious:
                if (pager.getCurrentItem() > 0) {
                    pager.setCurrentItem(pager.getCurrentItem() - 1, true);
                    setButtons();
                }
                break;

            case R.id.btnNext:
                if (pager.getCurrentItem() < 6) {
                    pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                    setButtons();
                }
                break;

            case R.id.btnAddSet:
                if (adapter != null) {
                    ((GoalSetsPagerViewFragment) adapter.getItem(pager.getCurrentItem())).addNewItem();
                }
                break;

            case R.id.btnSave:
                if(textPlanWeight.getText().toString().isEmpty()) {
                    showToast("Please input plan weight");
                } else {
                    postSaveDayGoals();
                }
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void setButtons() {
        int currPos = pager.getCurrentItem();
        if (currPos == 0) {
            btnPrevious.setEnabled(false);
            btnNext.setEnabled(true);
        } else if (currPos == 6) {
            btnPrevious.setEnabled(true);
            btnNext.setEnabled(false);
        } else {
            btnPrevious.setEnabled(true);
            btnNext.setEnabled(true);
        }
    }


    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);

        if (chooseDay == START_DATE) {

        } else if (chooseDay == END_DATE) {
            calendar.add(Calendar.DAY_OF_YEAR, -7); // start date
        }

        updateDates(calendar);
    }

    public void updateDates(Calendar calendar) { //param: start date
        startDate = Common.getStringDate(calendar);
        textStartDate.setText(startDate);

        selectedDate = startDate;
        textSelectedDate.setText(selectedDate);

        adapter = new GoalSetsViewPagerAdapter(getSupportFragmentManager());
        ArrayList<BaseModel> goals = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            String itemDate = Common.getStringDate(calendar);
            goals.add(new DayGoalModel(itemDate));
            adapter.addFragment(GoalSetsPagerViewFragment.newInstance(itemDate, null), itemDate);
            if (i < 6) calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        dayGoalAdapter.setData(goals);
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                selectedDate = adapter.getPageTitle(position).toString();
                textSelectedDate.setText(selectedDate);
                setButtons();
            }

            @Override
            public void onPageSelected(int position) {
                selectedDate = adapter.getPageTitle(position).toString();
                textSelectedDate.setText(selectedDate);
                setButtons();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                setButtons();
            }
        });

        endDate = Common.getStringDate(calendar);
        textEndDate.setText(endDate);

        showProgressDialog();
        goalContentHelper.getPlanList(startDate, endDate);
    }

    public void arrangeAndUpdateGoalData() {
        List<BaseModel> goalList = dayGoalAdapter.getData();
        ArrayList<BaseModel> newList = new ArrayList<>();
        for (int i=0; i<7; i++) {
            DayGoalModel item = (DayGoalModel) goalList.get(i);
            Boolean added = false;
            for(int j=0; j<goalContentHelper.ITEMS.size(); j++) {
                DayGoalModel jtem = (DayGoalModel) goalContentHelper.ITEMS.get(j);
                if(item.date!=null && item.date.equals(jtem.date)) {
                    newList.add(jtem);
                    added = true;
                    continue;
                }
            }
            if(!added) {
                newList.add(item);
            }
        }
        dayGoalAdapter.setData(newList);
    }

    public void arrangeAndUpdateSetList() {
        List<BaseModel> dayGoals = dayGoalAdapter.getData();
        for (int j=0; j<dayGoals.size(); j++) {
            String itemDay = ((DayGoalModel)dayGoals.get(j)).date;
            ArrayList<SetModel> tempList = new ArrayList<>();
            for (int i = 0; i < goalContentHelper.planSetList.size(); i++) {
                SetModel item = goalContentHelper.planSetList.get(i);
                if (item.date != null && item.date.equals(itemDay)) {
                    tempList.add(item);
                }
            }
            if(tempList.size()==0) {
                for (int i = 0; i < 5; i++) {
                    tempList.add(new SetModel(i, itemDay));
                }
            }
            adapter.getFragment(j).setList(tempList);
        }
    }



    int postingGoalsStatus = 0;
    public void postSaveDayGoals() {
        showProgressDialog();
        postingGoalsStatus = 0;
        for (int i = 0; i < 7; i++) {
            DayGoalModel item = (DayGoalModel) dayGoalAdapter.getData().get(i);
            goalContentHelper.postGoal(item, i, textPlanWeight.getText().toString());
        }
    }

    public void postDeleteAllRelatedSet() {
        goalContentHelper.deleteAllGoalSetForDate(startDate, endDate);
    }

    int currentPostingSetIndex = 0;
    int postingSetStatus = 0;

    public void postSetData(int index) {
        postingSetStatus = 0;
        List<SetModel> sets = ((GoalSetsPagerViewFragment) adapter.getItem(index)).getSets();
        if(sets.size()>0) {
            for (int i = 0; i < sets.size(); i++) {
                SetModel item = sets.get(i);
                goalContentHelper.postSet(item, i);
            }
        } else {
            onFinishedAction(GoalContentHelper.REQUEST_CREATE_SET, 0, 0);
        }
    }

    @Override
    public void onFinishedAction(int action, int index, int errMsg) {
        if (errMsg > 0) {
            onFinishedAction(action, index, getString(errMsg));
        } else {
            onFinishedAction(action, index, "");
        }
    }

    @Override
    public void onFinishedAction(int action, int index, String errMsg) {
        if (errMsg != null && !errMsg.isEmpty()) {
            showToast(errMsg);
            hideProgressDialog();
            return;
        }

        switch (action) {
            case GoalContentHelper.REQUEST_LIST:
                if (goalContentHelper.ITEMS.size() > 0) {
                    arrangeAndUpdateGoalData();
                    goalContentHelper.getPlanSetList(startDate, endDate);

                } else {
                    hideProgressDialog();
                }
                break;

            case GoalContentHelper.REQUEST_PLAN_SET_LIST:
                arrangeAndUpdateSetList();
                hideProgressDialog();

                break;

            case GoalContentHelper.REQUEST_CREATE:
                postingGoalsStatus++;
                if (postingGoalsStatus == 7) {
                    postDeleteAllRelatedSet();
                }
                break;

            case GoalContentHelper.REQUEST_DELETE_ALL_SET:
                currentPostingSetIndex = 0;
                postSetData(currentPostingSetIndex);
                break;

            case GoalContentHelper.REQUEST_CREATE_SET:
                postingSetStatus++;
                List<SetModel> list = adapter.getFragment(currentPostingSetIndex).getSets();
                if (postingSetStatus >= list.size()) {
                    currentPostingSetIndex++;
                    if (currentPostingSetIndex == 7) {
                        showToast("Success.");
                        hideProgressDialog();

                    } else {
                        postSetData(currentPostingSetIndex);
                    }
                }
                break;
        }
    }


}
