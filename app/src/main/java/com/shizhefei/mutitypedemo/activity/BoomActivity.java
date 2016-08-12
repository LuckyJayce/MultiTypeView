package com.shizhefei.mutitypedemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by LuckyJayce on 2016/8/8.
 */
public class BoomActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setGravity(Gravity.CENTER);
        setContentView(linearLayout);

        Button button = new Button(this);
        linearLayout.addView(button);
        button.setText("点我崩溃");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                "".charAt(100);
            }
        });
    }
}
