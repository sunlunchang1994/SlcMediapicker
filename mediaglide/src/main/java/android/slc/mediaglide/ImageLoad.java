package android.slc.mediaglide;

import android.slc.mp.SlcMp;
import android.slc.mp.SlcMpConfig;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class ImageLoad implements SlcMpConfig.IImageLoad {
    private RequestOptions mediaPickerRequestOptions;

    public ImageLoad() {
        mediaPickerRequestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.slc_mp_ic_loading_anim)                //加载成功之前占位图
                .error(R.drawable.slc_mp_ic_loading_failure);
    }

    @Override
    public void loadImage(ImageView imageView, String path, int itemType) {
        switch (itemType) {
            case SlcMp.MEDIA_TYPE_PHOTO:
            case SlcMp.MEDIA_TYPE_VIDEO:
                Glide.with(imageView).load(path).apply(mediaPickerRequestOptions).into(imageView);
                break;
        }
    }
}
