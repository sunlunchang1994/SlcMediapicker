package android.slc.mp.ui.page;

import android.slc.mp.po.SelectEvent;
import android.slc.mp.po.i.IBaseFolder;
import android.slc.mp.po.i.IBaseItem;
import android.slc.mp.po.i.IBaseResult;
import android.slc.mp.ui.SlcIMpDelegate;

import java.util.ArrayList;
import java.util.List;

public abstract class SlcMpPagerBaseDelegateImp<S extends IBaseResult<F, T>, F extends IBaseFolder<T>, T extends IBaseItem>
        implements SlcIMpPagerDelegate<S, F, T>,
        SlcIMpDelegate.OnSelectEventListener {
    private int mMediaType;
    protected SlcIMpDelegate mMediaPickerDelegate;
    protected S mResult;
    protected List<T> mCurrentItemList = null;

    public SlcMpPagerBaseDelegateImp(int mediaType, SlcIMpDelegate mediaPickerDelegate) {
        this.mMediaType = mediaType;
        this.mMediaPickerDelegate = mediaPickerDelegate;
        this.mMediaPickerDelegate.addOnSelectEventListener(this);
    }


    @Override
    public int getMediaType() {
        return mMediaType;
    }

    @Override
    public SlcIMpDelegate getMediaPickerDelegate() {
        return mMediaPickerDelegate;
    }

    @Override
    public S getResult() {
        if (mResult == null) {
            throw new IllegalStateException("数据未加载");
        }
        return mResult;
    }

    @Override
    public List<T> getCurrentItemList() {
        if (mCurrentItemList == null) {
            mCurrentItemList = new ArrayList<>();
            mCurrentItemList.addAll(getResult().getAllItems());
        }
        return mCurrentItemList;
    }


    @Override
    public boolean selectItem(int position) {
        T item = mCurrentItemList.get(position);
        return item.isChecked() ? mMediaPickerDelegate.removeItem(item) : mMediaPickerDelegate.addItem(item);
    }

    @Override
    public void setCurrentItemList(List<T> currentItemList) {
        this.mCurrentItemList.clear();
        this.mCurrentItemList.addAll(currentItemList);
    }

    @Override
    public Object onSelectEvent(int eventCode, SelectEvent event) {
        switch (eventCode) {
            case SelectEvent.EVENT_LISTENER_MEDIA_TYPE:
                return mMediaType;
        }
        return null;
    }
}
