package android.slc.medialoader.bean.i;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Taurus on 2017/5/24.
 */

public interface IBaseResult<F extends IBaseFolder<T>, T extends IBaseItem> extends Serializable {

    long getTotalSize();

    void setFolders(List<F> folders);

    List<F> getFolders();

    F getAllItemFolder();

    List<T> getAllItems();
}
