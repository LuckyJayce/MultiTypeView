package com.shizhefei.mutitypedemo.activity.longpage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shizhefei.mutitypedemo.R;

/**
 * Created by LuckyJayce on 2016/8/8.
 */
public class AirlineTicketFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_airlineticket, container, false);
        Log.d("dddd", "onCreateView :" + getClass().getSimpleName() + "  " + savedInstanceState);
        return view;
    }
}
