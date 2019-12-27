package com.slc.medialoader.callback;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.loader.content.Loader;

import com.slc.medialoader.bean.BaseResult;
import com.slc.medialoader.bean.VideoBaseFolder;
import com.slc.medialoader.bean.VideoBaseItem;
import com.slc.medialoader.bean.i.IBaseResult;
import com.slc.medialoader.bean.i.IFileProperty;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DATE_MODIFIED;
import static android.provider.MediaStore.MediaColumns.DISPLAY_NAME;
import static android.provider.MediaStore.MediaColumns.SIZE;
import static android.provider.MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.Video.VideoColumns.BUCKET_ID;
import static android.provider.MediaStore.Video.VideoColumns.DURATION;

/**
 * Created by Taurus on 2017/5/23.
 */

public abstract class OnVideoLoaderCallBack extends OnVideoLoaderBaseCallBack<IBaseResult<VideoBaseFolder<VideoBaseItem>,
        VideoBaseItem>> {

    public OnVideoLoaderCallBack() {
        super();
    }

    public OnVideoLoaderCallBack(IFileProperty fileProperty) {
        super(fileProperty);
    }

    @Override
    public void onLoadFinish(Loader<Cursor> loader, Cursor data) {
        List<VideoBaseFolder<VideoBaseItem>> folders = new ArrayList<>();
        if (data != null) {
            VideoBaseFolder<VideoBaseItem> folder, allItemFolder = new VideoBaseFolder<>();
            VideoBaseItem item;
            while (data.moveToNext()) {
                String folderId = data.getString(data.getColumnIndexOrThrow(BUCKET_ID));
                String folderName = data.getString(data.getColumnIndexOrThrow(BUCKET_DISPLAY_NAME));
                int videoId = data.getInt(data.getColumnIndexOrThrow(_ID));
                String name = data.getString(data.getColumnIndexOrThrow(DISPLAY_NAME));
                String path = data.getString(data.getColumnIndexOrThrow(DATA));
                long duration = data.getLong(data.getColumnIndexOrThrow(DURATION));
                long size = data.getLong(data.getColumnIndexOrThrow(SIZE));
                long modified = data.getLong(data.getColumnIndexOrThrow(DATE_MODIFIED));
                item = new VideoBaseItem(videoId, name, path, size, modified, duration);
                folder = new VideoBaseFolder<>();
                folder.setId(folderId);
                folder.setName(folderName);
                if (folders.contains(folder)) {
                    folders.get(folders.indexOf(folder)).addItem(item);
                } else {
                    folder.addItem(item);
                    folders.add(folder);
                }
                allItemFolder.addItem(item);
            }
            if (!allItemFolder.getItems().isEmpty()) {
                allItemFolder.setName("全部视频");
                folders.add(0, allItemFolder);
            }
        }
        onResult(new BaseResult<>(folders));
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
