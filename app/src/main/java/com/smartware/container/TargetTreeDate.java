package com.smartware.container;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.annotation.Target;

/**
 * Created by Juho.S on 2017-06-26.
 */
public class TargetTreeDate implements Parcelable{

    public final static int SCORE_DATE = 10;
    public final static int TEACHER_CHECK = 11;
    public final static int PARENT_CHECK = 12;

    private int date;
    private int score;
    private int type;
    private int weeks;


    public TargetTreeDate(){
        this.date = 0;
        this.score = -1;
        this.type = 0;
        this.weeks = 0;
    }

    public TargetTreeDate(int date, int score, int type, int weeks) {
        this.date = date;
        this.score = score;
        this.type = type;
        this.weeks = weeks;
    }

    protected TargetTreeDate(Parcel in) {
        date = in.readInt();
        score = in.readInt();
        type = in.readInt();
        weeks = in.readInt();
    }

    public static final Creator<TargetTreeDate> CREATOR = new Creator<TargetTreeDate>() {
        @Override
        public TargetTreeDate createFromParcel(Parcel in) {
            return new TargetTreeDate(in);
        }

        @Override
        public TargetTreeDate[] newArray(int size) {
            return new TargetTreeDate[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(date);
        parcel.writeInt(score);
        parcel.writeInt(type);
        parcel.writeInt(weeks);
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getWeeks() {
        return weeks;
    }

    public void setWeeks(int weeks) {
        this.weeks = weeks;
    }
}
