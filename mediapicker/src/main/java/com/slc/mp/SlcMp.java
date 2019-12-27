package com.slc.mp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.slc.mp.model.ExtensionMap;
import com.slc.mp.model.SlcMpOperateModel;
import com.slc.mp.model.SlcMpResultModel;
import com.slc.mp.po.i.IBaseItem;
import com.slc.mp.ui.activity.SlcMpBridgingActivity;
import com.slc.mp.utils.SlcMpFilePickerUtils;

import java.util.List;


public class SlcMp {
    //媒体类型
    public static final int MEDIA_TYPE_NULL = -1;
    public static final int MEDIA_TYPE_PHOTO = -2;
    public static final int MEDIA_TYPE_VIDEO = -3;
    public static final int MEDIA_TYPE_AUDIO = -4;
    public static final int MEDIA_TYPE_FILE = -5;

    private static SlcMp mSlcMp;
    private SlcMpConfig mGlobalSlcMpConfig = new SlcMpConfig.Builder().build();
    private SlcMpConfig mTemporarySlcMpConfig;
    private Context mApp;

    private SlcMp() {

    }

    /**
     * 获取实例
     *
     * @return
     */
    public static SlcMp getInstance() {
        if (mSlcMp == null) {
            synchronized (SlcMp.class) {
                if (mSlcMp == null) {
                    mSlcMp = new SlcMp();
                }
            }
        }
        return mSlcMp;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void initMp(Context context) {
        mApp = context.getApplicationContext();
    }

    /**
     * 获取Context
     *
     * @return
     */
    public Context getApp() {
        return mApp;
    }

    /**
     * 初始化选择器配置
     *
     * @param slcMpConfig
     */
    public void initGlobalMpConfig(SlcMpConfig slcMpConfig) {
        checkInit();
        if (slcMpConfig != null) {
            mGlobalSlcMpConfig = slcMpConfig;
        }
    }

    private void checkInit() {
        if (mApp == null) {
            throw new IllegalStateException("请初始化");
        }
    }

    /**
     * 选择配置
     *
     * @return
     */
    public SlcMpConfig optMpConfig() {
        return mTemporarySlcMpConfig != null ? mTemporarySlcMpConfig : mGlobalSlcMpConfig;
    }

    /**
     * 获取全局配置
     *
     * @return
     */
    public SlcMpConfig getGlobalMpConfig() {
        return mGlobalSlcMpConfig;
    }

    /**
     * 移除临时配置
     */
    public void removeTemporaryMpConfig() {
        mTemporarySlcMpConfig = null;
    }

    /**
     * 移除选择结果订阅
     * 在创建选择器时添加订阅(@Builder.observeForever)
     *
     * @param observer
     */
    public void removeObserver(@NonNull final Observer<List<IBaseItem>> observer) {
        SlcMpResultModel.getInstance().removeObserver(observer);
    }

    /**
     * 移除选择结果订阅 ,根据LifecycleOwner
     * 在创建选择器时添加订阅(@Builder.observeForever)
     *
     * @param owner
     */
    public void removeObservers(@NonNull final LifecycleOwner owner) {
        SlcMpResultModel.getInstance().removeObservers(owner);
    }

    /**
     * 获取一个选择器构建工具，根据activity
     *
     * @param activity
     * @return
     */
    public Builder with(Activity activity) {
        return new BuilderOfActivity(activity);
    }

    /**
     * 获取一个选择器构建工具，根据fragment
     * 该方法无效，未进行正确实现
     *
     * @param activity
     * @return
     */
    @Deprecated
    public Builder with(Fragment activity) {
        return new BuilderOfFragment(activity);
    }

    /**
     * 拍照
     *
     * @param context
     */
    public void takePhoto(Context context, @NonNull Observer<ExtensionMap> observer) {
        takePhoto(context, null, observer);
    }

    /**
     * 拍照
     *
     * @param context
     */
    public void takePhoto(Context context, @NonNull Observer<ExtensionMap> observer, String savePath) {
        takePhoto(context, null, observer, savePath);
    }

    /**
     * 拍照
     *
     * @param context
     */
    public void takePhoto(Context context, LifecycleOwner owner, @NonNull Observer<ExtensionMap> observer) {
        takePhoto(context, owner, observer, SlcMpFilePickerUtils.newTakePhotoSavePath());
    }

    /**
     * 拍照
     *
     * @param context
     * @param savePath
     */
    public void takePhoto(Context context, LifecycleOwner owner, @NonNull Observer<ExtensionMap> observer, String savePath) {
        if (owner != null) {
            SlcMpOperateModel.getInstance().observe(owner, observer);
        } else {
            SlcMpOperateModel.getInstance().observeForever(observer);
        }
        Bundle bundle = new Bundle();
        bundle.putString(Key.KEY_TAKE_PHOTO_SAVE_PATH, savePath);
        SlcMpBridgingActivity.show(context, Value.VALUE_REQUEST_CODE_TAKE_PHOTO_DEF, bundle);
    }

    /**
     * 根据intent获取结果
     * 不推荐使用，请使用@Builder.observe或Builder.observeForever
     *
     * @param intent
     * @return
     */
    @Deprecated
    @SuppressWarnings("unchecked")
    public static List<IBaseItem> getResultByIntent(@NonNull Intent intent) {
        try {
            return (List<IBaseItem>) intent.getSerializableExtra(Key.KEY_RESULT_LIST);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public abstract static class Builder {
        SlcMpConfig mSlcMpConfig;
        int mRequestCode = Value.VALUE_REQUEST_CODE_MP_DEF;

        public Builder() {
            SlcMp.getInstance().checkInit();
        }

        /**
         * 设置请求码
         * 该方法没有实际意义，选择结果监听已交给LiveData
         *
         * @param requestCode
         * @return
         */
        @Deprecated
        public Builder setRequestCode(int requestCode) {
            this.mRequestCode = requestCode;
            return this;
        }

        /**
         * 设置配置工具
         *
         * @param slcMpConfig
         * @return
         */
        public Builder setMpConfig(SlcMpConfig slcMpConfig) {
            this.mSlcMpConfig = slcMpConfig;
            return this;
        }

        /**
         * 添加订阅
         *
         * @param owner
         * @param observer
         * @return
         */
        public Builder observe(@NonNull LifecycleOwner owner, @NonNull Observer<List<IBaseItem>> observer) {
            SlcMpResultModel.getInstance().observe(owner, observer);
            return this;
        }

        /**
         * 添加订阅
         *
         * @param observer
         * @return
         */
        public Builder observeForever(@NonNull Observer<List<IBaseItem>> observer) {
            SlcMpResultModel.getInstance().observeForever(observer);
            return this;
        }

        /**
         * 构建
         */
        public void build() {
            if (mSlcMpConfig != null) {
                mSlcMpConfig.ensureMpConfig(SlcMp.getInstance().mGlobalSlcMpConfig);
                SlcMp.getInstance().mTemporarySlcMpConfig = mSlcMpConfig;
            }
        }
    }

    public static class BuilderOfActivity extends Builder {
        private Activity mActivity;

        public BuilderOfActivity(Activity activity) {
            this.mActivity = activity;
        }

        /**
         * 构建
         */
        @Override
        public void build() {
            super.build();
            if (mActivity != null) {
                /*Intent intent = new Intent(mActivity, MpBridgeActivity.class);
                intent.putExtra(Key.KEY_REQUEST_CODE, mRequestCode);
                mActivity.startActivity(intent);*/
                Intent intent = new Intent(mActivity, SlcMp.getInstance().optMpConfig().getTargetUi());
                intent.putExtras(SlcMp.getInstance().optMpConfig().getBundle());
                mActivity.startActivityForResult(intent, mRequestCode);
            }
        }
    }

    public static class BuilderOfFragment extends Builder {
        private Fragment mFragment;

        public BuilderOfFragment(Fragment fragment) {
            this.mFragment = fragment;
        }

        @Override
        public void build() {
            super.build();
            if (mFragment != null) {
                Intent intent = new Intent(mFragment.getContext(), SlcMp.getInstance().optMpConfig().getTargetUi());
                intent.putExtras(SlcMp.getInstance().optMpConfig().getBundle());
                mFragment.startActivityForResult(intent, mRequestCode);
            }
        }
    }

    /**
     * 相关数据字段 key
     */
    public static class Key {
        public static final String KEY_MEDIA_TYPE = "mediaType";
        public static final String KEY_MAX_PICKER = "maxPicker";
        public static final String KEY_SPAN_COUNT = "spanCount";
        public static final String KEY_MEDIA_TYPE_LIST = "mediaTypeList";
        public static final String KEY_MEDIA_TYPE_TITLE_LIST = "mediaTypeTitleList";
        public static final String KEY_MEDIA_TYPE_FILE_PROPERTY_LIST = "mediaTypeFilePropertyList";
        public static final String KEY_MEDIA_TYPE_MUDDY_LIST = "mediaTypeMuddyList";
        public static final String KEY_IS_MULTIPLE_SELECTION = "isMultipleSelection";
        public static final String KEY_IS_MULTIPLE_MEDIA_TYPE = "isMultipleMediaType";
        public static final String KEY_RESULT_LIST = "resultList";
        public static final String KEY_REQUEST_CODE = "requestCode";
        public static final String KEY_TAKE_PHOTO_SAVE_PATH = "takePhotoSavePath";//拍照保存路径
    }

    /**
     * 相关默认值
     */
    public static class Value {
        private static final int VALUE_REQUEST_CODE_BASE = 67;
        public static final int VALUE_REQUEST_CODE_MP_DEF = VALUE_REQUEST_CODE_BASE + 1;//媒体选择默认请求码
        public static final int VALUE_REQUEST_CODE_TAKE_PHOTO_DEF = VALUE_REQUEST_CODE_BASE + 2;//拍摄照片默认请求码
        //默认每行的个数
        public static final int VALUE_SPAN_COUNT_DEF_VALUE = 4;
        public static final int VALUE_DEF_MAX_PICKER = 9;
        public static final boolean VALUE_DEF_MULTIPLE_SELECTION = true;
        public static final boolean VALUE_DEF_MULTIPLE_MEDIA_TYPE = true;

    }
}
