package com.smartware.gucongc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.smartware.container.GraphData;
import com.smartware.container.TargetTreeData;
import com.smartware.container.UserData;
import com.smartware.common.ImageDownloader;
import com.smartware.container.GraphCache;
import com.smartware.common.Utils;
import com.smartware.container.ScheduleItem;
import com.smartware.manager.CM;
import com.smartware.manager.DM;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Juho.S 2017-05-16.
 */
public class ActMain extends AppCompatActivity implements View.OnClickListener {

    private final static boolean DEBUG = Utils.DEBUG;
    private final static String TAG = "ActMain";
    private static final String EBS_SMARTCOACH_PACKAGE = "com.smartware.ebscoach.student";

    private Utils mUtils = Utils.getInstance();
    private DM mDM = DM.getInstance();
    private CM mCM = CM.getInstance();
    //private GraphCache mGraphCache;
    private GraphData mGraphData;
    private Bundle mBundle;
    private Intent mIntent;

    private boolean isExistPackage;

    private String[] nav_list;                   //좌측 네비게이션 리스트 목록
    private ActionBarDrawerToggle drawerToggle;   //좌측 네비게이션 드로어 토클


    private int mMaxGraphWidth;
    /*    private int mMyPlanScore;
        private int mAllAvScore;
        private int mMyScore;
        private int mAvScore;*/
    //private int[] mScore = {0, 0, 0, 0};   //초기화

    /**
     * WUT : Week Update Type
     **/
    private static final int WUT_TODAY = 0;
    private static final int WUT_PREV = 1;
    private static final int WUT_NEXT = 2;

    private static final int MIN_DISTANCE = 100;

    private static final int[] RES_ID_TEXT_DAYs = {
            R.id.text_sunday,
            R.id.text_monday,
            R.id.text_tuesday,
            R.id.text_wendesday,
            R.id.text_thursday,
            R.id.text_friday,
            R.id.text_satday
    };

    private static final int[] RES_ID_TEXT_SELECTED_DAYs = {
            R.id.layout_sunday_selected,
            R.id.layout_monday_selected,
            R.id.layout_tuesday_selected,
            R.id.layout_wendesday_selected,
            R.id.layout_thursday_selected,
            R.id.layout_friday_selected,
            R.id.layout_satday_selected
    };

    private LinearLayout mLayoutWeek;

    private TextView[] mArrTextDay;
    private LinearLayout[] mArrLayoutDaySelected;

    private ImageButton mBtnAddSchedule;

    private ListView mListViewSchedule;

    /*private TextView mTextSeeAttendance;
    private TextView mTextSeeCourse;
    private TextView mTextSeeTodo;
    private TextView mTextSeeAssignment;*/

    private ImageView mImgLoadingAnim;

    private AnimationDrawable mAnimLoading;

    private ArrayList<ArrayList<ScheduleItem>> mListOfListScheduleItem;
    private ScheduleItemListViewAdapter mScheduleItemListViewAdapter;

    private long mStartDate;
    private long mEndDate;
    private long mTodayDate;

    private int mSelectedDayOfWeek = ScheduleItem.DOW_SUN;

    private int mNumPlanAttendance = 0;
    private int mNumPlanCourse = 0;
    private int mNumPlanTodo = 0;
    private int mNumPlanAssignment = 0;

    private int mNumDoAttendance = 0;
    private int mNumDoCourse = 0;
    private int mNumDoTodo = 0;
    private int mNumDoAssignment = 0;


    private LinearLayout mLayoutGraph;
    private LinearLayout mLayoutScore;

    private View mBarMyPlan;
    private View mBarAvPlan;
    private View mBarMyScore;
    private View mBarAvScore;
    private TextView mTxtMyScore1;
    private TextView mTxtAvScore1;
    private TextView mTxtMyScore2;
    private TextView mTxtAvScore2;
    private TextView mTxtTodaysScore;
    private TextView mTxtSchaduleMonth;
    private TextView mTxtMyTarget;

    private DrawerLayout mDrawerLayout;
    private InputMethodManager imm;
    private String mToday;
    private String mTodayOfMonth;
    private UserData mUserData = UserData.getInstance();
    private TargetTreeData mData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUtils.printLog(DEBUG, TAG, "[onCreate]");

        mCM.setContext(this);
        mUtils.makeDir(CM.CACHE_IMG_FILE_PATH);

        if (ImageDownloader.getContext() == null) {
            ImageDownloader.setContext(this);
            ImageDownloader.setCacheFilePath(CM.CACHE_IMG_FILE_PATH);
        }

