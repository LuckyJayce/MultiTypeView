package com.shizhefei.mutitypedemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.shizhefei.view.multitype.ItemBinderFactory;
import com.shizhefei.view.multitype.MultiTypeStateAdapter;
import com.shizhefei.view.multitype.MultiTypeView;
import com.shizhefei.view.multitype.data.FragmentData;
import com.shizhefei.view.multitype.data.SerializableData;
import com.shizhefei.mutitypedemo.R;
import com.shizhefei.mutitypedemo.activity.longpage.AirlineTicketFragment;
import com.shizhefei.mutitypedemo.activity.longpage.CultureFragment;
import com.shizhefei.mutitypedemo.activity.longpage.EditFragment;
import com.shizhefei.mutitypedemo.activity.longpage.FoodFragment;
import com.shizhefei.mutitypedemo.activity.longpage.HotelFragment;
import com.shizhefei.mutitypedemo.activity.longpage.InfoFragment;
import com.shizhefei.mutitypedemo.activity.longpage.RecommendFragment;
import com.shizhefei.mutitypedemo.activity.longpage.ShopFragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 这里演示带有恢复状态的MultiTypeStateAdapter，onSaveInstanceState保存状态，onCreate(Bundle savedInstanceState)恢复状态<br/>
 * Fragment的恢复机制可以查看 EditLazyFragment<br/>
 */
public class LongPageStateActivity extends AppCompatActivity {
    private MultiTypeStateAdapter multiTypeStateAdapter;
    private int page;
    private View refreshButton;
    private View loadMoreButton;
    private View insertAndRemoveButton;
    private View boomButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long_page);

        refreshButton = findViewById(R.id.refresh);
        loadMoreButton = findViewById(R.id.loadMore);
        insertAndRemoveButton = findViewById(R.id.insertAndRemove);
        boomButton = findViewById(R.id.boom);
        MultiTypeView recyclerView = (MultiTypeView) findViewById(R.id.recyclerView);

//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //带有FragmentManager的构造函数，默认帮您添加支持Fragment数据的FragmentHolderProvider
        //也就是说你可以把Fragment放在adapter上使用，这里的Fragment只有第一次滑动到对应位置才会onCreateView的方法
        ItemBinderFactory factory = new ItemBinderFactory(getSupportFragmentManager());
        multiTypeStateAdapter = new MultiTypeStateAdapter(factory);
//        if (savedInstanceState != null) {//之前有保存的数据
//            Toast.makeText(this, "恢复中", Toast.LENGTH_SHORT).show();
//            page = savedInstanceState.getInt("page");
//            multiTypeStateAdapter.restoreState(savedInstanceState);
//        } else {
//            List<Parcelable> data = loadData(0);
//            multiTypeStateAdapter.notifyDataChanged(data, true);
//        }

        if (savedInstanceState == null) {
            multiTypeStateAdapter = new MultiTypeStateAdapter(loadData(0), factory);
        } else {
            page = savedInstanceState.getInt("page");
            multiTypeStateAdapter = new MultiTypeStateAdapter(factory);
        }

        recyclerView.setAdapter(multiTypeStateAdapter);

        refreshButton.setOnClickListener(onClickListener);
        loadMoreButton.setOnClickListener(onClickListener);
        insertAndRemoveButton.setOnClickListener(onClickListener);
        boomButton.setOnClickListener(onClickListener);
        insertAndRemoveButton.setVisibility(View.GONE);

        Log.d("cccc", "LongPageStateActivity onCreate:");
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            Iterator<Fragment> iterator = fragments.iterator();
            while (iterator.hasNext()) {
                Fragment f = iterator.next();
                Log.d("cccc", "LongPageStateActivity onCreate:" + f);
            }
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == refreshButton) {
                List<Parcelable> data = loadData(0);
                multiTypeStateAdapter.notifyDataChanged(data, true);
            } else if (v == loadMoreButton) {
                List<Parcelable> data = loadData(page + 1);
                multiTypeStateAdapter.notifyDataChanged(data, false);
            } else if (v == boomButton) {
                startActivity(new Intent(getApplicationContext(), BoomActivity.class));
            }
        }
    };

    private List<Parcelable> loadData(int page) {
        List<Parcelable> data = new ArrayList<>();

        data.add(new FragmentData(EditFragment.class, "EditFragment" + page));

        //这里演示，可以添加任何的Serializable，以及保存数据，并且自动对应到数据类型的ItemViewProvider
        // 基本类型都是Serializable类型
        data.add(SerializableData.valueOf("HHHHH_1"));
        //data.add(SerializableData.valueOf(1));//没有注册int的ItemViewProvider，就不添加了演示了
        //Serializable数据列表转化为SerializableData的列表
        List<String> texts = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            texts.add(String.valueOf(i));
        }
        data.addAll(SerializableData.valueOfList(texts));

        data.add(new FragmentData(ShopFragment.class, "ShopFragment" + page));

        //FragmentData 注意tag要唯一，用于查找对应的Fragment和恢复Fragment.
        data.add(new FragmentData(InfoFragment.class, "InfoFragment" + page));
        data.add(new FragmentData(AirlineTicketFragment.class, "AirlineTicketFragment" + page));
        data.add(new FragmentData(HotelFragment.class, "HotelFragment" + page));
        data.add(new FragmentData(RecommendFragment.class, "RecommendFragment" + page));
        data.add(new FragmentData(FoodFragment.class, "FoodFragment" + page));


        FragmentData fragmentData = new FragmentData(CultureFragment.class, "CultureFragment" + page);
        //可以用这种方式获取已经创建的Fragment，不过这里获取到的是null，因为还没创建
        //那么什么时候创建？在RecyclerView滑到对应的Fragment位置才会创建.
        //你也可以通过multiTypeStateAdapter.getData().get(position) 获取对应的FragmentData，在通过FragmentData获取对应的Fragment.
        Fragment fragment = fragmentData.getFragment();
        //为什么要用FragmentData的方式添加，因为它实现了Parcelable可以作为数据保存下来
        data.add(fragmentData);

        this.page = page;
        return data;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("page", page);
//        multiTypeStateAdapter.saveState(outState);
    }

}
