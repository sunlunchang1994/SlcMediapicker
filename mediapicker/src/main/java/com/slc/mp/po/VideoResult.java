package com.slc.mp.po;

import com.slc.medialoader.bean.BaseResult;
import com.slc.mp.po.i.IVideoFolder;
import com.slc.mp.po.i.IVideoItem;
import com.slc.mp.po.i.IVideoResult;

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
