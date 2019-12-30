package android.slc.mp.po;


import android.slc.mp.po.i.IBaseFolder;
import android.slc.mp.po.i.IBaseItem;
import android.slc.mp.po.i.IBaseResult;

import java.util.List;

/**
 * Created by Taurus on 2017/5/24.
 */

public abstract class BaseResult<F extends IBaseFolder<T>, T extends IBaseItem> extends android.slc.medialoader.bean.BaseResult<F
        , T> implements IBaseResult<F,T> {
    public BaseResult() {
        super();
    }

    public BaseResult(List<F> folders) {
        super(folders);
    }
}
