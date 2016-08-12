package com.shizhefei.mutitypedemo;

import android.app.Application;

import com.shizhefei.view.multitype.ItemBinderFactory;
import com.shizhefei.mutitypedemo.type.Food;
import com.shizhefei.mutitypedemo.type.FoodProvider;
import com.shizhefei.mutitypedemo.type.Good;
import com.shizhefei.mutitypedemo.type.GoodProvider;
import com.shizhefei.mutitypedemo.type.ImageItem;
import com.shizhefei.mutitypedemo.type.ImageItemProvider;
import com.shizhefei.mutitypedemo.type.RichItem;
import com.shizhefei.mutitypedemo.type.RichItemProvider;
import com.shizhefei.mutitypedemo.type.StringProvider;
import com.squareup.leakcanary.LeakCanary;

public class ExampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);

        //这些代码可以放在应用初始化的时候，静态注册Provider
        //每个new ItemBinderFactory()实例共享这些注册的Provider
        ItemBinderFactory.registerStaticProvider(ImageItem.class, new ImageItemProvider());
        ItemBinderFactory.registerStaticProvider(RichItem.class, new RichItemProvider());
        ItemBinderFactory.registerStaticProvider(Food.class, new FoodProvider());
        ItemBinderFactory.registerStaticProvider(Good.class, new GoodProvider());
        ItemBinderFactory.registerStaticProvider(String.class, new StringProvider());
    }
}