package com.slc.mp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.core.content.FileProvider;
import androidx.collection.ArrayMap;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.slc.mp.ui.activity.SlcMpBrowsePhotoActivity;
import com.slc.mp.po.PhotoItem;
import com.slc.mp.po.i.IBaseItem;
import com.slc.mp.po.i.IPhotoItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class SlcMpMediaBrowseUtils {
    public static class Builder {
        public final static String CURRENT_POSITION = "currentPosition";
        public final static String PHOTO_LIST = "photoList";
        public final static String PHOTO_IS_EDIT = "isEdit";
        private int currentPosition;
        private int requestCode;
        private boolean isEdit;
        private ArrayList<IPhotoItem> photoList;

        public Builder() {

        }

        public Builder setCurrentPosition(int currentPosition) {
            if (currentPosition > 0) {
                this.currentPosition = currentPosition;
            }
            return this;
        }

        public Builder setEdit(boolean isEdit) {
            this.isEdit = isEdit;
            return this;
        }

        public Builder setRequestCode(int requestCode) {
            this.requestCode = requestCode;
            return this;
        }

        public Builder setPhoto(String... photos) {
            ArrayList<IPhotoItem> photoList = new ArrayList<>();
            if (photos != null) {
                for (String photo : photos) {
                    PhotoItem photoItem = new PhotoItem();
                    photoItem.setPath(photo);
                    photoList.add(photoItem);
                }
            }
            setPhotoList(photoList);
            return this;
        }

        public Builder setPhoto(IPhotoItem... photos) {
            ArrayList<IPhotoItem> photoList = new ArrayList<>();
            if (photos != null) {
                photoList.addAll(Arrays.asList(photos));
            }
            setPhotoList(photoList);
            return this;
        }

        public Builder setPhotoList(ArrayList<IPhotoItem> photoList) {
            this.photoList = photoList;
            return this;
        }

        public Builder setPhotoMap(ArrayMap<Integer, IBaseItem> photoMap) {
            ArrayList<IPhotoItem> photoList = new ArrayList<>();
            if (photoMap != null) {
                for (int i = 0; i < photoMap.size(); i++) {
                    photoList.add((PhotoItem) photoMap.valueAt(i));
                }
            }
            setPhotoList(photoList);
            return this;
        }

        public void build(Context context) {
            if (photoList != null && photoList.size() > 0) {
                Bundle bundle = new Bundle();
                bundle.putInt(CURRENT_POSITION, currentPosition);
                bundle.putSerializable(PHOTO_LIST, photoList);
                bundle.putBoolean(PHOTO_IS_EDIT, isEdit);
                Intent intent = new Intent(context, SlcMpBrowsePhotoActivity.class);
                intent.putExtras(bundle);
                if (isEdit) {
                    ((Activity) context).startActivityForResult(intent, requestCode);
                } else {
                    context.startActivity(intent);
                }
            } else {
                throw new IllegalStateException("photoList为空，该操作没有任何意义！");
            }
        }
    }

    public static void playerVideo(Context context, String path) {
        openMedia(context, path, "video/*");
    }

    public static void playerAudio(Context context, String path) {
        openMedia(context, path, "audio/*");
    }

    public static void openMedia(Context context, String path, String type) {
        Log.i("openMedia", type);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (path.startsWith("http")) {
            String extension = MimeTypeMap.getFileExtensionFromUrl(path);
            String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            intent.setDataAndType(Uri.parse(path), mimeType);
        } else {
            File file = new File(path);
            if (!file.exists()) {
                throw new IllegalStateException("文件不存在");
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context.getApplicationContext(),
                        context.getApplicationContext().getPackageName() +
                        ".provider", file);
                intent.setDataAndType(contentUri, type);
            } else {
                Uri uri = Uri.fromFile(file);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(uri, type);
            }
        }
        context.startActivity(intent);
    }
}
