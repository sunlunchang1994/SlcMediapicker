package com.slc.medialoader.callback;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.loader.content.Loader;

import com.slc.medialoader.bean.BaseResult;
import com.slc.medialoader.bean.PhotoBaseFolder;
import com.slc.medialoader.bean.PhotoBaseItem;
import com.slc.medialoader.bean.i.IBaseResult;
import com.slc.medialoader.bean.i.IFileProperty;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_ID;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DATE_MODIFIED;
import static android.provider.MediaStore.MediaColumns.DISPLAY_NAME;
import static android.provider.MediaStore.MediaColumns.SIZE;

/**
 * Created by Taurus on 2017/5/23.
 */

public abstract class OnPhotoLoaderCallBack extends OnPhotoLoaderBaseCallBack<IBaseResult<PhotoBaseFolder<PhotoBaseItem>, PhotoBaseItem>> {

    public OnPhotoLoaderCallBack() {
        super();
    }

    public OnPhotoLoaderCallBack(IFileProperty fileProperty) {
        super(fileProperty);
    }

    @Override
    public void onLoadFinish(Loader<Cursor> loader, Cursor data) {
        List<PhotoBaseFolder<PhotoBaseItem>> folders = new ArrayList<>();
        if (data != null) {

            PhotoBaseFolder<PhotoBaseItem> folder, allItemFolder = new PhotoBaseFolder<>();
            PhotoBaseItem item;
            while (data.moveToNext()) {
                String folderId = data.getString(data.getColumnIndexOrThrow(BUCKET_ID));
                String folderName = data.getString(data.getColumnIndexOrThrow(BUCKET_DISPLAY_NAME));
                int imageId = data.getInt(data.getColumnIndexOrThrow(_ID));
                String name = data.getString(data.getColumnIndexOrThrow(DISPLAY_NAME));
                long size = data.getLong(data.getColumnIndexOrThrow(SIZE));
                String path = data.getString(data.getColumnIndexOrThrow(DATA));
                long modified = data.getLong(data.getColumnIndexOrThrow(DATE_MODIFIED));
                int width = data.getInt(data.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH));
                int height = data.getInt(data.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT));
                item = new PhotoBaseItem(imageId, name, path, size, modified, width, height);
                folder = new PhotoBaseFolder<>();
                folder.setId(folderId);
                folder.setName(folderName);
                if (folders.contains(folder)) {
                    folders.get(folders.indexOf(folder)).addItem(item);
                } else {
                    folder.setCover(path);
                    folder.addItem(item);
                    folders.add(folder);
                }
                allItemFolder.addItem(item);
            }
            if (!allItemFolder.getItems().isEmpty()) {
                allItemFolder.setCover(allItemFolder.getItems().get(0).getPath());
                allItemFolder.setName("全部图片");
                folders.add(0, allItemFolder);
            }
        }
        onResult(new BaseResult<>(folders));
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
