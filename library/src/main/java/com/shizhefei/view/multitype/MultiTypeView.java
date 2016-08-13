/*
Copyright 2016 shizhefei（LuckyJayce）https://github.com/LuckyJayce

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.shizhefei.view.multitype;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by LuckyJayce on 2016/8/11.
 */
public class MultiTypeView extends RecyclerView {

    private MultiTypeAdapter<?> typeAdapter;

    public MultiTypeView(Context context) {
        super(context);
        init(context);
    }

    public MultiTypeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MultiTypeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter instanceof MultiTypeAdapter) {
            setAdapter((MultiTypeAdapter) adapter);
        } else {
            super.setAdapter(adapter);
        }
    }

    public void setAdapter(MultiTypeAdapter<?> adapter) {
        this.typeAdapter = adapter;
        super.setAdapter(typeAdapter);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle stateBundle = new Bundle();
        if (typeAdapter != null) {
            typeAdapter.onSaveInstanceState(this, stateBundle);
        }
        stateBundle.putParcelable("superParcelable", super.onSaveInstanceState());
        return stateBundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle stateBundle = (Bundle) state;
            if (typeAdapter != null) {
                typeAdapter.onRestoreInstanceState(this, stateBundle);
            }
            super.onRestoreInstanceState(stateBundle.getParcelable("superParcelable"));
        } else {
            super.onRestoreInstanceState(state);
        }
    }
}
