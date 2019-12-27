package com.slc.mp.po;

import com.slc.medialoader.bean.BaseResult;
import com.slc.mp.po.i.IFileFolder;
import com.slc.mp.po.i.IFileItem;
import com.slc.mp.po.i.IFileResult;

import java.util.List;

/**
 * Created by Taurus on 2017/5/24.
 */

public class FileResult extends BaseResult<IFileFolder, IFileItem> implements IFileResult {

    public FileResult() {
        super();
    }

    public FileResult(List<IFileFolder> folders) {
        super(folders);
    }
}
