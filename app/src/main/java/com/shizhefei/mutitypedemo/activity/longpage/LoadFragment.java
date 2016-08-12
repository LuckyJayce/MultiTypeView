package com.shizhefei.mutitypedemo.activity.longpage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shizhefei.mutitypedemo.R;


public class LoadFragment extends Fragment {
    private View progressBar;
    private String i;
    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("dddd", "onCreateView :" + getClass().getSimpleName() + "  " + savedInstanceState);
        i = getArguments().getString("1");
        int color = getArguments().getInt("color");
        View view = inflater.inflate(R.layout.test, container, false);
        view.setBackgroundColor(color);
        progressBar = view.findViewById(R.id.progressBar);
        textView = (TextView) view.findViewById(R.id.textView);
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
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
