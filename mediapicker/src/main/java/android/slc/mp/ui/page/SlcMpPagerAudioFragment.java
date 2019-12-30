package android.slc.mp.ui.page;

import android.os.Bundle;
import androidx.annotation.Nullable;

import android.slc.mp.R;
import android.slc.mp.ui.adapter.SlcMpAudioAdapter;
import android.slc.mp.ui.adapter.SlcMpBaseMpAdapter;
import android.view.View;
import android.widget.TextView;

import android.slc.mp.po.SelectEvent;
import android.slc.mp.po.i.IAudioFolder;
import android.slc.mp.po.i.IAudioItem;
import android.slc.mp.po.i.IAudioResult;
import android.slc.mp.ui.SlcIMpDelegate;

import java.util.List;

public class SlcMpPagerAudioFragment extends SlcMpPagerBaseFragment<IAudioResult, IAudioFolder, IAudioItem> {
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
    protected SlcMpBaseMpAdapter<IAudioItem> getMediaPickerAdapter(List<IAudioItem> data) {
        return new SlcMpAudioAdapter(getContext(), data, mMediaPickerListDelegate.getMediaPickerDelegate());
    }

    @Override
    protected int getContentView() {
        return R.layout.slc_mp_video_fragment;
    }

    @Override
    public SlcIMpPagerDelegate<IAudioResult, IAudioFolder, IAudioItem> getMediaPickerListDelegate(int mediaType, SlcIMpDelegate slcIMpDelegate) {
        return new SlcMpPagerAudioDelegateImp(slcIMpDelegate) {
            @Override
            public Object onSelectEvent(int eventCode, SelectEvent event) {
                switch (eventCode) {
                    case SelectEvent.EVENT_CHECK:
                        IAudioItem baseItem = event.getAuto(SelectEvent.PARAMETER_ITEM);
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
