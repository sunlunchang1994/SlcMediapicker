package com.slc.mp.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.core.content.FileProvider;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author slc
 * @date 2019/11/28 9:30
 */
public class SlcMpFilePickerUtils {
    /**
     * 拍照
     *
     * @param activity
     * @param requestCode
     * @param savePath
     */
    public static void takePhoto(Activity activity, int requestCode, String savePath) {
        if (TextUtils.isEmpty(savePath)) {
            savePath = newTakePhotoSavePath();
        }
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri fileUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {//兼容10.0及以上的写法
            fileUri = activity.getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//兼容7.0及以上的写法
            fileUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", new File(savePath));
        } else {
            fileUri = Uri.fromFile(new File(savePath));
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 获取一个新照片存储路径
     * 路径获取规则为本机照片存储路径加上过根据时间戳生成的文件名
     *
     * @return
     */
    public static String newTakePhotoSavePath() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()
                + "/Camera/" + SlcMpFilePickerUtils.getFileNameByTime("IMG", "jpg");
    }

    /**
     * 通知扫描
     *
     * @param context
     * @param filePath
     */
    public static void notifyMediaScannerScanFile(Context context, MediaScannerConnection.MediaScannerConnectionClient mediaScannerConnectionClient,
                                                  final String... filePath) {
        MediaScannerConnection.scanFile(context, filePath, null, mediaScannerConnectionClient);
    }

    /**
     * @param timeFormatHeader 格式化的头(除去时间部分)
     * @param extension        后缀名
     * @return 返回时间格式化后的文件名
     */
    public static String getFileNameByTime(String timeFormatHeader, String extension) {
        return getTimeFormatName(timeFormatHeader) + "." + extension;
    }

    //TODO 后加
    private static final String TIME_FORMAT = "_yyyyMMdd_HHmmss";

    private static String getTimeFormatName(String timeFormatHeader) {
        final Date date = new Date(System.currentTimeMillis());
        //必须要加上单引号
        final SimpleDateFormat dateFormat = new SimpleDateFormat("'" + timeFormatHeader + "'" + TIME_FORMAT,
                Locale.getDefault());
        return dateFormat.format(date);
    }
}
