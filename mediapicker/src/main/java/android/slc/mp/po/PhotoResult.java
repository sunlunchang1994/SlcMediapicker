package android.slc.mp.po;

import android.slc.mp.po.i.IPhotoFolder;
import android.slc.mp.po.i.IPhotoItem;
import android.slc.mp.po.i.IPhotoResult;

import android.slc.medialoader.bean.BaseResult;

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