        mListOfListScheduleItem = new ArrayList<ArrayList<ScheduleItem>>();

        /**
         * 7 : n(일, 월, ... 토) 
         * **/
        for (int i = 0; i < 7; i++) {
            ArrayList<ScheduleItem> listScheduleItem = new ArrayList<ScheduleItem>();
            mListOfListScheduleItem.add(listScheduleItem);
        }


        setContentView(R.layout.act_main);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        initView();
        mBundle = getIntent().getBundleExtra(Utils.BUNDLE);
        if (mBundle == null) {
            mBundle = new Bundle();
        }

        mScheduleItemListViewAdapter = new ScheduleItemListViewAdapter(this, R.layout.tc_schedule);
        mListViewSchedule.setAdapter(mScheduleItemListViewAdapter);

        setListViewScheduleItemClickListener();
        setBtnClickListener();

        mLayoutWeek.setOnTouchListener(new LinearLayoutSwipeListener());

        updateWeek(WUT_TODAY);
    }


    private void initView() {
        mUtils.printLog(DEBUG, TAG, "[initView]");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

        mLayoutWeek = (LinearLayout) findViewById(R.id.layout_week);

        mArrLayoutDaySelected = new LinearLayout[RES_ID_TEXT_SELECTED_DAYs.length];
        for (int i = 0; i < mArrLayoutDaySelected.length; i++) {
            mArrLayoutDaySelected[i] = (LinearLayout) findViewById(RES_ID_TEXT_SELECTED_DAYs[i]);
        }

        mArrTextDay = new TextView[RES_ID_TEXT_DAYs.length];
        for (int i = 0; i < mArrTextDay.length; i++) {
            mArrTextDay[i] = (TextView) findViewById(RES_ID_TEXT_DAYs[i]);
        }

        mBtnAddSchedule = (ImageButton) findViewById(R.id.btn_float_btn);

        mListViewSchedule = (ListView) findViewById(R.id.list_schedule);

/*        mTextSeeAttendance = (TextView) findViewById(R.id.text_see_attendance);
        mTextSeeCourse = (TextView) findViewById(R.id.text_see_course);
        mTextSeeTodo = (TextView) findViewById(R.id.text_see_todo);
        mTextSeeAssignment = (TextView) findViewById(R.id.text_see_assignment);*/

        mImgLoadingAnim = (ImageView) findViewById(R.id.img_loading);

        mAnimLoading = (AnimationDrawable) mImgLoadingAnim.getBackground();
        stopLoadingAnimation();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        mLayoutGraph = (LinearLayout) findViewById(R.id.layout_graph);
        mLayoutScore = (LinearLayout) findViewById(R.id.layout_todays_score);
        mBarMyPlan = findViewById(R.id.view_my_score_bar1);
        mBarAvPlan = findViewById(R.id.view_overall_av_bar1);
        mBarMyScore = findViewById(R.id.view_my_score_bar2);
        mBarAvScore = findViewById(R.id.view_overall_av_bar2);
        mTxtMyScore1 = (TextView) findViewById(R.id.txt_my_score_value1);
        mTxtAvScore1 = (TextView) findViewById(R.id.txt_moverall_av_value1);
        mTxtMyScore2 = (TextView) findViewById(R.id.txt_my_score_value2);
        mTxtAvScore2 = (TextView) findViewById(R.id.txt_moverall_av_value2);
        mTxtTodaysScore = (TextView) findViewById(R.id.txt_todays_score);
        mTxtSchaduleMonth = (TextView) findViewById(R.id.txt_schadule_month);
        mTxtMyTarget = (TextView) findViewById(R.id.txt_my_target);


        ListView listView = (ListView) findViewById(R.id.main_drawer);
        nav_list = getResources().getStringArray(R.array.nav_menu);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nav_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mUtils.printLog(DEBUG, TAG, "[onItemClick] item id : " + i);
                switch (i) {
                    case 0: //목표나무 관리
                        mIntent = new Intent(ActMain.this, ActTargetTree.class);
                        mIntent.putExtra(Utils.BUNDLE, mBundle);
                        startActivity(mIntent);
                        break;
                    case 1: //성적 관리
                        mIntent = new Intent(ActMain.this, ActManageRecord.class);
                        mBundle.putParcelable(GraphData.GRAPH_DATA, mGraphData);
                        mIntent.putExtra(Utils.BUNDLE, mBundle);
                        startActivity(mIntent);
                        break;
                    case 2: //설정
                        break;
                    case 3: //로그오프
                        mCM.setUserPwd("");
                        finish();
                        break;
                }
            }
        });
        Calendar cal = Calendar.getInstance(); // the value to be formatted
        DateFormat formatter = DateFormat.getDateInstance(DateFormat.LONG);
        formatter.setTimeZone(cal.getTimeZone());
        mToday = formatter.format(cal.getTime());


        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

        };
        mDrawerLayout.addDrawerListener(drawerToggle);

        getSupportActionBar().setTitle(mToday);
        //액션바 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF56aed2));
        //홈버튼 표시
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void setBtnClickListener() {
        mUtils.printLog(DEBUG, TAG, "[setBtnClickListener]");
        mBtnAddSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUtils.printLog(DEBUG, TAG, "[setBtnClickListener] mBtnAddSchedule pressed");
                showActEditSchedule();
            }
        });

        for (int i = 0; i < mArrTextDay.length; i++) {
            mArrTextDay[i].setTag(Integer.valueOf(i));
            mArrTextDay[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedDayOfWeek = (int) ((Integer) v.getTag());
                    mUtils.printLog(DEBUG, TAG, "[setBtnClickListener] mArrTextDay [" + mSelectedDayOfWeek + "] pressed");
                    refreshWeek();
                }
            });
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    //액션버튼 메뉴 액션바에 집어 넣기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    //액션버튼을 클릭했을때의 동작
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (drawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        mUtils.printLog(DEBUG, TAG, "[onResume]");
        super.onResume();
        isExistPackage = mUtils.getPackageList(this, EBS_SMARTCOACH_PACKAGE);
        //jh do asynktask
        //new DoLoadGraphData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    protected void onPause() {
        mUtils.printLog(DEBUG, TAG, "[onPause]");
        mDrawerLayout.closeDrawers();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mUtils.printLog(DEBUG, TAG, "[onDestroy]");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        mUtils.printLog(DEBUG, TAG, "[onStop]");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        mUtils.printLog(DEBUG, TAG, "[onRestart]");
        super.onRestart();
    }

    private boolean doFinish = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        mUtils.printLog(DEBUG, TAG, "[onKeyDown] keyCode : " + keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (doFinish) {
                    finishAffinity();
                } else {
                    Toast.makeText(this, R.string.wanna_finish, Toast.LENGTH_SHORT).show();
                    doFinish = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            doFinish = false;
                        }
                    }, 1000);
                }
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mUtils.printLog(DEBUG, TAG, "[onActivityResult] requestCode : " + requestCode + ", resultCode : " + resultCode);
        switch (resultCode) {
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mUtils.printLog(DEBUG, TAG, "[onWindowFocusChanged]");
        if (hasFocus) {
            if (mTxtMyTarget.getText().length() < 1) {
                mTxtMyTarget.setText(R.string.input_target);
            }
            if(mGraphData != null && mGraphData.getMainGraph().size() > 0){
              setGraphData();
            } else {
              new DoLoadGraphData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }

            new DoLoadTargetAndScore().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

    }

    public void setGraphData() {
        mMaxGraphWidth = mLayoutGraph.getWidth();
        mUtils.printLog(DEBUG, TAG, "[setGraphData] mMaxGraphWidth = " + mMaxGraphWidth);
        mUtils.printLog(DEBUG, TAG, "[setGraphData] GraphData.getMainGraph() size  = " + mGraphData.getMainGraph().size());
        mUtils.printLog(DEBUG, TAG, "[setGraphData] GraphData.getDetailGraph() size  = " + mGraphData.getDetailGraph().size());

        mUtils.setGraph(mBarMyPlan, mGraphData.getMainGraph().get(2).getPercent(), mMaxGraphWidth);
        mUtils.setGraph(mBarAvPlan, mGraphData.getMainGraph().get(3).getPercent(), mMaxGraphWidth);
        mUtils.setGraph(mBarMyScore, mGraphData.getMainGraph().get(6).getPercent(), mMaxGraphWidth);
        mUtils.setGraph(mBarAvScore, mGraphData.getMainGraph().get(7).getPercent(), mMaxGraphWidth);
        mTxtMyScore1.setText("" + mGraphData.getMainGraph().get(2).getPercent());
        mTxtAvScore1.setText("" + mGraphData.getMainGraph().get(3).getPercent());
        mTxtMyScore2.setText("" + mGraphData.getMainGraph().get(6).getPercent());
        mTxtAvScore2.setText("" + mGraphData.getMainGraph().get(7).getPercent());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_score_mng:
                mIntent = new Intent(this, ActManageRecord.class);
                mBundle.putSerializable(GraphData.GRAPH_DATA, mGraphData.getMainGraph());
                mBundle.putSerializable(GraphData.GRAPH_DATA_2ND, mGraphData.getDetailGraph());
                mUtils.printLog(DEBUG,TAG, "juho size :" +mGraphData.getDetailGraph().size());
                mIntent.putExtra(Utils.BUNDLE, mBundle);
                startActivity(mIntent);
                break;
            case R.id.layout_my_gugongc:
            case R.id.txt_my_target:
            case R.id.main_tree_img:
                mIntent = new Intent(this, ActTargetTree.class);
                //mIntent.putExtra(Utils.BUNDLE, mBundle);
                startActivity(mIntent);
                break;
            case R.id.layout_todays_score:
                mUtils.printLog(DEBUG, TAG, "[onClick] score clicked");
                break;
            case R.id.txt_todays_score:
                if (mTxtMyTarget.length() > 0) {
                    mBundle.putString(DialogEditScore.DIALOG_DATE, mTodayOfMonth);
                    mBundle.putString(DialogEditScore.GOAL_SEQ, mData.getGoalSeq());
                    mBundle.putString(DialogEditScore.DIALOG_SCORE, "" + mData.getAvMyMonthScore());
                    new DialogEditScore(this, mBundle).show();
                }
                break;
            case R.id.btn_month_pre:
                swipeRight();
                break;
            case R.id.btn_month_next:
                swipeLeft();
                break;
        }
    }

    private void actFinish(int type) {
        mUtils.printLog(DEBUG, TAG, "[actFinish] type : " + type);
        setResult(type);
        finish();
    }

    private void updateWeek(int type) {
        mUtils.printLog(DEBUG, TAG, "[updateWeek] type : " + type);


        switch (type) {
            case WUT_TODAY: {
                long today = System.currentTimeMillis();
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(today);
                mSelectedDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                today = mUtils.getMillisFromDateString(mUtils.getDateStringFromMillis(today, "yyyy-MM-dd") + "-00-00-00", "yyyy-MM-dd-kk-mm-ss");

                mUtils.printLog(DEBUG, TAG, "[updateWeek] today : " + mUtils.getDateStringFromMillis(today, "yyyy-MM-dd-kk-mm-ss"));

                mStartDate = mUtils.getMillisFromDateString(mUtils.getDateStringFromMillis(today - mSelectedDayOfWeek * Utils.MILLIS_OF_DAY, "yyyy-MM-dd") + "-00-00-00", "yyyy-MM-dd-kk-mm-ss");
                mEndDate = mUtils.getMillisFromDateString(mUtils.getDateStringFromMillis(mStartDate + 6 * Utils.MILLIS_OF_DAY, "yyyy-MM-dd") + "-23-59-59", "yyyy-MM-dd-kk-mm-ss");
            }
            break;
            case WUT_NEXT: {
                mStartDate += 7 * Utils.MILLIS_OF_DAY;
                mEndDate += 7 * Utils.MILLIS_OF_DAY;
            }
            break;
            case WUT_PREV: {
                mStartDate -= 7 * Utils.MILLIS_OF_DAY;
                mEndDate -= 7 * Utils.MILLIS_OF_DAY;
            }
            break;
            default:
                break;
        }

        new DoLoadScheduleList().executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR,
                mStartDate,
                mEndDate
        );
    }

    private void refreshWeek() {
        mUtils.printLog(DEBUG, TAG, "[refreshWeek]");
        mTodayDate = mStartDate + mSelectedDayOfWeek * Utils.MILLIS_OF_DAY;
        mTxtSchaduleMonth.setText(new SimpleDateFormat("yyyy. MM").format(new Date(mTodayDate)));


        for (int i = 0; i < mArrTextDay.length; i++) {
            mArrTextDay[i].setText("" + mUtils.getDayOfMonthFromMillis(mStartDate + i * Utils.MILLIS_OF_DAY));

            if (i == mSelectedDayOfWeek) {
                mArrLayoutDaySelected[i].setVisibility(View.VISIBLE);
                mArrTextDay[i].setTextSize(
                        TypedValue.COMPLEX_UNIT_SP,
                        CM.FONT_SIZE_DAY_OF_MONTH_SELECTED
                );
            } else {
                mArrLayoutDaySelected[i].setVisibility(View.INVISIBLE);
                mArrTextDay[i].setTextSize(
                        TypedValue.COMPLEX_UNIT_SP,
                        CM.FONT_SIZE_DAY_OF_MONTH_NOT_SELECTED
                );
            }
        }

        refreshList();
    }

    private void refreshList() {
        mUtils.printLog(DEBUG, TAG, "[refreshList]");

        mScheduleItemListViewAdapter.clear();
        mScheduleItemListViewAdapter.addAll(mListOfListScheduleItem.get(mSelectedDayOfWeek));
        mScheduleItemListViewAdapter.notifyDataSetChanged();

  /*      if (mNumPlanAttendance < 1) {
            mTextSeeAttendance.setText(getString(R.string.static_text_attendance) + "(0%)");
        } else {
            mTextSeeAttendance.setText(
                    getString(R.string.static_text_attendance) + "(" +
                            ((int) (((float) mNumDoAttendance / (float) mNumPlanAttendance) * 100.0f)) +
                            "%)");
        }

        if (mNumPlanCourse < 1) {
            mTextSeeCourse.setText(getString(R.string.static_text_course) + "(0%)");
        } else {
            mTextSeeCourse.setText(
                    getString(R.string.static_text_course) + "(" +
                            ((int) (((float) mNumDoCourse / (float) mNumPlanCourse) * 100.0f)) +
                            "%)");
        }

        if (mNumPlanTodo < 1) {
            mTextSeeTodo.setText(getString(R.string.static_text_todo) + "(0%)");
        } else {
            mTextSeeTodo.setText(
                    getString(R.string.static_text_todo) + "(" +
                            ((int) (((float) mNumDoTodo / (float) mNumPlanTodo) * 100.0f)) +
                            "%)");
        }

        if (mNumPlanAssignment < 1) {
            mTextSeeAssignment.setText(getString(R.string.static_text_assignment) + "(0%)");
        } else {
            mTextSeeAssignment.setText(
                    getString(R.string.static_text_assignment) + "(" +
                            ((int) (((float) mNumDoAssignment / (float) mNumPlanAssignment) * 100.0f)) +
                            "%)");
        }*/
    }

    private void setListViewScheduleItemClickListener() {
        mUtils.printLog(DEBUG, TAG, "[setListViewScheduleItemClickListener]");
        mListViewSchedule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ScheduleItem item = mListOfListScheduleItem.get(mSelectedDayOfWeek).get(position);
                mUtils.printLog(DEBUG, TAG, "[setListViewScheduleItemClickListener] [onItemClick] position : " + position + ", item : " + item.toString());
                //jh
                if (isExistPackage) {
                    Intent intent = getPackageManager().getLaunchIntentForPackage(EBS_SMARTCOACH_PACKAGE);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("gugongc", true);
                    UserData data = UserData.getInstance();
                    intent.putExtra("user id", "ggc." + mCM.getUserId());
                    intent.putExtra("user pw", mCM.getUserPwd());
                    intent.putExtra("url", "http://badau.net/EBS/testview?folder_id=361383&member_id=ggc.ggc2&client_app_name=ebs_smartcoach_student");
                    intent.putExtra("from", "[문제 풀기]");
                    intent.putExtra("workbook name", "수학 5-1");
                    startActivity(intent);
                } else {
                    String url = "market://details?id=" + EBS_SMARTCOACH_PACKAGE;
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i);
                }
            }
        });
    }

    private void startLoadingAnimation() {
        mUtils.printLog(DEBUG, TAG, "[startLoadingAnimation]");
        mImgLoadingAnim.setVisibility(View.VISIBLE);
        mAnimLoading.start();
    }

    private void stopLoadingAnimation() {
        mUtils.printLog(DEBUG, TAG, "[stopLoadingAnimation]");
        mImgLoadingAnim.setVisibility(View.GONE);
        mAnimLoading.stop();
    }

    private String getCustomDateStringFromMillis(long millis) {
        mUtils.printLog(DEBUG, TAG, "[getCustomDateStringFromMillis] millis : " + millis);
        return new SimpleDateFormat("aa hh:mm", Locale.getDefault()).format(millis);
    }


    private void swipeLeft() {
        mUtils.printLog(DEBUG, TAG, "[swipeLeft]");
        updateWeek(WUT_NEXT);
    }

    private void swipeRight() {
        mUtils.printLog(DEBUG, TAG, "[swipeRight]");
        updateWeek(WUT_PREV);
    }

    private void showActEditSchedule() {
        mUtils.printLog(DEBUG, TAG, "[showActEditSchedule]");
        Intent intent = new Intent(ActMain.this, ActEditScheduleSec.class);
        intent.putExtra(ActEditScheduleSec.EXTRA_DATE, mTodayDate);
        startActivityForResult(intent, 0);
        /*Intent intent = new Intent(ActMain.this, ActEditSchedule.class);
        intent.putExtra(ActEditSchedule.EXTRA_DATE, mStartDate + mSelectedDayOfWeek * Utils.MILLIS_OF_DAY);
        startActivityForResult(intent, 0);*/

    }

    private class LinearLayoutSwipeListener implements View.OnTouchListener {

        private float mDownX;
        private float mUpX;

        public LinearLayoutSwipeListener() {
        }

        public void onRightToLeftSwipe() {
            swipeLeft();
        }

        public void onLeftToRightSwipe() {
            swipeRight();
        }

        @SuppressLint("ClickableViewAccessibility")
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    mDownX = event.getX();
                }
                return true;
                case MotionEvent.ACTION_UP: {
                    mUpX = event.getX();

                    float deltaX = mDownX - mUpX;

                    if (Math.abs(deltaX) > MIN_DISTANCE) {
                        if (deltaX < 0) {
                            this.onLeftToRightSwipe();
                            return true;
                        }
                        if (deltaX > 0) {
                            this.onRightToLeftSwipe();
                            return true;
                        }
                    }
                }
                break;
            }

            return false;
        }
    }


    private class ScheduleItemListViewHolder {

        public LinearLayout layoutPlan;
        public TextView textPlanAttendance;
        public TextView textPlanCourse;
        public TextView textPlanTodo;
        public TextView textPlanAssignment;

        public LinearLayout layoutDo;
        public TextView textDoAttendance;
        public TextView textDoCourse;
        public TextView textDoTodo;
        public TextView textDoAssignment;

        public LinearLayout layoutItem;
        public Button btnChk;
        public TextView textTitle;
        public TextView textDate;
        public TextView textWrongAnswerNote;
    }

    private class ScheduleItemListViewAdapter extends ArrayAdapter<ScheduleItem> {

        private LayoutInflater mInflater;

        public ScheduleItemListViewAdapter(Context context, int layoutResource, ArrayList<ScheduleItem> objects) {
            super(context, layoutResource, objects);
            mUtils.printLog(DEBUG, TAG, "[WorkbookListViewAdapter]");
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public ScheduleItemListViewAdapter(Context context, int layoutResource) {
            super(context, layoutResource);
            mUtils.printLog(DEBUG, TAG, "[WorkbookListViewAdapter]");
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return super.getCount();
        }

        @Override
        public ScheduleItem getItem(int position) {
            return super.getItem(position);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            mUtils.printLog(!DEBUG, TAG, "[ScheduleItemListViewAdapter] [getView] position : " + position);

            ScheduleItem row = getItem(position);
            ScheduleItemListViewHolder vh = null;

            if (convertView == null) {

                vh = new ScheduleItemListViewHolder();
                convertView = mInflater.inflate(R.layout.tc_schedule, parent, false);

                vh.layoutPlan = (LinearLayout) convertView.findViewById(R.id.layout_plan);

                vh.layoutDo = (LinearLayout) convertView.findViewById(R.id.layout_do);

                vh.layoutItem = (LinearLayout) convertView.findViewById(R.id.layout_item);
                vh.btnChk = (Button) convertView.findViewById(R.id.btn_chk);
                vh.textTitle = (TextView) convertView.findViewById(R.id.text_title);
                vh.textDate = (TextView) convertView.findViewById(R.id.text_date);
                vh.textWrongAnswerNote = (TextView) convertView.findViewById(R.id.text_wrong_answer_note);

                convertView.setTag(vh);
            } else {
                try {
                    vh = (ScheduleItemListViewHolder) convertView.getTag();
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    return convertView;
                }
            }

            switch (row.getCellType()) {
                case ScheduleItem.CT_SECTION_PLAN: {
                    vh.layoutDo.setVisibility(View.GONE);
                    vh.layoutItem.setVisibility(View.GONE);
                    vh.layoutPlan.setVisibility(View.VISIBLE);
                }
                break;
                case ScheduleItem.CT_SECTION_DO: {
                    vh.layoutPlan.setVisibility(View.GONE);
                    vh.layoutItem.setVisibility(View.GONE);
                    vh.layoutDo.setVisibility(View.VISIBLE);

                }
                break;
                case ScheduleItem.CT_ITEM: {
                    vh.layoutPlan.setVisibility(View.GONE);
                    vh.layoutDo.setVisibility(View.GONE);
                    vh.layoutItem.setVisibility(View.VISIBLE);

                    vh.btnChk.setTag(Integer.valueOf(position));
                    vh.btnChk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Integer idx = (Integer) v.getTag();
                            ScheduleItem item = mListOfListScheduleItem.get(mSelectedDayOfWeek).get(idx);
                            mUtils.printLog(DEBUG, TAG, "[ScheduleItemListViewAdapter] btnChk pressed - idx : " + idx + ", item : " + item.toString());
                        }
                    });

                    vh.textTitle.setText(row.getTitle());
                    vh.textDate.setText(getCustomDateStringFromMillis(row.getDate()));
                    switch (row.getType()) {
                        case ScheduleItem.ST_COURSE:
                            vh.layoutItem.setBackgroundResource(R.color.color_schadule_theme_course);
                            break;
                        case ScheduleItem.ST_TODO:
                            vh.layoutItem.setBackgroundResource(R.color.color_schadule_theme_todo);
                            break;
                        case ScheduleItem.ST_ASSIGNMENT:
                            vh.layoutItem.setBackgroundResource(R.color.color_schadule_theme_assignment);
                            break;
                    }

                    switch (row.getCategory()) {
                        case ScheduleItem.CT_SECTION_PLAN: {
                            //jh
                            if (row.getDate() > Calendar.getInstance().getTimeInMillis()) {
                                vh.btnChk.setBackgroundResource(R.drawable.shape_schedule_icon_normal);
                            } else {
                                vh.btnChk.setBackgroundResource(R.drawable.shape_schedule_icon_red);
                            }

                            switch (row.getWrongAnswerNoteType()) {
                                case ScheduleItem.WANT_NONE:
                                    vh.textWrongAnswerNote.setVisibility(View.INVISIBLE);
                                    break;
                                case ScheduleItem.WANT_ENABLE:
                                    vh.textWrongAnswerNote.setVisibility(View.VISIBLE);
                                    break;
                            }
                        }
                        break;
                        case ScheduleItem.CT_SECTION_DO:
                        default: {
                            vh.btnChk.setBackgroundResource(R.drawable.shape_schedule_icon_green);
                            vh.textWrongAnswerNote.setVisibility(View.INVISIBLE);
                        }
                        break;
                    }
                }
                break;
            }

            return convertView;
        }
    }

    private class DoLoadScheduleList extends AsyncTask<Long, Integer, Integer> {

        private ArrayList<ScheduleItem> mListScheduleItem;

        @Override
        protected void onPreExecute() {
            mUtils.printLog(DEBUG, TAG, "[DoLoadScheduleList] [onPreExecute]");
            startLoadingAnimation();
            mListScheduleItem = new ArrayList<ScheduleItem>();
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Long... strData) {
            mUtils.printLog(DEBUG, TAG, "[DoLoadScheduleList] [doInBackground] strData[0] : "
                    + mUtils.getDateStringFromMillis(strData[0], "yyyy-MM-dd hh:mm:ss") +
                    ",  strData[1] : " + mUtils.getDateStringFromMillis(strData[1], "yyyy-MM-dd hh:mm:ss"));

            return DM.getInstance().getScheduleList(mListScheduleItem, strData[0], strData[1]);
        }

        @Override
        protected void onPostExecute(Integer result) {
            mUtils.printLog(DEBUG, TAG, "[DoLoadScheduleList] [onPostExecute] result : " + result);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

//					mUtils.printLog(DEBUG, TAG, "[DoLoadScheduleList] [onPostExecute] mListScheduleItem.size() : " + mListScheduleItem.size());

                    for (ArrayList<ScheduleItem> list : mListOfListScheduleItem) {
                        list.clear();
                    }

                    Calendar calendar = Calendar.getInstance();
                    ArrayList<ScheduleItem> planList = new ArrayList<ScheduleItem>();
                    ArrayList<ScheduleItem> doList = new ArrayList<ScheduleItem>();

                    mNumPlanAttendance = 0;
                    mNumPlanCourse = 0;
                    mNumPlanTodo = 0;
                    mNumPlanAssignment = 0;

                    mNumDoAttendance = 0;
                    mNumDoCourse = 0;
                    mNumDoTodo = 0;
                    mNumDoAssignment = 0;

                    for (ScheduleItem item : mListScheduleItem) {

                        calendar.setTimeInMillis(item.getDate());
                        item.setDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK) - 1);

                        switch (item.getType()) {
                            case ScheduleItem.ST_ATTENDANCE:
                                ++mNumPlanAttendance;
                                break;
                            case ScheduleItem.ST_COURSE:
                                ++mNumPlanCourse;
                                break;
                            case ScheduleItem.ST_TODO:
                                ++mNumPlanTodo;
                                break;
                            case ScheduleItem.ST_ASSIGNMENT:
                                ++mNumPlanAssignment;
                                break;
                        }

                        switch (item.getCategory()) {
                            case ScheduleItem.CTG_PLAN: {
                                planList.add(item);
                            }
                            break;
                            case ScheduleItem.CTG_DO: {
                                doList.add(item);

                                switch (item.getType()) {
                                    case ScheduleItem.ST_ATTENDANCE:
                                        ++mNumDoAttendance;
                                        break;
                                    case ScheduleItem.ST_COURSE:
                                        ++mNumDoCourse;
                                        break;
                                    case ScheduleItem.ST_TODO:
                                        ++mNumDoTodo;
                                        break;
                                    case ScheduleItem.ST_ASSIGNMENT:
                                        ++mNumDoAssignment;
                                        break;
                                }
                            }
                            break;
                        }
                    }

                    mUtils.printLog(DEBUG, TAG, "[DoLoadScheduleList] [onPostExecute] planList.size() : " + planList.size());
                    mUtils.printLog(DEBUG, TAG, "[DoLoadScheduleList] [onPostExecute] doList.size() : " + doList.size());

                    for (int i = 0; i < mListOfListScheduleItem.size(); i++) {
                        ScheduleItem item = new ScheduleItem();
                        item.setCellType(ScheduleItem.CTG_PLAN);
                        mListOfListScheduleItem.get(i).add(item);
                    }

                    for (ScheduleItem item : planList) {
                        mListOfListScheduleItem.get(item.getDayOfWeek()).add(item);
                    }

                    for (int i = 0; i < mListOfListScheduleItem.size(); i++) {
                        ScheduleItem item = new ScheduleItem();
                        item.setCellType(ScheduleItem.CTG_DO);
                        mListOfListScheduleItem.get(i).add(item);
                    }

                    for (ScheduleItem item : doList) {
                        mListOfListScheduleItem.get(item.getDayOfWeek()).add(item);
                    }

                    stopLoadingAnimation();
                    refreshWeek();

