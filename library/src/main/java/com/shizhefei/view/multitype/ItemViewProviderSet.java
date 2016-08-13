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
package com.shizhefei.view.multitype;

import java.util.Arrays;
import java.util.List;

/**
 * DATA数据类型的imteView提供者集合
 * Created by LuckyJayce on 2016/8/7.
 */
public abstract class ItemViewProviderSet<DATA> {

    protected List<ItemViewProvider<DATA>> itemProviders;
    private int providerType;

    public ItemViewProviderSet(ItemViewProvider<DATA> provider1) {
        this(Arrays.asList(provider1));
    }

    public ItemViewProviderSet(ItemViewProvider<DATA> provider1, ItemViewProvider<DATA> provider2) {
        this(Arrays.asList(provider1, provider2));
    }

    public ItemViewProviderSet(ItemViewProvider<DATA> provider1, ItemViewProvider<DATA> provider2, ItemViewProvider<DATA> provider3) {
        this(Arrays.asList(provider1, provider2, provider3));
    }

    public ItemViewProviderSet(ItemViewProvider<DATA> provider1, ItemViewProvider<DATA> provider2, ItemViewProvider<DATA> provider3, ItemViewProvider<DATA> provider4) {
        this(Arrays.asList(provider1, provider2, provider3, provider4));
    }

    public ItemViewProviderSet(ItemViewProvider<DATA>[] providers) {
        this(Arrays.asList(providers));
    }

    public ItemViewProviderSet(List<ItemViewProvider<DATA>> itemProviders) {
        this.itemProviders = itemProviders;
    }

    public void setProviderType(int providerType) {
        this.providerType = providerType;
    }

    public int getProviderType(int index) {
        return providerType + index;
    }

    public ItemViewProvider<DATA> getItemProvider(int index) {
        return itemProviders.get(index);
    }

    public int size() {
        return itemProviders.size();
    }

    protected abstract int selectIndex(DATA data);

    public final int select(DATA data) {
        int index = selectIndex(data);
        if (index < 0 || index > itemProviders.size() - 1) {
            throw new RuntimeException("selectIndex 的方法越界 selectIndex:" + index + " size:" + itemProviders.size());
        }
        return index;
    }
}
