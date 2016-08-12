package com.shizhefei.view.multitype;

import android.support.v4.app.FragmentManager;
import android.util.SparseArray;
import android.view.View;

import com.shizhefei.view.multitype.provider.FragmentData;
import com.shizhefei.view.multitype.data.IParcelableData;
import com.shizhefei.view.multitype.provider.FragmentDataProvider;
import com.shizhefei.view.multitype.provider.ViewProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * ItemData工厂
 * ItemDataFactory通过注册的data对应的ItemViewProvider生成ItemData
 * ItemBinder 包含Data(数据)，ItemViewProvider（ViewHolder并绑定data到ViewHolder），ProviderType(ViewHolder复用的type)
 * Created by LuckyJayce on 2016/8/5.
 */
public class ItemBinderFactory {

    //静态注册的providerType从1000开始分配
    private static final ItemBinderFactory INSTANCE = new ItemBinderFactory(1000);

    static {
        registerStaticProvider(View.class, new ViewProvider());
    }

    private FragmentDataProvider fragmentDataProvider;

    private FragmentManager fragmentManager;

    private Map<Class<?>, ItemViewProviderSet> providerSets = new HashMap<>();
    private SparseArray<ItemViewProvider> providerIndex = new SparseArray<>();
    //对应ViewHolder的type，以便于ViewHolder根据type进行复用
    private int providerType = 0;

    private ItemBinderFactory(int providerTypeStart) {
        providerType = providerTypeStart;
    }

    public ItemBinderFactory() {

    }

