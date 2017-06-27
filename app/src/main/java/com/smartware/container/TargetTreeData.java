package com.smartware.container;

import android.os.Parcel;
import android.os.Parcelable;

import com.smartware.common.Utils;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Juho.S on 2017-06-07.
 */
public class TargetTreeData implements Parcelable {

    private final static boolean DEBUG = Utils.DEBUG;
    public final static String TAG = "TargetTreeData";

    private int avMyMonthScore = 0;
    private String startedDate = "";
    private String target = "";
    private String goalSeq = "";

    //private int[] monthData = new int[31];
    private ArrayList<TargetTreeDate> monthData;

    //private int[] checkData = new int[12];
    private ArrayList<TargetTreeDate> checkData;

    /**
     * 테스트를 위한 생성자
     */
    public TargetTreeData(){
        monthData = new ArrayList<>();
        checkData = new ArrayList<>();
    }


    protected TargetTreeData(Parcel in) {
        avMyMonthScore = in.readInt();
        startedDate = in.readString();
        goalSeq = in.readString();
        target = in.readString();
        monthData = in.createTypedArrayList(TargetTreeDate.CREATOR);
        checkData = in.createTypedArrayList(TargetTreeDate.CREATOR);
    }

    public static final Creator<TargetTreeData> CREATOR = new Creator<TargetTreeData>() {
        @Override
        public TargetTreeData createFromParcel(Parcel in) {
            return new TargetTreeData(in);
        }

        @Override
        public TargetTreeData[] newArray(int size) {
            return new TargetTreeData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(avMyMonthScore);
        parcel.writeString(startedDate);
        parcel.writeString(target);
        parcel.writeString(goalSeq);
        parcel.writeTypedList(monthData);
        parcel.writeTypedList(checkData);
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int getAvMyMonthScore() {
        return avMyMonthScore;
    }

    public void setAvMyMonthScore(int avMyMonthScore) {
        this.avMyMonthScore = avMyMonthScore;
    }

    public String getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(String startedDate) {
        this.startedDate = startedDate;
    }

    public ArrayList<TargetTreeDate> getMonthData() {
        return monthData;
    }

    public void setMonthData(ArrayList<TargetTreeDate> monthData) {
        this.monthData = monthData;
    }
    public void setMonthDataIdx(int idx, int value) {
        this.monthData.get(idx).setScore(value);
    }

    public ArrayList<TargetTreeDate> getCheckData() {
        return checkData;
    }

    public void setCheckData(ArrayList<TargetTreeDate> checkData) {
        this.checkData = checkData;
    }
    public void setCheckDataIdx(int idx, int value) {
        this.checkData.get(idx).setScore(value);
    }

    public String getGoalSeq() {
        return goalSeq;
    }

    public void setGoalSeq(String goalSeq) {
        this.goalSeq = goalSeq;
    }
    /*    //test code
    public void setTestData() {
        final int[] score = {0, 50, -1, 60, 70, 80, 90, 100};
        final int[] check = {0, 1};

        for (int i = 0; i < monthData.length; i++) {
            monthData[i] = score[(int) (Math.random() * score.length)];
        }
        for (int i = 0; i < checkData.length; i++) {
            checkData[i] = check[(int) (Math.random() * check.length)];
        }
        Utils.getInstance().printLog(DEBUG, TAG, "[setTestData] monthData = " + monthData.toString());
        Utils.getInstance().printLog(DEBUG, TAG, "[setTestData] checkData = " + checkData.toString());
    }*/
}
