package com.smartware.manager;


import android.content.Context;
import android.content.SharedPreferences;

import com.smartware.common.Utils;
import com.smartware.container.GraphCache;
import com.smartware.gucongc.R;

import java.io.File;

/**
 * Created by Juho.S on 2017-05-16.
 */
public class CM {

    private static final boolean DEBUG = false;
    private static final String TAG = "CM";

    public static final float FONT_SIZE_DAY_OF_MONTH_SELECTED = 20.0f;
    public static final float FONT_SIZE_DAY_OF_MONTH_NOT_SELECTED = 14.0f;

    public static final String			TEST_QUESTION_BASE_URL = "http://badau.net/EBS/testview";

    public static final String APP_ROOT_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "smartware" + File.separator + "gugongc";

    public static final String CACHE_IMG_FILE_PATH = APP_ROOT_PATH + File.separator + ".cache";

    /**
     * Input Text Length
     * **/
    public static final int				MIN_LEN_USER_NAME = 2;
    public static final int				MAX_LEN_USER_NAME = 20;

    public static final int				MIN_LEN_USER_ID = 8;
    public static final int				MAX_LEN_USER_ID = 20;

    public static final int				MIN_LEN_USER_PW = 8;
    public static final int				MAX_LEN_USER_PW = 20;

    public static final int				MIN_LEN_PHONE_NUMBER = 10;	/** ex. 0110001111 (0111-000-1111) **/
    public static final int				MAX_LEN_PHONE_NUMBER = 11;	/** ex. 01012345678 (010-0000-1111) **/

    public static final int				MAX_LEN_USER_STATE = 30;
    public static final int				MAX_LEN_MY_INTRO = 200;

    public static final int				AUTH_CODE_LEN = 5;

    private static CM ourInstance = new CM();

    private Context mContext;

    public static CM getInstance() {
        return ourInstance;
    }

    private String userID = null;
    private String password = null;
    private String myGGCTarget = null;
    private String schoolName = null;
    private String schoolGrade = null;
    private int grade = 0;


    private int myGGCScore = 0;

    private CM() {
    }

    /**
     * LOGIN_STATUS
     * **/
    private static final String	LOGIN_STATUS = "login_status";