    /**
     * 带有FragmentManager的构造函数，默认帮您添加支持Fragment数据的FragmentHolderProvider
     *
     * @param fragmentManager
     */
    public ItemBinderFactory(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        registerProvider(FragmentData.class, fragmentDataProvider = new FragmentDataProvider(fragmentManager));
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    FragmentDataProvider getFragmentDataProvider() {
        return fragmentDataProvider;
    }

    /**
     * 根据type获取对应的ItemViewProvider
     *
     * @param providerType Provider的type
     * @return 对应的ItemViewProvider
     */
    public ItemViewProvider getProvider(int providerType) {
        ItemViewProvider provider = providerIndex.get(providerType);
        if (provider == null) {//如果没有，则通过静态实例去根据type去获取
            if (this != INSTANCE) {
                provider = INSTANCE.getProvider(providerType);
            }
        }
        return provider;
    }

    /**
     * 根据数据类型获取对应的Provider
     *
     * @param data 数据
     * @return Provider
     */
    public ItemViewProvider getProvider(Object data) {
        PT vt = getPT(data);
        return vt == null ? null : getPT(data).provider;
    }

    /**
     * 是否有对应数据的Provider
     *
     * @param data 数据
     * @return 是否有对应数据的Provider
     */
    public boolean hasProvider(Object data) {
        PT vt = getPT(data);
        return vt != null;
    }

    private Class<?> getDataClass(Object data) {
        if (data instanceof IParcelableData) {
            IParcelableData parcelableImp = (IParcelableData) data;
            return parcelableImp.getData().getClass();
        }
        return data.getClass();
    }

    /**
     * 获取数据对应的provider和Type
     *
     * @param data 数据
     * @return 包含provider和Type的PT
     */
    private PT getPT(Object data) {
        Class<?> dataClass = getDataClass(data);
        //根据数据的class从注册的providerSets获取对应的ItemViewProviderSet
        ItemViewProviderSet itemProviderSet = providerSets.get(dataClass);

        if (itemProviderSet != null) {

            //从ItemViewProviderSet中挑选出合适的provider，并返回出去
            int index = itemProviderSet.select(data);
            int providerType = itemProviderSet.getProviderType(index);
            ItemViewProvider provider = itemProviderSet.getItemProvider(index);
            return vtInstance.set(provider, providerType);

        } else {//如果class的没有找到，那么就通过继承的Class找对应itemProviderSet

            for (Map.Entry<Class<?>, ItemViewProviderSet> entry : providerSets.entrySet()) {
                Class<?> registerDataClass = entry.getKey();

                //data.getClass是否是registerDataClass的子类
                if (registerDataClass.isAssignableFrom(dataClass)) {
                    //是的话就拿子类的itemProviderSet从中挑选出合适的provider，并返回出去
                    itemProviderSet = entry.getValue();
                    int index = itemProviderSet.select(data);
                    int providerType = itemProviderSet.getProviderType(index);
                    ItemViewProvider provider = itemProviderSet.getItemProvider(index);
                    return vtInstance.set(provider, providerType);
                }
            }
        }
        //如果还没有找到，没有就从静态注册的providerSets找
        if (this != INSTANCE) {
            return INSTANCE.getPT(data);
        }
        return null;
    }

    /**
     * 注册Provider
     *
     * @param dataClass 数据的class
     * @param provider  provider
     * @param <DATA>    数据泛型
     */
    public <DATA> void registerProvider(Class<DATA> dataClass, ItemViewProvider<DATA> provider) {
        registerProvider(dataClass, new ItemViewProviderSet<DATA>(provider) {
            @Override
            protected int selectIndex(DATA data) {
                return 0;
            }
        });
    }

    /**
     * 注册多个Provider<br/>
     * 有时候需要根据TextItem里面的某个字段，生成不同的布局.比如聊天界面的message是一样的，但是有区分左右布局
     *
     * @param dataClass       数据的class
     * @param itemProviderSet 包含多个provider的set
     * @param <DATA>          数据泛型
     */
    public <DATA> void registerProvider(Class<DATA> dataClass, ItemViewProviderSet<DATA> itemProviderSet) {
        providerSets.put(dataClass, itemProviderSet);
        itemProviderSet.setProviderType(providerType);
        for (int i = 0; i < itemProviderSet.size(); i++) {
            providerIndex.put(providerType, itemProviderSet.getItemProvider(i));
            providerType++;
        }
    }

    /**
     * 全局注册Provider
     * 每个new ItemBinderFactory()实例共享这些注册的Provider
     *
     * @param dataClass 数据class
     * @param provider  provider
     * @param <DATA>    数据泛型
     */
    public static <DATA> void registerStaticProvider(Class<DATA> dataClass, ItemViewProvider<DATA> provider) {
        INSTANCE.registerProvider(dataClass, provider);
    }

    /**
     * 创建ItemData，ItemData用于adapter构造显示的界面
     * 里面包含data，provider，providerType
     *
     * @param data   数据
     * @param <DATA> 数据泛型
     * @return ItemBinder
     */
    <DATA> ItemBinder<DATA> buildItemData(DATA data) {
        PT pt = getPT(data);
        if (pt == null) {
            throw new RuntimeException("没有注册" + getDataClass(data) + " 对应的ItemProvider");
        }
        int type = pt.providerType;
        //每个Fragment都是唯一的type
        if (pt.provider.isUniqueProviderType(data)) {
            type = providerType;
            providerType++;
        }
        return new ItemBinder<>(data, pt.provider, type);
    }

//    public <DATA> ItemBinder<DATA> buildItemData(SerializableData data, ItemViewProvider<DATA> provider) {
//        return buildItemData((DATA) data.getData(), provider);
//    }
//
//    /**
//     * 创建ItemData，ItemData用于adapter构造显示的界面
//     * 里面包含data，provider，providerType
//     *
//     * @param data     数据
//     * @param provider provider
//     * @param <DATA>   数据泛型
//     * @return ItemBinder
//     */
//    public <DATA> ItemBinder<DATA> buildItemData(DATA data, ItemViewProvider<DATA> provider) {
//        int type;
//        //每个Fragment都是唯一的type
//        if (provider.isUniqueProviderType(data)) {
//            type = providerType;
//            providerType++;
//        } else {
//            int index = providerIndex.indexOfValue(provider);
//            if (index < 0) {
//                if (this != INSTANCE && (INSTANCE.hasProvider(provider))) {
//                    type = INSTANCE.getProviderType(provider);
//                } else {
//                    type = providerType;
//                    providerType++;
//                }
//            } else {
//                type = providerIndex.keyAt(index);
//            }
//        }
//        return new ItemBinder<>(data, provider, type);
//    }

    private boolean hasProvider(ItemViewProvider provider) {
        return providerIndex.indexOfValue(provider) > 0;
    }

    private int getProviderType(ItemViewProvider provider) {
        int index = providerIndex.indexOfValue(provider);
        return providerIndex.keyAt(index);
    }

    private PT vtInstance = new PT();

    private static class PT {
        ItemViewProvider provider;
        int providerType;

        public PT() {
        }

        public PT set(ItemViewProvider provider, int providerType) {
            this.provider = provider;
            this.providerType = providerType;
            return this;
        }
    }
}
