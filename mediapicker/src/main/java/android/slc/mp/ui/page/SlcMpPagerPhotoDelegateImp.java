package android.slc.mp.ui.page;

import android.app.Activity;
import android.database.Cursor;
import android.provider.MediaStore;
import android.slc.mp.model.ExtensionMap;
import android.slc.mp.model.SlcMpOperateModel;
import android.slc.mp.utils.SlcMpMediaBrowseUtils;
import android.slc.mp.utils.UriUtils;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.loader.content.Loader;

import android.slc.medialoader.MediaLoader;
import android.slc.medialoader.bean.i.IFileProperty;
import android.slc.medialoader.callback.OnPhotoLoaderBaseCallBack;
import android.slc.medialoader.utils.MediaLoaderFileUtils;
import android.slc.mp.SlcMp;
import android.slc.mp.po.AddPhotoItem;
import android.slc.mp.po.PhotoFolder;
import android.slc.mp.po.PhotoItem;
import android.slc.mp.po.PhotoResult;
import android.slc.mp.po.SelectEvent;
import android.slc.mp.po.i.IPhotoFolder;
import android.slc.mp.po.i.IPhotoItem;
import android.slc.mp.po.i.IPhotoResult;
import android.slc.mp.ui.SlcIMpDelegate;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_ID;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DATE_MODIFIED;
import static android.provider.MediaStore.MediaColumns.DISPLAY_NAME;
import static android.provider.MediaStore.MediaColumns.SIZE;

public class SlcMpPagerPhotoDelegateImp extends SlcMpPagerBaseDelegateImp<IPhotoResult, IPhotoFolder, IPhotoItem> {
    private List<IPhotoItem> mHeadItem = new ArrayList<>();
    private WeakReference<OnResultListener<List<IPhotoItem>>> resultListenerWeakReference;

    public SlcMpPagerPhotoDelegateImp(SlcIMpDelegate mediaPickerDelegate) {
        super(SlcMp.MEDIA_TYPE_PHOTO, mediaPickerDelegate);
        mHeadItem.add(new AddPhotoItem());
    }

    @Override
    public void loader(FragmentActivity fragmentActivity, final OnResultListener<List<IPhotoItem>> loaderCallBack) {
        resultListenerWeakReference = new WeakReference<>(loaderCallBack);
        MediaLoader.getLoader().loadPhotos(fragmentActivity,
                new OnPhotoLoaderBaseCallBack<IPhotoResult>(mMediaPickerDelegate.getFilePropertyWithMediaType(getMediaType())) {
                    @Override
                    public void onResult(IPhotoResult result) {
                        mResult = result;
                        loaderCallBack.onLoadResult(getCurrentItemList());
                    }

                    @Override
                    public void onLoadFinish(Loader<Cursor> loader, Cursor data) {
                        List<IPhotoFolder> folders = new ArrayList<>();
                        if (data != null) {
                            IPhotoFolder folder, allItemFolder = new PhotoFolder();
                            PhotoItem item;
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
                                item = new PhotoItem(imageId, name, path, size, modified, width, height);
                                item.setMediaTypeTag(getMediaType());
                                item.setExtension(MediaLoaderFileUtils.getFileExtension(path));
                                folder = new PhotoFolder();
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
                            allItemFolder.getItems().addAll(0, mHeadItem);
                            if (allItemFolder.getItems().size() > mHeadItem.size()) {
                                allItemFolder.setCover(allItemFolder.getItems().get(mHeadItem.size()).getPath());
                                allItemFolder.setName("全部图片");
                                folders.add(0, allItemFolder);
                            }
                        }
                        onResult(new PhotoResult(folders));
                    }

                });
    }

    @Override
    public void onItemClick(int position) {
        if (getCurrentItemList().get(position) instanceof AddPhotoItem) {
            takePhoto();
        } else {
            new SlcMpMediaBrowseUtils.Builder().setCurrentPosition(position).setEdit(true)
                    .setPhoto(getCurrentItemList().get(position)).build(mMediaPickerDelegate.getContext());
        }
    }

    @Override
    public void onItemChildClick(int position) {
        if (getCurrentItemList().get(position) instanceof AddPhotoItem) {
            takePhoto();
        } else {
            new SlcMpMediaBrowseUtils.Builder().setCurrentPosition(position).setEdit(true)
                    .setPhoto(getCurrentItemList().get(position)).build(mMediaPickerDelegate.getContext());
        }
    }

    /**
     * 拍照
     */
    protected void takePhoto() {
        SlcMp.getInstance().takePhoto(mMediaPickerDelegate.getContext(), new Observer<ExtensionMap>() {
            @Override
            public void onChanged(ExtensionMap extensionMap) {
                SlcMpOperateModel.getInstance().removeObserver(this);
                switch (extensionMap.getResultCode()) {
                    case Activity.RESULT_OK:
                        final File file = UriUtils.uri2File(mMediaPickerDelegate.getContext(), extensionMap.getUri());
                        MediaLoader.getLoader().loadPhotos((FragmentActivity) mMediaPickerDelegate.getContext(),
                                new OnPhotoLoaderBaseCallBack<IPhotoResult>(new IFileProperty() {
                                    @Override
                                    public String createSelection() {
                                        StringBuilder extensionBuilder = new StringBuilder();
                                        extensionBuilder.append("(").append(DATA).append(" == ? ").append(")");
                                        return extensionBuilder.toString();
                                    }

                                    @Override
                                    public String[] createSelectionArgs() {
                                        return new String[]{file.getPath()};
                                    }

                                    @Override
                                    public String createSortOrderSql() {
                                        return MediaStore.MediaColumns.DATE_MODIFIED + " DESC";
                                    }
                                }) {
                                    @Override
                                    public void onLoadFinish(Loader<Cursor> loader, Cursor data) {
                                        if (data != null) {
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
                                                PhotoItem item = new PhotoItem(imageId, name, path, size, modified, width, height);
                                                item.setMediaTypeTag(getMediaType());
                                                item.setExtension(MediaLoaderFileUtils.getFileExtension(path));

                                                PhotoFolder folder = new PhotoFolder();
                                                folder.setId(folderId);
                                                folder.setName(folderName);
                                                if (mResult.getFolders().contains(folder)) {
                                                    mResult.getFolders().get(mResult.getFolders().indexOf(folder)).getItems().add(0, item);
                                                } else {
                                                    folder.setCover(path);
                                                    folder.addItem(item);
                                                    mResult.getFolders().add(folder);
                                                }
                                                mCurrentItemList.add(mHeadItem.size(), item);
                                                mResult.getAllItemFolder().getItems().add(mHeadItem.size(), item);
                                            }
                                            onResult(mResult);
                                        }
                                    }

                                    @Override
                                    public void onResult(IPhotoResult result) {
                                        if (resultListenerWeakReference.get() != null) {
                                            resultListenerWeakReference.get().onLoadResult(getCurrentItemList());
                                        }
                                    }
                                });
                        break;
                }
            }
        });
    }

    @Override
    public Object onSelectEvent(int eventCode, SelectEvent event) {
        switch (eventCode) {
            case SelectEvent.EVENT_LISTENER_MEDIA_TYPE:
                return SlcMp.MEDIA_TYPE_PHOTO;
        }
        return null;
    }

}
