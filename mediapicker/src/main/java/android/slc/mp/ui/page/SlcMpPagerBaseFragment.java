package android.slc.mp.ui.page;

import androidx.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.slc.mp.R;
import android.slc.mp.popup.SlcMpPopup;
import android.slc.mp.ui.adapter.SlcMpBaseMpAdapter;
import android.slc.mp.ui.adapter.SlcMpFolderAdapter;
import android.slc.mp.ui.adapter.base.SlcMpBaseAdapter;
import android.slc.mp.ui.widget.SlcMpMaxHeightRecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import android.slc.mp.SlcMp;
import android.slc.mp.po.i.IBaseFolder;
import android.slc.mp.po.i.IBaseItem;
import android.slc.mp.po.i.IBaseResult;
import android.slc.mp.ui.SlcIMpDelegate;
import android.slc.mp.ui.vm.SlcMpPageViewModel;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public abstract class SlcMpPagerBaseFragment<S extends IBaseResult<F, T>, F extends IBaseFolder<T>,
        T extends IBaseItem> extends Fragment implements
        SlcIMpFragmentView<S, F, T>, SlcIMpPagerDelegate.OnResultListener<List<T>>, View.OnClickListener,
        SlcMpBaseAdapter.OnItemClickListener, SlcMpBaseAdapter.OnItemChildClickListener {
    public final static String POSITION = "position";
    private View mContentView;
    protected SlcIMpPagerDelegate<S, F, T> mMediaPickerListDelegate;
    protected RecyclerView mediaPickerRecyclerView;
    protected SlcMpBaseMpAdapter<T> adapter;

    public static SlcMpPagerBaseFragment newInstance(int mediaType, SlcIMpDelegate slcIMpDelegate) {
        return newInstance(0, mediaType, slcIMpDelegate);
    }

    public static SlcMpPagerBaseFragment newInstance(int position, int mediaType,
                                                     SlcIMpDelegate slcIMpDelegate) {
        SlcMpPagerBaseFragment fragment = null;
        switch (mediaType) {
            case SlcMp.MEDIA_TYPE_PHOTO:
                fragment = new SlcMpPagerPhotoFragment();
                break;
            case SlcMp.MEDIA_TYPE_VIDEO:
                fragment = new SlcMpPagerVideoFragment();
                break;
            case SlcMp.MEDIA_TYPE_AUDIO:
                fragment = new SlcMpPagerAudioFragment();
                break;
            //case SlcMp.MEDIA_TYPE_FILE:
            default:
                fragment = new SlcMpPagerFileFragment();
                break;
        }
        //此举暂时用不到
        /*if (fragment == null) {
            throw new IllegalStateException("媒体类型异常，请参考MediaPicker中的媒体类型");
        }*/
        fragment.setMediaPickerDelegate(mediaType, slcIMpDelegate);
        Bundle bundle = new Bundle();
        bundle.putInt(SlcMp.Key.KEY_MEDIA_TYPE, mediaType);
        bundle.putInt(POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * 设置代理器
     *
     * @param mediaType
     * @param slcIMpDelegate
     */
    public void setMediaPickerDelegate(int mediaType, SlcIMpDelegate slcIMpDelegate) {
        this.mMediaPickerListDelegate = getMediaPickerListDelegate(mediaType, slcIMpDelegate);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mContentView = inflater.inflate(getContentView(), container, false);
        initPagerModel();
        slcMtBindView(savedInstanceState);
        slcMtInitData();
        return mContentView;
    }

    protected void initPagerModel() {
        if (getArguments() != null) {
            int index = getArguments().getInt(POSITION, 0);
            if (index > 0) {
                SlcMpPageViewModel slcMpPageViewModel = ViewModelProviders.of(this).get(SlcMpPageViewModel.class);
                slcMpPageViewModel.setIndex(index);
            }
        }
    }

    @Override
    public void slcMtBindView(@Nullable Bundle savedInstanceState) {
        mediaPickerRecyclerView = findViewById(getMediaPickerViewId());
        mediaPickerRecyclerView.setLayoutManager(getMediaPickerRecyclerViewLayoutManager());
    }

    /**
     * 获取列表管理器
     *
     * @return
     */
    protected RecyclerView.LayoutManager getMediaPickerRecyclerViewLayoutManager() {
        return new GridLayoutManager(getContext(),
                mMediaPickerListDelegate.getMediaPickerDelegate().getSpanCount());
    }

    @IdRes
    protected abstract int getMediaPickerViewId();

    @Override
    public void slcMtInitData() {
        mMediaPickerListDelegate.loader(getActivity(), this);
    }

    @Override
    public void onLoadResult(List<T> data) {
        if (adapter == null) {
            adapter = getMediaPickerAdapter(data);
            //adapter.setOnItemClickListener(this);
            adapter.setOnItemChildClickListener(this);
            mediaPickerRecyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        F allItemFolder = mMediaPickerListDelegate.getResult().getAllItemFolder();
        setSelectFolderName(allItemFolder == null ? null : mMediaPickerListDelegate.getResult().getAllItemFolder().getName());
    }

    protected abstract SlcMpBaseMpAdapter<T> getMediaPickerAdapter(List<T> data);

    public <V extends View> V findViewById(@IdRes int id) {
        return mContentView.findViewById(id);
    }

    @LayoutRes
    protected abstract int getContentView();

    @Override
    public void onItemClick(SlcMpBaseAdapter adapter, View view, int position) {
        mMediaPickerListDelegate.onItemClick(position);
    }

    @Override
    public void onItemChildClick(SlcMpBaseAdapter adapter, View view, int position) {
        if (view.getId() == R.id.checkbox) {
            CheckBox checkBox = (CheckBox) view;
            checkBox.setChecked(!checkBox.isClickable());
            mMediaPickerListDelegate.selectItem(position);
        }else{
            mMediaPickerListDelegate.onItemChildClick(position);
        }
    }

    protected void onCheck(int position, IBaseItem baseItem) {
        SlcMpPagerBaseFragment.this.adapter.notifyDataSwitch(position);
    }

    protected void showSwitchDialog(View anchor) {
        SlcMpMaxHeightRecyclerView recyclerView = new SlcMpMaxHeightRecyclerView(getContext());
        int heightPixels = getResources().getDisplayMetrics().heightPixels;
        recyclerView.setMaxHeight((int) (heightPixels - heightPixels / 3f));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SlcMpBaseAdapter slcBaseAdapter = getSwitchAdapter();
        slcBaseAdapter.setOnItemClickListener(new SlcMpBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(SlcMpBaseAdapter adapter, View view, int position) {
                SlcMpPopup.dismissByKey(String.valueOf(SlcMpPagerBaseFragment.this.hashCode()));
                onSwitchDialogItemClick(position);
            }
        });
        recyclerView.setAdapter(slcBaseAdapter);
        new SlcMpPopup.ShadowPopupWindowBuilder(getContext()).setKey(String.valueOf(hashCode())).setContentView(recyclerView).setAnchor(anchor)
                .setBgDrawable(new ColorDrawable(Color.WHITE)).create().show();
    }

    @Override
    public void onDestroyView() {
        SlcMpPopup.dismissByKey(String.valueOf(SlcMpPagerBaseFragment.this.hashCode()));
        super.onDestroyView();
    }

    protected SlcMpBaseAdapter getSwitchAdapter() {
        return new SlcMpFolderAdapter<>(getContext(), mMediaPickerListDelegate.getResult().getFolders());
    }

    protected void onSwitchDialogItemClick(int position) {
        F baseFolder = mMediaPickerListDelegate.getResult().getFolders().get(position);
        setSelectFolderName(baseFolder.getName());
        mMediaPickerListDelegate.setCurrentItemList(baseFolder.getItems());
        adapter.notifyDataSetChanged();
    }

    protected abstract void setSelectFolderName(String folderName);
}