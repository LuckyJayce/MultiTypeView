/*
Copyright 2016 shizhefei（LuckyJayce）https://github.com/LuckyJayce

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.shizhefei.view.multitype.provider;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shizhefei.view.multitype.ItemViewProvider;

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
        ChildViewHeightLayout layout = new ChildViewHeightLayout(parent.getContext());
        layout.setLayoutParams(ViewUtils.getRightLayoutParams(parent, layoutWidth, layoutHeight));
        layout.setId(providerType);
        return new FragmentViewHolder(layout) {
            @Override
            public String toString() {
                return "Fragment :" + super.toString();
            }
        };
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
        super.onViewAttachedToWindow(viewHolder);
        FragmentViewHolder viewH = (FragmentViewHolder) viewHolder;
        FragmentData fragmentData = viewH.fragmentData;
        int containerViewId = viewHolder.itemView.getId();
        Fragment fragment = fragmentData.fragment;
        if (fragment == null) {
            fragmentData.setContainerViewId(containerViewId);
            fragment = getFragment(fragmentData);
            if (fragment.isAdded()) {
                fragmentManager.beginTransaction().remove(fragment).commitNowAllowingStateLoss();
            }
            fragmentManager.beginTransaction().add(containerViewId, fragment, fragmentData.getTag()).commitNowAllowingStateLoss();
            fragmentData.setFragment(fragment);
        }
        fragment.setUserVisibleHint(true);
        fragment.setMenuVisibility(true);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder viewHolder) {
        super.onViewDetachedFromWindow(viewHolder);
        FragmentViewHolder viewH = (FragmentViewHolder) viewHolder;
        Fragment fragment = viewH.fragmentData.getFragment();
        if (fragment != null) {
            fragment.setUserVisibleHint(false);
            fragment.setMenuVisibility(false);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, FragmentData fragmentData) {
        FragmentViewHolder viewH = (FragmentViewHolder) viewHolder;
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

    private Fragment getFragment(FragmentData fragmentData) {
        if (fragmentData.fragment != null) {
            return fragmentData.fragment;
        }
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentData.getTag());
        if (fragment == null) {
            fragment = instantiate(fragmentData);
            fragment.setMenuVisibility(false);
            fragment.setUserVisibleHint(false);
        }
        return fragment;
    }

    private Fragment instantiate(FragmentData fragmentData) {
        try {
            Class<? extends Fragment> fragmentClass = fragmentData.getFragmentClass();
            Fragment f = fragmentData.getFragmentClass().newInstance();
            if (fragmentClass != null) {
                Bundle arguments = fragmentData.getArguments();
                arguments.setClassLoader(f.getClass().getClassLoader());
                f.setArguments(arguments);
            }
            return f;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("创建fragmentClass 失败", e);
        }
    }

    private class FragmentViewHolder extends RecyclerView.ViewHolder {
        private FragmentData fragmentData;

        public FragmentViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public boolean isUniqueProviderType(FragmentData fragment) {
        return true;
    }
}