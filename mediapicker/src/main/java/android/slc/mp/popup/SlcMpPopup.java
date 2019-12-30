package android.slc.mp.popup;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.AnimRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import android.slc.mp.R;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by slc on 2019/3/8.
 */

public class SlcMpPopup {
    private static Map<String, BaseOperate> operateMap = new LinkedHashMap<>();

    public static void addOperate(String key, BaseOperate baseOperate) {
        if (!TextUtils.isEmpty(key) && baseOperate != null) {
            operateMap.put(key, baseOperate);
        }
    }

    public static void removeOperate(String key) {
        if (!TextUtils.isEmpty(key)) {
            operateMap.remove(key);
        }
    }

    public static void dismissByKey(String key) {
        BaseOperate baseOperate = getOperateByKey(key);
        if (baseOperate != null) {
            //removeOperate(key);
            baseOperate.dismiss();
        }
    }

    public static BaseOperate getOperateByKey(String key) {
        return operateMap.get(key);
    }

    /**
     * Return the width of screen, in pixel.
     *
     * @return the width of screen, in pixel
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //noinspection ConstantConditions
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            //noinspection ConstantConditions
            wm.getDefaultDisplay().getSize(point);
        }
        return point.x;
    }

    /**
     * Return the height of screen, in pixel.
     *
     * @return the height of screen, in pixel
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //noinspection ConstantConditions
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            //noinspection ConstantConditions
            wm.getDefaultDisplay().getSize(point);
        }
        return point.y;
    }

    public static abstract class BaseBuilder<T extends BaseBuilder> {
        boolean mCancelable = true;
        String mKey;

        public BaseBuilder(@NonNull Context context) {
        }

        public abstract Context getContext();

        int dip2px(float dipValue) {
            final float scale = getContext().getResources().getDisplayMetrics().density;
            return (int) (dipValue * scale + 0.5f);
        }

        public T setCancelable(boolean cancelable) {
            this.mCancelable = cancelable;
            return (T) this;
        }

        public T setKey(String key) {
            mKey = key;
            return (T) this;
        }

        /**
         * 设置显示动画
         *
         * @param animRes
         * @return
         */
        public abstract T setAnimRes(@AnimRes int animRes);

        /**
         * 设置背景
         *
         * @param drawable
         * @return
         */
        public abstract T setBgDrawable(Drawable drawable);

        /**
         * 设置背景
         *
         * @param drawableRes
         * @return
         */
        public abstract T setBgDrawableRes(@DrawableRes int drawableRes);

        /**
         * 设置最大高度
         *
         * @param maxHeight
         * @return
         */
        public abstract T setMaxHeight(int maxHeight);

        /**
         * 设置最大宽度
         *
         * @param maxWidth
         * @return
         */
        public abstract T setMaxWidth(int maxWidth);

        /**
         * 设置最大高度和宽度
         *
         * @param maxHeight
         * @param maxWidth
         * @return
         */
        public abstract T setMaxWidthAndHeight(int maxWidth, int maxHeight);

