package com.shizhefei.view.multitype.data;

import android.os.Parcelable;

/**
 * Created by LuckyJayce on 2016/8/11.
 */
public interface IParcelableData<DATA> extends Parcelable {
    DATA getData();
}
