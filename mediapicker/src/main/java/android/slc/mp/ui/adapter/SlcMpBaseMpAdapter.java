package android.slc.mp.ui.adapter;

import android.content.Context;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import android.slc.mp.R;
import android.slc.mp.ui.adapter.base.SlcMpBaseViewHolder;
import android.view.ViewGroup;

import android.slc.mp.po.i.IBaseItem;
import android.slc.mp.ui.SlcIMpDelegate;
import android.slc.mp.ui.adapter.base.SlcMpBaseAdapter;

import java.util.List;

public abstract class SlcMpBaseMpAdapter<T extends IBaseItem> extends SlcMpBaseAdapter<T> {
    public static final int ITEM_TYPE_DEF = 0;//默认item类型
    protected SlcIMpDelegate mMediaPickerDelegate;
    protected int lastPosition = -1;

    public SlcMpBaseMpAdapter(@LayoutRes int layoutRes, @NonNull Context context, @NonNull List<T> date,
                              SlcIMpDelegate mediaPickerDelegate) {
        super(context, date);
        mMediaPickerDelegate = mediaPickerDelegate;
        addLayoutByType(ITEM_TYPE_DEF, layoutRes);
    }

    @Override
    protected int getDefItemViewType(int position) {
        /*if (mData != null && !mData.isEmpty()) {
            T data = mData.get(position);
            return SlcMp.getMediaTypeByItem(data);
        }*/
        return super.getDefItemViewType(position);
    }

    public void notifyDataSwitch(int curPosition) {
        if (lastPosition != -1) {
            notifyItemChanged(lastPosition);
        }
        lastPosition = curPosition;
        notifyItemChanged(curPosition);
    }

    @Override
    protected SlcMpBaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        SlcMpBaseViewHolder slcMpBaseViewHolder = super.onCreateDefViewHolder(parent, viewType);
        slcMpBaseViewHolder.addOnClickListener(getCheckId());
        slcMpBaseViewHolder.addOnClickListener(getContentClickViewId());
        return slcMpBaseViewHolder;
    }

    @IdRes
    protected int getCheckId() {
        return R.id.checkbox;
    }

    @IdRes
    protected int getContentClickViewId() {
        return R.id.fl_content;
    }
    /*@Override
    protected void convert(SlcMpBaseViewHolder helper, final T item) {
        switch (SlcMp.getMediaTypeByItem(item)) {
            case SlcMp.MEDIA_TYPE_PHOTO:
                convert(helper, (PhotoBaseItem) item);
                break;
            case SlcMp.MEDIA_TYPE_VIDEO:
                convert(helper, (VideoBaseItem) item);
                break;
            case SlcMp.MEDIA_TYPE_AUDIO:
                convert(helper, (AudioBaseItem) item);
                break;
            case SlcMp.MEDIA_TYPE_FILE:
                convert(helper, (FileBaseItem) item);
                break;
        }
    }

    protected void convert(SlcMpBaseViewHolder helper, final PhotoBaseItem item) {
        helper.setChecked(R.id.checkbox, item.isChecked());
        SlcMp.getInstance().optMpConfig().loadImage((ImageView) helper.getView(R.id.imageView), item.getPath(),
                SlcMp.MEDIA_TYPE_PHOTO);
    }

    protected void convert(SlcMpBaseViewHolder helper, final VideoBaseItem item) {
        helper.setChecked(R.id.checkbox, item.isChecked());
        SlcMp.getInstance().optMpConfig().loadImage((ImageView) helper.getView(R.id.imageView), item.getPath(),
                SlcMp.MEDIA_TYPE_VIDEO);
        helper.setText(R.id.tv_title, SlcMpTimeUtil.getTime(SlcMpTimeUtil.getFormat(item.getDuration()), item.getDuration()));

    }

    protected void convert(SlcMpBaseViewHolder helper, final AudioBaseItem item) {
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

    protected void convert(SlcMpBaseViewHolder helper, final FileBaseItem item) {
        helper.setChecked(R.id.checkbox, item.isChecked());
        helper.setText(R.id.tv_title, item.getDisplayName());
        SlcMp.getInstance().optMpConfig().loadImage((ImageView) helper.getView(R.id.imageView), item.getPath(),
                SlcMp.MEDIA_TYPE_FILE);
    }*/

}
