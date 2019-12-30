package android.slc.medialoader.callback;

import android.net.Uri;
import android.provider.MediaStore;

import android.slc.medialoader.bean.i.IBaseResult;
import android.slc.medialoader.bean.i.IFileProperty;

/**
 * Created by Taurus on 2017/5/23.
 */

public abstract class OnVideoLoaderBaseCallBack<S extends IBaseResult> extends BaseLoaderCallBack<S> {

    public OnVideoLoaderBaseCallBack() {
        super();
    }

    public OnVideoLoaderBaseCallBack(IFileProperty fileProperty) {
        super(fileProperty);
    }

    @Override
    public String[] getSelectProjection() {
        String[] PROJECTION = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.BUCKET_ID,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.MediaColumns.SIZE,
                MediaStore.Video.Media.DATE_MODIFIED
        };
        return PROJECTION;
    }

    @Override
    public Uri getQueryUri() {
        return MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    }

    @Override
    public String getSelections() {
        if (mFileProperty != null) {
            return mFileProperty.createSelection();
        }
        return "(" + MediaStore.MediaColumns.SIZE + " > ? )";
    }

    @Override
    public String[] getSelectionsArgs() {
        if (mFileProperty != null) {
            return mFileProperty.createSelectionArgs();
        }
        return new String[]{"0"};
    }

    @Override
    public String getSortOrderSql() {
        if (mFileProperty != null) {
            return mFileProperty.createSortOrderSql();
        }
        return MediaStore.MediaColumns.DATE_MODIFIED + " DESC";
    }
}
