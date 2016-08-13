/*
Copyright 2016 shizhefei（LuckyJayce）https://github.com/LuckyJayce
Copyright 2016 drakeet.   https://github.com/drakeet

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

package com.shizhefei.view.multitype;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.shizhefei.view.multitype.data.SerializableData;
import com.shizhefei.view.multitype.provider.FragmentData;
import com.shizhefei.view.multitype.provider.FragmentDataProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class MultiTypeAdapter<ITEM_DATA> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected final ItemBinderFactory factory;
    protected List<ItemBinder<ITEM_DATA>> itemBinders = new ArrayList<>();
    protected ArrayList<ITEM_DATA> datas = new ArrayList<>();
    protected HashMap<ITEM_DATA, ItemBinder<ITEM_DATA>> data_Providers = new HashMap<>();
    protected SparseArray<ItemViewProvider<ITEM_DATA>> providerIndex = new SparseArray<>();
    protected RecyclerView recyclerView;
    private LayoutInflater layoutInflater;

    public MultiTypeAdapter(@NonNull ItemBinderFactory factory) {
        this.factory = factory;
        registerAdapterDataObserver(observer);
    }

    public MultiTypeAdapter(@NonNull List<? extends ITEM_DATA> addList, @NonNull ItemBinderFactory factory) {
        this.factory = factory;
        datas.addAll(addList);
        itemBinders.addAll(toItemData(addList, false));
        registerAdapterDataObserver(observer);
    }

    private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            if (!isSelfNotify) {
                updateAllData(datas);
            }
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            if (!isSelfNotify) {
                LinkedHashMap<ITEM_DATA, ItemBinder<ITEM_DATA>> map = new LinkedHashMap<>();
                int end = positionStart + itemCount;
                for (int i = positionStart; i < end; i++) {
                    ItemBinder<ITEM_DATA> itemBinder = itemBinders.get(i);
                    map.put(itemBinder.getData(), itemBinder);
                }
                for (int i = positionStart; i < end; i++) {
                    ITEM_DATA data = datas.get(i);
                    ItemBinder<ITEM_DATA> itemBinder = map.remove(data);
                    if (itemBinder == null) {
                        itemBinder = factory.buildItemData(data);
                    }
                    itemBinders.set(i, itemBinder);
                }
                doRemoveItemData(map.values());
            }
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            if (!isSelfNotify) {
                List<ITEM_DATA> addList = datas.subList(positionStart, positionStart + itemCount);
                List<ItemBinder<ITEM_DATA>> addItemBinder = toItemData(addList, false);
                itemBinders.addAll(positionStart, addItemBinder);
            }
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            if (!isSelfNotify) {
                List<ItemBinder<ITEM_DATA>> removeItemBinder = itemBinders.subList(positionStart, positionStart + itemCount);
                doRemoveItemData(removeItemBinder);
                int end = positionStart + itemCount - 1;
                for (int i = 0; i < itemCount; i++) {
                    itemBinders.remove(end - i);
                }
            }
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            if (!isSelfNotify) {
                int end = fromPosition + itemCount - 1;
                List<ItemBinder<ITEM_DATA>> moveFromItemBinder = new ArrayList<>(itemBinders.subList(fromPosition, fromPosition + itemCount));
                itemBinders.addAll(toPosition, moveFromItemBinder);
                for (int i = 0; i < itemCount; i++) {
                    itemBinders.remove(end - i);
                }
            }
        }
    };

    public void notifyDataChanged(List<ITEM_DATA> addList, boolean isRefresh) {
        notifyDataChanged2(addList, isRefresh);
    }

    public void notifyDataChanged2(List<? extends ITEM_DATA> addList, boolean isRefresh) {
        if (isRefresh) {
            updateAllData(addList);
            isSelfNotify = true;
            notifyDataSetChanged();
            isSelfNotify = false;
        } else {
            if (addList != datas) {
                datas.addAll(addList);
            }
            itemBinders.addAll(toItemData(addList, false));
            isSelfNotify = true;
            notifyItemRangeInserted(itemBinders.size() - addList.size(), addList.size());
            isSelfNotify = false;
        }
    }

    private boolean isSelfNotify;

    private void updateAllData(List<? extends ITEM_DATA> addList) {
        if (addList != datas) {
            datas.clear();
            datas.addAll(addList);
        }
        providerIndex.clear();
        itemBinders.clear();
        itemBinders.addAll(toItemData(addList, true));

    }

    @Override
    public int getItemViewType(int position) {
        return itemBinders.get(position).getProviderType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int providerType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemViewProvider<?> provider = providerIndex.get(providerType);
        return provider.onCreateViewHolder(layoutInflater, parent, providerType);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemBinder itemBinder = itemBinders.get(position);
        itemBinder.provider.onBindViewHolder(holder, SerializableData.getData(itemBinder.getData()));
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ItemViewProvider<?> provider = providerIndex.get(holder.getItemViewType());
        if (provider != null) {
            provider.onViewAttachedToWindow(holder);
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        ItemViewProvider<?> provider = providerIndex.get(holder.getItemViewType());
        if (provider != null) {
            provider.onViewDetachedFromWindow(holder);
        }
    }

    protected void onRestoreInstanceState(MultiTypeView multiTypeView, Bundle savedInstanceState) {
        for (ItemBinder binder : itemBinders) {
            ItemViewProvider provider = binder.provider;
            provider.onRestoreInstanceState(savedInstanceState, binder.data);
        }
        FragmentDataProvider provider = factory.getFragmentDataProvider();
        if (provider == null) {
            return;
        }
    }

    protected void onSaveInstanceState(MultiTypeView multiTypeView, Bundle savedInstanceState) {
        for (ItemBinder binder : itemBinders) {
            ItemViewProvider provider = binder.provider;
            provider.onSaveInstanceState(savedInstanceState, binder.data);
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public int getItemCount() {
        return datas.size();
    }


    public List<ITEM_DATA> getData() {
        return datas;
    }

    public boolean isEmpty() {
        return datas.isEmpty();
    }

    private List<ItemBinder<ITEM_DATA>> toItemData(@NonNull List<? extends ITEM_DATA> addList, boolean refresh) {
        HashMap<? extends ITEM_DATA, ItemBinder<ITEM_DATA>> removeItemData = null;
        if (refresh) {
            removeItemData = data_Providers;
            data_Providers = new HashMap<>();
        }
        List<ItemBinder<ITEM_DATA>> providers = new ArrayList<>(addList.size());
        for (ITEM_DATA object : addList) {
            ItemBinder<ITEM_DATA> itemBinder;
            if (refresh) {
                itemBinder = removeItemData.remove(object);
            } else {
                itemBinder = data_Providers.get(object);
            }
            if (itemBinder == null) {
                itemBinder = factory.buildItemData(object);
            }
            data_Providers.put(object, itemBinder);
            providers.add(itemBinder);
            providerIndex.put(itemBinder.providerType, itemBinder.provider);
        }

        if (refresh) {
            //处理Fragment的移除
            doRemoveItemData(removeItemData.values());
        }
        return providers;
    }

    protected void doRemoveItemData(Collection<ItemBinder<ITEM_DATA>> removeItemBinder) {
        FragmentManager fragmentManager = factory.getFragmentManager();
        if (fragmentManager == null) {
            return;
        }
        FragmentTransaction transaction = null;
        for (ItemBinder<?> itemBinder : removeItemBinder) {
            Object data = itemBinder.data;
            Fragment fragment = null;
            if (data instanceof FragmentData) {
                FragmentData fragmentData = (FragmentData) data;
                fragment = fragmentData.getFragment();
                fragmentData.resetState();
            } else if (data instanceof Fragment) {
                fragment = (Fragment) data;
            }
            if (fragment != null) {
                if (transaction == null) {
                    transaction = fragmentManager.beginTransaction();
                }
                transaction.remove(fragment);
            }
            if (recyclerView != null) {
                ItemViewProvider provider = itemBinder.provider;
                if (provider.isUniqueProviderType(SerializableData.getData(data))) {
                    RecyclerView.RecycledViewPool pool = recyclerView.getRecycledViewPool();
                    //清除对应的providerType的缓存
                    pool.setMaxRecycledViews(itemBinder.providerType, 0);
                    //重新设置providerType类型的可以缓存
                    pool.setMaxRecycledViews(itemBinder.providerType, 5);
                }
            }
        }
        if (transaction != null) {
            transaction.commitNowAllowingStateLoss();
        }
    }

    public int size() {
        return datas.size();
    }

    public static <DATA extends Parcelable> void saveState(String key, MultiTypeAdapter<DATA> adapter, Bundle outState) {
        saveState(key, adapter, outState, adapter.size());
    }

    public static <DATA extends Parcelable> void saveState(String key, MultiTypeAdapter<DATA> adapter, Bundle outState, int maxSize) {
        List<DATA> datas = adapter.getData();
        int size = adapter.size();
        int saveCount = maxSize < size ? maxSize : size;
        ArrayList<Parcelable> list = new ArrayList<>(maxSize);
        for (int i = 0; i < saveCount; i++) {
            list.add(datas.get(i));
        }
        outState.putParcelableArrayList(key, list);
    }

    /**
     * 恢复状态
     *
     * @param savedInstanceState
     */
    public static <DATA extends Parcelable> boolean restoreState(String key, MultiTypeAdapter<DATA> adapter, Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return false;
        }
        List<DATA> list = savedInstanceState.getParcelableArrayList(key);
        if (list != null) {
            adapter.notifyDataChanged(list, true);
            return true;
        }
        return false;
    }

}