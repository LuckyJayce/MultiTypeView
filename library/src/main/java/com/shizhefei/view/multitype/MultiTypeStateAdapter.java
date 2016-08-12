/*
 * Copyright 2016 drakeet. https://github.com/drakeet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.shizhefei.view.multitype;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.shizhefei.view.multitype.data.FragmentData;

import java.util.ArrayList;
import java.util.List;

/**
 * 可以添加任何Parcelable数据的MultiTypeStateAdapter
 * 可以恢复列表数据的MultiTypeStateAdapter
 */
public class MultiTypeStateAdapter extends TypeAdapter<Parcelable> {

    private final String restoreTag;

    /**
     * @param factory ItemBinder工厂
     */
    public MultiTypeStateAdapter(@NonNull ItemBinderFactory factory) {
        super(factory);
        this.restoreTag = "MultiTypeStateAdapter";
    }

    /**
     * @param factory    ItemBinder工厂
     * @param restoreTag 用于一个界面有来区分有多个MultiTypeStateAdapter的情况，不过这种情况一般很少
     */
    public MultiTypeStateAdapter(@NonNull ItemBinderFactory factory, @NonNull String restoreTag) {
        super(factory);
        this.restoreTag = restoreTag;
    }

    /**
     * @param addList 添加的数据list
     * @param factory ItemBinder工厂
     */
    public MultiTypeStateAdapter(@NonNull List<Parcelable> addList, @NonNull ItemBinderFactory factory) {
        super(addList, factory);
        this.restoreTag = "MultiTypeStateAdapter";
    }

    /**
     * restoreTag 用于一个界面有来区分有多个MultiTypeStateAdapter的情况，不过这种情况一般很少
     *
     * @param addList    添加的数据list
     * @param factory    ItemBinder工厂
     * @param restoreTag 用于保存和恢复数据的key
     */
    public MultiTypeStateAdapter(@NonNull List<Parcelable> addList, @NonNull ItemBinderFactory factory, @NonNull String restoreTag) {
        super(addList, factory);
        this.restoreTag = restoreTag;
    }

//    public void saveState(Bundle outState) {
//        saveState(outState, datas.size());
//    }

    @Override
    public void onSaveInstanceState(MultiTypeView multiTypeView, Bundle savedInstanceState) {
        super.onSaveInstanceState(multiTypeView,savedInstanceState);
        FragmentManager fragmentManager = factory.getFragmentManager();
        int saveCount = datas.size();
        ArrayList<Parcelable> list = new ArrayList<>(saveCount);
        for (int i = 0; i < saveCount; i++) {
            Parcelable p = datas.get(i);
            if (p instanceof FragmentData) {
                ((FragmentData) p).saveState(fragmentManager);
            }
            list.add(p);
        }
        savedInstanceState.putParcelableArrayList(restoreTag, list);
    }

    @Override
    public void onRestoreInstanceState(MultiTypeView multiTypeView, Bundle savedInstanceState) {
        super.onRestoreInstanceState(multiTypeView, savedInstanceState);
        ArrayList<Parcelable> list = getDataFromRestoreState(savedInstanceState);
        Log.d("cccc", "restoreState:" + list.size());
        if (list != null) {
            notifyDataChanged(list, true);
        }
    }
//
//    /**
//     * 保存状态
//     *
//     * @param outState
//     * @param maxSize
//     */
//    public void saveState(Bundle outState, int maxSize) {
//        int size = datas.size();
//        int saveCount = maxSize < size ? maxSize : size;
//        ArrayList<Parcelable> list = new ArrayList<>(maxSize);
//        for (int i = 0; i < saveCount; i++) {
//            Parcelable p = datas.get(i);
//            if (p instanceof FragmentData) {
//                ((FragmentData) p).saveState();
//            }
//            list.add(p);
//        }
//        outState.putParcelableArrayList(restoreTag, list);
////        clearAdapterFragments();
//    }
//
//    /**
//     * 恢复状态
//     *
//     * @param outState
//     */
//    public void restoreState(Bundle outState) {
//        ArrayList<Parcelable> list = getDataFromRestoreState(outState);
//        Log.d("cccc", "restoreState:" + list.size());
//        if (list != null) {
//            notifyDataChanged(list, true);
//        }
//    }
//
    /**
     * 从Bundle获取保存的数据
     *
     * @param outState
     * @return
     */
    public ArrayList<Parcelable> getDataFromRestoreState(Bundle outState) {
        if (outState.containsKey(restoreTag)) {
            return outState.getParcelableArrayList(restoreTag);
        }
        return null;
    }
}