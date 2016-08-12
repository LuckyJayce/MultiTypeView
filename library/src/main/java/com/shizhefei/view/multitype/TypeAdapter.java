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
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shizhefei.view.multitype.data.FragmentData;
import com.shizhefei.view.multitype.data.SerializableData;
import com.shizhefei.view.multitype.provider.FragmentDataProvider;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

class TypeAdapter<ITEM_DATA> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected final ItemBinderFactory factory;
    protected List<ItemBinder<ITEM_DATA>> itemBinders = new ArrayList<>();
    protected ArrayList<ITEM_DATA> datas = new ArrayList<>();
    protected HashMap<ITEM_DATA, ItemBinder<ITEM_DATA>> data_Providers = new HashMap<>();
    protected SparseArray<ItemViewProvider<ITEM_DATA>> providerIndex = new SparseArray<>();
    protected RecyclerView recyclerView;
    private ArrayList<FragmentState> fragmentStates;

    public TypeAdapter(@NonNull ItemBinderFactory factory) {
        this.factory = factory;
        registerAdapterDataObserver(observer);
    }

    public TypeAdapter(@NonNull List<ITEM_DATA> addList, @NonNull ItemBinderFactory factory) {
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
                updateAllData(datas);
            }
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
            if (!isSelfNotify) {
                updateAllData(datas);
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

    private void updateAllData(List<ITEM_DATA> addList) {
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
        LayoutInflater in = LayoutInflater.from(parent.getContext());
        ItemViewProvider<?> provider = providerIndex.get(providerType);
        return provider.onCreateViewHolder(in, parent, providerType);
    }

    private int hashCode = -1;

    protected int getHashCode() {
        if (hashCode == -1) {
            hashCode = hashCode();
        }
        return hashCode();
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

    public void onRestoreInstanceState(MultiTypeView multiTypeView, Bundle savedInstanceState) {
        for (ItemBinder binder : itemBinders) {
            ItemViewProvider provider = binder.provider;
            provider.onRestoreInstanceState(savedInstanceState, binder.data);
        }
        FragmentDataProvider provider = factory.getFragmentDataProvider();
        if (provider == null) {
            return;
        }
        try {
            LayoutInflater inflater = LayoutInflater.from(multiTypeView.getContext());
            fragmentStates = savedInstanceState.getParcelableArrayList("fragments");
            Field field = RecyclerView.ViewHolder.class.getDeclaredField("mItemViewType");
            field.setAccessible(true);
            for (FragmentState data : fragmentStates) {
                Log.d("pppp", "onRestoreInstanceState:" + data.getTag() + " " + data.getContainerViewId());
                int containerViewId = data.getContainerViewId();
                RecyclerView.ViewHolder viewHolder = provider.onCreateViewHolder(inflater, multiTypeView, containerViewId);
                field.set(viewHolder, containerViewId);
                multiTypeView.addView(viewHolder.itemView);
                Log.d("pppp", "add-------------  :" + viewHolder.itemView.getLayoutParams().width + "  " + viewHolder.itemView.getLayoutParams().height);
                viewHolder.itemView.setVisibility(View.GONE);
                multiTypeView.getRecycledViewPool().putFragmentViewHolder(containerViewId, viewHolder);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onSaveInstanceState(MultiTypeView multiTypeView, Bundle savedInstanceState) {
        for (ItemBinder binder : itemBinders) {
            ItemViewProvider provider = binder.provider;
            provider.onSaveInstanceState(savedInstanceState, binder.data);
        }
        ArrayList<FragmentState> list = new ArrayList<>();
        for (Object data : datas) {
            if (data instanceof FragmentData) {
                FragmentData fragmentData = (FragmentData) data;
                if (fragmentData.getFragment() != null && fragmentData.getContainerViewId() != View.NO_ID) {
                    list.add(new FragmentState(fragmentData.getContainerViewId(), fragmentData.getTag()));
                }
            }
        }
        savedInstanceState.putParcelableArrayList("fragments", list);
    }

    public int getFragmentContainerViewId(String tag) {
        Log.d("pppp", "getFragmentContainerViewId:" + tag + " fragmentStates:" + fragmentStates);
        if (fragmentStates != null) {
            Log.d("pppp", "getFragmentContainerViewId:" + tag);
            for (FragmentState state : fragmentStates) {
                Log.d("pppp", "getFragmentContainerViewId:" + tag + "  ??:" + state.getTag());
                if (tag.equals(state.getTag())) {
                    Log.d("pppp", "getFragmentContainerViewId:" + tag + " state.getContainerViewId():" + state.getContainerViewId());
                    return state.getContainerViewId();
                }
            }
        }
        return View.NO_ID;
    }


    public static class FragmentState implements Parcelable {
        private int containerViewId;
        private String tag;

        public FragmentState(int containerViewId, String tag) {
            this.containerViewId = containerViewId;
            this.tag = tag;
        }

        public String getTag() {
            return tag;
        }

        public int getContainerViewId() {
            return containerViewId;
        }

        protected FragmentState(Parcel in) {
            containerViewId = in.readInt();
            tag = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(containerViewId);
            dest.writeString(tag);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<FragmentState> CREATOR = new Creator<FragmentState>() {
            @Override
            public FragmentState createFromParcel(Parcel in) {
                return new FragmentState(in);
            }

            @Override
            public FragmentState[] newArray(int size) {
                return new FragmentState[size];
            }
        };
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

    private List<ItemBinder<ITEM_DATA>> toItemData(@NonNull List<ITEM_DATA> addList, boolean refresh) {
        HashMap<ITEM_DATA, ItemBinder<ITEM_DATA>> removeItemData = null;
        if (refresh) {
            removeItemData = data_Providers;
            data_Providers = new HashMap<>();
        }
        List<ItemBinder<ITEM_DATA>> providers = new ArrayList<>(addList.size());
        for (ITEM_DATA object : addList) {
            ItemBinder itemBinder;
            if (refresh) {
                itemBinder = removeItemData.remove(object);
            } else {
                itemBinder = data_Providers.get(object);
            }
            if (itemBinder == null) {
                if (object instanceof ItemBinder) {
                    itemBinder = (ItemBinder) object;
                } else {
                    itemBinder = factory.buildItemData(this, object);
                }
            }
            data_Providers.put(object, itemBinder);
            providers.add(itemBinder);
            providerIndex.put(itemBinder.providerType, itemBinder.provider);
        }

        if (refresh) {
            Log.d("cccc", "toItemData refresh size:" + removeItemData.size());
            //处理Fragment的移除
            doRemoveItemData(removeItemData.values());
        }
        return providers;
    }

    protected void doRemoveItemData(Collection<ItemBinder<ITEM_DATA>> removeItemBinder) {
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
                    FragmentManager fragmentManager = fragment.getFragmentManager();
                    if (fragmentManager != null) {
                        transaction = fragmentManager.beginTransaction();
                    }
                }
                if (transaction != null) {
                    transaction.remove(fragment);
                }
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

    /**
     * 清除掉由该Adapter生成的Fragment
     * 放在这个位置
     *
     * @Override protected void onSaveInstanceState(Bundle outState) {
     * multiTypeAdapter.clearAdapterFragments(getSupportFragmentManager());
     * super.onSaveInstanceState(outState);
     * }
     * 因为FragmentManager有个保存机制，会在这个方法保存存在的Fragment，如果你Fragment想要重新创建，不用之前的那么就可以在这里清理掉.
     * MultiTypeStateAdapter 的adapter建议不用清理，毕竟希望复用使用之前的Fragment以及里面的数据
     */
    public void clearAdapterFragments() {
        FragmentManager fragmentManager = factory.getFragmentManager();
        if (fragmentManager == null) {
            return;
        }
        try {
            List<Fragment> fragments = fragmentManager.getFragments();
            String end = "#" + getHashCode();
            if (fragments != null) {
                for (Fragment f : fragments) {
                    if (f != null) {
                        String tag = f.getTag();
                        if (!TextUtils.isEmpty(tag) && tag.endsWith(end)) {
                            f.setUserVisibleHint(false);
                            f.setMenuVisibility(false);
                        }
                    }
                }
            }

//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            String end = "#" + getHashCode();
//            List<Fragment> fragments = fragmentManager.getFragments();
//            if (fragments != null) {
//                for (Fragment f : fragments) {
//                    String tag = f.getTag();
//                    if (!TextUtils.isEmpty(tag) && tag.endsWith(end)) {
//                        f.setUserVisibleHint(false);
//                        f.setMenuVisibility(false);
//                    }
//                }
////                transaction.commitNowAllowingStateLoss();
////                Iterator<Fragment> iterator = fragments.iterator();
////                while (iterator.hasNext()) {
////                    if (iterator.next() == null) {
////                        iterator.remove();
////                    }
////                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}