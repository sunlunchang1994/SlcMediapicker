package android.slc.mp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.slc.mp.model.ExtensionMap;
import android.slc.mp.utils.SlcMpFilePickerUtils;

import androidx.annotation.Nullable;

import android.slc.mp.SlcMp;
import android.slc.mp.model.SlcMpOperateModel;

/**
 * @author slc
 * @date 2019/11/29 11:28
 */
public class SlcMpBridgingActivity extends Activity {
    /**
     * 显示
     *
     * @param context
     * @param requestCode
     */
    public static void show(Context context, int requestCode) {
        show(context, requestCode, new Bundle());
    }

    /**
     * 显示
     *
     * @param context
     * @param requestCode
     * @param bundle
     */
    public static void show(Context context, int requestCode, Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putInt(SlcMp.Key.KEY_REQUEST_CODE, requestCode);
        Intent intent = new Intent(context, SlcMpBridgingActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int requestCode = getIntent().getIntExtra(SlcMp.Key.KEY_REQUEST_CODE, 0);
        switch (requestCode) {
            case SlcMp.Value.VALUE_REQUEST_CODE_TAKE_PHOTO_DEF:
                String takePhotoSavePath = getIntent().getStringExtra(SlcMp.Key.KEY_TAKE_PHOTO_SAVE_PATH);
                if (takePhotoSavePath == null) {
                    takePhotoSavePath = SlcMpFilePickerUtils.newTakePhotoSavePath();
                    getIntent().putExtra(SlcMp.Key.KEY_TAKE_PHOTO_SAVE_PATH, takePhotoSavePath);
                }
                SlcMpFilePickerUtils.takePhoto(this, requestCode, takePhotoSavePath);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case SlcMp.Value.VALUE_REQUEST_CODE_TAKE_PHOTO_DEF:
                    handleTakePhotoResult(resultCode);
                    break;
            }
        }
    }

    /**
     * 处理拍照返回结果
     */
    private void handleTakePhotoResult(final int resultCode) {
        SlcMpFilePickerUtils.notifyMediaScannerScanFile(this, new MediaScannerConnection.MediaScannerConnectionClient() {
            @Override
            public void onMediaScannerConnected() {
            }

            @Override
            public void onScanCompleted(String path, Uri uri) {
                SlcMpOperateModel.getInstance().postValue(new ExtensionMap().setResultCode(resultCode).setPath(path).setUri(uri));
                finish();
            }
        }, getIntent().getStringExtra(SlcMp.Key.KEY_TAKE_PHOTO_SAVE_PATH));
    }
}
