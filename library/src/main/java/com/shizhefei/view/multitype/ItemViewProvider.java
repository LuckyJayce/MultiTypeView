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
