package com.jq.app.ui.profile.sets_pager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jq.app.R;
import com.jq.app.data.content_helpers.VideoContentHelper;
import com.jq.app.data.model.SetModel;
import com.jq.app.data.model.VideoModel;
import com.jq.app.ui.exercise.ExerciseActivity;
import com.jq.app.ui.profile.VideoChooserActivity;
import com.jq.app.ui.profile.adapter.GoalSetAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class GoalSetsPagerViewFragment extends Fragment implements GoalSetAdapter.OnListItemInteractionListener {

    RecyclerView setList;
    GoalSetAdapter setAdapter;
    String mDate;
    ArrayList<SetModel> mSets = new ArrayList<>();

    public GoalSetsPagerViewFragment() {}

    public static GoalSetsPagerViewFragment newInstance(String date, List<SetModel> setList) {
        GoalSetsPagerViewFragment fragment = new GoalSetsPagerViewFragment();
        fragment.setDate(date);
        if(setList==null || setList.size()==0) {
            for (int i = 0; i < 5; i++) {
                fragment.addItem(new SetModel(i, date));
            }
        } else {
            for (int i=0; i<setList.size(); i++) {
                fragment.addItem(setList.get(i));
            }
        }
        return fragment;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public void setList(ArrayList<SetModel> sets) {
        this.mSets = sets;
        if(setAdapter!=null) {
            setAdapter.setData(mSets);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        View view = inflater.inflate(R.layout.fragment_goal_sets_pager_view, container, false);

        setList = view.findViewById(R.id.setList);
        setList.setLayoutManager(new LinearLayoutManager(getActivity()));
        setAdapter = new GoalSetAdapter(mSets, this);
        setList.setAdapter(setAdapter);

        return view;
    }

    public void addItem(SetModel item) {
        mSets.add(item);
        if(setAdapter!=null) {
            setAdapter.setData(mSets);
        }
    }

    public void addNewItem() {
        SetModel newItem = new SetModel();
        newItem.order_number = mSets.size();
        addItem(newItem);
    }

    public List<SetModel> getSets() {
        return mSets;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            String video_id = data.getStringExtra("video_id");
            if(video_id!=null && !video_id.isEmpty()) {
                currentChoosingItem.video_id = video_id;
                setAdapter.notifyDataSetChanged();
            }
        }
    }

    public static SetModel currentChoosingItem;
    @Override
    public void onChooseVideo(SetModel item) {
        currentChoosingItem = item;
        if(item.video_id!=null && !item.video_id.isEmpty()) {
            final VideoModel model = VideoContentHelper.getVideoModelFromTemp(item.video_id);
            if(model==null) {
                showMediaCategoryDialog();

            } else {
                MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                        .title("Chosen video")
                        .customView(R.layout.item_video, true)
                        .positiveText("Ok")
                        .negativeText("Update")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                showMediaCategoryDialog();
                            }
                        })
                        .show();

                View customView = dialog.getCustomView();
                if (model.thumbnail_url != null && !model.thumbnail_url.isEmpty()) {
                    Picasso.with(getActivity()).load(model.thumbnail_url).into((ImageView) customView.findViewById(R.id.video_thumb));
                }
                ((TextView)customView.findViewById(R.id.title)).setText(model.title);
                ((TextView)customView.findViewById(R.id.description)).setText(model.description);

                if (model.is_like) {
                    ((ImageView) customView.findViewById(R.id.icon_like)).setImageResource(R.drawable.ic_favorite);
                } else {
                    ((ImageView) customView.findViewById(R.id.icon_like)).setImageResource(R.drawable.ic_favorite_border);
                }

                customView.findViewById(R.id.video_thumb).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), ExerciseActivity.class);
                        i.putExtra(ExerciseActivity.KEY_VIDEO_ITEM, model);
                        startActivity(i);
                    }
                });
            }

        } else {
            showMediaCategoryDialog();
        }
    }

    public void showMediaCategoryDialog() {
        new com.afollestad.materialdialogs.MaterialDialog.Builder(getActivity())
                .title(R.string.media_category)
                .items(R.array.dot_categories)
                .itemsCallback(new com.afollestad.materialdialogs.MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(com.afollestad.materialdialogs.MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        chosenMediaCategory(which, text);
                    }
                })
                .show();
    }

    private String category;
    public void chosenMediaCategory(int which, CharSequence text) {
        category = String.valueOf(text);
        showDotsCategoryDialog();
    }

    public void showDotsCategoryDialog() {
        new com.afollestad.materialdialogs.MaterialDialog.Builder(getActivity())
                .title(R.string.dots_category)
                .items(R.array.dot_sub_categories)
                .itemsCallback(new com.afollestad.materialdialogs.MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(com.afollestad.materialdialogs.MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        chosenBodyPartCategory(which, text);
                    }
                })
                .show();
    }

    public void chosenBodyPartCategory(int which, CharSequence text) {
        String sub_category = String.valueOf(text);
        Intent i = new Intent(getActivity(), VideoChooserActivity.class);
        i.putExtra(VideoChooserActivity.KEY_WORKOUT, category);
        i.putExtra(VideoChooserActivity.KEY_BODY_PART, sub_category);
        startActivityForResult(i, 1);
    }

    @Override
    public void onClickedDelete(int position) {
        mSets.remove(position);

        for (int i=0; i<mSets.size(); i++) {
            SetModel item = mSets.get(i);
            item.order_number = i;
        }

        if(setAdapter!=null) {
            setAdapter.setData(mSets);
        }
    }


}
