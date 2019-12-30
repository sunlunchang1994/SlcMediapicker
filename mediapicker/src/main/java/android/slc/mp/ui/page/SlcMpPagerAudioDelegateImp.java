package android.slc.mp.ui.page;

import android.database.Cursor;
import android.slc.mp.utils.SlcMpMediaBrowseUtils;

import androidx.fragment.app.FragmentActivity;
import androidx.loader.content.Loader;

import android.slc.medialoader.MediaLoader;
import android.slc.medialoader.callback.OnAudioLoaderBaseCallBack;
import android.slc.medialoader.utils.MediaLoaderFileUtils;
import android.slc.mp.SlcMp;
import android.slc.mp.po.AudioFolder;
import android.slc.mp.po.AudioItem;
import android.slc.mp.po.AudioResult;
import android.slc.mp.po.SelectEvent;
import android.slc.mp.po.i.IAudioFolder;
import android.slc.mp.po.i.IAudioItem;
import android.slc.mp.po.i.IAudioResult;
import android.slc.mp.ui.SlcIMpDelegate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Audio.AudioColumns.DURATION;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DATE_MODIFIED;
import static android.provider.MediaStore.MediaColumns.DISPLAY_NAME;
import static android.provider.MediaStore.MediaColumns.SIZE;

public class SlcMpPagerAudioDelegateImp extends SlcMpPagerBaseDelegateImp<IAudioResult, IAudioFolder, IAudioItem> {

    public SlcMpPagerAudioDelegateImp(SlcIMpDelegate mediaPickerDelegate) {
        super(SlcMp.MEDIA_TYPE_AUDIO, mediaPickerDelegate);
    }

    @Override
    public void loader(FragmentActivity fragmentActivity, final OnResultListener<List<IAudioItem>> loaderCallBack) {
        MediaLoader.getLoader().loadAudios(fragmentActivity,
                new OnAudioLoaderBaseCallBack<IAudioResult>(mMediaPickerDelegate.getFilePropertyWithMediaType(getMediaType())) {

                    @Override
                    public void onResult(IAudioResult result) {
                        mResult = result;
                        loaderCallBack.onLoadResult(getCurrentItemList());
                    }

                    @Override
                    public void onLoadFinish(Loader<Cursor> loader, Cursor data) {
                        List<IAudioFolder> folders = new ArrayList<>();
                        if (data != null) {
                            IAudioFolder folder, allItemFolder = new AudioFolder();
                            IAudioItem item;
                            while (data.moveToNext()) {
                                int audioId = data.getInt(data.getColumnIndexOrThrow(_ID));
                                String name = data.getString(data.getColumnIndexOrThrow(DISPLAY_NAME));
                                String path = data.getString(data.getColumnIndexOrThrow(DATA));
                                long duration = data.getLong(data.getColumnIndexOrThrow(DURATION));
                                long size = data.getLong(data.getColumnIndexOrThrow(SIZE));
                                long modified = data.getLong(data.getColumnIndexOrThrow(DATE_MODIFIED));
                                item = new AudioItem();
                                item.setExtension(MediaLoaderFileUtils.getFileExtension(path));
                                item.setMediaTypeTag(getMediaType());
                                item.setId(audioId);
                                item.setDisplayName(name);
                                item.setPath(path);
                                item.setDuration(duration);
                                item.setSize(size);
                                item.setModified(modified);
                                String parent = path.substring(0, path.lastIndexOf(File.separator));
                                folder = new AudioFolder(parent, parent.substring(parent.lastIndexOf(File.separator) + 1));
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
                        onResult(new AudioResult(folders));
                    }

                });
    }

    @Override
    public void onItemClick(int position) {
        SlcMpMediaBrowseUtils.playerAudio(mMediaPickerDelegate.getContext(), getCurrentItemList().get(position).getPath());
    }

    @Override
    public void onItemChildClick(int position) {
        SlcMpMediaBrowseUtils.playerAudio(mMediaPickerDelegate.getContext(), getCurrentItemList().get(position).getPath());
    }

    @Override
    public Object onSelectEvent(int eventCode, SelectEvent event) {
        switch (eventCode) {
            case SelectEvent.EVENT_LISTENER_MEDIA_TYPE:
                return SlcMp.MEDIA_TYPE_AUDIO;
        }
        return null;
    }

}
