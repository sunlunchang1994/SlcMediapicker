package com.slc.mp.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.slc.mp.R;
import com.slc.mp.SlcMp;
import com.slc.mp.po.SelectEvent;
import com.slc.mp.ui.SlcIMpDelegate;
import com.slc.mp.ui.SlcIMpView;
import com.slc.mp.ui.adapter.SlcMpSectionsPagerAdapter;
import com.slc.mp.ui.page.SlcMpPagerBaseFragment;

public class SlcMpActivity extends AppCompatActivity implements SlcIMpView,
        SlcIMpDelegate.OnSelectEventListener {
    private SlcIMpDelegate mMediaPickerDelegate;
    private Toolbar mToolbar;
    private CharSequence mTitleTemp;
    private MenuItem completeMenuItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slc_mp_activity_picker);
        slcMtBindView(savedInstanceState);
        slcMtInitData();
    }

    @Override
    public void slcMtBindView(@Nullable Bundle savedInstanceState) {
        mToolbar = findViewById(R.id.toolbar);
        completeMenuItem = mToolbar.getMenu().add(R.string.slc_m_p_complete);
        completeMenuItem.setVisible(false);
        completeMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        completeMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mMediaPickerDelegate.complete();
                return false;
            }
        });
    }

    @Override
    public void slcMtInitData() {
        mMediaPickerDelegate = new SlcMpDelegateImp(this);
        mMediaPickerDelegate.addOnSelectEventListener(this);
        if (mMediaPickerDelegate.getMediaTypeList().size() > 1) {
            SlcMpSectionsPagerAdapter slcMpSectionsPagerAdapter = new SlcMpSectionsPagerAdapter(this, getSupportFragmentManager(),
                    mMediaPickerDelegate);
            ViewPager viewPager = findViewById(R.id.view_pager);
            viewPager.setVisibility(View.VISIBLE);
            viewPager.setAdapter(slcMpSectionsPagerAdapter);
            TabLayout mTabLayout = findViewById(R.id.tabs);
            mTabLayout.setVisibility(View.VISIBLE);
            mTabLayout.setupWithViewPager(viewPager);
            mToolbar.setTitle(R.string.slc_m_p_media_picker);
        } else {
            int mediaType = mMediaPickerDelegate.getMediaTypeList().get(0);
            getSupportFragmentManager().beginTransaction().add(R.id.contentView,
                    SlcMpPagerBaseFragment.newInstance(mediaType, mMediaPickerDelegate)).commit();
            mToolbar.setTitle(mMediaPickerDelegate.getTitleByMediaType(mediaType));
        }
        mTitleTemp = mToolbar.getTitle();
    }


    @Override
    public Object onSelectEvent(int eventCode, SelectEvent event) {
        switch (eventCode) {
            case SelectEvent.EVENT_SELECT_COUNT:
                boolean isMultipleSelection = event.getAuto(SelectEvent.PARAMETER_IS_MULTIPLE_SELECTION);
                int count = event.getAuto(SelectEvent.PARAMETER_SELECT_ITEM_COUNT);
                if(count > 0){
                    mToolbar.setTitle(getString(R.string.slc_m_p_select_count, String.valueOf(count)));
                    completeMenuItem.setVisible(true);
                }else{
                    mToolbar.setTitle(mTitleTemp);
                    completeMenuItem.setVisible(false);
                }
                /*if (isMultipleSelection && count > 0) {
                    mToolbar.setTitle(getString(R.string.slc_m_p_select_count, String.valueOf(count)));
                    completeMenuItem.setVisible(true);
                } else {
                    mToolbar.setTitle(mTitleTemp);
                    completeMenuItem.setVisible(false);
                }*/
                break;
            case SelectEvent.EVENT_OVER_FLOW:
                Toast.makeText(SlcMp.getInstance().getApp(), SlcMp.getInstance().getApp()
                                .getString(R.string.slc_m_p_max_count_hint, String.valueOf(mMediaPickerDelegate.getMaxCount())),
                        Toast.LENGTH_SHORT).show();
                break;
            case SelectEvent.EVENT_LISTENER_MEDIA_TYPE:
                return SlcMp.MEDIA_TYPE_NULL;
            case SelectEvent.EVENT_NO_ALLOW_MUDDY_MEDIA_TYPE:
                Toast.makeText(SlcMp.getInstance().getApp(), R.string.slc_m_p_this_type_is_not_allowed_this_time,
                        Toast.LENGTH_SHORT).show();
                break;
            case SelectEvent.EVENT_NO_ALLOW_MULTIPLE_MEDIA_TYPE:
                Toast.makeText(SlcMp.getInstance().getApp(),
                        R.string.slc_m_p_cannot_select_multiple_types_at_the_same_time,
                        Toast.LENGTH_SHORT).show();
                break;
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        mMediaPickerDelegate.destroy();
        super.onDestroy();
    }
}
