package com.shizhefei.view.multitype.provider;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shizhefei.view.multitype.ItemViewProvider;
import com.shizhefei.view.multitype.ViewUtils;
import com.shizhefei.view.multitype.data.FragmentData;

/**
 * FragmentData提供者
 */
public class FragmentDataProvider extends ItemViewProvider<FragmentData> {
    private FragmentManager fragmentManager;
    private final int layoutWidth;
    private final int layoutHeight;

    public FragmentDataProvider(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        this.layoutWidth = ViewUtils.UNSET_LAYOUT_SIZE;
        this.layoutHeight = ViewUtils.UNSET_LAYOUT_SIZE;
    }

    public FragmentDataProvider(FragmentManager fragmentManager, int layoutWidth, int layoutHeight) {
        this.fragmentManager = fragmentManager;
        this.layoutWidth = layoutWidth;
        this.layoutHeight = layoutHeight;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int providerType) {
        Log.d("pppp","FragmentDataProvider onCreateViewHolder:"+providerType);
        ChildViewHeightFrameLayout layout = new ChildViewHeightFrameLayout(parent.getContext());
        layout.setLayoutParams(ViewUtils.getRightLayoutParams(parent, layoutWidth, layoutHeight));
        layout.setId(providerType);
        return new MyViewH(layout) {
            @Override
            public String toString() {
                return "Fragment :" + super.toString();
            }
        };
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
        super.onViewAttachedToWindow(viewHolder);
        MyViewH viewH = (MyViewH) viewHolder;
        int containerViewId = viewHolder.itemView.getId();
        if (!viewH.fragmentData.isAdd(containerViewId)) {
            viewH.fragmentData.addFragment(fragmentManager, containerViewId);
        }
        Fragment fragment = viewH.fragmentData.getFragment();
        if (fragment != null) {
            fragment.setUserVisibleHint(true);
            fragment.setMenuVisibility(true);
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder viewHolder) {
        super.onViewDetachedFromWindow(viewHolder);
        MyViewH viewH = (MyViewH) viewHolder;
        Fragment fragment = viewH.fragmentData.getFragment();
        if (fragment != null) {
            fragment.setUserVisibleHint(false);
            fragment.setMenuVisibility(false);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, FragmentData fragmentData) {
        MyViewH viewH = (MyViewH) viewHolder;
        viewH.fragmentData = fragmentData;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, FragmentData data) {
        super.onRestoreInstanceState(savedInstanceState, data);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState, FragmentData data) {
        super.onSaveInstanceState(savedInstanceState, data);
        Fragment fragment = data.getFragment();
        if (fragment != null) {
            fragment.setUserVisibleHint(false);
            fragment.setMenuVisibility(false);
        }
    }

    private class MyViewH extends RecyclerView.ViewHolder {
        private FragmentData fragmentData;

        public MyViewH(View itemView) {
            super(itemView);
        }
    }

    @Override
    public boolean isUniqueProviderType(FragmentData fragment) {
        return true;
    }
}