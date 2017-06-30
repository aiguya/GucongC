package com.smartware.gucongc;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.smartware.common.Utils;
import com.smartware.container.Chapter;
import com.smartware.container.ScheduleItem;
import com.smartware.container.Workbook;
import com.smartware.manager.CM;
import com.smartware.manager.DM;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Juho.S on 2017-06-15.
 */
public class ActEditScheduleSec extends AppCompatActivity {

    private final static boolean DEBUG = Utils.DEBUG;
    private final static String TAG = "ActEditScheduleSec";
    public static final String EXTRA_DATE = "date";
    private static final String EXTRA_WORKBOOK_TITLE = "workbook title";
    private static final String EXTRA_WORK_URL = "workbook title";

    private long  mTodayDate;

    private final int[] mArrayWeeksID = {
            R.id.txt_sunday,
            R.id.txt_monday,
            R.id.txt_tuesday,
            R.id.txt_wednesday,
            R.id.txt_thursday,
            R.id.txt_friday,
            R.id.txt_saturday
    };
    private boolean[] mRepeaArray = new boolean[7];

    private ArrayList<TextView> mArrayWeeksTxt;
    private Button mBtnSelectSchedule;
    private EditText mEditTitle;
    private RadioGroup mCategoryGroup;
    private View mPlatPanel;
    private ImageView mImgLoadingAnim;
    private AnimationDrawable mAnimLoading;
    private InputMethodManager mInputMethodManager;
    private TimePicker mTimePicker;

    private String mSortedFolderID;
    private String mStartDate;
    private String mStartTime;
    private int mSortdFolderIDSum;
    private int mType = 1;
    private Workbook mWorkbook;
    private ScheduleItem mScheduleItem;

