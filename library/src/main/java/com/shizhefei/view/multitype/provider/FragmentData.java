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
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * FragmentData，Fragment数据 实现Parcelable，可以将Fragment的状态保存下来，以便于恢复
 * Created by LuckyJayce on 2016/8/9.
 */
public class FragmentData implements Parcelable {
    Bundle arguments = new Bundle();
    final Class<? extends Fragment> fragmentClass;
    Fragment fragment;
    String tag;
    int containerViewId = View.NO_ID;

    /**
     * @param fragmentClass fragment的class
     * @param fragmentTag   tag必须是唯一
     */
    public FragmentData(Class<? extends Fragment> fragmentClass, String fragmentTag) {
        this.fragmentClass = fragmentClass;
        this.tag = fragmentTag;
    }

    public String getTag() {
        return tag;
    }

    public Class<? extends Fragment> getFragmentClass() {
        return fragmentClass;
    }

    public Bundle getArguments() {
        return arguments;
    }

    /**
     * 获取Fragment,如果Fragment还没有创建就返回null
     *
     * @return
     */
    public Fragment getFragment() {
        return fragment;
    }

    void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }


    void setContainerViewId(int containerViewId) {
        this.containerViewId = containerViewId;
    }

    public void resetState() {
        tag = "";
        fragment = null;
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
    }

    protected FragmentData(Parcel in) {
        this.arguments = in.readBundle();
        this.fragmentClass = (Class<? extends Fragment>) in.readSerializable();
        this.tag = in.readString();
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
}
