package android.slc.mp.ui.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.appbar.AppBarLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.slc.mp.utils.SlcMpMediaBrowseUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.slc.mp.R;
import android.slc.mp.SlcMp;
import android.slc.mp.SlcMpConfig;
import android.slc.mp.po.i.IPhotoItem;

import java.util.ArrayList;
import java.util.List;

public class SlcMpBrowsePhotoActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private int currentPosition;
    private boolean isEdit;
    private List<IPhotoItem> photoList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slc_mp_activity_photo_browse);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        bindView();
        initData(getIntent().getExtras());
    }

    private void bindView() {
        appBarLayout = findViewById(R.id.appBarLayout);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.slc_mp_ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appBarLayout.setTargetElevation(0f);
        }
        viewPager = findViewById(R.id.view_pager);
        viewPager.addOnPageChangeListener(this);
    }

    private void initData(Bundle bundle) {
        if (bundle == null) {
            throw new IllegalStateException("图片数据为空，该操作没有任何意义！");
        }
        currentPosition = bundle.getInt(SlcMpMediaBrowseUtils.Builder.CURRENT_POSITION, 0);
        photoList = (List<IPhotoItem>) bundle.getSerializable(SlcMpMediaBrowseUtils.Builder.PHOTO_LIST);
        if (photoList == null || photoList.isEmpty()) {
            throw new IllegalStateException("photoList为空，该操作没有任何意义！");
        }
        if (photoList.size() < currentPosition || currentPosition < 0) {
            currentPosition = 0;
        }
        toolbar.setTitle(getString(R.string.slc_m_p_x_x, String.valueOf(currentPosition + 1),
                String.valueOf(photoList.size())));
        isEdit = bundle.getBoolean(SlcMpMediaBrowseUtils.Builder.PHOTO_IS_EDIT);
        if (isEdit) {
            MenuItem menuItem = toolbar.getMenu().add(R.string.slc_m_p_edit);
            menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return false;
                }
            });
        }
        viewPager.setAdapter(new SimpleFragmentAdapter());
        viewPager.setCurrentItem(currentPosition, false);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {
        if (i == ViewPager.SCROLL_STATE_IDLE) {
            currentPosition = viewPager.getCurrentItem();
            toolbar.setTitle(getString(R.string.slc_m_p_x_x, String.valueOf(currentPosition + 1),
                    String.valueOf(photoList.size())));
        }
    }

    public class SimpleFragmentAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return photoList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            (container).removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final IPhotoItem photoItem = photoList.get(position);
            /*View contentView = getLayoutInflater().inflate(R.layout.slc_media_picker_photo_browse_long_view, container,
            false);
            SubsamplingScaleImageView subsamplingScaleImageView = contentView.findViewById(R.id.photoView);
            subsamplingScaleImageView.setImage(ImageSource.uri(photoItem.getPath()));
            subsamplingScaleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });*/
            View contentView = getLayoutInflater().inflate(R.layout.slc_mp_photo_browse_view, container, false);
            final PhotoView imageView = contentView.findViewById(R.id.photoView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    appBarLayout.setVisibility(appBarLayout.getVisibility() != View.VISIBLE ? View.VISIBLE : View.GONE);
                }
            });
            imageView.setMaximumScale(4.8f);
            SlcMp.getInstance().optMpConfig().loadBrowsePhoto(SlcMpBrowsePhotoActivity.this, photoItem.getPath(),
                    new SlcMpConfig.IImageBrowseLoadCallBack() {
                        @Override
                        public Object getTag() {
                            return photoItem;
                        }

                        @Override
                        public void load(Bitmap bitmap) {
                            imageView.setImageBitmap(bitmap);
                        }

                        @Override
                        public void loadStart(@Nullable Drawable placeholder) {
                            imageView.setImageDrawable(placeholder);
                        }

                        @Override
                        public void loadFailed(@Nullable Drawable errorDrawable) {
                            imageView.setImageDrawable(errorDrawable);
                        }
                    });
            //}
            container.addView(contentView, 0);
            return contentView;
        }
    }
}
