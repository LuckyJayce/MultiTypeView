package com.shizhefei.mutitypedemo.type;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shizhefei.view.multitype.ItemViewProvider;
import com.shizhefei.mutitypedemo.R;

/**
 * Created by LuckyJayce on 2016/8/8.
 */
public class MessageLeftProvider extends ItemViewProvider<Message> {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int providerType) {
        return new ItemViewHolder(inflater.inflate(R.layout.item_message_left, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, Message message) {
        ItemViewHolder vh = (ItemViewHolder) viewHolder;
        vh.textView.setText(message.text);
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_message_textView);
        }
    }
}
