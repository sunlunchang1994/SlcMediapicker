package android.slc.medialoader.bean;

import android.slc.medialoader.bean.i.IBaseFolder;
import android.slc.medialoader.bean.i.IBaseItem;
import android.slc.medialoader.bean.i.IBaseResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taurus on 2017/5/24.
 */

public class BaseResult<F extends IBaseFolder<T>, T extends IBaseItem> implements IBaseResult<F, T> {
    protected long totalSize = -1;
    protected List<F> folders;

    public BaseResult() {
    }

    public BaseResult(List<F> folders) {
        this.folders = folders;
    }

    public long getTotalSize() {
        if (totalSize == -1) {
            totalSize = getAllItems().size();
        }
        return totalSize;
    }

    public void setFolders(List<F> folders) {
        this.folders = folders;
    }

    public List<F> getFolders() {
        return folders;
    }

    public F getAllItemFolder() {
        if (folders.isEmpty()) {
            return null;
        }
        return folders.get(0);
    }

    public List<T> getAllItems() {
        F folder = getAllItemFolder();
        if (folder == null) {
            return new ArrayList<>();
        }
        return folder.getItems();
    }
}
