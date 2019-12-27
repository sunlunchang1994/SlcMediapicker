package com.slc.medialoader.callback;

import com.slc.medialoader.bean.i.IBaseResult;
import com.slc.medialoader.bean.i.IFileProperty;

/**
 * Created by Taurus on 2017/5/23.
 */

public abstract class OnFileLoaderBaseCallBack<S extends IBaseResult> extends BaseFileLoaderCallBack<S> {

    public OnFileLoaderBaseCallBack() {
        super();
    }

    public OnFileLoaderBaseCallBack(IFileProperty property) {
        super(property);
    }

}
