package com.shizhefei.view.multitype;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
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

    private void init(Context context) {
        recyclerView = new RecyclerView(context);
        addView(recyclerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setId(ViewUtils.madeId());
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
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
