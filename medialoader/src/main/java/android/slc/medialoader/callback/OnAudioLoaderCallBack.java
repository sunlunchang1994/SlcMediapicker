package android.slc.medialoader.callback;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.loader.content.Loader;

import android.slc.medialoader.bean.AudioBaseFolder;
import android.slc.medialoader.bean.AudioBaseItem;
import android.slc.medialoader.bean.BaseResult;
import android.slc.medialoader.bean.i.IFileProperty;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Audio.AudioColumns.DURATION;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DATE_MODIFIED;
import static android.provider.MediaStore.MediaColumns.DISPLAY_NAME;
import static android.provider.MediaStore.MediaColumns.SIZE;

/**
 * Created by Taurus on 2017/5/23.
 */

public abstract class OnAudioLoaderCallBack extends OnAudioLoaderBaseCallBack<BaseResult<AudioBaseFolder<AudioBaseItem>,
        AudioBaseItem>> {
    public OnAudioLoaderCallBack() {
        super();
    }

    public OnAudioLoaderCallBack(IFileProperty fileProperty) {
        super(fileProperty);
    }

    @Override
    public void onLoadFinish(Loader<Cursor> loader, Cursor data) {
        List<AudioBaseFolder<AudioBaseItem>> folders = new ArrayList<>();
        if (data != null) {
            AudioBaseFolder<AudioBaseItem> folder, allItemFolder = new AudioBaseFolder<>();
            AudioBaseItem item;
            while (data.moveToNext()) {

                int audioId = data.getInt(data.getColumnIndexOrThrow(_ID));
                String name = data.getString(data.getColumnIndexOrThrow(DISPLAY_NAME));
                String path = data.getString(data.getColumnIndexOrThrow(DATA));
                long duration = data.getLong(data.getColumnIndexOrThrow(DURATION));
                long size = data.getLong(data.getColumnIndexOrThrow(SIZE));
                long modified = data.getLong(data.getColumnIndexOrThrow(DATE_MODIFIED));
                item = new AudioBaseItem();
                item.setId(audioId);
                item.setDisplayName(name);
                item.setPath(path);
                item.setDuration(duration);
                item.setSize(size);
                item.setModified(modified);
                String parent = path.substring(0, path.lastIndexOf(File.separator));
                folder = new AudioBaseFolder<>(parent, parent.substring(parent.lastIndexOf(File.separator) + 1));
                if (folders.contains(folder)) {
                    folders.get(folders.indexOf(folder)).addItem(item);
                } else {
                    folder.addItem(item);
                    folders.add(folder);
                }
                allItemFolder.addItem(item);
            }
            if (!allItemFolder.getItems().isEmpty()) {
                allItemFolder.setName("全部音乐");
                folders.add(0, allItemFolder);
            }
        }
        onResult(new BaseResult<>(folders));
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
