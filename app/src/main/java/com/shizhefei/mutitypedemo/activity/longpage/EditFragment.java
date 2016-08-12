package com.shizhefei.mutitypedemo.activity.longpage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.shizhefei.mutitypedemo.R;
import com.shizhefei.recyclerview.HFAdapter;
import com.shizhefei.recyclerview.HFRecyclerAdapter;
import com.shizhefei.view.multitype.ItemBinderFactory;
import com.shizhefei.view.multitype.MultiTypeAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 这里演示Fragment的状态恢复机制，TextView和EditTextView都是实现了View的onSaveInstanceState和onRestoreInstanceState的方法
 * 所以它会自动帮助你恢复，不用你手动恢复界面，其他的View实现这两个方法也同理
 * 可以参考：http://ju.outofmemory.cn/entry/169700
 * Fragment主要保存逻辑上的数据,onSaveInstanceState去保存,onActivityCreated做恢复动作
 * Created by LuckyJayce on 2016/8/9.
 */
public class EditFragment extends Fragment {
    private EditText userNameEditText;
    private EditText passwordEditText;
    private TextView textView;
    private RecyclerView recyclerView;
    private MultiTypeAdapter multiTypeAdapter;
    private List<Object> datas;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            datas = (List<Object>) savedInstanceState.getSerializable("data");
            Log.d("cccc", getClass().getSimpleName()+" 读取:" + savedInstanceState);
        }

        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        userNameEditText = (EditText) view.findViewById(R.id.editText);
        passwordEditText = (EditText) view.findViewById(R.id.editText2);
        textView = (TextView) view.findViewById(R.id.textView4);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        //特别注意，recyclerView嵌套recyclerView，里面一层的recyclerView的view是不能被回收的，而且是一次性创建。
        //所以里面的recyclerView 适用于少量的item，大量的item还是建议放在外面的recyclerView去实现
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));

        Log.d("dddd", "onCreateView :" + getClass().getSimpleName() + "  " + savedInstanceState);

        userNameEditText.addTextChangedListener(textWatcher);
        if (datas == null) {
            datas = load();
        }
        multiTypeAdapter = new MultiTypeAdapter(datas, new ItemBinderFactory());
        HFRecyclerAdapter adapter = new HFRecyclerAdapter(multiTypeAdapter);
        adapter.setOnItemClickListener(onItemClickListener);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("cccc", "EditLazyFragment 保存:" + this);
        List<Object> datas = multiTypeAdapter.getData();
        outState.putSerializable("data", (Serializable) datas);
    }

    private HFAdapter.OnItemClickListener onItemClickListener = new HFAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(HFAdapter adapter, RecyclerView.ViewHolder vh, int position) {
            if (position >= 0) {
                multiTypeAdapter.getData().remove(position);
                multiTypeAdapter.notifyItemRemoved(position);
            }
        }
    };

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            textView.setText(s.toString());
        }
    };

    private List<Object> load() {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            list.add(String.valueOf(i));
        }
        return list;
    }


}
