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

/**
 * Created by LuckyJayce on 2016/8/8.
 */
public class HotelFragment extends Fragment {
    private View addButton;
    private View minusButton;
    private TextView numberTextView;
    private int number = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hotel, container, false);
        addButton = view.findViewById(R.id.add_button);
        minusButton = view.findViewById(R.id.minus_button);
        numberTextView = (TextView) view.findViewById(R.id.number_textView);

        addButton.setOnClickListener(onClickListener);
        minusButton.setOnClickListener(onClickListener);

        numberTextView.setText(String.valueOf(number));
        Log.d("dddd", "onCreateView :" + getClass().getSimpleName() + "  " + savedInstanceState);
        return view;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == addButton) {
                number++;
                numberTextView.setText(String.valueOf(number));
            } else if (v == minusButton) {
                if (number > 0) {
                    number--;
                    numberTextView.setText(String.valueOf(number));
                }
            }
        }
    };
}


