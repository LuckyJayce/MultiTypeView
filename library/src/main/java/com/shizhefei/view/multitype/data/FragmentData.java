package com.shizhefei.view.multitype.data;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * FragmentData，Fragment数据 实现Parcelable，可以将Fragment的状态保存下来，以便于恢复
 * Created by LuckyJayce on 2016/8/9.
 */
public class FragmentData implements IParcelableData<Class<? extends Fragment>> {
    private Bundle arguments = new Bundle();
    private final Class<? extends Fragment> fragmentClass;
    private Fragment fragment;
    private String tag;
    private Fragment.SavedState savedState;
    private int containerViewId = View.NO_ID;

    public FragmentData(Class<? extends Fragment> fragmentClass, String key) {
        this.fragmentClass = fragmentClass;
        this.tag = key;
    }

    public String getTag() {
        return tag;
    }

    public void addFragment(FragmentManager fragmentManager, int containerViewId) {
        this.containerViewId = containerViewId;
        fragment = getFragment(fragmentManager);
        if (!fragment.isAdded()) {
            fragmentManager.beginTransaction().add(containerViewId, fragment, tag).commitNowAllowingStateLoss();
//            fragmentManager.beginTransaction().remove(fragment).commitNowAllowingStateLoss();
        }
    }

    public boolean isAdd(int containerViewId) {
        return this.containerViewId == containerViewId;
    }

    public void saveState(FragmentManager fragmentManager) {
        if (fragment != null && fragmentManager != null) {
            try {
                savedState = fragmentManager.saveFragmentInstanceState(fragment);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Fragment getFragment(FragmentManager fragmentManager) {
        if (fragment != null) {
            return fragment;
        }
        fragment = fragmentManager.findFragmentByTag(tag);
        Log.d("cccc", "FragmentData getFragment" + fragment);
        if (fragment == null) {
            fragment = instantiate();
            if (savedState != null) {
                fragment.setInitialSavedState(savedState);
            }
            fragment.setMenuVisibility(false);
            fragment.setUserVisibleHint(false);
        }
        return fragment;
    }

    /**
     * 获取Fragment,如果Fragment还没有创建就返回null
     *
     * @return
     */
    public Fragment getFragment() {
        return fragment;
    }

    public void resetState() {
        tag = "";
        fragment = null;
        savedState = null;
    }

    protected Fragment instantiate() {
        try {
            Fragment f = fragmentClass.newInstance();
            if (fragmentClass != null) {
                arguments.setClassLoader(f.getClass().getClassLoader());
                f.setArguments(arguments);
            }
            return f;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("创建fragmentClass 失败", e);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(this.arguments);
        dest.writeSerializable(this.fragmentClass);
        dest.writeString(this.tag);
        dest.writeParcelable(savedState, flags);
    }

    protected FragmentData(Parcel in) {
        this.arguments = in.readBundle();
        this.fragmentClass = (Class<? extends Fragment>) in.readSerializable();
        this.tag = in.readString();
        this.savedState = in.readParcelable(Fragment.SavedState.class.getClassLoader());
    }

    public static final Creator<FragmentData> CREATOR = new Creator<FragmentData>() {
        @Override
        public FragmentData createFromParcel(Parcel source) {
            return new FragmentData(source);
        }

        @Override
        public FragmentData[] newArray(int size) {
            return new FragmentData[size];
        }
    };

    public FragmentData putBoolean(@Nullable String key, boolean value) {
        arguments.putBoolean(key, value);
        return this;
    }


    public FragmentData putInt(@Nullable String key, int value) {
        arguments.putInt(key, value);
        return this;
    }

    public FragmentData putString(@Nullable String key, @Nullable String value) {
        arguments.putString(key, value);
        return this;
    }

    public FragmentData putLong(@Nullable String key, long value) {
        arguments.putLong(key, value);
        return this;
    }

    public FragmentData putDouble(@Nullable String key, double value) {
        arguments.putDouble(key, value);
        return this;
    }

    public FragmentData putAll(Bundle bundle) {
        arguments.putAll(bundle);
        return this;
    }

    public FragmentData putByte(@Nullable String key, byte value) {
        arguments.putByte(key, value);
        return this;
    }

    public FragmentData putChar(@Nullable String key, char value) {
        arguments.putChar(key, value);
        return this;
    }

    public FragmentData putShort(@Nullable String key, short value) {
        arguments.putShort(key, value);
        return this;
    }

    public FragmentData putFloat(@Nullable String key, float value) {
        arguments.putFloat(key, value);
        return this;
    }

    public FragmentData putCharSequence(@Nullable String key, @Nullable CharSequence value) {
        arguments.putCharSequence(key, value);
        return this;
    }

    public FragmentData putParcelable(@Nullable String key, @Nullable Parcelable value) {
        arguments.putParcelable(key, value);
        return this;
    }

    public FragmentData putParcelableArray(@Nullable String key, @Nullable Parcelable[] value) {
        arguments.putParcelableArray(key, value);
        return this;
    }

    public FragmentData putParcelableArrayList(@Nullable String key, @Nullable ArrayList<? extends Parcelable> value) {
        arguments.putParcelableArrayList(key, value);
        return this;
    }

    public FragmentData putSparseParcelableArray(@Nullable String key, @Nullable SparseArray<? extends Parcelable> value) {
        arguments.putSparseParcelableArray(key, value);
        return this;
    }

    public FragmentData putIntegerArrayList(@Nullable String key, @Nullable ArrayList<Integer> value) {
        arguments.putIntegerArrayList(key, value);
        return this;
    }

    public FragmentData putStringArrayList(@Nullable String key, @Nullable ArrayList<String> value) {
        arguments.putStringArrayList(key, value);
        return this;
    }

    public FragmentData putCharSequenceArrayList(@Nullable String key, @Nullable ArrayList<CharSequence> value) {
        arguments.putCharSequenceArrayList(key, value);
        return this;
    }

    public FragmentData putSerializable(@Nullable String key, @Nullable Serializable value) {
        arguments.putSerializable(key, value);
        return this;
    }

    public FragmentData putByteArray(@Nullable String key, @Nullable byte[] value) {
        arguments.putByteArray(key, value);
        return this;
    }

    public FragmentData putShortArray(@Nullable String key, @Nullable short[] value) {
        arguments.putShortArray(key, value);
        return this;
    }

    public FragmentData putCharArray(@Nullable String key, @Nullable char[] value) {
        arguments.putCharArray(key, value);
        return this;
    }

    public FragmentData putFloatArray(@Nullable String key, @Nullable float[] value) {
        arguments.putFloatArray(key, value);
        return this;
    }

    public FragmentData putCharSequenceArray(@Nullable String key, @Nullable CharSequence[] value) {
        arguments.putCharSequenceArray(key, value);
        return this;
    }

    public FragmentData putBundle(@Nullable String key, @Nullable Bundle value) {
        arguments.putBundle(key, value);
        return this;
    }

    @Override
    public Class<? extends Fragment> getData() {
        return fragmentClass;
    }

    public int getContainerViewId() {
        return containerViewId;
    }
}
