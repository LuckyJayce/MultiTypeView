package com.shizhefei.view.multitype.provider;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shizhefei.view.multitype.ItemViewProvider;
import com.shizhefei.view.multitype.ViewUtils;

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
                if (fragmentData.savedState != null) {
                    fragment.setInitialSavedState(fragmentData.savedState);
                }
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
        Fragment.SavedState savedState = savedInstanceState.getParcelable("FragmentData#" + data.getTag());
        data.setSavedState(savedState);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState, FragmentData data) {
        super.onSaveInstanceState(savedInstanceState, data);
        Fragment fragment = data.getFragment();
        if (fragment != null) {
            fragment.setUserVisibleHint(false);
            fragment.setMenuVisibility(false);
            try {
                Fragment.SavedState savedState = fragmentManager.saveFragmentInstanceState(fragment);
                savedInstanceState.putParcelable("FragmentData#" + data.getTag(), savedState);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Fragment getFragment(FragmentData fragmentData) {
        if (fragmentData.fragment != null) {
            return fragmentData.fragment;
        }
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentData.getTag());
        if (fragment == null) {
            fragment = instantiate(fragmentData);
            if (fragmentData.savedState != null) {
                fragment.setInitialSavedState(fragmentData.savedState);
            }
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