    public void setLoginStatus(boolean status) {
        Utils.getInstance().printLog(DEBUG, TAG, "[setLoginStatus] status : " + status);
        SharedPreferences pref = mContext.getSharedPreferences(mContext.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.putBoolean(LOGIN_STATUS, status);
        prefEditor.commit();
    }

    /**
     * USER_ID
     **/
    private static final String USER_ID = "user_id";

    public void setUserId(String userId) {
        Utils.getInstance().printLog(DEBUG, TAG, "[setUserId] userId : " + userId);
        userID = userId;
        SharedPreferences pref = mContext.getSharedPreferences(mContext.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.putString(USER_ID, userId);
        prefEditor.commit();
    }

    public String getUserId() {
        if (userID == null) {
            Utils.getInstance().printLog(DEBUG, TAG, "[getUserId]");
            SharedPreferences pref = mContext.getSharedPreferences(mContext.getString(R.string.app_name), Context.MODE_PRIVATE);
            return pref.getString(USER_ID, "");
        } else {
            return userID;
        }

    }

    /**
     * USER_PWD
     **/
    private static final String USER_PWD = "user_pwd";

    public void setUserPwd(String userPwd) {
        Utils.getInstance().printLog(DEBUG, TAG, "[setUserPwd] userPwd : " + userPwd);
        password = userPwd;
        SharedPreferences pref = mContext.getSharedPreferences(mContext.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.putString(USER_PWD, userPwd);
        prefEditor.commit();
    }

    public String getUserPwd() {
        if (password == null) {
            Utils.getInstance().printLog(DEBUG, TAG, "[getUserPwd]");
            SharedPreferences pref = mContext.getSharedPreferences(mContext.getString(R.string.app_name), Context.MODE_PRIVATE);
            return pref.getString(USER_PWD, "");
        } else {
            return password;
        }
    }

    /**
     * Device ID
     * **/
    public static final String					DEVICE_ID = "device_id";
    private String deviceID = "";

    public void setDeviceId(String deviceId) {
        Utils.getInstance().printLog(DEBUG, TAG, "[setDeviceId] device_id : " + deviceId);
        SharedPreferences pref = mContext.getSharedPreferences(mContext.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.putString(DEVICE_ID, deviceId);
        prefEditor.commit();
        return;
    }

    public String getDeviceId() {
        Utils.getInstance().printLog(DEBUG, TAG, "[getDeviceId]");
        SharedPreferences pref = mContext.getSharedPreferences(mContext.getString(R.string.app_name), Context.MODE_PRIVATE);
        return pref.getString(DEVICE_ID, "");
    }

    /**
     * WORKBOOK_OPENED_CHAPTER
     **/
    private static final String WORKBOOK_OPENED_CHAPTER = "workbook_opened_chapter";

    public void setWorkBookOpenedChapter(int chapter) {
        Utils.getInstance().printLog(DEBUG, TAG, "[setWorkBookOpenedChapter] color : " + chapter);
        SharedPreferences pref = mContext.getSharedPreferences(mContext.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.putInt(WORKBOOK_OPENED_CHAPTER, chapter);
        prefEditor.commit();
    }

    public int getWorkBookOpenedChapter() {
        Utils.getInstance().printLog(DEBUG, TAG, "[getWorkBookOpenedChapter]");
        SharedPreferences pref = mContext.getSharedPreferences(mContext.getString(R.string.app_name), Context.MODE_PRIVATE);
        return pref.getInt(WORKBOOK_OPENED_CHAPTER, 0);
    }

    public void setContext(Context context) {
        mContext = context;
    }


    public Context getContext() {
        return mContext;
    }

    private GraphCache graphCache;

    public GraphCache getGraphCache() {
        return graphCache;
    }

    public void setGraphCache(GraphCache graphCache) {
        this.graphCache = graphCache;
    }


    private static final String MY_GGC_TARGET = "my_ggc_target";

    public String getMyGGCTarget() {
        if (myGGCTarget == null) {
            Utils.getInstance().printLog(DEBUG, TAG, "[getMyGGCTarget]");
            SharedPreferences pref = mContext.getSharedPreferences(mContext.getString(R.string.app_name), Context.MODE_PRIVATE);
            return pref.getString(MY_GGC_TARGET, "");
        } else {
            return myGGCTarget;
        }
    }

    public void setMyGGCTarget(String myGGCTarget) {
        Utils.getInstance().printLog(DEBUG, TAG, "[setMyGGCTarget] myGGCTarget : " + myGGCTarget);
        this.myGGCTarget = myGGCTarget;
        SharedPreferences pref = mContext.getSharedPreferences(mContext.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.putString(MY_GGC_TARGET, myGGCTarget);
        prefEditor.commit();
    }


    private static final String MY_GGC_SCORE = "my_ggc_score";

    public int getMyGGCScore() {
        if (myGGCScore == 0) {
            Utils.getInstance().printLog(DEBUG, TAG, "[getMyGGCScore]");
            SharedPreferences pref = mContext.getSharedPreferences(mContext.getString(R.string.app_name), Context.MODE_PRIVATE);
            return pref.getInt(MY_GGC_SCORE, 0);
        } else {
            return myGGCScore;
        }

    }

    public void setMyGGCScore(int myGGCScore) {
        Utils.getInstance().printLog(DEBUG, TAG, "[setMyGGCScore] myGGCScore : " + myGGCScore);
        this.myGGCScore = myGGCScore;
        SharedPreferences pref = mContext.getSharedPreferences(mContext.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.putInt(MY_GGC_SCORE, myGGCScore);
        prefEditor.commit();
    }

    private static final String SCHOOL_NAME = "school_name";

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolGrade() {
        if (schoolGrade == null) {
            Utils.getInstance().printLog(DEBUG, TAG, "[getMyGGCScore]");
            SharedPreferences pref = mContext.getSharedPreferences(mContext.getString(R.string.app_name), Context.MODE_PRIVATE);
            return pref.getString(SCHOOL_NAME, "");
        } else {
            return schoolGrade;
        }

    }

    public void setSchoolGrade(String schoolGrade) {
        this.schoolGrade = schoolGrade;
        Utils.getInstance().printLog(DEBUG, TAG, "[setSchoolGrade] schoolGrade : " + schoolGrade);
        SharedPreferences pref = mContext.getSharedPreferences(mContext.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.putString(SCHOOL_NAME, schoolGrade);
        prefEditor.commit();
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    private static final String PARENT_PASSWORD = "parent_password";
    private String parentPassword ="";

    public String getParentPassword() {
        if (schoolGrade.length() > 0) {
            return schoolGrade;
        } else {
            Utils.getInstance().printLog(DEBUG, TAG, "[getParentPassword]");
            SharedPreferences pref = mContext.getSharedPreferences(mContext.getString(R.string.app_name), Context.MODE_PRIVATE);
            return pref.getString(PARENT_PASSWORD, "");
        }

    }

    public void setParentPassword(String parentPW) {
        this.parentPassword = parentPW;
        Utils.getInstance().printLog(DEBUG, TAG, "[setParentPassword] parentPassword : " + parentPassword);
        SharedPreferences pref = mContext.getSharedPreferences(mContext.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.putString(PARENT_PASSWORD, parentPassword);
        prefEditor.commit();
    }
}

