package com.slc.mp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;

import com.slc.medialoader.bean.i.IFileProperty;
import com.slc.mp.po.SelectEvent;
import com.slc.mp.po.i.IBaseItem;

import java.util.List;
import java.util.Map;

public interface SlcIMpDelegate {
    /**
     * 获取文件选择参数
     * @return
     */
    Bundle getParameter();

    /**
     * 获取上下问环境
     * @return
     */
    Context getContext();

    /**
     * 获取显示的媒体类型集合
     * @return
     */
    List<Integer> getMediaTypeList();

    /**
     * 获取允许混选的文件集合
     * @return
     */
    List<Integer> getMediaTypeMuddyList();

    /**
     * 获取选择的文件集合
     * @return
     */
    ArrayMap<Integer, IBaseItem> getSelectItemArrayMap();

    /**
     * 获取文件筛选器集合
     * @return
     */
    Map<Integer, IFileProperty> getMediaTypeFilePropertyList();

    /**
     * 根据媒体类型获取文件筛选器
     * @param mediaType
     * @return
     */
    IFileProperty getFilePropertyWithMediaType(int mediaType);

    /**
     * 添加一个item(文件选择)
     * @param baseItem
     * @return 返回true表示文件选择成功，false则反之
     */
    boolean addItem(IBaseItem baseItem);

    /**
     * 移除一个item(文件选择)
     * @param baseItem
     * @return 返回true表示文件移除成功，false则反之
     */
    boolean removeItem(IBaseItem baseItem);

    /**
     * 添加所有(文件选择)
     * @param baseItemList
     */
    void addAll(List<IBaseItem> baseItemList);

    /**
     * 移除所有(文件选择)
     */
    void removeAll();

    /**
     * 获取listView一行的个数
     * @return
     */
    int getSpanCount();

    /**
     * 获取选择的最大个数
     * @return
     */
    int getMaxCount();

    /**
     * 是否允许选择多个
     * @return
     */
    boolean isMultipleSelection();

    /**
     * 是否允许同时选择多个媒体类型
     * @return
     */
    boolean isMultipleMediaType();

    /**
     * 添加选择事件监听
     * @param onSelectEventListener
     */
    void addOnSelectEventListener(OnSelectEventListener onSelectEventListener);


    /**
     * 根据媒体类型获取title
     * @param mediaType
     * @return
     */
    String getTitleByMediaType(int mediaType);

    /**
     * 根据index获取title
     * @param position
     * @return
     */
    String getTitleByPosition(int position);

    /**
     * 选择完成
     */
    void complete();

    /**
     * 销毁
     */
    void destroy();
    /**
     * 文件选择事件监听器
     */
    interface OnSelectEventListener {
        /**
         * 事件
         * @param eventCode 事件码
         * @param event 事件主体
         * @return
         */
        Object onSelectEvent(int eventCode, SelectEvent event);

    }
}
