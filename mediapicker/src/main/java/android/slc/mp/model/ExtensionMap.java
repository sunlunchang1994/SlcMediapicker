package android.slc.mp.model;

import android.net.Uri;

import androidx.collection.ArrayMap;

/**
 * @author slc
 * @date 2019/11/29 14:37
 */
public class ExtensionMap extends ArrayMap<String, Object> {
    private int resultCode;
    private String path;
    private Uri uri;

    public int getResultCode() {
        return resultCode;
    }

    public ExtensionMap setResultCode(int resultCode) {
        this.resultCode = resultCode;
        return this;
    }

    public String getPath() {
        return path;
    }

    public ExtensionMap setPath(String path) {
        this.path = path;
        return this;
    }

    public Uri getUri() {
        return uri;
    }

    public ExtensionMap setUri(Uri uri) {
        this.uri = uri;
        return this;
    }

    public ExtensionMap param(String key, Object value) {
        put(key, value);
        return this;
    }
}
