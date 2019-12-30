package android.slc.medialoader.callback;

import android.net.Uri;
import android.provider.MediaStore;

import android.slc.medialoader.bean.i.IBaseResult;
import android.slc.medialoader.bean.i.IFileProperty;

/**
 * Created by Taurus on 2017/5/23.
 */

public abstract class OnPhotoLoaderBaseCallBack<S extends IBaseResult> extends BaseLoaderCallBack<S> {

    public OnPhotoLoaderBaseCallBack() {
        super();
    }

    public OnPhotoLoaderBaseCallBack(IFileProperty fileProperty) {
        super(fileProperty);
    }

    @Override
    public Uri getQueryUri() {
        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }

    @Override
    public String[] getSelectProjection() {
        String[] PROJECTION = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.WIDTH,
                MediaStore.Images.Media.HEIGHT,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DATE_MODIFIED
        };
        return PROJECTION;
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
