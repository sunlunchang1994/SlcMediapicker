package com.slc.mp.po;

import com.slc.medialoader.bean.BaseResult;
import com.slc.mp.po.i.IPhotoFolder;
import com.slc.mp.po.i.IPhotoItem;
import com.slc.mp.po.i.IPhotoResult;

import java.util.List;

/**
 * Created by Taurus on 2017/5/24.
 */

public class PhotoResult extends BaseResult<IPhotoFolder, IPhotoItem> implements IPhotoResult {
    public PhotoResult() {
        super();
    }

    public PhotoResult(List<IPhotoFolder> folders) {
        super(folders);
    }

}
