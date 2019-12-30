package android.slc.mp.ui.adapter;

import android.content.Context;
import androidx.annotation.NonNull;

import android.slc.mp.R;
import android.slc.mp.ui.adapter.base.SlcMpBaseViewHolder;
import android.widget.ImageView;

import android.slc.mp.SlcMp;
import android.slc.mp.po.i.IVideoItem;
import android.slc.mp.ui.SlcIMpDelegate;
import android.slc.mp.ui.utils.SlcMpTimeUtil;

import java.util.List;

public class SlcMpVideoAdapter extends SlcMpBaseMpAdapter<IVideoItem> {

    public SlcMpVideoAdapter(@NonNull Context context, @NonNull List<IVideoItem> date,
                             SlcIMpDelegate mediaPickerDelegate) {
        super(R.layout.slc_mp_item_video, context, date, mediaPickerDelegate);
    }

    @Override
    protected void convert(SlcMpBaseViewHolder helper, final IVideoItem item) {
        helper.setChecked(R.id.checkbox, item.isChecked());
        SlcMp.getInstance().optMpConfig().loadImage((ImageView) helper.getView(R.id.imageView), item.getPath(),
                SlcMp.MEDIA_TYPE_VIDEO);
        helper.setText(R.id.tv_title, SlcMpTimeUtil.getTime(SlcMpTimeUtil.getFormat(item.getDuration()), item.getDuration()));
    }
}
