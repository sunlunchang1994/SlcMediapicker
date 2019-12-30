package android.slc.medialoader.callback;

import android.net.Uri;
import android.provider.MediaStore;

import android.slc.medialoader.bean.i.IBaseResult;
import android.slc.medialoader.bean.i.IFileProperty;

/**
 * Created by Taurus on 2017/5/23.
 */

public abstract class BaseFileLoaderCallBack<T extends IBaseResult> extends BaseLoaderCallBack<T> {

    public static final String VOLUME_NAME = "external";

    public BaseFileLoaderCallBack() {
        super();
    }

    public BaseFileLoaderCallBack(IFileProperty property) {
        super(property);
    }

    @Override
    public Uri getQueryUri() {
        return MediaStore.Files.getContentUri(VOLUME_NAME);
    }

    @Override
    public String[] getSelectProjection() {
        return new String[]{
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.DATE_MODIFIED
        };
    }

}
