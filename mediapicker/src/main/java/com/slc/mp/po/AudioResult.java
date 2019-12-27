package com.slc.mp.po;

import com.slc.mp.po.i.IAudioFolder;
import com.slc.mp.po.i.IAudioItem;
import com.slc.mp.po.i.IAudioResult;

import java.util.List;

/**
 * Created by Taurus on 2017/5/24.
 */

public class AudioResult extends BaseResult<IAudioFolder, IAudioItem> implements IAudioResult {

    public AudioResult() {
        super();
    }

    public AudioResult(List<IAudioFolder> folders) {
        super(folders);
    }

}
