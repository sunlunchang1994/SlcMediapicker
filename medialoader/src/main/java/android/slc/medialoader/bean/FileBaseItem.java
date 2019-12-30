package android.slc.medialoader.bean;

import android.slc.medialoader.bean.i.IFileBaseItem;

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
