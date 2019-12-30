package android.slc.mp.ui.adapter;

import android.content.Context;
import androidx.annotation.NonNull;

import android.slc.mp.R;
import android.slc.mp.ui.adapter.base.SlcMpBaseAdapter;
import android.slc.mp.ui.adapter.base.SlcMpBaseViewHolder;
import android.widget.ImageView;

import android.slc.medialoader.bean.i.IAudioBaseFolder;
import android.slc.medialoader.bean.i.IFileBaseFolder;
import android.slc.medialoader.bean.i.IPhotoBaseFolder;
import android.slc.medialoader.bean.i.IVideoBaseFolder;
import android.slc.mp.SlcMp;
import android.slc.mp.po.i.IBaseFolder;
import android.slc.mp.po.i.IPhotoFolder;

import java.util.List;

public class SlcMpFolderAdapter<T extends IBaseFolder> extends SlcMpBaseAdapter<T> {

    public SlcMpFolderAdapter(@NonNull Context context, @NonNull List<T> date) {
        super(context, date);
        addLayoutByType(0, R.layout.slc_mp_select_folder_item);
    }

    @Override
    protected void convert(SlcMpBaseViewHolder helper, T item) {
        if (item instanceof IPhotoFolder) {
            IPhotoBaseFolder photoFolder = (IPhotoFolder) item;
            SlcMp.getInstance().optMpConfig().loadFolder((ImageView) helper.getView(R.id.imageView),
                    photoFolder.getCover(), SlcMp.MEDIA_TYPE_PHOTO);
            helper.setText(R.id.tv_name, item.getName());
            helper.setText(R.id.tv_count, SlcMp.getInstance().getApp().getString(R.string.slc_m_p_zhang,
                    String.valueOf(item.getItems().size())));
        } else if (item instanceof IVideoBaseFolder) {
            helper.setText(R.id.tv_name, item.getName());
            helper.setText(R.id.tv_count, SlcMp.getInstance().getApp().getString(R.string.slc_m_p_bu,
                    String.valueOf(item.getItems().size())));
            helper.setImageResource(R.id.imageView, R.drawable.slc_mp_ic_movie);
            SlcMp.getInstance().optMpConfig().loadFolder((ImageView) helper.getView(R.id.imageView),
                    null, SlcMp.MEDIA_TYPE_VIDEO);
        } else if (item instanceof IAudioBaseFolder) {
            helper.setText(R.id.tv_name, item.getName());
            helper.setText(R.id.tv_count, SlcMp.getInstance().getApp().getString(R.string.slc_m_p_shou,
                    String.valueOf(item.getItems().size())));
            helper.setImageResource(R.id.imageView, R.drawable.slc_mp_ic_audiotrack);
            SlcMp.getInstance().optMpConfig().loadFolder((ImageView) helper.getView(R.id.imageView),
                    null, SlcMp.MEDIA_TYPE_AUDIO);
        } else if (item instanceof IFileBaseFolder) {
            helper.setText(R.id.tv_name, item.getName());
            helper.setText(R.id.tv_count, SlcMp.getInstance().getApp().getString(R.string.slc_m_p_ge,
                    String.valueOf(item.getItems().size())));
            helper.setImageResource(R.id.imageView,R.drawable.slc_mp_ic_folder);
            SlcMp.getInstance().optMpConfig().loadFolder((ImageView) helper.getView(R.id.imageView),
                    null, SlcMp.MEDIA_TYPE_FILE);
        }
    }
}
