package android.slc.medialoader.bean;

import android.provider.MediaStore;
import android.slc.medialoader.bean.i.IFileProperty;

public class WordFileProperty implements IFileProperty {
    @Override
    public String createSelection() {
        StringBuilder extensionBuilder = new StringBuilder();
        extensionBuilder.append("(").append(MediaStore.Files.FileColumns.DATA).append(" like ? ").append(")");
        extensionBuilder.append(" OR ");
        extensionBuilder.append("(").append(MediaStore.Files.FileColumns.DATA).append(" like ? ").append(")");
        return extensionBuilder.toString();
    }

    @Override
    public String[] createSelectionArgs() {
        return new String[]{"%.doc", "%.docx"};
    }

    @Override
    public String createSortOrderSql() {
        return MediaStore.MediaColumns.DATE_MODIFIED + " DESC";
    }
}
