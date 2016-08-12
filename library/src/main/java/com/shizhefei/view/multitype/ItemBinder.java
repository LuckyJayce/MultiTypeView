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