package android.slc.medialoader.callback;

import android.slc.medialoader.bean.i.IBaseResult;
import android.slc.medialoader.bean.i.IFileProperty;

/**
 * Created by Taurus on 2017/5/23.
 */

public abstract class BaseLoaderCallBack<T extends IBaseResult> extends OnLoaderCallBack {
    protected IFileProperty mFileProperty;

    public BaseLoaderCallBack() {
    }

    public BaseLoaderCallBack(IFileProperty fileProperty) {
        this.mFileProperty = fileProperty;
    }

    public abstract void onResult(T result);

    @Override
    public String getSelections() {
        if (mFileProperty != null) {
            return mFileProperty.createSelection();
        }
        return null;
        //return "(" + MediaStore.MediaColumns.SIZE + " > ? )";
    }

    @Override
    public String[] getSelectionsArgs() {
        if (mFileProperty != null) {
            return mFileProperty.createSelectionArgs();
        }
        return null;
        //return new String[]{"0"};
    }

    @Override
    public String getSortOrderSql() {
        if (mFileProperty != null) {
            return mFileProperty.createSortOrderSql();
        }
        return null;
        //return MediaStore.MediaColumns.DATE_MODIFIED + " DESC";
    }

}
