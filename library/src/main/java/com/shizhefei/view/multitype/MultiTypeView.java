package com.shizhefei.view.multitype;

import android.content.Context;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by LuckyJayce on 2016/8/11.
 */
public class MultiTypeView extends FrameLayout {
    private RecyclerView recyclerView;
    private MultiTypeAdapter<?> typeAdapter;

    public MultiTypeView(Context context) {
        super(context);
        init(context);
    }

    public MultiTypeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MultiTypeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public class MyCustomLayoutManager extends LinearLayoutManager {
        private static final float MILLISECONDS_PER_INCH = 50f;
        private Context mContext;

        public MyCustomLayoutManager(Context context) {
            super(context);
            mContext = context;
        }

        @Override
        public void smoothScrollToPosition(RecyclerView recyclerView,
                                           RecyclerView.State state, final int position) {

            LinearSmoothScroller smoothScroller =
                    new LinearSmoothScroller(mContext) {

                        //This controls the direction in which smoothScroll looks
                        //for your view
                        @Override
                        public PointF computeScrollVectorForPosition
                        (int targetPosition) {
                            return MyCustomLayoutManager.this
                                    .computeScrollVectorForPosition(targetPosition);
                        }

                        //This returns the milliseconds it takes to
                        //scroll one pixel.
                        @Override
                        protected float calculateSpeedPerPixel
                        (DisplayMetrics displayMetrics) {
                            return MILLISECONDS_PER_INCH/displayMetrics.densityDpi;
                        }
                    };

            smoothScroller.setTargetPosition(position);
            startSmoothScroll(smoothScroller);
        }
    }

    private void init(Context context) {
        recyclerView = new RecyclerView(context);
        addView(recyclerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setId(ViewUtils.madeId());
        recyclerView.setLayoutManager(new MyCustomLayoutManager(context));
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setAdapter(MultiTypeAdapter<?> adapter) {
        this.typeAdapter = adapter;
        this.recyclerView.setAdapter(typeAdapter);
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return recyclerView.getLayoutManager();
    }

    public MultiTypeAdapter<?> getAdapter() {
        return typeAdapter;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle stateBundle = new Bundle();
        MultiTypeAdapter adapter = getAdapter();
        if (adapter != null) {
            adapter.onSaveInstanceState(this, stateBundle);
        }
        stateBundle.putParcelable("superParcelable", super.onSaveInstanceState());
        return stateBundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle stateBundle = (Bundle) state;
            MultiTypeAdapter adapter = getAdapter();
            if (adapter != null) {
                adapter.onRestoreInstanceState(this, stateBundle);
            }
            super.onRestoreInstanceState(stateBundle.getParcelable("superParcelable"));
        } else {
            super.onRestoreInstanceState(state);
        }
    }

}
