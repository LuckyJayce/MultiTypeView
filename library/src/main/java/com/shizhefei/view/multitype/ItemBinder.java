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

/**
 * Item绑定着 = 数据 + ItemViewProvider + providerType
 * 包含Data(数据)，ItemViewProvider（ViewHolder并绑定data到ViewHolder），ProviderType(ViewHolder复用的type)
 *
 * @param <DATA>
 */
public class ItemBinder<DATA> {
    /**
     * 数据
     */
    DATA data;
    /**
     * 对应ViewHolder的type，以便于ViewHolder根据type进行复用
     */
    int providerType;
    /**
     * 用于创建ViewHolder和绑定数据到ViewHolder上
     */
    ItemViewProvider<DATA> provider;

    ItemBinder(DATA data, ItemViewProvider<DATA> provider, int providerType) {
        this.data = data;
        this.providerType = providerType;
        this.provider = provider;
    }

    public int getProviderType() {
        return providerType;
    }

    public DATA getData() {
        return data;
    }
}