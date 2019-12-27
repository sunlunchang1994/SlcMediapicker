package com.slc.mp.ui.page;

import com.slc.mp.po.i.IBaseFolder;
import com.slc.mp.po.i.IBaseItem;
import com.slc.mp.po.i.IBaseResult;
import com.slc.mp.ui.SlcIMpDelegate;
import com.slc.mp.ui.SlcIMpView;

public interface SlcIMpFragmentView<S extends IBaseResult<F, T>, F extends IBaseFolder<T>, T extends IBaseItem> extends SlcIMpView {
    SlcIMpPagerDelegate<S, F, T> getMediaPickerListDelegate(int mediaType, SlcIMpDelegate slcIMpDelegate);
}
