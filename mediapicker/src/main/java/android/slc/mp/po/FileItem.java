package android.slc.mp.po;

import android.slc.mp.po.i.IFileItem;

/**
 * Created by Taurus on 2017/5/23.
 */

public class FileItem extends BaseItem implements IFileItem {

    private String mime;

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

}
