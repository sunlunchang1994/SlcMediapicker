package android.slc.medialoader.bean.i;

import java.io.Serializable;

public interface IFileProperty extends Serializable {
    String createSelection();

    String[] createSelectionArgs();

    String createSortOrderSql();
}
