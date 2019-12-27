package com.slc.medialoader.bean;

import com.slc.medialoader.bean.i.IFileBaseItem;

/**
 * Created by Taurus on 2017/5/23.
 */

public class FileBaseItem extends BaseItem implements IFileBaseItem {

    private String mime;

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

}
