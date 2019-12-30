package android.slc.mp.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.recyclerview.widget.RecyclerView;

import android.slc.mp.R;
import android.util.AttributeSet;

public class SlcMpMaxHeightRecyclerView extends RecyclerView {
    private int mMaxHeight;

    public SlcMpMaxHeightRecyclerView(Context context) {
        super(context);
    }

    public SlcMpMaxHeightRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public SlcMpMaxHeightRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs) {
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.SlcMpMaxHeightRecyclerView);
        mMaxHeight = arr.getLayoutDimension(R.styleable.SlcMpMaxHeightRecyclerView_maxHeight, mMaxHeight);
        arr.recycle();
    }
    public void setMaxHeight(int maxHeight){
        mMaxHeight = maxHeight;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mMaxHeight > 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
