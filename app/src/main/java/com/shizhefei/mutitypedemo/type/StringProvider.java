package com.shizhefei.mutitypedemo.type;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shizhefei.view.multitype.ItemViewProvider;
import com.shizhefei.mutitypedemo.R;

/**
 * Created by LuckyJayce on 2016/8/9.
 */
public class StringProvider extends ItemViewProvider<String> {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int providerType) {
        return new RecyclerView.ViewHolder(inflater.inflate(R.layout.item_string, parent, false)) {
        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, String s) {
        TextView textView = (TextView) viewHolder.itemView;
        textView.setText(s);
    }
}
