package android.slc.medialoader.callback;

import android.net.Uri;
import android.provider.MediaStore;

import android.slc.medialoader.bean.i.IBaseResult;
import android.slc.medialoader.bean.i.IFileProperty;

/**
 * Created by Taurus on 2017/5/23.
 */

public abstract class OnAudioLoaderBaseCallBack<S extends IBaseResult> extends BaseLoaderCallBack<S> {
    public OnAudioLoaderBaseCallBack() {
        super();
    }

    public OnAudioLoaderBaseCallBack(IFileProperty fileProperty) {
        super(fileProperty);
    }


    @Override
    public Uri getQueryUri() {
        return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    }

    @Override
    public String[] getSelectProjection() {
        String[] PROJECTION = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.MediaColumns.SIZE,
                MediaStore.Audio.Media.DATE_MODIFIED
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
