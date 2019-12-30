package android.slc.mp.model;

import androidx.lifecycle.ForceExpansionLiveData;

/**
 * @author slc
 * @date 2019/11/29 14:25
 */
public class SlcMpOperateModel extends ForceExpansionLiveData<ExtensionMap> {
    private static SlcMpOperateModel INSTANCE;

    public static SlcMpOperateModel getInstance() {
        if (INSTANCE == null) {
            synchronized (SlcMpOperateModel.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SlcMpOperateModel();
                }
            }
        }
        return INSTANCE;
    }

    private SlcMpOperateModel() {
    }
}
