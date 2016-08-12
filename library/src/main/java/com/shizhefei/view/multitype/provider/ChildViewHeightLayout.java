package com.shizhefei.view.multitype.provider;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 根据子View来决定自身自身View的大小
 */
class ChildViewHeightLayout extends ViewGroup {

    public ChildViewHeightLayout(Context context) {
        super(context);
    }

    public ChildViewHeightLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChildViewHeightLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int mTotalHeight = 0;
        // 遍历所有子视图
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            // 获取在onMeasure中计算的视图尺寸
            int measureHeight = childView.getMeasuredHeight();
            int measuredWidth = childView.getMeasuredWidth();
            childView.layout(0, mTotalHeight, measuredWidth, mTotalHeight + measureHeight);
            mTotalHeight += measureHeight;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getChildCount() == 0) {
            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                    MeasureSpec.AT_MOST);
            super.onMeasure(expandSpec, expandSpec);
        } else {
            measureChildren(widthMeasureSpec, heightMeasureSpec);
            View chi = getChildAt(0);
            setMeasuredDimension(chi.getMeasuredWidth(), chi.getMeasuredHeight());
        }
    }
}