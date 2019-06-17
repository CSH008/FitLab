package com.jq.app.ui.createplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.jq.app.App;
import com.jq.app.R;
import com.jq.app.ui.MainActivity;
import com.jq.app.ui.PlannerNew.Adapter.CustomAdapter;
import com.jq.app.ui.PlannerNew.Adapter.EditItemTouchHelperCallback;
import com.jq.app.ui.PlannerNew.Adapter.ItemAdapter;
import com.jq.app.ui.PlannerNew.interfaces.OnStartDragListener;
import com.jq.app.ui.PlannerNew.model.Planner;
import com.jq.app.ui.base.BaseMainActivity;
import com.jq.app.ui.save_video.SaveVideoActivity;
import com.jq.app.ui.share.model.Image;
import com.jq.app.util.Config;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PreparePlannerActivity extends BaseMainActivity implements OnStartDragListener, View.OnDragListener {

    private TabLayout tabLayout;
    private String currentCategory;
    ArrayList<Planner> dataOneList = new ArrayList<>();
    CustomAdapter dataAdapterOne;

    private Planner videoToMove;
    private int newContactPosition = -1;


    ArrayList<Planner> dataTwoList;

    ItemAdapter dataAdapterTwo;
    ItemTouchHelper mItemTouchHelper;


    private RecyclerView recyclerViewOne, recyclerTwo;
    //    private RadioGroup radioGroup;
    private LinearLayout ll_drag_n_drop;

    private ImageView btn_save;
    private String equipment;
    private String bodypart;


    private TextView tv_available_videos;
    private static final int REQUEST_CODE = 404;
    private String note = "";
    private FilterDialog dialog;
    ArrayList<String> notes = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_planner);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(" ");
        if (getIntent().getBooleanExtra("from_slider", false)) {
            bodypart = "0";
            equipment = "0";

            if (getIntent().getExtras().getString("parts") != null && !getIntent().getExtras().getString("parts").equalsIgnoreCase("")) {
                bodypart=getIntent().getExtras().getString("parts");
                equipment=getIntent().getExtras().getString("equipments");
                currentCategory=getIntent().getExtras().getString("category");
            }

        } else {
            Bundle extras = getIntent().getExtras();
            boolean body_part = extras.getBoolean("body_part");
            String selecedValues = extras.getString("selected");
            if (body_part) {
                bodypart = selecedValues;
                equipment = "0";
            } else {
                bodypart = "0";
                equipment = selecedValues;
            }
        }


        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(listener);
        tv_available_videos = findViewById(R.id.tv_available_videos);
        dataTwoList = new ArrayList<>();
        initView();
        setupOneAdatper();
        setupTwoAdapter();
        recyclerTwo.setOnDragListener(this);
        int tab = getIntent().getIntExtra("tab", 0);
        if (tab == 0) {
            setCurrentCategory(tab);
            callApiVideo(bodypart, equipment, currentCategory);
        }
        customTab(tabLayout);
        tabLayout.getTabAt(tab).select();
    }


    public void initView() {
        recyclerViewOne = findViewById(R.id.recyclerview_main);
        recyclerTwo = findViewById(R.id.recyclerview_child);
        ll_drag_n_drop = findViewById(R.id.ll_drag_n_drop);

        btn_save = findViewById(R.id.btn_save);


        tv_available_videos = findViewById(R.id.tv_available_videos);
        EditText editTextSearch = findViewById(R.id.edit_text_search);
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (dataAdapterOne != null && dataOneList != null && dataOneList.size() > 0) {
                    dataAdapterOne.getFilter().filter(s.toString());
                }

            }
        });
        /**
         * save video list
         */
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataTwoList.size() > 1) {
                    /**
                     * start save activity
                     */
                    Intent mIntent = new Intent(PreparePlannerActivity.this, SaveVideoActivity.class);
                    mIntent.putParcelableArrayListExtra("video_list", dataTwoList);
                    mIntent.putExtra("note", note);
                    mIntent.putStringArrayListExtra("notes", notes);
                    startActivityForResult(mIntent, REQUEST_CODE);
                } else {
                    Toast.makeText(PreparePlannerActivity.this, "Workout list is empty!", Toast.LENGTH_SHORT).show();
                }


            }
        });

        /**
         * default loading
         */


    }

    public void setupOneAdatper() {
        recyclerViewOne.setLayoutManager(new GridLayoutManager(this, 2));
        dataAdapterOne = new CustomAdapter(this, dataOneList, recyclerViewOne);
        recyclerViewOne.setAdapter(dataAdapterOne);
    }

    public void setupTwoAdapter() {
        dataTwoList.add(new Planner("", "", "", "", "", "", "", "", "-1", ""));
        recyclerTwo.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerTwo.setLayoutManager(mLayoutManager);

        dataAdapterTwo = new ItemAdapter(this, dataTwoList, this);
        ItemTouchHelper.Callback callback = new EditItemTouchHelperCallback(dataAdapterTwo);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerTwo);
        recyclerTwo.setAdapter(dataAdapterTwo);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.imageview_delete:
                if (dataTwoList != null) {
                    dataTwoList.clear();
                    ll_drag_n_drop.setVisibility(View.VISIBLE);
                    dataTwoList.add(new Planner("", "", "", "", "", "", "", "", "-1", ""));
                    dataAdapterTwo.notifyDataSetChanged();
                }
                break;
            case R.id.imageview_edit:
                new NoteListDialog(this, notes, new NoteListDialog.Callback() {
                    @Override
                    public void onNotesList(List<String> notes) {
                        PreparePlannerActivity.this.notes = (ArrayList<String>) notes;
                    }
                }).show();
                break;
            case R.id.image_filter:
                dialog = new FilterDialog(this, new FilterDialog.Callback() {
                    @Override
                    public void onApply(String category, String parts, String equipments) {
                        if (category == null) {
                            category = currentCategory;
                        }
                        if (parts == null) {
                            parts = bodypart;
                        }
                        if (equipments == null) {
                            equipments = equipment;
                        }
                        callApiVideo(parts, equipments, category);

                    }
                });
                dialog.show();
                break;
        }

    }

    TabLayout.OnTabSelectedListener listener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            setCurrentCategory(tab.getPosition());
            callApiVideo(bodypart, equipment, currentCategory);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    private void setCurrentCategory(int position) {
        switch (position) {
            case 0:
                currentCategory = "3";
                break;
            case 1:
                currentCategory = "1";
                break;
            case 2:
                currentCategory = "2";
                break;
            case 3:
                currentCategory = "4";
                break;
            case 4:
                currentCategory = "5";
                break;
        }

        Log.v("CURRENT_CATEGORY", currentCategory);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_home).setVisible(true);
        menu.findItem(R.id.action_clock).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_home) {
            ActivityCompat.finishAffinity(this);
            startActivity(new Intent(this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public void callApiVideo(String bodypart, String equipment, String currentCategory) {
        showProgressDialog();
        //   progressDialog.show();
        /**
         * a) mobility      1
         * b) strtching     2
         * c) excerise      3
         */

        AndroidNetworking.post(Config.PLANNER_VIDEO)
                .addBodyParameter("email", App.my_email)
                .addBodyParameter("workout_code", currentCategory)
//                .addBodyParameter("body_part", bodypart)
                .addBodyParameter("bodypart_code", bodypart)
                .addBodyParameter("equipment", equipment)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int status = response.getInt("status");
                            if (status == 1) {
                                dataOneList.clear();
                                dataAdapterOne.notifyDataSetChanged();
                                JSONArray mJsonArray = response.getJSONArray("data");
                                tv_available_videos.setText(mJsonArray.length() + " exercises matches in this");
                                for (int i = 0; i < response.getJSONArray("data").length(); i++) {
                                    Planner mPlanner = new Planner(
                                            response.getJSONArray("data").getJSONObject(i).getString("workout_name"),
                                            response.getJSONArray("data").getJSONObject(i).getString("work_out_code"),
                                            response.getJSONArray("data").getJSONObject(i).getString("body_part_code"),
                                            response.getJSONArray("data").getJSONObject(i).getString("comment"),
                                            response.getJSONArray("data").getJSONObject(i).getString("url"),
                                            response.getJSONArray("data").getJSONObject(i).getString("thumbnail_url"),
                                            response.getJSONArray("data").getJSONObject(i).getString("title"),
                                            response.getJSONArray("data").getJSONObject(i).getString("description"),
                                            response.getJSONArray("data").getJSONObject(i).getString("id"),
                                            response.getJSONArray("data").getJSONObject(i).getString("is_like")
                                    );

                                    dataOneList.add(mPlanner);

                                }
                            } else {
                                dataOneList.clear();
                                dataAdapterOne.notifyDataSetChanged();
                                Toast.makeText(PreparePlannerActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                            }
                            dataAdapterOne.setTempList(dataOneList);
                            dataAdapterOne.notifyDataSetChanged();

                        } catch (Exception e) {
                            dataOneList.clear();
                            dataAdapterOne.notifyDataSetChanged();
                            e.printStackTrace();
                        }
                        hideProgressDialog();
                    }

                    @Override
                    public void onError(ANError anError) {
                        hideProgressDialog();
                        dataOneList.clear();
                        dataAdapterOne.notifyDataSetChanged();
                        Toast.makeText(PreparePlannerActivity.this, R.string.msg_network_error, Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public boolean onDrag(View v, DragEvent dragEvent) {
        View selectedView = (View) dragEvent.getLocalState();
        RecyclerView selectedRecyclerView = recyclerViewOne;
        int currentPosition = -1;
        try {
            currentPosition = recyclerViewOne.getChildAdapterPosition(selectedView);
            //Ensure the position is valid
            if (currentPosition != -1) {
                videoToMove = dataOneList.get(currentPosition);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        switch (dragEvent.getAction()) {
            case DragEvent.ACTION_DRAG_LOCATION:
                View onTopOf = selectedRecyclerView.findChildViewUnder(dragEvent.getX(), dragEvent.getY());
                newContactPosition = selectedRecyclerView.getChildAdapterPosition(onTopOf);
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                break;
            case DragEvent.ACTION_DROP:
                //when item is droppe off to recyclerview
                dataTwoList.add(videoToMove);
                dataAdapterTwo.notifyDataSetChanged();

                /**
                 * hide drag and drop text
                 */
                ll_drag_n_drop.setVisibility(View.GONE);

                break;
            case DragEvent.ACTION_DRAG_ENDED:
                selectedView.setVisibility(View.VISIBLE);
                if (newContactPosition != -1) {
                    selectedRecyclerView.scrollToPosition(newContactPosition);
                    newContactPosition = 1;
                } else {
                    selectedRecyclerView.scrollToPosition(0);
                }

            default:
                break;

        }

        // Log.d("data----->", dragEvent.getLocalState().toString());


        return true;
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11 && resultCode == RESULT_OK) {
            if (dialog != null) {
                dialog.setSelecedParts(data.getStringExtra("parts"));
            }
        }
    }
}
