package com.slc.medialoader;

import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.content.Loader;
import android.text.TextUtils;
import android.util.Log;

import com.slc.medialoader.bean.i.IBaseResult;
import com.slc.medialoader.callback.OnAudioLoaderBaseCallBack;
import com.slc.medialoader.callback.OnFileLoaderBaseCallBack;
import com.slc.medialoader.callback.OnLoaderCallBack;
import com.slc.medialoader.callback.OnPhotoLoaderBaseCallBack;
import com.slc.medialoader.callback.OnVideoLoaderBaseCallBack;
import com.slc.medialoader.loader.AbsLoaderCallBack;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Created by Taurus on 2017/5/23.
 */

public class MediaLoader {

    private static final int DEFAULT_START_ID = 1000;

    private final String TAG = "MediaLoader";
    private static MediaLoader loader = new MediaLoader();

    private Map<String, Queue<LoaderTask>> mTaskGroup = new HashMap<>();

    private Map<String, Integer> mIds = new HashMap<>();

    private final int MSG_CODE_LOAD_FINISH = 101;
    private final int MSG_CODE_LOAD_START = 102;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_CODE_LOAD_FINISH:
                    Message message = Message.obtain();
                    message.what = MSG_CODE_LOAD_START;
                    message.obj = msg.obj;
                    sendMessage(message);
                    break;
                case MSG_CODE_LOAD_START:
                    String group = (String) msg.obj;
                    if (!TextUtils.isEmpty(group)) {
                        Queue<LoaderTask> queue = mTaskGroup.get(group);
                        LoaderTask task = queue.poll();
                        if (task != null) {
                            queueLoader(task.activity.get(), task.onLoaderCallBack);
                        }
                        Log.d(TAG, "after poll current group = " + group + " queue length = " + queue.size());
                    }
                    break;
            }
        }
    };

    private MediaLoader() {
    }

    public static MediaLoader getLoader() {
        return loader;
    }

    private int checkIds(FragmentActivity activity) {
        String name = activity.getClass().getName();
        int id;
        if (!mIds.containsKey(name)) {
            id = DEFAULT_START_ID;
            mIds.put(name, id);
        } else {
            int preId = mIds.get(name);
            preId++;
            mIds.put(name, preId);
            id = preId;
        }
        return id;
    }

    private void loadMedia(FragmentActivity activity, AbsLoaderCallBack absLoaderCallBack) {
        activity.getSupportLoaderManager().restartLoader(checkIds(activity), null, absLoaderCallBack);
    }

    private synchronized void load(FragmentActivity activity, OnLoaderCallBack onLoaderCallBack) {

        String name = activity.getClass().getSimpleName();
        Queue<LoaderTask> queue = mTaskGroup.get(name);
        LoaderTask task = new LoaderTask(new WeakReference<>(activity), onLoaderCallBack);
        if (queue == null) {
            queue = new LinkedList<>();
            mTaskGroup.put(name, queue);
        }
        queue.offer(task);
        Log.d(TAG, "after offer current queue group = " + name + " queue length = " + queue.size());
        if (queue.size() == 1) {
            Message message = Message.obtain();
            message.what = MSG_CODE_LOAD_START;
            message.obj = name;
            mHandler.sendMessage(message);
        }
    }

    private void queueLoader(final FragmentActivity activity, OnLoaderCallBack onLoaderCallBack) {
        loadMedia(activity, new AbsLoaderCallBack(activity, onLoaderCallBack) {
            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                super.onLoaderReset(loader);
                Queue<LoaderTask> queue = mTaskGroup.get(activity.getClass().getSimpleName());
                if (queue != null) {
                    queue.clear();
                }
                Log.d(TAG, "***onLoaderReset***");
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                super.onLoadFinished(loader, data);
                Message message = Message.obtain();
                message.what = MSG_CODE_LOAD_FINISH;
                message.obj = activity.getClass().getSimpleName();
                mHandler.sendMessage(message);
                Log.d(TAG, "***onLoaderFinished***");
            }
        });
    }

    public <S extends IBaseResult> void loadPhotos(FragmentActivity activity,
                                                   OnPhotoLoaderBaseCallBack<S> onPhotoLoaderCallBack) {
        load(activity, onPhotoLoaderCallBack);
    }

    public <S extends IBaseResult> void loadVideos(FragmentActivity activity,
                                                   OnVideoLoaderBaseCallBack<S> onVideoLoaderCallBack) {
        load(activity, onVideoLoaderCallBack);
    }

    public <S extends IBaseResult> void loadAudios(FragmentActivity activity,
                                                   OnAudioLoaderBaseCallBack<S> onAudioLoaderCallBack) {
        load(activity, onAudioLoaderCallBack);
    }

    public <S extends IBaseResult> void loadFiles(FragmentActivity activity, OnFileLoaderBaseCallBack<S> onFileLoaderCallBack) {
        load(activity, onFileLoaderCallBack);
    }

    public static class LoaderTask {
        public WeakReference<FragmentActivity> activity;
        public OnLoaderCallBack onLoaderCallBack;

        public LoaderTask(WeakReference<FragmentActivity> activity, OnLoaderCallBack onLoaderCallBack) {
            this.activity = activity;
            this.onLoaderCallBack = onLoaderCallBack;
        }
    }

}
