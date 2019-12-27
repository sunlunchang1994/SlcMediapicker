package com.slc.mp.ui.page;

import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.content.Loader;

import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import com.slc.medialoader.MediaLoader;
import com.slc.medialoader.callback.OnFileLoaderBaseCallBack;
import com.slc.medialoader.utils.MediaLoaderFileUtils;
import com.slc.mp.SlcMp;
import com.slc.mp.po.FileFolder;
import com.slc.mp.po.FileItem;
import com.slc.mp.po.FileResult;
import com.slc.mp.po.i.IFileFolder;
import com.slc.mp.po.i.IFileItem;
import com.slc.mp.po.i.IFileResult;
import com.slc.mp.ui.SlcIMpDelegate;
import com.slc.mp.utils.SlcMpMediaBrowseUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.provider.MediaStore.MediaColumns.DATE_MODIFIED;
import static android.provider.MediaStore.MediaColumns.MIME_TYPE;

public class SlcMpPagerFileDelegateImp extends SlcMpPagerBaseDelegateImp<IFileResult, IFileFolder, IFileItem> {

    public SlcMpPagerFileDelegateImp(SlcIMpDelegate mediaPickerDelegate) {
        this(SlcMp.MEDIA_TYPE_AUDIO, mediaPickerDelegate);
    }

    public SlcMpPagerFileDelegateImp(int mediaType, SlcIMpDelegate mediaPickerDelegate) {
        super(mediaType, mediaPickerDelegate);
    }

    @Override
    public void loader(FragmentActivity fragmentActivity, final OnResultListener<List<IFileItem>> loaderCallBack) {
        MediaLoader.getLoader().loadFiles(fragmentActivity,
                new OnFileLoaderBaseCallBack<IFileResult>(mMediaPickerDelegate.getFilePropertyWithMediaType(getMediaType())) {
                    @Override
                    public void onResult(IFileResult result) {
                        mResult = result;
                        loaderCallBack.onLoadResult(getCurrentItemList());
                    }

                    @Override
                    public void onLoadFinish(Loader<Cursor> loader, Cursor data) {
                        List<IFileFolder> folders = new ArrayList<>();
                        if (data != null) {
                            IFileFolder folder, allItemFolder = new FileFolder();
                            IFileItem item;
                            while (data.moveToNext()) {
                                int fileId = data.getInt(data.getColumnIndexOrThrow(BaseColumns._ID));
                                String path = data.getString(data.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
                                long size = data.getLong(data.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE));
                                String name = data.getString(data.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME));
                                String mime = data.getString(data.getColumnIndexOrThrow(MIME_TYPE));
                                long modified = data.getLong(data.getColumnIndexOrThrow(DATE_MODIFIED));
                                item = new FileItem();
                                item.setMediaTypeTag(getMediaType());
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
                                folder = new FileFolder(parent, parent.substring(parent.lastIndexOf(File.separator) + 1));
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
                        onResult(new FileResult(folders));
                    }

                });
    }

    @Override
    public void onItemClick(int position) {
        SlcMpMediaBrowseUtils.openMedia(mMediaPickerDelegate.getContext(), getCurrentItemList().get(position).getPath(),
                getCurrentItemList().get(position).getMime());
    }

    @Override
    public void onItemChildClick(int position) {
        SlcMpMediaBrowseUtils.openMedia(mMediaPickerDelegate.getContext(), getCurrentItemList().get(position).getPath(),
                getCurrentItemList().get(position).getMime());
    }

}
