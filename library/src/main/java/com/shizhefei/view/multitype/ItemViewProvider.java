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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * DATA数据类型的imteView提供者<br/><br/>
 * 用于创建ViewHolder和绑定数据到ViewHolder上<br/>
 * 相当于提供了对应数据的Item界面<br/><br/>
 * 注意：千万不要把方法参数保存到字段中！！！！！
 * 注意：千万不要把方法参数保存到字段中！！！！！
 * 注意：千万不要把方法参数保存到字段中！！！！！
 * 重要的事情说三遍
 *
 * @param <DATA>
 */
public abstract class ItemViewProvider<DATA> {

    public abstract RecyclerView.ViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int providerType);

    public abstract void onBindViewHolder(RecyclerView.ViewHolder viewHolder, DATA data);

    public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {

    }

    public void onViewDetachedFromWindow(RecyclerView.ViewHolder viewHolder) {

    }

    public boolean isUniqueProviderType(DATA data) {
        return false;
    }

    public void onSaveInstanceState(Bundle savedInstanceState, DATA data) {

    }

    public void onRestoreInstanceState(Bundle savedInstanceState, DATA data) {

    }
}
