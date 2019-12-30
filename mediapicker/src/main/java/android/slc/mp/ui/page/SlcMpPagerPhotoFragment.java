package android.slc.mp.ui.page;

import android.os.Bundle;
import android.slc.mp.R;
import android.slc.mp.ui.adapter.SlcMpBaseMpAdapter;
import android.slc.mp.ui.adapter.SlcMpPhotoAdapter;
import android.slc.mp.ui.adapter.base.SlcMpBaseAdapter;
import android.slc.mp.utils.SlcMpMediaBrowseUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import android.slc.mp.po.SelectEvent;
import android.slc.mp.po.i.IPhotoFolder;
import android.slc.mp.po.i.IPhotoItem;
import android.slc.mp.po.i.IPhotoResult;
import android.slc.mp.ui.SlcIMpDelegate;

import java.util.List;

public class SlcMpPagerPhotoFragment extends SlcMpPagerBaseFragment<IPhotoResult, IPhotoFolder, IPhotoItem> implements View.OnClickListener {
    private TextView tv_select_folder;

    @Override
    public void slcMtBindView(@Nullable Bundle savedInstanceState) {
        super.slcMtBindView(savedInstanceState);
        tv_select_folder = findViewById(R.id.tv_select_folder);
        findViewById(R.id.ll_select_folder).setOnClickListener(this);
        findViewById(R.id.tv_preview).setOnClickListener(this);
    }

    @Override
    protected int getMediaPickerViewId() {
        return R.id.recyclerView;
    }

    @Override
    protected SlcMpBaseMpAdapter<IPhotoItem> getMediaPickerAdapter(List<IPhotoItem> data) {
        return new SlcMpPhotoAdapter(getContext(), data, mMediaPickerListDelegate.getMediaPickerDelegate());
    }

    @Override
    protected int getContentView() {
        return R.layout.slc_mp_photo_fragment;
    }

    @Override
    public SlcIMpPagerDelegate<IPhotoResult, IPhotoFolder, IPhotoItem> getMediaPickerListDelegate(int mediaType,
                                                                                                  SlcIMpDelegate slcIMpDelegate) {
        return new SlcMpPagerPhotoDelegateImp(slcIMpDelegate) {
            @Override
            public Object onSelectEvent(int eventCode, SelectEvent event) {
                switch (eventCode) {
                    case SelectEvent.EVENT_CHECK:
                        IPhotoItem baseItem = event.getAuto(SelectEvent.PARAMETER_ITEM);
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
        } else if (v.getId() == R.id.tv_preview) {
            if (mMediaPickerListDelegate.getMediaPickerDelegate().getSelectItemArrayMap().isEmpty())
                Toast.makeText(getContext(), R.string.slc_m_p_please_select_the_image_you_want_to_preview, Toast.LENGTH_SHORT).show();
            else
                new SlcMpMediaBrowseUtils.Builder().setEdit(true)
                        .setPhotoMap(mMediaPickerListDelegate.getMediaPickerDelegate().getSelectItemArrayMap()).build(getContext());
        }
    }

    @Override
    public void onItemChildClick(SlcMpBaseAdapter adapter, View view, int position) {
        if (view.getId() == R.id.fl_take_photo) {
            mMediaPickerListDelegate.onItemChildClick(position);
        } else {
            super.onItemChildClick(adapter, view, position);
        }
    }
}
