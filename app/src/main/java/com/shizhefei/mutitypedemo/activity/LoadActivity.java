package com.shizhefei.mutitypedemo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shizhefei.view.multitype.ItemBinderFactory;
import com.shizhefei.view.multitype.MultiTypeAdapter;
import com.shizhefei.mutitypedemo.R;
import com.shizhefei.mutitypedemo.activity.longpage.LoadFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LuckyJayce on 2016/8/9.
 */
public class LoadActivity extends AppCompatActivity {

    private int page;
    private View refreshButton;
    private View loadMoreButton;
    private View insertAndRemoveButton;
    private View boomButton;
    private MultiTypeAdapter multiTypeAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long_page);

        refreshButton = findViewById(R.id.refresh);
        loadMoreButton = findViewById(R.id.loadMore);
        insertAndRemoveButton = findViewById(R.id.insertAndRemove);
        boomButton = findViewById(R.id.boom);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //带有FragmentManager的构造函数，默认帮您添加支持Fragment数据的FragmentHolderProvider
        //也就是说你可以把Fragment放在adapter上使用，这里的Fragment只有第一次滑动到对应位置才会onCreateView的方法
        ItemBinderFactory factory = new ItemBinderFactory(getSupportFragmentManager());
        recyclerView.setAdapter(multiTypeAdapter = new MultiTypeAdapter(loadData(0), factory));

        refreshButton.setOnClickListener(onClickListener);
        loadMoreButton.setOnClickListener(onClickListener);
        insertAndRemoveButton.setOnClickListener(onClickListener);
        boomButton.setOnClickListener(onClickListener);
        insertAndRemoveButton.setVisibility(View.GONE);
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == refreshButton) {
                List<Object> data = loadData(0);
                multiTypeAdapter.notifyDataChanged(data, true);
            } else if (v == loadMoreButton) {
                List<Object> data = loadData(page + 1);
                multiTypeAdapter.notifyDataChanged(data, false);
            } else if (v == boomButton) {
                startActivity(new Intent(getApplicationContext(), BoomActivity.class));
            }
        }
    };

    private List<Object> loadData(int page) {
        List<Object> data = new ArrayList<>();
        for (int i = 0; i < colors.length; i++) {
            Fragment f = new LoadFragment();
            Bundle loadData = new Bundle();
            loadData.putInt("color", colors[i]);
            loadData.putString("1", String.valueOf(page) + "#" + i);
            f.setArguments(loadData);
            data.add(f);
        }
        this.page = page;
        return data;
    }

    private int[] colors = {Color.parseColor("#F44336"), Color.parseColor("#E91E63"), Color.parseColor("#3F51B5"),
            Color.parseColor("#2196F3"), Color.parseColor("#4CAF50"), Color.parseColor("#CDDC39"),
            Color.parseColor("#FFEB3B"), Color.parseColor("#FF9800"), Color.parseColor("#FF5722"),
    };
}