        /**
         * 创建对话框，
         */
        public abstract BaseOperate create();
    }

    public static class ShadowPopupWindowBuilder extends BaseBuilder<ShadowPopupWindowBuilder> {
        private Context mContext;
        private FrameLayout mRootView;
        private FrameLayout mContentParentView;
        private boolean mIsEnsureContentParentGravity;
        private int mDuration = -1;
        private int mPositiveAnimRes = R.style.SlcMpShadowAnimDef;
        private int mNegativeAnimRes = R.style.SlcMpShadowAnimNegativeDef;
        protected Drawable mBgDrawable;
        protected int mMaxWidth, mMaxHeight;
        protected int mDirection;
        private OnContentViewInitListener mOnContentViewInitListener;

        public ShadowPopupWindowBuilder(@NonNull Context context) {
            super(context);
            this.mContext = context;
            init();
        }

        private void init() {
            mRootView = new FrameLayout(mContext);
            mRootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            mContentParentView = new FrameLayout(mContext);
            mContentParentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            mRootView.addView(mContentParentView);
        }

        @Override
        public Context getContext() {
            return mContext;
        }

        @Override
        public ShadowPopupWindowBuilder setAnimRes(@AnimRes int animRes) {
            this.mPositiveAnimRes = animRes;
            return this;
        }

        public ShadowPopupWindowBuilder setAnimNegativeAnimRes(@AnimRes int animRes) {
            this.mNegativeAnimRes = animRes;
            return this;
        }

        @Override
        public ShadowPopupWindowBuilder setBgDrawable(Drawable drawable) {
            this.mBgDrawable = drawable;
            return this;
        }

        @Override
        public ShadowPopupWindowBuilder setBgDrawableRes(int drawableRes) {
            setBgDrawable(mContext.getResources().getDrawable(drawableRes));
            return this;
        }

        @Deprecated
        @Override
        public ShadowPopupWindowBuilder setMaxHeight(int maxHeight) {
            this.mMaxHeight = maxHeight;
            return this;
        }

        @Deprecated
        @Override
        public ShadowPopupWindowBuilder setMaxWidth(int maxWidth) {
            this.mMaxWidth = maxWidth;
            return this;
        }

        @Deprecated
        @Override
        public ShadowPopupWindowBuilder setMaxWidthAndHeight(int maxWidth, int maxHeight) {
            setMaxHeight(maxHeight);
            setMaxWidth(maxWidth);
            return this;
        }

        public ShadowPopupWindowBuilder getLightDuration(int duration) {
            this.mDuration = duration;
            return this;
        }

        public ShadowPopupWindowBuilder setAnchor(@NonNull View anchor) {
            int anchorHeight = anchor.getHeight();
            int[] anchorLocation = new int[2];
            anchor.getLocationOnScreen(anchorLocation);
            int heightPixels = getScreenHeight(mContext);
            FrameLayout.LayoutParams contentParentViewLayoutParams =
                    (FrameLayout.LayoutParams) mContentParentView.getLayoutParams();
            if (anchorLocation[1] + anchorHeight / 2 <= heightPixels / 2) {
                contentParentViewLayoutParams.height = heightPixels - anchorLocation[1] - anchorHeight;
                contentParentViewLayoutParams.gravity = Gravity.BOTTOM;
                mDirection = Gravity.BOTTOM;
            } else {
                contentParentViewLayoutParams.height = anchorLocation[1];
                contentParentViewLayoutParams.gravity = Gravity.TOP;
                mDirection = Gravity.TOP;
            }
            mIsEnsureContentParentGravity = true;
            ensureContentViewGravity();
            return this;
        }

        /*public ShadowPopupWindowBuilder setAnchor(@NonNull View anchor, int xOff, int yOff) {
            setAnchor(anchor);
            return this;
        }*/
        public ShadowPopupWindowBuilder setContentView(@LayoutRes int contentLayout) {
            setContentView(LayoutInflater.from(mContext).inflate(contentLayout, null));
            return this;
        }

        public ShadowPopupWindowBuilder setContentView(@NonNull View contentView) {
            contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            mContentParentView.removeAllViews();
            mContentParentView.addView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            ensureContentViewGravity();
            return this;
        }

        protected void ensureContentViewGravity() {
            if (mContentParentView.getChildCount() != 0 && mIsEnsureContentParentGravity) {
                FrameLayout.LayoutParams contentViewLayoutParams =
                        (FrameLayout.LayoutParams) mContentParentView.getChildAt(0).getLayoutParams();
                FrameLayout.LayoutParams contentViewParentLayoutParams =
                        (FrameLayout.LayoutParams) mContentParentView.getLayoutParams();
                contentViewLayoutParams.gravity = contentViewParentLayoutParams.gravity == Gravity.BOTTOM ? Gravity.TOP :
                        Gravity.BOTTOM;
            }
        }

        public ShadowPopupWindowBuilder setContentViewInitListener(OnContentViewInitListener onContentViewInitListener) {
            this.mOnContentViewInitListener = onContentViewInitListener;
            return this;
        }

        @Override
        public BaseOperate create() {
            int[] attr = new int[]{android.R.attr.windowEnterAnimation, android.R.attr.windowEnterAnimation};
            TypedArray array = mContext.getTheme().obtainStyledAttributes(mDirection == Gravity.BOTTOM ? mNegativeAnimRes :
                    mPositiveAnimRes, attr);
            final int enterAnimId = array.getResourceId(0, mDirection == Gravity.BOTTOM ? R.anim.slc_mp_top_shadow_in_def :
                    R.anim.slc_mp_bottom_shadow_in_def);
            final int exitAnimId = array.getResourceId(1, mDirection == Gravity.BOTTOM ? R.anim.slc_mp_top_shadow_out_def :
                    R.anim.slc_mp_bottom_shadow_out_def);
            array.recycle();
            if (mBgDrawable != null) {
                mContentParentView.getChildAt(0).setBackground(mBgDrawable);
            }
            if (mOnContentViewInitListener != null) {
                mOnContentViewInitListener.onContentViewInit(mContentParentView.getChildAt(0));
            }
            return new BaseOperate() {
                @Override
                public void dismiss() {
                    SlcMpPopup.removeOperate(getKey());
                    exitAnim(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            ((ViewGroup) ((Activity) mContext).getWindow().getDecorView()).removeView(mRootView);
                        }
                    });
                }


                @Override
                public void show() {
                    mRootView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                        }
                    });
                    mContentParentView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isCancelable()) {
                                dismiss();
                            }
                        }
                    });
                    ((ViewGroup) ((Activity) mContext).getWindow().getDecorView()).addView(mRootView);
                    enterAnim();
                    SlcMpPopup.addOperate(getKey(), this);
                }

                @Override
                public boolean isCancelable() {
                    return mCancelable;
                }

                @Override
                public String getKey() {
                    return mKey;
                }

                private ValueAnimator enterAnim() {
                    Animation viewAnimation = AnimationUtils.loadAnimation(mContext, enterAnimId);
                    mContentParentView.getChildAt(0).setAnimation(viewAnimation);
                    ValueAnimator valueAnimator = ValueAnimator.ofInt(0x00000000, 0x80000000);
                    valueAnimator.setEvaluator(new ArgbEvaluator());
                    valueAnimator.setDuration(mDuration == -1 ? viewAnimation.getDuration() : mDuration);
                    valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            mContentParentView.setBackgroundColor((Integer) animation.getAnimatedValue());
                        }
                    });
                    valueAnimator.start();
                    return valueAnimator;
                }

                private ValueAnimator exitAnim(Animator.AnimatorListener animationListener) {
                    Animation viewAnimation = AnimationUtils.loadAnimation(mContext, exitAnimId);
                    mContentParentView.getChildAt(0).startAnimation(viewAnimation);
                    ValueAnimator valueAnimator = ValueAnimator.ofInt(0x80000000, 0x00000000);
                    valueAnimator.setEvaluator(new ArgbEvaluator());
                    valueAnimator.setDuration(mDuration == -1 ? viewAnimation.getDuration() : mDuration);
                    valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            mContentParentView.setBackgroundColor((Integer) animation.getAnimatedValue());
                        }
                    });
                    valueAnimator.addListener(animationListener);
                    valueAnimator.start();
                    return valueAnimator;
                }
            };
        }

        public interface OnContentViewInitListener {
            void onContentViewInit(View view);
        }
    }

    private static Map<Integer, PointF> offsetByViewHashCode = new HashMap<>();

    private static PointF getOffsetByViewHashCode(int hashCode) {
        PointF pointF = offsetByViewHashCode.get(hashCode);
        offsetByViewHashCode.remove(hashCode);
        return pointF;
    }


    public interface BaseOperate {
        void dismiss();

        void show();

        boolean isCancelable();

        String getKey();

    }

    public interface DialogOperate extends BaseOperate {
        Dialog getDialog();
    }

    public interface PopupOperate extends BaseOperate {
        PopupWindow getPopupWindow();
    }
}
