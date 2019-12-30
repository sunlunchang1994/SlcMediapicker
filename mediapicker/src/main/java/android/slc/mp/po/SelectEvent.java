package android.slc.mp.po;

import androidx.collection.ArrayMap;

public class SelectEvent extends ArrayMap<Object, Object> {
    public static final int EVENT_SELECT_COUNT = 1;
    public static final int EVENT_CHECK = 2;
    public static final int EVENT_OVER_FLOW = 3;
    public static final int EVENT_NO_ALLOW_MUDDY_MEDIA_TYPE = 4;
    public static final int EVENT_NO_ALLOW_MULTIPLE_MEDIA_TYPE = 5;
    public static final int EVENT_LISTENER_MEDIA_TYPE = 6;

    public static final String PARAMETER_SELECT_ITEM_COUNT = "selectItemCount";
    public static final String PARAMETER_IS_MULTIPLE_SELECTION = "isMultipleSelection";
    public static final String PARAMETER_ITEM = "item";

    public <V> V getAuto(Object key) {
        return (V) super.get(key);
    }

    public <K> K keyAtAuto(int index) {
        return (K) super.keyAt(index);
    }

    public <V> V valueAtAuto(int index) {
        return (V) super.valueAt(index);
    }

    public SelectEvent putOfChained(Object key, Object value) {
        super.put(key, value);
        return this;
    }
}
