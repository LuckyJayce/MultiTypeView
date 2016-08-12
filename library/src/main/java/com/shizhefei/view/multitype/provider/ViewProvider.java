package com.shizhefei.view.multitype.provider;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shizhefei.view.multitype.ItemViewProvider;
import com.shizhefei.view.multitype.ViewUtils;

/**
 * View提供者
 */
public class ViewProvider extends ItemViewProvider<View> {
    private final int layoutWidth;
    private final int layoutHeight;

    public ViewProvider() {
        this.layoutWidth = ViewUtils.UNSET_LAYOUT_SIZE;
        this.layoutHeight = ViewUtils.UNSET_LAYOUT_SIZE;
    }

    public ViewProvider(int layoutWidth, int layoutHeight) {
        this.layoutWidth = layoutWidth;
        this.layoutHeight = layoutHeight;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int providerType) {
        ChildViewHeightFrameLayout layout = new ChildViewHeightFrameLayout(parent.getContext());
        layout.setLayoutParams(ViewUtils.getRightLayoutParams(parent, layoutWidth, layoutHeight));
        return new RecyclerView.ViewHolder(layout) {
        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams != null) {
            view.setTag(layoutParams);
        } else {
            layoutParams = view.getLayoutParams();
        }
        ChildViewHeightFrameLayout layout = (ChildViewHeightFrameLayout) viewHolder.itemView;
        layout.removeAllViews();
        if (view.getParent() != null) {
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            viewGroup.removeView(view);
        }
        if (layoutParams != null) {
            layout.addView(view, layoutParams);
        } else {
            layout.addView(view);
        }
    }
}