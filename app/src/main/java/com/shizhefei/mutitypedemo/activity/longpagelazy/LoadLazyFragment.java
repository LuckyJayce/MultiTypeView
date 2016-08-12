package com.shizhefei.mutitypedemo.activity.longpagelazy;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.shizhefei.fragment.LazyFragment;
import com.shizhefei.mutitypedemo.R;


public class LoadLazyFragment extends LazyFragment {
    private View progressBar;
    private String i;
    private TextView textView;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        Log.d("dddd", "onCreateView :" + getClass().getSimpleName() + "  " + savedInstanceState);
        setContentView(R.layout.test);
        i = getArguments().getString("1");
        int color = getArguments().getInt("color");
        getContentView().setBackgroundColor(color);
        progressBar = findViewById(R.id.progressBar);
        textView = (TextView) findViewById(R.id.textView);
        textView.setText(i);
        textView.append("  3秒后加载完毕");
        textView.postDelayed(new Runnable() {
            @Override
            public void run() {
                textView.setText(i);
                textView.append("  2秒后加载完毕");
            }
        }, 1000);
        textView.postDelayed(new Runnable() {
            @Override
            public void run() {
                textView.setText(i);
                textView.append("  1秒后加载完毕");
            }
        }, 2000);
        textView.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                textView.setText(i);
                textView.append("  加载完毕");
            }
        }, 3000);
    }
}
