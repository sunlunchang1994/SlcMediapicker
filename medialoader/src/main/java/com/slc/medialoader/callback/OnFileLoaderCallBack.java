package com.slc.medialoader.callback;

import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import androidx.loader.content.Loader;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import com.slc.medialoader.bean.BaseResult;
import com.slc.medialoader.bean.FileBaseFolder;
import com.slc.medialoader.bean.FileBaseItem;
import com.slc.medialoader.bean.i.IFileProperty;
import com.slc.medialoader.utils.MediaLoaderFileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.provider.MediaStore.MediaColumns.DATE_MODIFIED;
import static android.provider.MediaStore.MediaColumns.MIME_TYPE;

/**
 * Created by Taurus on 2017/5/23.
 */

public abstract class OnFileLoaderCallBack extends OnFileLoaderBaseCallBack<BaseResult<FileBaseFolder<FileBaseItem>,
        FileBaseItem>> {

    public OnFileLoaderCallBack() {
        super();
    }

    public OnFileLoaderCallBack(IFileProperty property) {
        super(property);
    }

    @Override
    public void onLoadFinish(Loader<Cursor> loader, Cursor data) {
        List<FileBaseFolder<FileBaseItem>> folders = new ArrayList<>();
        if (data != null) {
            FileBaseFolder<FileBaseItem> folder, allItemFolder = new FileBaseFolder();
            FileBaseItem item;
            while (data.moveToNext()) {
                int fileId = data.getInt(data.getColumnIndexOrThrow(BaseColumns._ID));
                String path = data.getString(data.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
                long size = data.getLong(data.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE));
                String name = data.getString(data.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME));
                String mime = data.getString(data.getColumnIndexOrThrow(MIME_TYPE));
                long modified = data.getLong(data.getColumnIndexOrThrow(DATE_MODIFIED));
                item = new FileBaseItem();
                item.setId(fileId);
                item.setPath(path);
                item.setSize(size);
                if(TextUtils.isEmpty(name)){
                    name = MediaLoaderFileUtils.getFileName(path);
                }
                item.setDisplayName(name);
                modified = MediaLoaderFileUtils.getFileLastModified(path);
                item.setModified(modified);
                item.setExtension(MediaLoaderFileUtils.getFileExtension(path));
                if (TextUtils.isEmpty(mime)) {
                    mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(item.getExtension());
                }
                item.setMime(mime);

                String parent = path.substring(0, path.lastIndexOf(File.separator));
                folder = new FileBaseFolder<>(parent, parent.substring(parent.lastIndexOf(File.separator) + 1));
                if (folders.contains(folder)) {
                    folders.get(folders.indexOf(folder)).addItem(item);
                } else {
                    folder.addItem(item);
                    folders.add(folder);
                }
                allItemFolder.addItem(item);
            }
            if (!allItemFolder.getItems().isEmpty()) {
                allItemFolder.setName("全部文件");
                folders.add(0, allItemFolder);
            }
        }
        onResult(new BaseResult<>(folders));
    }

}
