package com.slc.mp.po;


import com.slc.mp.po.i.IBaseFolder;
import com.slc.mp.po.i.IBaseItem;
import com.slc.mp.po.i.IBaseResult;

import java.util.List;

/**
 * Created by Taurus on 2017/5/24.
 */

public abstract class BaseResult<F extends IBaseFolder<T>, T extends IBaseItem> extends com.slc.medialoader.bean.BaseResult<F
        , T> implements IBaseResult<F,T> {
    public BaseResult() {
        super();
    }

    public BaseResult(List<F> folders) {
        super(folders);
    }
}
