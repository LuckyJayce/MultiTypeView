package com.shizhefei.mutitypedemo.activity.longpagelazy;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.shizhefei.fragment.LazyFragment;
import com.shizhefei.mutitypedemo.R;

/**
 * Created by LuckyJayce on 2016/8/8.
 */
public class HotelLazyFragment extends LazyFragment {
    private View addButton;
    private View minusButton;
    private TextView numberTextView;
    private int number = 1;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_hotel);
        addButton = findViewById(R.id.add_button);
        minusButton = findViewById(R.id.minus_button);
        numberTextView = (TextView) findViewById(R.id.number_textView);

        addButton.setOnClickListener(onClickListener);
        minusButton.setOnClickListener(onClickListener);

        numberTextView.setText(String.valueOf(number));
        Log.d("dddd", "onCreateView :" + getClass().getSimpleName() + "  " + savedInstanceState);
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


