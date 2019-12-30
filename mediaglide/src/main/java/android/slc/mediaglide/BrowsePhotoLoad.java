package android.slc.mediaglide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.slc.mp.SlcMpConfig;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

public class BrowsePhotoLoad implements SlcMpConfig.IImageBrowseLoad {
    private RequestOptions imageBrowseRequestOptions;

    public BrowsePhotoLoad() {
        imageBrowseRequestOptions = new RequestOptions()
                .placeholder(R.drawable.slc_mp_ic_loading_anim)                //加载成功之前占位图
                .error(R.drawable.slc_mp_ic_loading_failure);
    }

    @Override
    public void loadImage(Context context, String path, final SlcMpConfig.IImageBrowseLoadCallBack iImageBrowseLoadCallBack) {
        Glide.with(context).asBitmap().load(path).apply(imageBrowseRequestOptions).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                iImageBrowseLoadCallBack.load(resource);
            }

            @Override
            public void onLoadStarted(@Nullable Drawable placeholder) {
                super.onLoadStarted(placeholder);
                iImageBrowseLoadCallBack.loadStart(placeholder);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                iImageBrowseLoadCallBack.loadStart(errorDrawable);
            }
        });
    }
}
