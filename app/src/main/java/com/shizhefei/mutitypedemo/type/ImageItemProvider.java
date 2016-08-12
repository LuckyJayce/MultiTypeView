package com.shizhefei.mutitypedemo.type;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shizhefei.view.multitype.ItemViewProvider;
import com.shizhefei.mutitypedemo.R;

public class ImageItemProvider extends ItemViewProvider<ImageItem> {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent,int providerType) {
        View root = inflater.inflate(R.layout.item_image, parent, false);
        return new ItemViewHolder(root);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, ImageItem data) {
        ItemViewHolder holder = (ItemViewHolder) viewHolder;
        holder.imageView.setImageResource(data.resId);
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
        }
    }

}
