package com.smartware.common;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by Juho.S on 2017-05-17.
 */
public class Utils {

    public static final boolean		DEBUG = true;
    private static final String				TAG = "Utils";
    public final static String BUNDLE = "Bundle data";

    public static final long				MILLIS_OF_DAY = 24 * 60 * 60 * 1000;

    private final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    private final Pattern 						PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[0-9])(?=.*[a-z])(?=[\\S]+$).{6,12}$"
    );


    /**
     * Instance of this class
     * **/

    private static Toast sToast;

    private static Utils ourInstance = new Utils();

    public static Utils getInstance() {
        return ourInstance;
    }

    private Utils() {
    }

    public void printLog(boolean bPrint, String tag, String msg) {
        if (DEBUG && bPrint) {
            Log.d(tag, msg);
        }
    }

    //수평 그래프 그리는 함수
    public void setGraph(View graphBar, int score, int maxGraphSize) {
        ViewGroup.LayoutParams layoutParams = graphBar.getLayoutParams();
        //printLog(DEBUG, TAG, "[setGraph] score = " + score);
        if (score > 100) {
            layoutParams.width = maxGraphSize;
        } else {
            layoutParams.width = (int) (maxGraphSize * (score / 100f));
        }
        //printLog(DEBUG, TAG, "[setGraph] width = " + layoutParams.width);
        graphBar.setLayoutParams(layoutParams);
    }

    //키보드 숨기는 함수
    public void hideSoftKeyboard(Context context, View view) {
        InputMethodManager mgr = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    //키보드 보여주는 함수
    public void showSoftKeyboard(Context context, View view) {
        InputMethodManager mgr = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.showSoftInput(view,  InputMethodManager.SHOW_FORCED);
    }

    public String getDateStringFromMillis(long time, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        return formatter.format(time);
    }

    public long getMillisFromDateString(String strDate, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        try {
            return formatter.parse(strDate).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }

    public int getDayOfMonthFromMillis(long millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public int getMonthFromMillis(long millis) {
        Calendar		cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        return cal.get(Calendar.MONTH);
    }

    public String getDayOfWeekFromMillis(long millis) {
        Calendar		cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        return getDateStringFromMillis(millis, "E");
    }

    public int getHourFromMillis(long millis) {
        Calendar		cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        return cal.get(Calendar.HOUR);
    }

    public int getMinuteFromMillis(long millis) {
        Calendar		cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        return cal.get(Calendar.MINUTE);
    }

    public boolean isExistedFile(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public void makeDir(String path) {
        File			filePath = new File(path);
        if (!filePath.exists()) {
            if (!filePath.mkdirs()) {
                return;
            }
        }
    }
    public boolean getPackageList(Context context, String packageName) {
        printLog(DEBUG, TAG, "[getPackageList]");
        PackageManager pkgMgr = context.getPackageManager();
        List<ResolveInfo> mApps;
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mApps = pkgMgr.queryIntentActivities(mainIntent, 0);
        try {
            for (int i = 0; i < mApps.size(); i++) {
                if (mApps.get(i).activityInfo.packageName.startsWith(packageName)) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public boolean isValidPassword(String pwd) {
        printLog(DEBUG, TAG, "[isValidPassword] pwd : " + pwd);
        return PASSWORD_PATTERN.matcher(pwd).matches();
    }
    public boolean isValidEmailAddress(String emailAddress) {
        printLog(DEBUG, TAG, "[isValidEmailAddress] emailAddress : " + emailAddress);
        return EMAIL_ADDRESS_PATTERN.matcher(emailAddress).matches();
    }

    public boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

    public String getRefinedNumber(String raw) {
        String refined = "";
        for (int i = 0; i < raw.length(); i++) {
            char	c = raw.charAt(i);
            if (isDigit(c)) {
                refined += c;
            }
        }
        return refined;
    }


}
