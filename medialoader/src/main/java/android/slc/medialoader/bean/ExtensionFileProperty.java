package android.slc.medialoader.bean;

import android.provider.MediaStore;
import android.util.Log;

import android.slc.medialoader.bean.i.IFileProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExtensionFileProperty implements IFileProperty {
    private List<String> extension;

    public static ExtensionFileProperty getWordFileProperty() {
        List<String> extension = new ArrayList<>();
        extension.add(".doc");
        extension.add(".docx");
        return getInstance(extension);
    }

    public static ExtensionFileProperty getExcelFileProperty() {
        List<String> extension = new ArrayList<>();
        extension.add(".xls");
        extension.add(".xlsx");
        return getInstance(extension);
    }

    public static ExtensionFileProperty getPowerPointFileProperty() {
        List<String> extension = new ArrayList<>();
        extension.add(".ppt");
        extension.add(".pptx");
        return getInstance(extension);
    }

    public static ExtensionFileProperty getPdfFileProperty() {
        List<String> extension = new ArrayList<>();
        extension.add(".pdf");
        return getInstance(extension);
    }

    public static ExtensionFileProperty getApkFileProperty() {
        List<String> extension = new ArrayList<>();
        extension.add(".apk");
        return getInstance(extension);
    }

    public static ExtensionFileProperty getInstance(List<String> extension) {
        return new ExtensionFileProperty(extension);
    }

    public static ExtensionFileProperty getInstance(String... extension) {
        return new ExtensionFileProperty(Arrays.asList(extension));
    }

    public ExtensionFileProperty(List<String> extension) {
        this.extension = extension;
    }

    @Override
    public String createSelection() {
        String selection = null;
        if (extension != null) {
            int size = extension.size();
            StringBuilder extensionBuilder = new StringBuilder();
            for (int i = 0; i < size; i++) {
                extensionBuilder.append("(").append(MediaStore.Files.FileColumns.DATA).append(" like ? ").append(")");
                if (i < size - 1) {
                    extensionBuilder.append(" OR ");
                }
            }
            selection = extensionBuilder.toString();
        }
        Log.d("FileProperty", selection == null ? "null" : ("" + selection));
        return selection;
    }

    @Override
    public String[] createSelectionArgs() {
        String[] args = null;
        StringBuilder sb = new StringBuilder();
        if (extension != null) {
            int size = extension.size();
            if (size > 0)
                args = new String[size];
            for (int i = 0; i < size; i++) {
                args[i] = "%" + extension.get(i);
                sb.append(args[i]);
                if (i < size - 1) {
                    sb.append(",");
                }
            }
        }
        Log.d("FileProperty", sb.toString());
        return args;
    }

    @Override
    public String createSortOrderSql() {
        return MediaStore.MediaColumns.DATE_MODIFIED + " DESC";
    }
}
