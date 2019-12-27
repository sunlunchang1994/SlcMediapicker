package com.slc.mp.ui.page;

import androidx.fragment.app.FragmentActivity;

import com.slc.mp.po.i.IBaseFolder;
import com.slc.mp.po.i.IBaseItem;
import com.slc.mp.po.i.IBaseResult;
import com.slc.mp.ui.SlcIMpDelegate;

import java.util.List;

public interface SlcIMpPagerDelegate<S extends IBaseResult<F, T>, F extends IBaseFolder<T>, T extends IBaseItem> {
    void loader(FragmentActivity fragmentActivity, OnResultListener<List<T>> loaderCallBack);

    int getMediaType();

    SlcIMpDelegate getMediaPickerDelegate();

    S getResult();

    List<T> getCurrentItemList();

    void setCurrentItemList(List<T> itemList);

    void onItemClick(int position);

    void onItemChildClick(int position);

    boolean selectItem(int position);

    interface OnResultListener<T> {
        void onLoadResult(T data);
    }
}