//					for (int m = 0; m < mListOfListScheduleItem.size(); m++) {
//						ArrayList<ScheduleItem> list = mListOfListScheduleItem.get(m);
//						for (int n = 0; n < list.size(); n++) {
//							mUtils.printLog(DEBUG, TAG, "[DoLoadScheduleList] [onPostExecute] item : " + list.get(n).toString());
//						}
//					}

                }
            });
        }

        @Override
        protected void onCancelled() {
            mUtils.printLog(DEBUG, TAG, "[DoLoadScheduleList] [onCancelled]");
            stopLoadingAnimation();
            super.onCancelled();
        }
    }

    protected class DoLoadTargetAndScore extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            mUtils.printLog(DEBUG, TAG, "[DoLoadTargetAndScore] [onPreExecute]");
            mData = new TargetTreeData();
            startLoadingAnimation();
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... strings) {
            mUtils.printLog(DEBUG, TAG, "[DoLoadTargetAndScore] [doInBackground]");
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat format = new SimpleDateFormat("dd");
            mTodayOfMonth = format.format(date);
            return DM.getInstance().getTargetData(mData, mTodayOfMonth);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            mUtils.printLog(DEBUG, TAG, "[DoLoadTargetAndScore] [onPostExecute]");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTxtMyTarget.setText(mData.getTarget());
                    mTxtTodaysScore.setText("" + mData.getAvMyMonthScore());
                }
            });
            stopLoadingAnimation();
        }

    }

    protected class DoLoadGraphData extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            mUtils.printLog(DEBUG, TAG, "[DoLoadGraphData] [onPreExecute]");
            mGraphData = new GraphData();
            startLoadingAnimation();
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... strings) {
            mUtils.printLog(DEBUG, TAG, "[DoLoadGraphData] [doInBackground]");

            return DM.getInstance().getGraphData(mGraphData);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            mUtils.printLog(DEBUG, TAG, "[DoLoadGraphData] [onPostExecute]");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setGraphData();
                }
            });
            stopLoadingAnimation();
        }


    }

}