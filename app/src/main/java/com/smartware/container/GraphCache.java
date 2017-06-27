package com.smartware.container;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Juho.S on 2017-05-22.
 */
public class GraphCache implements Parcelable {
    public final static String GRAPH_CACHE = "GraphCache";

    private int[] mainGraphValues = {       // 첫 화면 그래프의 임시값
            100,                             // 나의 계획 준수율
            95,                             // 평균 계획 준수율
            92,                             // 나의 과제 점수
            98};                            // 평균 과제 점수

    public int[] getMainGraphValues() {
        return mainGraphValues;
    }

    public void setMainGraphValues(int[] mainGraphValues) {
        this.mainGraphValues = mainGraphValues;
    }

    private int[] mngGraphValues;

    public int[] getMngGraphValues() {
        return mngGraphValues;
    }

    public void setMngGraphValues(int[] mngGraphValues) {
        this.mngGraphValues = mngGraphValues;
    }

    //테스트용 임시 데이터
    private int[] detailGraphValues_1;
    private int[] detailGraphValues_2;
    private int[] detailGraphValues_3;
    private int[] detailGraphValues_4;
    private int[] detailGraphValues_5;
    private String[] monthSet;

    private ArrayList<int[]> graphDetailsData;

    public GraphCache() {   // 임시데이터
        mngGraphValues = new int[]{        // 성적 관리 그래프 임시값
                80,                             // 공부 시스템 점수
                83,                             // 평균 공시 점수
                65,                             // 과제 수행율
                80,                             // 평균 과제 수행율
                65,                             // 과제 점수
                88,                             // 평균 과제 점수
                75,                             // 계획 준수율
                92,                             // 평균 계획 준수율
                85,                             // 출석율
                95};                            // 평균 출석율
        detailGraphValues_1 = new int[]{80, 50, 60, 40, 70, 100, 90, 55, 65, 85};
        detailGraphValues_2 = new int[]{80, 50, 60, 40, 70, 100, 90, 55, 65, 85};
        detailGraphValues_3 = new int[]{80, 50, 60, 40, 70, 100, 90, 55, 65, 85};
        detailGraphValues_4 = new int[]{80, 50, 60, 40, 70, 100, 90, 55, 65, 85};
        detailGraphValues_5 = new int[]{80, 50, 60, 40, 70, 100, 90, 55, 65, 85};
        monthSet = new String[]{"2017.01", "2017.02", "2017.03", "2017.04", "2017.05"};
        graphDetailsData = new ArrayList<>();
        graphDetailsData.add(detailGraphValues_1);
        graphDetailsData.add(detailGraphValues_2);
        graphDetailsData.add(detailGraphValues_3);
        graphDetailsData.add(detailGraphValues_4);
        graphDetailsData.add(detailGraphValues_5);
    }

    public GraphCache(int[] mainGraphValues, int[] mngGraphValues, int[] detailGraphValues_1,
                      int[] detailGraphValues_2, int[] detailGraphValues_3, int[] detailGraphValues_4,
                      int[] detailGraphValues_5, String[] monthSet, ArrayList<int[]> graphDetailsData) {
        this.mainGraphValues = mainGraphValues;
        this.mngGraphValues = mngGraphValues;
        this.detailGraphValues_1 = detailGraphValues_1;
        this.detailGraphValues_2 = detailGraphValues_2;
        this.detailGraphValues_3 = detailGraphValues_3;
        this.detailGraphValues_4 = detailGraphValues_4;
        this.detailGraphValues_5 = detailGraphValues_5;
        this.monthSet = monthSet;
        this.graphDetailsData = graphDetailsData;
    }

    public GraphCache(Parcel in) {
        mainGraphValues = in.createIntArray();
        mngGraphValues = in.createIntArray();
        detailGraphValues_1 = in.createIntArray();
        detailGraphValues_2 = in.createIntArray();

        detailGraphValues_3 = in.createIntArray();
        detailGraphValues_4 = in.createIntArray();
        detailGraphValues_5 = in.createIntArray();
        monthSet = in.createStringArray();
    }

    public static final Creator<GraphCache> CREATOR = new Creator<GraphCache>() {
        @Override
        public GraphCache createFromParcel(Parcel in) {
            return new GraphCache(in);
        }

        @Override
        public GraphCache[] newArray(int size) {
            return new GraphCache[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeIntArray(mainGraphValues);
        parcel.writeIntArray(mngGraphValues);
        parcel.writeIntArray(detailGraphValues_1);
        parcel.writeIntArray(detailGraphValues_2);
        parcel.writeIntArray(detailGraphValues_3);
        parcel.writeIntArray(detailGraphValues_4);
        parcel.writeIntArray(detailGraphValues_5);
        parcel.writeStringArray(monthSet);
    }

    public int[] getDetailGraphValues_1() {
        return detailGraphValues_1;
    }

    public void setDetailGraphValues_1(int[] detailGraphValues_1) {
        this.detailGraphValues_1 = detailGraphValues_1;
    }

    public int[] getDetailGraphValues_2() {
        return detailGraphValues_2;
    }

    public void setDetailGraphValues_2(int[] detailGraphValues_2) {
        this.detailGraphValues_2 = detailGraphValues_2;
    }

    public int[] getDetailGraphValues_3() {
        return detailGraphValues_3;
    }

    public void setDetailGraphValues_3(int[] detailGraphValues_3) {
        this.detailGraphValues_3 = detailGraphValues_3;
    }

    public int[] getDetailGraphValues_4() {
        return detailGraphValues_4;
    }

    public void setDetailGraphValues_4(int[] detailGraphValues_4) {
        this.detailGraphValues_4 = detailGraphValues_4;
    }

    public int[] getDetailGraphValues_5() {
        return detailGraphValues_5;
    }

    public void setDetailGraphValues_5(int[] detailGraphValues_5) {
        this.detailGraphValues_5 = detailGraphValues_5;
    }

    public String[] getMonthSet() {
        return monthSet;
    }

    public void setMonthSet(String[] monthSet) {
        this.monthSet = monthSet;
    }

    public ArrayList<int[]> getGraphDetailsData() {
        return graphDetailsData;
    }

    public void setGraphDetailsData(ArrayList<int[]> graphDetailsData) {
        this.graphDetailsData = graphDetailsData;
    }
}
