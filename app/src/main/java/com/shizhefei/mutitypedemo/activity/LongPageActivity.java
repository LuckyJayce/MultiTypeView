package com.shizhefei.mutitypedemo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shizhefei.mutitypedemo.R;
import com.shizhefei.mutitypedemo.activity.longpagelazy.AirlineTicketLazyFragment;
import com.shizhefei.mutitypedemo.activity.longpagelazy.CultureLazyFragment;
import com.shizhefei.mutitypedemo.activity.longpagelazy.EditLazyFragment;
import com.shizhefei.mutitypedemo.activity.longpagelazy.FoodLazyFragment;
import com.shizhefei.mutitypedemo.activity.longpagelazy.HotelLazyFragment;
import com.shizhefei.mutitypedemo.activity.longpagelazy.InfoLazyFragment;
import com.shizhefei.mutitypedemo.activity.longpagelazy.RecommendLazyFragment;
import com.shizhefei.mutitypedemo.activity.longpagelazy.ShopLazyFragment;
import com.shizhefei.mutitypedemo.type.ImageItem;
import com.shizhefei.mutitypedemo.type.RichItem;
import com.shizhefei.mutitypedemo.util.DisplayUtils;
import com.shizhefei.view.multitype.ItemBinderFactory;
import com.shizhefei.view.multitype.MultiTypeAdapter;
import com.shizhefei.view.multitype.MultiTypeView;
import com.shizhefei.view.multitype.provider.FragmentData;

import java.util.ArrayList;
import java.util.List;

public class LongPageActivity extends AppCompatActivity {
    private MultiTypeAdapter<Object> multiTypeAdapter;
    private int page;
    private View refreshButton;
    private View loadMoreButton;
    private View insertAndRemoveButton;
    private View boomButton;
    private MultiTypeView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long_page);

        refreshButton = findViewById(R.id.refresh);
        loadMoreButton = findViewById(R.id.loadMore);
        insertAndRemoveButton = findViewById(R.id.insertAndRemove);
        boomButton = findViewById(R.id.boom);
        recyclerView = (MultiTypeView) findViewById(R.id.recyclerView);

        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //带有FragmentManager的构造函数，默认帮您添加支持Fragment数据的FragmentHolderProvider
        //也就是说你可以把Fragment放在adapter上使用，这里的Fragment只有第一次滑动到对应位置才会onCreateView的方法
        ItemBinderFactory factory = new ItemBinderFactory(getSupportFragmentManager());
        multiTypeAdapter = new MultiTypeAdapter<>(loadData(0), factory);

        recyclerView.setAdapter(multiTypeAdapter);


        refreshButton.setOnClickListener(onClickListener);
        loadMoreButton.setOnClickListener(onClickListener);
        insertAndRemoveButton.setOnClickListener(onClickListener);
        boomButton.setOnClickListener(onClickListener);
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
            } else if (v == insertAndRemoveButton) {
                multiTypeAdapter.getData().remove(3);
                multiTypeAdapter.notifyItemRemoved(3);
                multiTypeAdapter.getData().add(1, new RichItem("LLLLL", R.drawable.food_1));
                multiTypeAdapter.notifyItemInserted(1);
            } else if (v == boomButton) {
                startActivity(new Intent(getApplicationContext(), BoomActivity.class));
            }
        }
    };

    private List<Object> loadData(int page) {
        List<Object> data = new ArrayList<>();
        TextView textView = new TextView(this);
        textView.setText("第" + page + "页");
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundColor(Color.GRAY);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtils.dipToPix(this, 100)));
        data.add(textView);
        for (int i = 0; i < 2; i++) {
            data.add("HHHHH");
            data.add(new RichItem("LLLLL", R.drawable.food_1));
            data.add(new ImageItem(R.mipmap.ic_launcher));
        }

        data.add(new FragmentData(InfoLazyFragment.class, "InfoLazyFragment" + page));
        data.add(new FragmentData(EditLazyFragment.class, "EditLazyFragment" + page));
        data.add(new FragmentData(HotelLazyFragment.class, "HotelLazyFragment" + page));
        data.add(new FragmentData(AirlineTicketLazyFragment.class, "AirlineTicketLazyFragment" + page));
        data.add(new FragmentData(ShopLazyFragment.class, "ShopLazyFragment" + page));
        data.add(new FragmentData(RecommendLazyFragment.class, "RecommendLazyFragment" + page));
        data.add(new FragmentData(FoodLazyFragment.class, "FoodLazyFragment" + page));
        data.add(new FragmentData(CultureLazyFragment.class, "CultureLazyFragment" + page));

        this.page = page;
        return data;
    }
}
