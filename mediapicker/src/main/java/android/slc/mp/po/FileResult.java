package android.slc.mp.po;

import android.slc.mp.po.i.IFileFolder;
import android.slc.mp.po.i.IFileItem;
import android.slc.mp.po.i.IFileResult;

import android.slc.medialoader.bean.BaseResult;

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
