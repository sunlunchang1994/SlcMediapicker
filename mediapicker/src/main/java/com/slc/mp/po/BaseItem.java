package com.slc.mp.po;

import com.slc.mp.po.i.IBaseItem;

/**
 * Created by Taurus on 16/8/28.
 */
public abstract class BaseItem extends com.slc.medialoader.bean.BaseItem implements IBaseItem {
    private boolean checked;
    private int mediaTypeTag;

    public BaseItem() {
    }

    public BaseItem(int id, String displayName, String path) {
        this(id, displayName, path, 0);
    }

    public BaseItem(int id, String displayName, String path, long size) {
        this(id, displayName, path, size, 0);
    }

    public BaseItem(int id, String displayName, String path, long size, long modified) {
        super(id, displayName, path, size, modified);
    }

    @Override
    public void setMediaTypeTag(int mediaTypeTag) {
        this.mediaTypeTag = mediaTypeTag;
    }

    @Override
    public int getMediaTypeTag() {
        return mediaTypeTag;
    }

    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public boolean isChecked() {
        return checked;
    }
}
