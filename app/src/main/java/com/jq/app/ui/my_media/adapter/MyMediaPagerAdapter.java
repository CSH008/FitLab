package com.jq.app.ui.my_media.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.jq.app.ui.search.pager.BodyPartPagerViewFragment;
import com.jq.app.util.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class MyMediaPagerAdapter extends FragmentPagerAdapter {
    private final List<BaseFragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentTitleList = new ArrayList<>();

    public MyMediaPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public void addFragment(BaseFragment fragment, String title) {
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);

        //BodyPartPagerViewFragment fragment = fragmentList.get(position);
        //fragment.refresh(position); //Refresh what you need on this fragment
    }

}
