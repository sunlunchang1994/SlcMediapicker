package android.slc.mp.ui.page;

import android.database.Cursor;
import android.slc.mp.utils.SlcMpMediaBrowseUtils;

import androidx.fragment.app.FragmentActivity;
import androidx.loader.content.Loader;

import android.slc.medialoader.MediaLoader;
import android.slc.medialoader.callback.OnVideoLoaderBaseCallBack;
import android.slc.medialoader.utils.MediaLoaderFileUtils;
import android.slc.mp.SlcMp;
import android.slc.mp.po.SelectEvent;
import android.slc.mp.po.VideoFolder;
import android.slc.mp.po.VideoItem;
import android.slc.mp.po.VideoResult;
import android.slc.mp.po.i.IVideoFolder;
import android.slc.mp.po.i.IVideoItem;
import android.slc.mp.po.i.IVideoResult;
import android.slc.mp.ui.SlcIMpDelegate;

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

public class SlcMpPagerVideoDelegateImp extends SlcMpPagerBaseDelegateImp<IVideoResult, IVideoFolder, IVideoItem> {

    public SlcMpPagerVideoDelegateImp(SlcIMpDelegate mediaPickerDelegate) {
        super(SlcMp.MEDIA_TYPE_VIDEO, mediaPickerDelegate);
    }

    @Override
    public void loader(FragmentActivity fragmentActivity, final OnResultListener<List<IVideoItem>> loaderCallBack) {
        MediaLoader.getLoader().loadVideos(fragmentActivity,
                new OnVideoLoaderBaseCallBack<IVideoResult>(mMediaPickerDelegate.getFilePropertyWithMediaType(getMediaType())) {
                    @Override
                    public void onResult(IVideoResult result) {
                        mResult = result;
                        loaderCallBack.onLoadResult(getCurrentItemList());
                    }

                    @Override
                    public void onLoadFinish(Loader<Cursor> loader, Cursor data) {
                        List<IVideoFolder> folders = new ArrayList<>();
                        if (data != null) {
                            IVideoFolder folder, allItemFolder = new VideoFolder();
                            IVideoItem item;
                            while (data.moveToNext()) {
                                String folderId = data.getString(data.getColumnIndexOrThrow(BUCKET_ID));
                                String folderName = data.getString(data.getColumnIndexOrThrow(BUCKET_DISPLAY_NAME));
                                int videoId = data.getInt(data.getColumnIndexOrThrow(_ID));
                                String name = data.getString(data.getColumnIndexOrThrow(DISPLAY_NAME));
                                String path = data.getString(data.getColumnIndexOrThrow(DATA));
                                long duration = data.getLong(data.getColumnIndexOrThrow(DURATION));
                                long size = data.getLong(data.getColumnIndexOrThrow(SIZE));
                                long modified = data.getLong(data.getColumnIndexOrThrow(DATE_MODIFIED));
                                item = new VideoItem(videoId, name, path, size, modified, duration);
                                item.setExtension(MediaLoaderFileUtils.getFileExtension(path));
                                item.setMediaTypeTag(getMediaType());
                                folder = new VideoFolder();
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
                        onResult(new VideoResult(folders));
                    }
                });
    }

    @Override
    public void onItemClick(int position) {
        SlcMpMediaBrowseUtils.playerVideo(mMediaPickerDelegate.getContext(), getCurrentItemList().get(position).getPath());
    }

    @Override
    public void onItemChildClick(int position) {
        SlcMpMediaBrowseUtils.playerVideo(mMediaPickerDelegate.getContext(), getCurrentItemList().get(position).getPath());
    }

    @Override
    public Object onSelectEvent(int eventCode, SelectEvent event) {
        switch (eventCode) {
            case SelectEvent.EVENT_LISTENER_MEDIA_TYPE:
                return SlcMp.MEDIA_TYPE_VIDEO;
        }
        return null;
    }
}
