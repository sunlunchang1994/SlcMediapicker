package android.slc.mp.po;

import android.slc.mp.po.i.IVideoFolder;
import android.slc.mp.po.i.IVideoItem;
import android.slc.mp.po.i.IVideoResult;

import android.slc.medialoader.bean.BaseResult;

import java.util.List;

/**
 * Created by Taurus on 2017/5/24.
 */

public class VideoResult extends BaseResult<IVideoFolder, IVideoItem> implements IVideoResult {
    public VideoResult() {
    }

    public VideoResult(List<IVideoFolder> folders) {
        super(folders);
    }


}
