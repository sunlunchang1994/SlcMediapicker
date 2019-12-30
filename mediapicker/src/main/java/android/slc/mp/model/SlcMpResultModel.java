package android.slc.mp.model;

import android.slc.mp.po.i.IBaseItem;

import java.util.List;

public class SlcMpResultModel extends BaseMpModel<List<IBaseItem>> {
    private static SlcMpResultModel INSTANCE;

    public static SlcMpResultModel getInstance() {
        if (INSTANCE == null) {
            synchronized (SlcMpResultModel.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SlcMpResultModel();
                }
            }
        }
        return INSTANCE;
    }

    private SlcMpResultModel() {
    }

}
