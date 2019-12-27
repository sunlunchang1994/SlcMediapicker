package com.slc.mp.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import android.widget.ImageView;

import com.slc.medialoader.utils.AudioCoverUtil;
import com.slc.mp.R;
import com.slc.mp.SlcMp;
import com.slc.mp.po.i.IAudioItem;
import com.slc.mp.ui.SlcIMpDelegate;
import com.slc.mp.ui.adapter.base.SlcMpBaseViewHolder;

import java.util.List;

public class SlcMpAudioAdapter extends SlcMpBaseMpAdapter<IAudioItem> {

    public SlcMpAudioAdapter(@NonNull Context context, @NonNull List<IAudioItem> date, SlcIMpDelegate mediaPickerDelegate) {
        super(R.layout.slc_mp_item_audio, context, date, mediaPickerDelegate);
    }

    @Override
    protected void convert(SlcMpBaseViewHolder helper, final IAudioItem item) {
        helper.setChecked(R.id.checkbox, item.isChecked());
        helper.setText(R.id.tv_title, item.getDisplayName());
        Bitmap coverBitmap = AudioCoverUtil.createAlbumArt(item.getPath());
        if (coverBitmap != null) {
            helper.setImageBitmap(R.id.imageView, coverBitmap);
        } else {
            helper.setImageResource(R.id.imageView, R.drawable.slc_mp_ic_audiotrack);
        }
        SlcMp.getInstance().optMpConfig().loadImage((ImageView) helper.getView(R.id.imageView), item.getPath(),
                SlcMp.MEDIA_TYPE_AUDIO);
    }
}
