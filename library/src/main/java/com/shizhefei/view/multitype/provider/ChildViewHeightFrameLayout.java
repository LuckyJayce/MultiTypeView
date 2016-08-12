package com.shizhefei.view.multitype.provider;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 根据子View来决定自身自身View的大小
 */
class ChildViewHeightFrameLayout extends FrameLayout {

    public ChildViewHeightFrameLayout(Context context) {
        super(context);
    }

    public ChildViewHeightFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChildViewHeightFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getChildCount() == 0) {
            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                    MeasureSpec.AT_MOST);
            super.onMeasure(expandSpec, expandSpec);
//            Log.d("eeee", "onMeasure:00000000000 :" + this);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            View chi = getChildAt(0);
            setMeasuredDimension(chi.getMeasuredWidth(), chi.getMeasuredHeight());
//            Log.d("eeee", "onMeasure:11111111111 :" + this);
        }

//        Log.d("eeee", "onMeasure:" + getMeasuredWidth() + " h:" + getMeasuredHeight() + "  " + this);
    }
}