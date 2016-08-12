package com.shizhefei.mutitypedemo.activity.longpage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shizhefei.view.multitype.ItemBinderFactory;
import com.shizhefei.view.multitype.MultiTypeAdapter;
import com.shizhefei.mutitypedemo.R;
import com.shizhefei.mutitypedemo.type.Food;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LuckyJayce on 2016/8/8.
 */
public class FoodFragment extends Fragment {
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        recyclerView.setAdapter(new MultiTypeAdapter(load(), new ItemBinderFactory()));
        Log.d("dddd", "onCreateView :" + getClass().getSimpleName() + "  " + savedInstanceState);
        return view;
    }

    private List<Object> load() {
        List<Object> foods = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            foods.add(new Food());
        }
        return foods;
    }
}
