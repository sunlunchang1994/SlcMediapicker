package android.slc.mp.ui.page;

import android.slc.mp.po.i.IBaseFolder;
import android.slc.mp.po.i.IBaseItem;
import android.slc.mp.po.i.IBaseResult;
import android.slc.mp.ui.SlcIMpDelegate;
import android.slc.mp.ui.SlcIMpView;

public interface SlcIMpFragmentView<S extends IBaseResult<F, T>, F extends IBaseFolder<T>, T extends IBaseItem> extends SlcIMpView {
    SlcIMpPagerDelegate<S, F, T> getMediaPickerListDelegate(int mediaType, SlcIMpDelegate slcIMpDelegate);
}
