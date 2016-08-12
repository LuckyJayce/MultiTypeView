package com.shizhefei.mutitypedemo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shizhefei.view.multitype.ItemBinderFactory;
import com.shizhefei.view.multitype.MultiTypeAdapter;
import com.shizhefei.view.multitype.MultiTypeView;
import com.shizhefei.view.multitype.data.FragmentData;
import com.shizhefei.mutitypedemo.R;
import com.shizhefei.mutitypedemo.activity.longpage.AirlineTicketFragment;
import com.shizhefei.mutitypedemo.activity.longpage.FoodFragment;
import com.shizhefei.mutitypedemo.activity.longpage.HotelFragment;
import com.shizhefei.mutitypedemo.activity.longpage.InfoFragment;
import com.shizhefei.mutitypedemo.activity.longpage.RecommendFragment;
import com.shizhefei.mutitypedemo.activity.longpagelazy.EditLazyFragment;
import com.shizhefei.mutitypedemo.type.ImageItem;
import com.shizhefei.mutitypedemo.type.RichItem;
import com.shizhefei.mutitypedemo.util.DisplayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LongPageActivity extends AppCompatActivity {
    private MultiTypeAdapter multiTypeAdapter;
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
        multiTypeAdapter = new MultiTypeAdapter(factory);

        Log.d("pppp", "LongPageActivity savedInstanceState:" + savedInstanceState);

        if (savedInstanceState != null) {
            print(savedInstanceState);
        }

        if (savedInstanceState != null)
            multiTypeAdapter.onRestoreInstanceState(recyclerView, savedInstanceState);

        multiTypeAdapter.notifyDataChanged(loadData(0), true);

        recyclerView.setAdapter(multiTypeAdapter);


        refreshButton.setOnClickListener(onClickListener);
        loadMoreButton.setOnClickListener(onClickListener);
        insertAndRemoveButton.setOnClickListener(onClickListener);
        boomButton.setOnClickListener(onClickListener);

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment f : fragments) {
                Log.d("cccc", "Activity onCreate方法 :" + f);
            }
        }
    }

    private void print(Bundle bundle) {
        Set<String> keySet = bundle.keySet();
        for (String key : keySet) {
            Log.d("pppp", "key:" + key + " " + bundle.get(key) + " savedInstanceState.get(key):" + bundle.get(key).getClass());
            Object o = bundle.get(key);
            if (o instanceof Bundle) {
                print((Bundle) o);
            } else if (o instanceof SparseArray) {
                print((SparseArray) o);
            }
        }
    }

    private void print(SparseArray sparseArray) {
        int count = sparseArray.size();
        for (int i = 0; i < count; i++) {
            Log.d("pppp", "SparseArray key:" + sparseArray.keyAt(i) + " " + sparseArray.valueAt(i));
            Object o = sparseArray.valueAt(i);
            if (o instanceof Bundle) {
                print((Bundle) o);
            } else if (o instanceof SparseArray) {
                print((SparseArray) o);
            }
        }
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
//        data.add(new FragmentData(InfoLazyFragment.class, "InfoFragment" + page));
//        data.add(new FragmentData(HotelLazyFragment.class, "HotelFragment" + page));
//        data.add(new FragmentData(AirlineTicketLazyFragment.class, "AirlineTicketFragment" + page));
//        data.add(new FragmentData(RecommendLazyFragment.class, "RecommendFragment" + page));
//        data.add(new FragmentData(FoodLazyFragment.class, "FoodFragment" + page));

        data.add(new FragmentData(EditLazyFragment.class, "EditFragment" + page));
        data.add(new FragmentData(InfoFragment.class, "InfoFragment" + page));
        data.add(new FragmentData(HotelFragment.class, "HotelFragment" + page));
        data.add(new FragmentData(AirlineTicketFragment.class, "AirlineTicketFragment" + page));
        data.add(new FragmentData(RecommendFragment.class, "RecommendFragment" + page));
        data.add(new FragmentData(FoodFragment.class, "FoodFragment" + page));

        this.page = page;
        return data;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        multiTypeAdapter.onSaveInstanceState(recyclerView, outState);
    }
}
