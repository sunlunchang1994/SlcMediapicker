package com.slc.mp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;
import androidx.collection.SparseArrayCompat;
import androidx.fragment.app.Fragment;

import com.slc.medialoader.bean.i.IFileProperty;
import com.slc.mp.R;
import com.slc.mp.SlcMp;
import com.slc.mp.model.SlcMpResultModel;
import com.slc.mp.po.SelectEvent;
import com.slc.mp.po.i.IBaseItem;
import com.slc.mp.ui.SlcIMpDelegate;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlcMpDelegateImp implements SlcIMpDelegate {
    private Bundle mBundle;//文件选择界面参数
    private SparseArrayCompat<String> mTableTitle = new SparseArrayCompat<>();//多种文件的选择是显示的table的title集合
    private Map<Integer, IFileProperty> mediaTypeFilePropertyList;//文件过滤器
    private ArrayMap<Integer, IBaseItem> selectItemArrayMap = new ArrayMap<>();//选择过的文件集合
    private List<WeakReference<OnSelectEventListener>> onSelectEventListenerWeakReferenceList = new ArrayList<>();//选择事件集合
    private Fragment mFragment;//fragment
    private Activity mActivity;//activity

    public SlcMpDelegateImp(Activity activity) {
        this.mActivity = activity;
        mBundle = activity.getIntent().getExtras();
        initData();
    }

    public SlcMpDelegateImp(Fragment fragment) {
        this.mFragment = fragment;
        mBundle = fragment.getArguments();
        initData();
    }

    private void initData() {
        //如果文件选择参数为空，则初始化一个默认的参数
        if (mBundle == null) {
            mBundle = new Bundle();
        }
        //初始化文件选择类型，如果文件选择类型为空，则使用默认配置的文件选择类型
        ArrayList<Integer> mediaTypeList = mBundle.getIntegerArrayList(SlcMp.Key.KEY_MEDIA_TYPE_LIST);
        if (mediaTypeList == null) {
            mediaTypeList = new ArrayList<>();
        }
        if (mediaTypeList.isEmpty()) {
            mediaTypeList.add(SlcMp.MEDIA_TYPE_PHOTO);
            mediaTypeList.add(SlcMp.MEDIA_TYPE_VIDEO);
            mediaTypeList.add(SlcMp.MEDIA_TYPE_AUDIO);
            mediaTypeList.add(SlcMp.MEDIA_TYPE_FILE);
            mBundle.putIntegerArrayList(SlcMp.Key.KEY_MEDIA_TYPE_LIST, mediaTypeList);
        }
        //初始化文件混合选择集合
        ArrayList<Integer> mediaTypeMuddyList = mBundle.getIntegerArrayList(SlcMp.Key.KEY_MEDIA_TYPE_MUDDY_LIST);
        if (mediaTypeMuddyList == null) {
            mediaTypeMuddyList = new ArrayList<>();
        }
        if (mediaTypeMuddyList.isEmpty()) {
            mediaTypeMuddyList.addAll(mediaTypeList);
            mBundle.putIntegerArrayList(SlcMp.Key.KEY_MEDIA_TYPE_MUDDY_LIST, mediaTypeMuddyList);
        }
        //设置文件过滤器集合
        mediaTypeFilePropertyList = (HashMap<Integer, IFileProperty>) mBundle.getSerializable(SlcMp.Key.KEY_MEDIA_TYPE_FILE_PROPERTY_LIST);
        if (mediaTypeFilePropertyList == null) {
            mediaTypeFilePropertyList = new HashMap<>();
        }
        //初始化每个页面的一行的个数
        if (mBundle.getInt(SlcMp.Key.KEY_SPAN_COUNT, SlcMp.Value.VALUE_SPAN_COUNT_DEF_VALUE) < 1) {
            mBundle.putInt(SlcMp.Key.KEY_SPAN_COUNT, 1);
        }
        //初始化最多可以选择多少个
        if (mBundle.getInt(SlcMp.Key.KEY_MAX_PICKER, 0) < 1) {
            mBundle.putInt(SlcMp.Key.KEY_MAX_PICKER, SlcMp.Value.VALUE_DEF_MAX_PICKER);
        }
        //设置文件选择页面table的title集合
        HashMap<Integer, String> mediaTypeTitleList =
                (HashMap<Integer, String>) mBundle.getSerializable(SlcMp.Key.KEY_MEDIA_TYPE_TITLE_LIST);
        if (mediaTypeTitleList == null) {
            mediaTypeTitleList = new HashMap<>();
        }
        //初始化title
        for (Integer type : mediaTypeList) {
            String title = mediaTypeTitleList.get(type);
            switch (type) {
                case SlcMp.MEDIA_TYPE_PHOTO:
                    mTableTitle.put(SlcMp.MEDIA_TYPE_PHOTO, TextUtils.isEmpty(title) ?
                            SlcMp.getInstance().getApp().getString(R.string.slc_m_p_photo) : title);
                    break;
                case SlcMp.MEDIA_TYPE_VIDEO:
                    mTableTitle.put(SlcMp.MEDIA_TYPE_VIDEO,
                            TextUtils.isEmpty(title) ?
                                    SlcMp.getInstance().getApp().getString(R.string.slc_m_p_video) : title);
                    break;
                case SlcMp.MEDIA_TYPE_AUDIO:
                    mTableTitle.put(SlcMp.MEDIA_TYPE_AUDIO,
                            TextUtils.isEmpty(title) ?
                                    SlcMp.getInstance().getApp().getString(R.string.slc_m_p_audio) : title);
                    break;
                case SlcMp.MEDIA_TYPE_FILE:
                    mTableTitle.put(SlcMp.MEDIA_TYPE_FILE,
                            TextUtils.isEmpty(title) ?
                                    SlcMp.getInstance().getApp().getString(R.string.slc_m_p_file) : title);

                    break;
                default:
                    //mTableTitle.put(type, title);
                    mTableTitle.put(type, TextUtils.isEmpty(title) ?
                            SlcMp.getInstance().getApp().getString(R.string.slc_m_p_file) : title);
                    break;
            }
        }

    }

    @Override
    public Bundle getParameter() {
        return mBundle;
    }

    @Override
    public Context getContext() {
        return mActivity;
    }

    @Override
    public List<Integer> getMediaTypeList() {
        return mBundle.getIntegerArrayList(SlcMp.Key.KEY_MEDIA_TYPE_LIST);
    }

    @Override
    public List<Integer> getMediaTypeMuddyList() {
        return mBundle.getIntegerArrayList(SlcMp.Key.KEY_MEDIA_TYPE_MUDDY_LIST);
    }

    @Override
    public ArrayMap<Integer, IBaseItem> getSelectItemArrayMap() {
        return selectItemArrayMap;
    }

    @Override
    public Map<Integer, IFileProperty> getMediaTypeFilePropertyList() {
        return mediaTypeFilePropertyList;
    }

    @Override
    public IFileProperty getFilePropertyWithMediaType(int mediaType) {
        return mediaTypeFilePropertyList.get(mediaType);
    }

    protected boolean existMultipleMediaType(IBaseItem baseItem) {
        if (selectItemArrayMap.isEmpty()) {
            return false;
        }
        return selectItemArrayMap.valueAt(0).getMediaTypeTag() != baseItem.getMediaTypeTag();
    }

    @Override
    public boolean addItem(IBaseItem baseItem) {
        boolean isSelect = false;
        if (baseItem != null) {
            //是否支持选择多个
            if (isMultipleSelection()) {
                //混合选择是否支持该文件的媒体类型
                if (!getMediaTypeMuddyList().contains(baseItem.getMediaTypeTag())) {
                    notAllowedMuddyMediaType(baseItem);
                    return false;
                }
                //选择该媒体类型前是否选择过其他的媒体类型
                if (!isMultipleMediaType() && existMultipleMediaType(baseItem)) {
                    noAllowMultipleMediaType(baseItem);
                    return false;
                }
                //是否超过最大选择数
                if (selectItemArrayMap.size() < getMaxCount()) {
                    isSelect = true;
                    baseItem.setChecked(true);
                    selectItemArrayMap.put(baseItem.getId(), baseItem);
                    onCheck(baseItem);
                    onSelectChange();
                } else {
                    //如果超过，则发送超过最大个数通知
                    onOverflow();
                }
            } else {
                isSelect = true;
                baseItem.setChecked(true);
                onCheck(baseItem);
                removeAll();
                selectItemArrayMap.put(baseItem.getId(), baseItem);
                onSelectChange();
            }
        }
        return isSelect;
    }

    @Override
    public boolean removeItem(IBaseItem baseItem) {
        if (baseItem != null) {
            baseItem.setChecked(false);
            selectItemArrayMap.remove(baseItem.getId());
            onSelectChange();
        }
        return true;
    }

    @Override
    public void addAll(List<IBaseItem> baseItemList) {
        if (baseItemList != null) {
            for (IBaseItem baseItem : baseItemList) {
                baseItem.setChecked(true);
                selectItemArrayMap.put(baseItem.getId(), baseItem);
                onCheck(baseItem);
            }
        }
        onSelectChange();
    }

    @Override
    public void removeAll() {
        for (Map.Entry<Integer, IBaseItem> itemEntry : selectItemArrayMap.entrySet()) {
            itemEntry.getValue().setChecked(false);
            onCheck(itemEntry.getValue());
        }
        selectItemArrayMap.clear();
        onSelectChange();
    }

    @Override
    public int getSpanCount() {
        return mBundle.getInt(SlcMp.Key.KEY_SPAN_COUNT, SlcMp.Value.VALUE_SPAN_COUNT_DEF_VALUE);
    }

    @Override
    public int getMaxCount() {
        return mBundle.getInt(SlcMp.Key.KEY_MAX_PICKER);
    }

    @Override
    public boolean isMultipleSelection() {
        return mBundle.getBoolean(SlcMp.Key.KEY_IS_MULTIPLE_SELECTION, SlcMp.Value.VALUE_DEF_MULTIPLE_SELECTION);
    }

    @Override
    public boolean isMultipleMediaType() {
        return mBundle.getBoolean(SlcMp.Key.KEY_IS_MULTIPLE_MEDIA_TYPE, SlcMp.Value.VALUE_DEF_MULTIPLE_MEDIA_TYPE);
    }

    @Override
    public void addOnSelectEventListener(@NonNull OnSelectEventListener onSelectEventListener) {
        onSelectEventListenerWeakReferenceList.add(new WeakReference<>(onSelectEventListener));
    }


    @Override
    public String getTitleByMediaType(int mediaType) {
        return mTableTitle.get(mediaType);
    }

    @Override
    public String getTitleByPosition(int position) {
        return getTitleByMediaType(getMediaTypeList().get(position));
    }

    /**
     * 通知选择改变
     */
    protected void onSelectChange() {
        for (WeakReference<OnSelectEventListener> onSelectChangeListenerWeakReference :
                onSelectEventListenerWeakReferenceList) {
            OnSelectEventListener onSelectEventListener = onSelectChangeListenerWeakReference.get();
            if (onSelectEventListener != null) {
                onSelectEventListener.onSelectEvent(SelectEvent.EVENT_SELECT_COUNT, new SelectEvent()
                        .putOfChained(SelectEvent.PARAMETER_SELECT_ITEM_COUNT, selectItemArrayMap.size())
                        .putOfChained(SelectEvent.PARAMETER_IS_MULTIPLE_SELECTION, isMultipleSelection()));
            }
        }
    }

    /**
     * 通知选择或取消了一个
     *
     * @param baseItem
     */
    protected void onCheck(IBaseItem baseItem) {
        for (WeakReference<OnSelectEventListener> onSelectChangeListenerWeakReference :
                onSelectEventListenerWeakReferenceList) {
            OnSelectEventListener onSelectEventListener = onSelectChangeListenerWeakReference.get();
            if (onSelectEventListener != null) {
                int listenerMediaType = (int) onSelectEventListener.onSelectEvent(SelectEvent.EVENT_LISTENER_MEDIA_TYPE,
                        new SelectEvent());
                if (listenerMediaType == baseItem.getMediaTypeTag() || listenerMediaType == SlcMp.MEDIA_TYPE_NULL) {
                    onSelectEventListener.onSelectEvent(SelectEvent.EVENT_CHECK, new SelectEvent()
                            .putOfChained(SelectEvent.PARAMETER_ITEM, baseItem));
                }
            }
        }
    }

    /**
     * 选择溢出
     */
    protected void onOverflow() {
        for (WeakReference<OnSelectEventListener> onSelectChangeListenerWeakReference :
                onSelectEventListenerWeakReferenceList) {
            OnSelectEventListener onSelectEventListener = onSelectChangeListenerWeakReference.get();
            if (onSelectEventListener != null) {
                onSelectEventListener.onSelectEvent(SelectEvent.EVENT_OVER_FLOW, new SelectEvent());
            }
        }
    }

    /**
     * 选择了不允许的类型
     */
    protected void notAllowedMuddyMediaType(IBaseItem baseItem) {
        for (WeakReference<OnSelectEventListener> onSelectChangeListenerWeakReference :
                onSelectEventListenerWeakReferenceList) {
            OnSelectEventListener onSelectEventListener = onSelectChangeListenerWeakReference.get();
            if (onSelectEventListener != null) {
                onSelectEventListener.onSelectEvent(SelectEvent.EVENT_NO_ALLOW_MUDDY_MEDIA_TYPE,
                        new SelectEvent().putOfChained(SelectEvent.PARAMETER_ITEM, baseItem));
            }
        }
    }

    /**
     * 不允许选择多种类型
     *
     * @param baseItem
     */
    protected void noAllowMultipleMediaType(IBaseItem baseItem) {
        for (WeakReference<OnSelectEventListener> onSelectChangeListenerWeakReference :
                onSelectEventListenerWeakReferenceList) {
            OnSelectEventListener onSelectEventListener = onSelectChangeListenerWeakReference.get();
            if (onSelectEventListener != null) {
                onSelectEventListener.onSelectEvent(SelectEvent.EVENT_NO_ALLOW_MULTIPLE_MEDIA_TYPE,
                        new SelectEvent().putOfChained(SelectEvent.PARAMETER_ITEM, baseItem));
            }
        }
    }


    @Override
    public void complete() {
        if (this.mFragment != null) {
            //TODO 使用fragment完成
        } else {
            ArrayList<IBaseItem> selectItems = new ArrayList<>(selectItemArrayMap.values());
            SlcMpResultModel.getInstance().setValue(selectItems);
            Intent intent = new Intent();
            intent.putExtra(SlcMp.Key.KEY_RESULT_LIST, selectItems);
            this.mActivity.setResult(Activity.RESULT_OK, intent);
            this.mActivity.finish();
        }
    }

    @Override
    public void destroy() {
        //移除临时的选择配置 防止没存泄露
        SlcMp.getInstance().removeTemporaryMpConfig();
    }
}
