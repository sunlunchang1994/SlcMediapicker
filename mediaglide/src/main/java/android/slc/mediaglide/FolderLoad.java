package android.slc.mediaglide;

import android.content.res.ColorStateList;
import android.slc.mp.SlcMp;
import android.slc.mp.SlcMpConfig;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class FolderLoad implements SlcMpConfig.IImageLoad {
    private RequestOptions mediaPickerRequestOptions;

    public FolderLoad() {
        mediaPickerRequestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.slc_mp_ic_loading_anim)                //加载成功之前占位图
                .error(R.drawable.slc_mp_ic_loading_failure);
    }

    @Override
    public void loadImage(ImageView imageView, String path, int itemType) {
        switch (itemType) {
            case SlcMp.MEDIA_TYPE_PHOTO:
                Glide.with(imageView).load(path).apply(mediaPickerRequestOptions).into(imageView);
                break;
            case SlcMp.MEDIA_TYPE_AUDIO:
            case SlcMp.MEDIA_TYPE_VIDEO:
                break;
            default:
                ImageViewCompat.setImageTintList(imageView, ColorStateList.valueOf(ContextCompat.getColor(imageView.getContext(),R.color.slcDefIconColor)));
                break;
        }
    }
}
