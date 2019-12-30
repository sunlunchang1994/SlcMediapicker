package android.slc.mp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import androidx.collection.ArrayMap;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.slc.mp.R;
import android.view.View;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import android.slc.mp.po.VideoItem;
import android.slc.mp.po.i.IBaseItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SlcMpBrowseVideoActivity extends AppCompatActivity {
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private VideoView videoView;
    private List<VideoItem> videoItems = new ArrayList<>();

    public static class Builder {
        private final static String VIDEO_LIST = "videoList";
        private final static String VIDEO_IS_EDIT = "isEdit";
        private int requestCode;
        private boolean isEdit;
        private ArrayList<VideoItem> videoItems;

        public Builder() {

        }

        public Builder setRequestCode(int requestCode) {
            this.requestCode = requestCode;
            return this;
        }

        public Builder setVideo(String... videos) {
            ArrayList<VideoItem> videoItems = new ArrayList<>();
            if (videos != null) {
                for (String photo : videos) {
                    VideoItem videoItem = new VideoItem();
                    videoItem.setPath(photo);
                    videoItems.add(videoItem);
                }
            }
            setVideoList(videoItems);
            return this;
        }

        public Builder setVideoItem(VideoItem... photos) {
            ArrayList<VideoItem> photoList = new ArrayList<>();
            if (photos != null) {
                photoList.addAll(Arrays.asList(photos));
            }
            setVideoList(photoList);
            return this;
        }

        public Builder setVideoList(ArrayList<VideoItem> videoItemList) {
            this.videoItems = videoItemList;
            return this;
        }

        public Builder setVideoMap(ArrayMap<Integer, IBaseItem> photoMap) {
            ArrayList<VideoItem> videoItems = new ArrayList<>();
            if (photoMap != null) {
                for (int i = 0; i < photoMap.size(); i++) {
                    videoItems.add((VideoItem) photoMap.valueAt(i));
                }
            }
            setVideoList(videoItems);
            return this;
        }

        public Builder setEdit(boolean isEdit) {
            this.isEdit = isEdit;
            return this;
        }

        public void build(Context context) {
            if (videoItems != null && videoItems.size() > 0) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(VIDEO_LIST, videoItems);
                bundle.putBoolean(VIDEO_IS_EDIT, isEdit);
                Intent intent = new Intent(context, SlcMpBrowseVideoActivity.class);
                intent.putExtras(bundle);
                if (isEdit) {
                    ((Activity) context).startActivityForResult(intent, requestCode);
                } else {
                    context.startActivity(intent);
                }
            } else {
                throw new IllegalStateException("videoList为空，该操作没有任何意义！");
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slc_mp_activity_video_browse);
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
        videoView = findViewById(R.id.video_view);
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appBarLayout.setVisibility(appBarLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
    }

    private void initData(Bundle bundle) {
        if (bundle == null) {
            throw new IllegalStateException("图片数据为空，该操作没有任何意义！");
        }
        videoItems = (List<VideoItem>) bundle.getSerializable(Builder.VIDEO_LIST);
        if (videoItems == null || videoItems.isEmpty()) {
            throw new IllegalStateException("videoList为空，该操作没有任何意义！");
        }
        videoView.setVideoPath(videoItems.get(0).getPath());
        videoView.start();
    }
}
