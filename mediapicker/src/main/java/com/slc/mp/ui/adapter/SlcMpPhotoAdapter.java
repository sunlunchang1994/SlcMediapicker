package com.slc.mp.ui.adapter;

import android.content.Context;

import androidx.annotation.NonNull;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.slc.mp.R;
import com.slc.mp.SlcMp;
import com.slc.mp.po.AddPhotoItem;
import com.slc.mp.po.i.IPhotoItem;
import com.slc.mp.ui.SlcIMpDelegate;
import com.slc.mp.ui.adapter.base.SlcMpBaseViewHolder;

import java.util.List;

public class SlcMpPhotoAdapter extends SlcMpBaseMpAdapter<IPhotoItem> {
    public static final int ITEM_TYPE_TAKE_PHOTO = 1;//默认item类型

    public SlcMpPhotoAdapter(@NonNull Context context, @NonNull List<IPhotoItem> date, SlcIMpDelegate mediaPickerDelegate) {
        super(R.layout.slc_mp_item_photo, context, date, mediaPickerDelegate);
        addLayoutByType(ITEM_TYPE_TAKE_PHOTO, R.layout.slc_mp_item_photo_add);
    }

    @Override
    protected SlcMpBaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        SlcMpBaseViewHolder slcMpBaseViewHolder = super.onCreateDefViewHolder(parent, viewType);
        slcMpBaseViewHolder.addOnClickListener(R.id.fl_take_photo);
        return slcMpBaseViewHolder;
    }

    @Override
    protected int getDefItemViewType(int position) {
        if (mData.get(position) instanceof AddPhotoItem) {
            return ITEM_TYPE_TAKE_PHOTO;
        } else {
            return super.getDefItemViewType(position);
        }
    }

    @Override
    protected void convert(SlcMpBaseViewHolder helper, final IPhotoItem item) {
        switch (helper.getItemViewType()) {
            case ITEM_TYPE_DEF:
                helper.setChecked(R.id.checkbox, item.isChecked());
                SlcMp.getInstance().optMpConfig().loadImage((ImageView) helper.getView(R.id.imageView), item.getPath(),
                        SlcMp.MEDIA_TYPE_PHOTO);
                break;
            case ITEM_TYPE_TAKE_PHOTO:
                break;
        }

    }
}
