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
package com.shizhefei.view.multitype.data;

import android.os.Parcel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LuckyJayce on 2016/8/10.
 */
public class SerializableData implements IParcelableData<Serializable> {
    
    private Serializable data;

    private SerializableData(Serializable data) {
        this.data = data;
    }

    public static boolean equals(SerializableData serializableData, Serializable data) {
        return serializableData.data.equals(data);
    }

    public static Object getData(Object data) {
        if (data instanceof SerializableData) {
            data = ((SerializableData) data).getData();
        }
        return data;
    }

    public static SerializableData valueOf(Serializable data) {
        return new SerializableData(data);
    }

    public static List<SerializableData> valueOfList(List<? extends Serializable> data) {
        ArrayList<SerializableData> list = new ArrayList<>(data.size());
        for (Serializable s : data) {
            list.add(valueOf(s));
        }
        return list;
    }

    @Override
    public String toString() {
        return new StringBuilder("Serializable dataClass:").append(data.getClass()).append(" data:" + data).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SerializableData that = (SerializableData) o;
        return data.equals(that.data);
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }

    public Serializable getData() {
        return data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.data);
    }

    protected SerializableData(Parcel in) {
        this.data = (Serializable) in.readSerializable();
    }

    public static final Creator<SerializableData> CREATOR = new Creator<SerializableData>() {
        @Override
        public SerializableData createFromParcel(Parcel source) {
            return new SerializableData(source);
        }

        @Override
        public SerializableData[] newArray(int size) {
            return new SerializableData[size];
        }
    };
}
