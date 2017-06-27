package com.smartware.container;

/**
 * Created by userpc on 2017-05-09.
 */
public class UserData {
    private static UserData instance;

    private String UserID;
    private String userPW;
    private int todaysScore = 0;

    private UserData() {
    }

    public static UserData getInstance() {
        if (instance == null) {
            instance = new UserData();
        }
        return instance;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUserPW() {
        return userPW;
    }

    public void setUserPW(String userPW) {
        this.userPW = userPW;
    }

    public int getTodaysScore() {
        return todaysScore;
    }

    public void setTodaysScore(int todaysScore) {
        this.todaysScore = todaysScore;
    }
}
