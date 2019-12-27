package com.slc.mp.po.i;

public interface IBaseItem extends com.slc.medialoader.bean.i.IBaseItem, ICheckedItem {
    void setMediaTypeTag(int mediaTypeTag);

    int getMediaTypeTag();
}
