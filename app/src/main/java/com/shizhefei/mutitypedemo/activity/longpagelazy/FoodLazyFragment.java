package com.shizhefei.mutitypedemo.activity.longpagelazy;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.shizhefei.fragment.LazyFragment;
import com.shizhefei.mutitypedemo.R;
import com.shizhefei.mutitypedemo.type.Food;
import com.shizhefei.view.multitype.ItemBinderFactory;
import com.shizhefei.view.multitype.MultiTypeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LuckyJayce on 2016/8/8.
 */
public class FoodLazyFragment extends LazyFragment {
    private RecyclerView recyclerView;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_food);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        recyclerView.setAdapter(new MultiTypeAdapter<Object>(load(), new ItemBinderFactory()));
        Log.d("dddd", "onCreateView :" + getClass().getSimpleName() + "  " + savedInstanceState);
    }

    private List<Food> load() {
        List<Food> foods = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            foods.add(new Food());
        }
        return foods;
    }
}
