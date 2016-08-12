package com.shizhefei.mutitypedemo.activity.longpage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shizhefei.mutitypedemo.R;
import com.shizhefei.mutitypedemo.type.Good;
import com.shizhefei.view.multitype.ItemBinderFactory;
import com.shizhefei.view.multitype.MultiTypeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LuckyJayce on 2016/8/8.
 */
public class ShopFragment extends Fragment {
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("dddd", "onCreateView :" + getClass().getSimpleName() + "  " + savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        //特别注意，recyclerView嵌套recyclerView，里面一层的recyclerView的view是不能被回收的，而且是一次性创建。
        //所以里面的recyclerView 适用于少量的item，大量的item还是建议放在外面的recyclerView去实现
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(new MultiTypeAdapter<Object>(load(), new ItemBinderFactory()));

//        FragmentStatePagerAdapter

        return view;
    }

    private List<Good> load() {
        List<Good> foods = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            foods.add(new Good());
        }
        return foods;
    }
}
