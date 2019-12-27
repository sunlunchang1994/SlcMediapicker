package androidx.lifecycle;

/**
 * @author slc
 * @date 2019/11/28 16:03
 */
public class ForceExpansionLiveData<T> extends MutableLiveData<T> {

    public static final int START_VERSION = LiveData.START_VERSION;

    public int getVersion() {
        return super.getVersion();
    }
}
