package com.shizhefei.mutitypedemo.activity.longpagelazy;

import android.os.Bundle;
import android.util.Log;

import com.shizhefei.fragment.LazyFragment;
import com.shizhefei.mutitypedemo.R;

/**
 * Created by LuckyJayce on 2016/8/8.
 */
public class InfoLazyFragment extends LazyFragment {
    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_info);
        Log.d("dddd", "onCreateView :" + getClass().getSimpleName() + "  " + savedInstanceState);
    }
}
