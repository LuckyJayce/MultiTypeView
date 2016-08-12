package com.shizhefei.mutitypedemo.activity.longpagelazy;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.shizhefei.fragment.LazyFragment;
import com.shizhefei.mutitypedemo.R;
import com.shizhefei.mutitypedemo.type.Good;
import com.shizhefei.view.multitype.ItemBinderFactory;
import com.shizhefei.view.multitype.MultiTypeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LuckyJayce on 2016/8/8.
 */
public class ShopLazyFragment extends LazyFragment {
    private RecyclerView recyclerView;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);

        Log.d("dddd", "onCreateView :" + getClass().getSimpleName() + "  " + savedInstanceState);

        setContentView(R.layout.fragment_shop);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //特别注意，recyclerView嵌套recyclerView，里面一层的recyclerView的view是不能被回收的，而且是一次性创建。
        //所以里面的recyclerView 适用于少量的item，大量的item还是建议放在外面的recyclerView去实现
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(new MultiTypeAdapter<Object>(load(), new ItemBinderFactory()));
    }

    private List<Good> load() {
        List<Good> foods = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            foods.add(new Good());
        }
        return foods;
    }
}
