package android.slc.mp.po.i;

public interface IBaseItem extends android.slc.medialoader.bean.i.IBaseItem, ICheckedItem {
    void setMediaTypeTag(int mediaTypeTag);

    int getMediaTypeTag();
}
