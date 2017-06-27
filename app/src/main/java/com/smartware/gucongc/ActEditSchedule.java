
package com.smartware.gucongc;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.smartware.common.Utils;

public class ActEditSchedule extends Activity {

    private static final boolean DEBUG = true;
    private static final String TAG = "ActEditSchedule";

    public static final String EXTRA_DATE = "date";

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

    private Utils mUtil = Utils.getInstance();

    private Button mBtnCancel;
    private Button mBtnSave;

    @SuppressWarnings("unused")
    private TextView mTextDayOfWeeks;

    private EditText mEditTitle;
    private Button mBtnSelectDate;
    private Button mBtnAssignment;
    private RadioButton mRbAttendance;
    private RadioButton mRbCourse;
    private RadioButton mRbTodo;
    private RadioButton mRbAssignment;
    private TimePicker mTimePicker;

    private LinearLayout[] mArrLayoutDaySelected;
    private TextView[] mArrTextDay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUtil.printLog(DEBUG, TAG, "[onCreate]");

        initView();
        setBtnClickListener();
    }

    @Override
    protected void onPause() {
        mUtil.printLog(DEBUG, TAG, "[onPause]");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mUtil.printLog(DEBUG, TAG, "[onDestroy]");
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        mUtil.printLog(DEBUG, TAG, "[onResume]");
        super.onResume();
    }

    @Override
    protected void onStop() {
        mUtil.printLog(DEBUG, TAG, "[onStop]");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        mUtil.printLog(DEBUG, TAG, "[onRestart]");
        super.onRestart();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        mUtil.printLog(DEBUG, TAG, "[onKeyDown] keyCode : " + keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                actFinish(RESULT_CANCELED);
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mUtil.printLog(DEBUG, TAG, "[onActivityResult] requestCode : " + requestCode + ", resultCode : " + resultCode);
        switch (resultCode) {
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initView() {
        mUtil.printLog(DEBUG, TAG, "[initView]");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.act_edit_schedule);

        mBtnCancel = (Button) findViewById(R.id.btn_cancel);
        mBtnSave = (Button) findViewById(R.id.btn_float_save);

        mEditTitle = (EditText) findViewById(R.id.edit_schedule_title);
        mTextDayOfWeeks = (TextView) findViewById(R.id.text_repeat_day_of_week);

        mBtnSelectDate = (Button) findViewById(R.id.btn_repeat_day_of_week);
        mBtnAssignment = (Button) findViewById(R.id.btn_select_assignment);

        mRbAttendance = (RadioButton) findViewById(R.id.rb_attendance);
        mRbCourse = (RadioButton) findViewById(R.id.rb_course);
        mRbTodo = (RadioButton) findViewById(R.id.rb_todo);
        mRbAssignment = (RadioButton) findViewById(R.id.rb_assignment);

        mTimePicker = (TimePicker) findViewById(R.id.time_picker);

        mArrTextDay = new TextView[RES_ID_TEXT_DAYs.length];
        for (int i = 0; i < mArrTextDay.length; i++) {
            mArrTextDay[i] = (TextView) findViewById(RES_ID_TEXT_DAYs[i]);
        }

        mArrLayoutDaySelected = new LinearLayout[RES_ID_TEXT_SELECTED_DAYs.length];
        for (int i = 0; i < mArrLayoutDaySelected.length; i++) {
            mArrLayoutDaySelected[i] = (LinearLayout) findViewById(RES_ID_TEXT_SELECTED_DAYs[i]);
            mArrLayoutDaySelected[i].setVisibility(View.INVISIBLE);
        }

    }

    private void setBtnClickListener() {
        mUtil.printLog(DEBUG, TAG, "[setBtnClickListener]");
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUtil.printLog(DEBUG, TAG, "[setBtnClickListener] mBtnCancel pressed");
                actFinish(RESULT_CANCELED);
            }
        });
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strTitle = mEditTitle.getText().toString();
                int hour = mTimePicker.getCurrentHour();
                int minute = mTimePicker.getCurrentMinute();
                mUtil.printLog(DEBUG, TAG, "[setBtnClickListener] mBtnSave pressed, strTitle : " + strTitle + ", hour : " + hour + ", minute : " + minute);

                actFinish(RESULT_CANCELED);
            }
        });

        mBtnAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUtil.printLog(DEBUG, TAG, "[setBtnClickListener] mBtnAssignment pressed");
                showActEditSchedule();
            }
        });

        mBtnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUtil.printLog(DEBUG, TAG, "[setBtnClickListener] mBtnSelectDate pressed");
            }
        });

        mRbAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUtil.printLog(DEBUG, TAG, "[setBtnClickListener] mRbAttendance pressed");
            }
        });

        mRbCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUtil.printLog(DEBUG, TAG, "[setBtnClickListener] mRbCourse pressed");
            }
        });

        mRbTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUtil.printLog(DEBUG, TAG, "[setBtnClickListener] mRbTodo pressed");
            }
        });

        mRbAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUtil.printLog(DEBUG, TAG, "[setBtnClickListener] mRbAssignment pressed");
            }
        });

        for (int i = 0; i < mArrTextDay.length; i++) {
            mArrTextDay[i].setTag(Integer.valueOf(i));
            mArrTextDay[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int idx = (int) ((Integer) v.getTag());
                    mUtil.printLog(DEBUG, TAG, "[setBtnClickListener] mArrTextDay pressed - " + idx);
                    if (mArrLayoutDaySelected[idx].getVisibility() == View.VISIBLE) {
                        mArrLayoutDaySelected[idx].setVisibility(View.INVISIBLE);
                    } else {
                        mArrLayoutDaySelected[idx].setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    private void actFinish(int type) {
        mUtil.printLog(DEBUG, TAG, "[actFinish] type : " + type);
        setResult(type);
        finish();
    }

    private void showActEditSchedule() {
        mUtil.printLog(DEBUG, TAG, "[showActEditSchedule]");

        Intent intent = new Intent(ActEditSchedule.this, ActFindAssignment.class);
        intent.putExtra(ActFindAssignment.EXTRA_STUDENT_GRADE, "es");
        startActivityForResult(intent, 0);

    }
}

