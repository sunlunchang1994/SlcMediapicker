package com.slc.mp.ui.page;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.slc.mp.R;
import com.slc.mp.po.SelectEvent;
import com.slc.mp.po.i.IVideoFolder;
import com.slc.mp.po.i.IVideoItem;
import com.slc.mp.po.i.IVideoResult;
import com.slc.mp.ui.SlcIMpDelegate;
import com.slc.mp.ui.adapter.SlcMpBaseMpAdapter;
import com.slc.mp.ui.adapter.SlcMpVideoAdapter;

import java.util.List;

public class SlcMpPagerVideoFragment extends SlcMpPagerBaseFragment<IVideoResult, IVideoFolder, IVideoItem> {
    private TextView tv_select_folder;

    @Override
    public void slcMtBindView(@Nullable Bundle savedInstanceState) {
        super.slcMtBindView(savedInstanceState);
        tv_select_folder = findViewById(R.id.tv_select_folder);
        findViewById(R.id.ll_select_folder).setOnClickListener(this);
    }

    @Override
    protected int getMediaPickerViewId() {
        return R.id.recyclerView;
    }

    @Override
    protected SlcMpBaseMpAdapter<IVideoItem> getMediaPickerAdapter(List<IVideoItem> data) {
        return new SlcMpVideoAdapter(getContext(), data, mMediaPickerListDelegate.getMediaPickerDelegate());
    }

    @Override
    protected int getContentView() {
        return R.layout.slc_mp_video_fragment;
    }

    @Override
    public SlcIMpPagerDelegate<IVideoResult, IVideoFolder, IVideoItem> getMediaPickerListDelegate(int mediaType,
                                                                                                  SlcIMpDelegate slcIMpDelegate) {
        return new SlcMpPagerVideoDelegateImp(slcIMpDelegate) {
            @Override
            public Object onSelectEvent(int eventCode, SelectEvent event) {
                switch (eventCode) {
                    case SelectEvent.EVENT_CHECK:
                        IVideoItem baseItem = event.getAuto(SelectEvent.PARAMETER_ITEM);
                        int position = getCurrentItemList().indexOf(baseItem);
                        if (position >= 0) {
                            onCheck(position, baseItem);
                        }
                        return null;
                }
                return super.onSelectEvent(eventCode, event);
            }
        };
    }

    @Override
    protected void setSelectFolderName(String folderName) {
        tv_select_folder.setText(folderName);
        findViewById(R.id.ll_select_folder).setVisibility(folderName == null ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_select_folder) {
            showSwitchDialog(v);
        }
    }

}
