package com.smartware.container;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Juho.S on 2017-06-21.
 */
public class LeaderBoard implements Parcelable{


    private int myPoint;
    private int myPointRank;
    private int maxPlayer;

    private ArrayList<String> rankUserNames;
    private ArrayList<String> rankUserPoints;
    private ArrayList<String> rankHistory;
    private ArrayList<String> rankHistoryPeriod;

    public LeaderBoard(){

        rankUserNames = new ArrayList<>();
        rankUserPoints = new ArrayList<>();
        rankHistory = new ArrayList<>();
        rankHistoryPeriod = new ArrayList<>();
    }

    public LeaderBoard(int myPoint, int myPointRank, int maxPlayer, ArrayList<String> rankUserNames, ArrayList<String> rankUserPoints, ArrayList<String> rankHistory, ArrayList<String> rankHistoryPeriod) {
        this.myPoint = myPoint;
        this.myPointRank = myPointRank;
        this.maxPlayer = maxPlayer;
        this.rankUserNames = rankUserNames;
        this.rankUserPoints = rankUserPoints;
        this.rankHistory = rankHistory;
        this.rankHistoryPeriod = rankHistoryPeriod;
    }

    protected LeaderBoard(Parcel in) {
        myPoint = in.readInt();
        myPointRank = in.readInt();
        maxPlayer = in.readInt();
        rankUserNames = in.createStringArrayList();
        rankUserPoints = in.createStringArrayList();
        rankHistory = in.createStringArrayList();
        rankHistoryPeriod = in.createStringArrayList();
    }

    public static final Creator<LeaderBoard> CREATOR = new Creator<LeaderBoard>() {
        @Override
        public LeaderBoard createFromParcel(Parcel in) {
            return new LeaderBoard(in);
        }

        @Override
        public LeaderBoard[] newArray(int size) {
            return new LeaderBoard[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(myPoint);
        parcel.writeInt(myPointRank);
        parcel.writeInt(maxPlayer);
        parcel.writeStringList(rankUserNames);
        parcel.writeStringList(rankUserPoints);
        parcel.writeStringList(rankHistory);
        parcel.writeStringList(rankHistoryPeriod);
    }

    public int getmyPoint() {
        return myPoint;
    }

    public void setmyPoint(int myPoint) {
        this.myPoint = myPoint;
    }

    public int getmyPointRank() {
        return myPointRank;
    }

    public void setmyPointRank(int myPointRank) {
        this.myPointRank = myPointRank;
    }

    public ArrayList<String> getRankUserNames() {
        return rankUserNames;
    }

    public void setRankUserNames(ArrayList<String> rankUserNames) {
        this.rankUserNames = rankUserNames;
    }

    public ArrayList<String> getRankUserPoints() {
        return rankUserPoints;
    }

    public void setRankUserPoints(ArrayList<String> rankUserPoints) {
        this.rankUserPoints = rankUserPoints;
    }

    public ArrayList<String> getRankHistory() {
        return rankHistory;
    }

    public void setRankHistory(ArrayList<String> rankHistory) {
        this.rankHistory = rankHistory;
    }

    public ArrayList<String> getRankHistoryPeriod() {
        return rankHistoryPeriod;
    }

    public void setRankHistoryPeriod(ArrayList<String> rankHistoryPeriod) {
        this.rankHistoryPeriod = rankHistoryPeriod;
    }

    public int getMaxPlayer() {
        return maxPlayer;
    }

    public void setMaxPlayer(int maxPlayer) {
        this.maxPlayer = maxPlayer;
    }
}
