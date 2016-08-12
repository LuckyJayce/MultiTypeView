package com.shizhefei.mutitypedemo.type;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shizhefei.view.multitype.ItemViewProvider;
import com.shizhefei.mutitypedemo.R;

public class RichItemProvider extends ItemViewProvider<RichItem> {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int providerType) {
        View root = inflater.inflate(R.layout.item_rich, parent, false);
        return new ItemViewHolder(root);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, RichItem data) {
        ItemViewHolder holder = (ItemViewHolder) viewHolder;
        holder.text.setText(data.text);
        holder.image.setImageResource(data.imageResId);
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView text;
        private final ImageView image;

        public ItemViewHolder(View itemView) {
            super(itemView);
            this.text = (TextView) itemView.findViewById(R.id.text);
            this.image = (ImageView) itemView.findViewById(R.id.image);
        }
    }

}
