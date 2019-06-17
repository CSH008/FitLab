package com.jq.app.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jq.app.R;
import com.jq.app.data.content_helpers.ProfileHelper;
import com.jq.app.data.local_helpers.FavoriteHelper;
import com.jq.app.data.local_helpers.LocalImageHelper;
import com.jq.app.data.local_helpers.LocalVideoHelper;
import com.jq.app.ui.PlannerNew.PlannerFragmentNew;
import com.jq.app.ui.base.BaseMainActivity;
import com.jq.app.ui.base.WorkoutFragment;
import com.jq.app.ui.calendar.CalendarFragment;
import com.jq.app.ui.createplanner.PreparePlannerActivity;
import com.jq.app.ui.favorite.FavoriteFragment;
import com.jq.app.ui.home.HomeFragment;
import com.jq.app.ui.my_workout.Workout;
import com.jq.app.ui.noteList.NoteFragment;
import com.jq.app.ui.sport.SportFragment;
import com.jq.app.ui.my_media.MyImagesFragment;
import com.jq.app.ui.my_media.MyMediaFragment;
import com.jq.app.ui.my_media.MyVideosFragment;
import com.jq.app.ui.my_workout.MyWorkoutFragment;
import com.jq.app.ui.payment.PaymentFragment;
import com.jq.app.ui.profile.ProfileFragment;
import com.jq.app.ui.share.MyShareFragment;
import com.jq.app.ui.share.fragments.MyShareImagesFragment;
import com.jq.app.ui.share.fragments.MyShareVideosFragment;
import com.jq.app.ui.time_calendar.TimeCalendarActivity;
import com.jq.app.util.Common;
import com.jq.chatsdk.Utils.helper.ChatSDKProfileHelper;
import com.jq.chatsdk.Utils.helper.ChatSDKUiHelper;
import com.jq.chatsdk.dao.BUser;
import com.jq.chatsdk.network.BNetworkManager;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends BaseMainActivity implements NavigationView.OnNavigationItemSelectedListener,
        HomeFragment.OnFragmentInteractionListener, PaymentFragment.OnFragmentInteractionListener,
        FavoriteFragment.OnFragmentInteractionListener, MyMediaFragment.OnFragmentInteractionListener,
        MyImagesFragment.OnFragmentInteractionListener, MyVideosFragment.OnFragmentInteractionListener,
        MyShareFragment.OnFragmentInteractionListener, MyShareVideosFragment.OnFragmentInteractionListener,
        MyShareImagesFragment.OnFragmentInteractionListener,
        PlannerFragmentNew.OnFragmentInteractionListener, SportFragment.OnFragmentInteractionListener,
        WorkoutFragment.OnSubCategoryListerner {

    HomeFragment homeFragment = new HomeFragment();
    FavoriteFragment favoriteFragment = new FavoriteFragment();
    MyMediaFragment myMediaFragmewnt = new MyMediaFragment();
    PaymentFragment paymentFragment = new PaymentFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    MyShareFragment shareFragment = new MyShareFragment();
    //    CalendarFragment calendarFragment = new CalendarFragment();
    MyWorkoutFragment myWorkoutFragment = new MyWorkoutFragment();
    PlannerFragmentNew plannerFragmentNew = new PlannerFragmentNew();
    SportFragment sportFragment = new SportFragment();
    NoteFragment noteFragment = NoteFragment.getInstance("2019-01-01", "2019-12-29");

    protected ChatSDKUiHelper chatSDKUiHelper;
    protected ChatSDKProfileHelper chatSDKProfileHelper;
    BUser currentUser;
    CircleImageView profileImage;
    TextView txtUsername;
    private static boolean isFirst = true;
    private NavigationView navigationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(" ");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);
        headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                showProfilePage();
            }
        });


        profileImage = headerLayout.findViewById(R.id.profileImage);
        txtUsername = headerLayout.findViewById(R.id.txtUsername);

        getSupportFragmentManager().beginTransaction().replace(R.id.frLayout, homeFragment).commit();
        if (isFirst) {
            isFirst = false;
            FavoriteHelper.getInstance().setContext(getApplicationContext());
            LocalImageHelper.getInstance().setContext(getApplicationContext());
            LocalVideoHelper.getInstance().setContext(getApplicationContext());
        }

        ProgressBar progressBar = headerLayout.findViewById(com.jq.chatsdk.R.id.chat_sdk_progressbar);
        chatSDKUiHelper = ChatSDKUiHelper.getInstance().get(this);
        chatSDKProfileHelper = new ChatSDKProfileHelper(this, profileImage, progressBar, chatSDKUiHelper, this.findViewById(android.R.id.content));
        chatSDKProfileHelper.loadProfilePic();

        setTodayWeight();
    }

    public void setTodayWeight() {
        Calendar calendar = Calendar.getInstance();
        final String today = Common.getStringDate(calendar);

        String lastWeightInfo = Common.getStringWithValueKey(this, "body_weight");
        if (lastWeightInfo.contains(today)) {
            return; // already set today weight;
        }

        String lastWeight = "0";
        if (!lastWeightInfo.isEmpty() && lastWeightInfo.split("-").length > 1) {
            lastWeight = lastWeightInfo.split("-")[1];
        }

        new MaterialDialog.Builder(this)
                .title("How much weight(lbs) today?")
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .input("Input your weight today", lastWeight, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        Common.saveStringWithKeyValue(MainActivity.this, "body_weight", today + "-" + input.toString());
                        ProfileHelper profileHelper = new ProfileHelper(MainActivity.this);
                        profileHelper.saveTodayWeight(today, input.toString());
                    }
                })
                .cancelable(false).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        currentUser = BNetworkManager.sharedManager().getNetworkAdapter().currentUserModel();
        if (currentUser != null) {
            txtUsername.setText(currentUser.getMetaName());

        } else {
            logout();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        /**
         * Home
         */
        if (id == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frLayout, homeFragment).commit();

            /**
             * Planner
             */

        } else if (id == R.id.nav_note) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frLayout, this.noteFragment).commit();

        } else if (id == R.id.nav_sport) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frLayout, sportFragment).commit();

            /**
             * Planner
             */

        } else if (id == R.id.nav_planner) {
            Intent intent = new Intent(this, PreparePlannerActivity.class);
            intent.putExtra("from_slider", true);
            startActivity(intent);
            // getSupportFragmentManager().beginTransaction().replace(R.id.frLayout, plannerFragmentNew).commit();

            /**
             * My Workout
             */
        } else if (id == R.id.nav_my_workout) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frLayout, myWorkoutFragment).commit();

            /**
             * Calendar
             */
        } else if (id == R.id.nav_calendar) {


            int size = navigationView.getMenu().size();
            for (int i = 0; i < size; i++) {
//                Log.e(TAG, "onNavigationItemSelected: "+ i +" : "+navigationView.getMenu().getItem(i).getTitle());
                navigationView.getMenu().getItem(i).setCheckable(false);
            }


//            getSupportFragmentManager().beginTransaction().replace(R.id.frLayout, calendarFragment).commit();
            startActivity(new Intent(MainActivity.this, TimeCalendarActivity.class));


            /**
             * My Media
             */
        } else if (id == R.id.nav_my_media) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frLayout, myMediaFragmewnt).commit();
            /**
             * Favorite
             */
        } else if (id == R.id.nav_favorite) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frLayout, favoriteFragment).commit();
            /**
             * Share
             */
        } else if (id == R.id.nav_share) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frLayout, shareFragment).commit();

            /**
             * Payment
             */
        } else if (id == R.id.nav_payment) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frLayout, paymentFragment).commit();

            /**
             * Logout
             */

        } else if (id == R.id.nav_logout) {
            logout();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showProfilePage() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frLayout, profileFragment).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }


    public void showSelecterFragment(Uri uri, String image_video, String bodyPart) {
        WorkoutFragment myWorkoutFragment2 = new WorkoutFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("uri", uri);
        bundle.putString("image_video", image_video);
        bundle.putString("body_part", bodyPart);
        myWorkoutFragment2.setArguments(bundle);
        Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.frLayout);
        if (fragmentById instanceof WorkoutFragment) {
            ((WorkoutFragment) fragmentById).updateArgs(bundle);
        } else {
            getSupportFragmentManager().beginTransaction().add(R.id.frLayout, myWorkoutFragment2).addToBackStack(null).commitAllowingStateLoss();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == 404) {
                /**
                 * self refresh Planner fragment
                 */
                String requiredValue = data.getExtras().getString("key");
                if (requiredValue.equalsIgnoreCase("SaveVideoActivity")) {
                    plannerFragmentNew = new PlannerFragmentNew();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getSupportFragmentManager().beginTransaction().replace(R.id.frLayout, plannerFragmentNew).commit();
                        }
                    }, 500);
                }
            }
        } catch (Exception ex) {
            Toast.makeText(MainActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }

    }


    @Override
    public void subCategoryClicked(String category, int position, String userStatusLevel) {
        //showDotsCategoryDialog(category,position,userStatusLevel);
    }

}
