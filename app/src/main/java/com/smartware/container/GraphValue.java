package com.smartware.container;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Juho.S on 2017-06-27.
 */
public class GraphValue implements Parcelable{

    public GraphValue() {
    }

    public String gubun;
    public int percent;
    public String date;
    public int all_av;

    protected GraphValue(Parcel in) {
        gubun = in.readString();
        percent = in.readInt();
        date = in.readString();
        all_av = in.readInt();
    }

    public static final Creator<GraphValue> CREATOR = new Creator<GraphValue>() {
        @Override
        public GraphValue createFromParcel(Parcel in) {
            return new GraphValue(in);
        }

        @Override
        public GraphValue[] newArray(int size) {
            return new GraphValue[size];
        }
    };

    public String getGubun() {
        return gubun;
    }

    public void setGubun(String gubun) {
        this.gubun = gubun;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAll_av() {
        return all_av;
    }

    public void setAll_av(int all_av) {
        this.all_av = all_av;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(gubun);
        parcel.writeInt(percent);
        parcel.writeString(date);
        parcel.writeInt(all_av);
    }
}
