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
package com.shizhefei.view.multitype.provider;

import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

import com.shizhefei.view.multitype.MultiTypeView;

/**
 * 分配View的id
 * Created by LuckyJayce on 2016/8/8.
 */
class ViewUtils {

    private static int id = 2345;

    public static int madeId() {
        return id++;
    }

    public static ViewGroup.LayoutParams getRightLayoutParams(ViewGroup parent, int layoutWidth, int layoutHeight) {
        ViewGroup.LayoutParams layoutParams = null;
        RecyclerView.LayoutManager layoutManager = null;
        if (parent instanceof MultiTypeView) {
            MultiTypeView multiTypeView = (MultiTypeView) parent;
            layoutManager = multiTypeView.getLayoutManager();
        }
        if (parent instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) parent;
            layoutManager = recyclerView.getLayoutManager();
        }
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            if (linearLayoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            } else {
                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager gridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            if (gridLayoutManager.getOrientation() == StaggeredGridLayoutManager.VERTICAL) {
                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            } else {
                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }
        }
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        return layoutParams;
    }


    private static int fragmentId = -1;

    /**
     * 既是fragment的view的id又是fragment的providerType
     *
     * @return
     */
    public static int madeFragmentContainerViewIdAndProviderType() {
        if (fragmentId == -1) {
            //确保fragmentId的唯一性

            //这里用了SystemClock.uptimeMillis()手机开机运行的毫秒数，而不用 System.currentTimeMillis(),是因为SystemClock.uptimeMillis()不会受到日期时间更改影响
            //因为时间是long型，所以通过对Integer.MAX_VALUE / 100取余数，范围就在 0到Integer.MAX_VALUE / 100之间.
            //如果要和下一个时间取余数的time重合两种可能，
            // 1. 开机时间又经过了Integer.MAX_VALUE / 100（超过一天）
            // 2.手机又重新开机（那fragment的恢复机制根本不需要）
            int time = (int) (SystemClock.uptimeMillis() % (Integer.MAX_VALUE / 100));
            //这里加了个startOffset是避免，这里的ProviderType和之前view的重复
            int startOffset = Integer.MAX_VALUE / 1000;
            int duran = 10;
            fragmentId = time * duran + startOffset;
        }
        return fragmentId--;
    }

    public static final int UNSET_LAYOUT_SIZE = -1000;
}