    private Utils mUtil = Utils.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_edit_schedule_sec);
        mTodayDate = getIntent().getLongExtra(EXTRA_DATE, 0);
        initView();
        setEditTextEnterKeyListener();

        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void initView() {
        mUtil.printLog(DEBUG, TAG, "[initView]");
        getSupportActionBar().setTitle(new SimpleDateFormat("MM/dd").format(new Date(mTodayDate)) + " " + getText(R.string.edit_schdule));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF56aed2));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_navigate_before_white_36dp);
        mStartDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(mTodayDate));
        mStartTime = new SimpleDateFormat("HH:mm").format(new Date(Calendar.getInstance().getTimeInMillis()));
        mUtil.printLog(DEBUG, TAG, "[initView] mStartDate : " + mStartDate);
        mUtil.printLog(DEBUG, TAG, "[initView] mStartTime : " + mStartTime);
        mArrayWeeksTxt = new ArrayList<>();
        for (int id : mArrayWeeksID) {
            mArrayWeeksTxt.add((TextView) findViewById(id));
        }
        mImgLoadingAnim = (ImageView) findViewById(R.id.img_loading);
        mAnimLoading = (AnimationDrawable) mImgLoadingAnim.getBackground();
        stopLoadingAnimation();

        mPlatPanel = findViewById(R.id.view_repeat_setting);
        mPlatPanel.setVisibility(View.GONE);
        mTimePicker = (TimePicker)findViewById(R.id.time_picker);

        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int min) {
                mUtil.printLog(DEBUG,TAG, "[onTimeChanged] time : " + hour + ":"+ min );
                String h = hour == 0 ? "00" : "" + hour;
                String m = min == 0 ? "00" : "" + min;
                mStartTime = h + ":"+ m;
            }
        });
        mEditTitle = (EditText) findViewById(R.id.edit_schedule_title);
        mEditTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return false;
            }
        });
        mBtnSelectSchedule = (Button) findViewById(R.id.btn_select_assignment);
        mCategoryGroup = (RadioGroup) findViewById(R.id.group_category);
        mCategoryGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                mUtil.printLog(DEBUG, TAG, "[onCheckedChanged] index : " + i);
                mEditTitle.setText("");
                switch (i) {
                    case R.id.rb_course:
                        mEditTitle.setEnabled(false);
                        mBtnSelectSchedule.setClickable(true);
                        mBtnSelectSchedule.setText(R.string.edit_workbook);
                        mType = ScheduleItem.ST_COURSE;
                        mBtnSelectSchedule.setBackgroundResource(R.drawable.btn_blue_plat);
                        mPlatPanel.setVisibility(View.GONE);
                        break;
                    case R.id.rb_todo:
                        mEditTitle.setEnabled(true);
                        mBtnSelectSchedule.setText(R.string.edit_schdule);
                        mBtnSelectSchedule.setBackgroundResource(R.color.color_text_gray);
                        mBtnSelectSchedule.setClickable(false);
                        mPlatPanel.setVisibility(View.GONE);
                        mType = ScheduleItem.ST_TODO;
                        break;
                    case R.id.rb_assignment:
                        mEditTitle.setEnabled(false);
                        mBtnSelectSchedule.setText(R.string.edit_assignment);
                        mBtnSelectSchedule.setBackgroundResource(R.drawable.btn_blue_plat);
                        mBtnSelectSchedule.setClickable(true);
                        mPlatPanel.setVisibility(View.VISIBLE);
                        mType = ScheduleItem.ST_ASSIGNMENT;
                        break;
                }
            }
        });
    }

    private void hideKeyboard() {
        mUtil.printLog(DEBUG, TAG, "[hideKeyboard]");
        mInputMethodManager.hideSoftInputFromWindow(mEditTitle.getWindowToken(), 0);
    }


    private void showActEditSchedule() {
        mUtil.printLog(DEBUG, TAG, "[showActEditSchedule]");

        Intent intent = new Intent(this, ActFindAssignment.class);
        intent.putExtra(ActFindAssignment.EXTRA_STUDENT_GRADE, CM.getInstance().getSchoolGrade());

        intent.putExtra(ActFindAssignment.EXTRA_STUDENT_GRADE, "es");
        startActivityForResult(intent, 0);

    }

    private void setEditTextEnterKeyListener() {
        mUtil.printLog(DEBUG, TAG, "[setListViewWorkbookItemClickListener]");
        mEditTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mUtil.printLog(DEBUG, TAG, "[setListViewWorkbookItemClickListener] EditorInfo.IME_ACTION_DONE");
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });
    }

    //액션버튼 메뉴 액션바에 집어 넣기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.schedule_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                actFinish(RESULT_CANCELED);
                Toast.makeText(this, "뒤로 가기", Toast.LENGTH_SHORT).show();
                break;
            //저장
            case R.id.action_save:
                if(mEditTitle.getText().length() < 0) {
                    break;
                }

                mScheduleItem = new ScheduleItem();
                mScheduleItem.setType(mType);
                mScheduleItem.setTitle(mEditTitle.getText().toString());
                if(mType == ScheduleItem.ST_COURSE){
                    mScheduleItem.setFolderID(mSortedFolderID);
                }
                new DoMakeSchedule().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void actFinish(int type) {
        mUtil.printLog(DEBUG, TAG, "[actFinish] type : " + type);
        setResult(type);
        finish();
    }

    private void sortFolderID(ArrayList<Chapter> list) {
        mSortedFolderID = "";
        mSortdFolderIDSum = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isCheckBoxChecked() && list.get(i).getCellType() == Chapter.CT_UNIT_DETAIL) {
                mSortdFolderIDSum++;
                if (mSortdFolderIDSum < list.size() && mSortdFolderIDSum > 1) {
                    mSortedFolderID = mSortedFolderID + "|";
                }
                mSortedFolderID = mSortedFolderID + list.get(i).getFolderId();
            }
        }
        mUtil.printLog(DEBUG, TAG, "[sortFolderID] mSortdFolderIDSum = " + mSortdFolderIDSum);
        mUtil.printLog(DEBUG, TAG, "[sortFolderID] mSortedFolderID : " + mSortedFolderID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mUtil.printLog(DEBUG, TAG, "[onActivityResult] requestCode : " + requestCode + ", resultCode : " + resultCode);
        switch (resultCode) {
            case RESULT_OK:
                mWorkbook = data.getParcelableExtra(ActFindAssignment.EXTRA_WORKBOOK);
                String str_content = "";
                for (int i = 0; i < mWorkbook.getListChapter().size(); i++) {
                    if (mWorkbook.getListChapter().get(i).isCheckBoxChecked() == true) {
                        str_content = mWorkbook.getListChapter().get(i).getChapterName();
                        break;
                    }
                }
                mEditTitle.setText("[" + mWorkbook.getTitle() + "] " + str_content);
                sortFolderID(mWorkbook.getListChapter());
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startLoadingAnimation() {
        mUtil.printLog(DEBUG, TAG, "[startLoadingAnimation]");
        mImgLoadingAnim.setVisibility(View.VISIBLE);
        mAnimLoading.start();
    }

    private void stopLoadingAnimation() {
        mUtil.printLog(DEBUG, TAG, "[stopLoadingAnimation]");
        mImgLoadingAnim.setVisibility(View.GONE);
        mAnimLoading.stop();
    }


    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.txt_sunday:
                mRepeaArray[0] = !mRepeaArray[0];
                if (mRepeaArray[0]) {
                    v.setBackgroundResource(R.drawable.shape_ring_light_blue);
                } else {
                    v.setBackground(null);
                }
                break;
            case R.id.txt_monday:
                mRepeaArray[1] = !mRepeaArray[1];
                if (mRepeaArray[1]) {
                    v.setBackgroundResource(R.drawable.shape_ring_light_blue);
                } else {
                    v.setBackground(null);
                }
                break;
            case R.id.txt_tuesday:
                mRepeaArray[2] = !mRepeaArray[2];
                if (mRepeaArray[2]) {
                    v.setBackgroundResource(R.drawable.shape_ring_light_blue);
                } else {
                    v.setBackground(null);
                }
                break;
            case R.id.txt_wednesday:
                mRepeaArray[3] = !mRepeaArray[3];
                if (mRepeaArray[3]) {
                    v.setBackgroundResource(R.drawable.shape_ring_light_blue);
                } else {
                    v.setBackground(null);
                }
                break;
            case R.id.txt_thursday:
                mRepeaArray[4] = !mRepeaArray[4];
                if (mRepeaArray[4]) {
                    v.setBackgroundResource(R.drawable.shape_ring_light_blue);
                } else {
                    v.setBackground(null);
                }
                break;
            case R.id.txt_friday:
                mRepeaArray[5] = !mRepeaArray[5];
                if (mRepeaArray[5]) {
                    v.setBackgroundResource(R.drawable.shape_ring_light_blue);
                } else {
                    v.setBackground(null);
                }
                break;
            case R.id.txt_saturday:
                mRepeaArray[6] = !mRepeaArray[6];
                if (mRepeaArray[6]) {
                    v.setBackgroundResource(R.drawable.shape_ring_light_blue);
                } else {
                    v.setBackground(null);
                }
                break;
            case R.id.btn_select_assignment:
                if (mCategoryGroup.getCheckedRadioButtonId() == R.id.rb_course) {
                    showActEditSchedule();
                } else if (mCategoryGroup.getCheckedRadioButtonId() == R.id.rb_assignment) {
                    Toast.makeText(this, "준비중입니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        if (mArrayWeeksTxt.contains(v)) {
            mUtil.printLog(DEBUG, TAG, "[onClick] " + ((TextView) v).getText().toString());
        }
    }

    private class DoMakeSchedule extends AsyncTask<String, Void, Integer>{

        @Override
        protected void onPreExecute() {
            startLoadingAnimation();
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... strings) {
            mUtil.printLog(DEBUG,TAG, "[DoMakeSchedule] [doInBackground]");
            String repeat = "";
            int sum = 0;
            for(int i = 0; i < mRepeaArray.length ; i ++ ){
                if(mRepeaArray[i]){
                    if(i > 0 && repeat.length() > 0){
                        repeat = repeat + "|";
                    }
                    repeat = repeat + i;
                    sum ++;
                }
            }
            if(sum == 0){
                repeat = "0|1|2|3|4|5|6";
            }
            if(mScheduleItem.getType() == ScheduleItem.ST_TODO){
                mSortdFolderIDSum = sum * 4;
            }
            mScheduleItem.setRepeat(repeat);
            int res = DM.getInstance().makeSchedule(mScheduleItem, mStartDate, mStartTime, mSortdFolderIDSum);
            mUtil.printLog(DEBUG,TAG, "[DoMakeSchedule] [doInBackground] repeat = " + repeat);
            return res;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            stopLoadingAnimation();
            switch (integer){
                case DM.RES_SUCCESS:
                    Toast.makeText(ActEditScheduleSec.this, "저장", Toast.LENGTH_SHORT).show();
                    actFinish(RESULT_OK);
                    break;
                case DM.RES_FAIL:
                    break;
                default:
                    break;
            }
            super.onPostExecute(integer);
        }
    }

}
