package com.smartware.gucongc;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.smartware.common.RSASecurity;
import com.smartware.common.Utils;
import com.smartware.container.MemberInfo;
import com.smartware.manager.CM;
import com.smartware.manager.DM;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Juho.S on 2017-06-08.
 */
public class ActJoin extends AppCompatActivity {

    private static final boolean DEBUG = true;
    private static final String TAG = "ActJoin";

    private static final int CERTIFICATION = 1;
    private Utils mUtil = Utils.getInstance();

    private EditText mEditUserId;
    private EditText mEditUserName;
    private EditText mEditEmail;
    private EditText mEditUserPw;
    private EditText mEditUserPwConfirm;
    private EditText mEditUserPhoneNumber;
    private EditText mEditAuthCode;
    private TextView mTxtSchoolName;
    private Button mBtnDuplicationCheckUserId;
    private Button mBtnDuplicationCheckUserEmail;
    private Button mBtnRequestAuthCode;
    private Button mBtnSendAuthCode;
    private Button mBtnFindSchool;
    private Button mBtnJoin;
    private ImageView mImgUserNameValidation;
    private ImageView mImgUserPwValidation;
    private ImageView mImgUserPwConfirmValidation;
    private ImageView mImgLoadingAnim;
    private AnimationDrawable mAnimLoading;
    private Spinner mSpinnerSchool;
    private Spinner mSpinnerGrade;

    private TextWatcher mTextWatcherUserName;
    private TextWatcher mTextWatcherUserPw;
    private TextWatcher mTextWatcherUserPwConfirm;

    private String mVerifiedUserId = "";
    private String mVerifiedUserEmail = "";
    private String mInputUserID = "";
    private String mInputUserPW = "";
    private String mInputUserName = "";
    private String mUserSchoolGrade = "";
    private String mUserGrade = "";
    private String mUserPhoneNumber = "";
    private String mInputUserEmail = "";
    private String mAuthCode = "";
    private String mUserSchoolName = "";

    private boolean mIsAuthenticated = false;
    private HashMap<String, String> mHashMapResult;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUtil.printLog(DEBUG, TAG, "[onCreate]");
        RSASecurity.getInstance().setContext(ActJoin.this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();

        setEditTextConstraints();
        setTextWatcher();


        mEditUserName.addTextChangedListener(mTextWatcherUserName);
        mEditUserPw.addTextChangedListener(mTextWatcherUserPw);
        mEditUserPwConfirm.addTextChangedListener(mTextWatcherUserPwConfirm);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    private void initView() {
        mUtil.printLog(DEBUG, TAG, "[initView]");
        setContentView(R.layout.act_join);
        getSupportActionBar().setTitle(getText(R.string.join));
        //액션바 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF56aed2));
        //홈버튼 표시
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_navigate_before_white_36dp);

        mEditUserId = (EditText) findViewById(R.id.edit_user_id);
        mEditUserName = (EditText) findViewById(R.id.edit_user_name);
        mEditEmail = (EditText) findViewById(R.id.edit_user_email);
        mEditUserPw = (EditText) findViewById(R.id.edit_user_pw);
        mEditUserPwConfirm = (EditText) findViewById(R.id.edit_user_pw_confirm);
        mEditUserPhoneNumber = (EditText) findViewById(R.id.edit_user_phone_number);
        mEditAuthCode = (EditText) findViewById(R.id.edit_auth_code);
        mTxtSchoolName = (TextView) findViewById(R.id.txt_find_school);
        mBtnDuplicationCheckUserId = (Button) findViewById(R.id.btn_user_id_dup_check);
        mBtnDuplicationCheckUserEmail = (Button) findViewById(R.id.btn_user_email_dup_check);
        mBtnRequestAuthCode = (Button) findViewById(R.id.btn_request_auth_code);
        mBtnSendAuthCode = (Button) findViewById(R.id.btn_send_auth_code);
        mBtnFindSchool = (Button) findViewById(R.id.btn_find_school);
        mBtnJoin = (Button) findViewById(R.id.btn_ok);
        mImgUserNameValidation = (ImageView) findViewById(R.id.img_user_name_validation);
        mImgUserPwValidation = (ImageView) findViewById(R.id.img_user_pw_validation);
        mImgUserPwConfirmValidation = (ImageView) findViewById(R.id.img_user_pw_confirm_validation);
        mImgLoadingAnim = (ImageView) findViewById(R.id.img_loading);

        mAnimLoading = (AnimationDrawable) mImgLoadingAnim.getBackground();
        stopLoadingAnimation();

        mEditUserPhoneNumber.setEnabled(true);
        mBtnRequestAuthCode.setEnabled(true);
        mBtnSendAuthCode.setEnabled(false);
        mEditAuthCode.setEnabled(false);

        mSpinnerSchool = (Spinner) findViewById(R.id.school_spinner);
        mSpinnerGrade = (Spinner) findViewById(R.id.grade_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.school_grades, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mSpinnerSchool.setAdapter(adapter);
        mSpinnerSchool.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayAdapter<CharSequence> adapter2 = null;
                if (i == 0) {
                    adapter2 = ArrayAdapter.createFromResource(ActJoin.this,
                            R.array.elementary_grade, R.layout.spinner_item);
                    adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
                } else {
                    adapter2 = ArrayAdapter.createFromResource(ActJoin.this,
                            R.array.normal_grade, R.layout.spinner_item);
                    adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
                }
                mSpinnerGrade.setAdapter(adapter2);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.elementary_grade, R.layout.spinner_item);
        adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mSpinnerGrade.setAdapter(adapter2);
    }

    private void setEditTextConstraints() {
        mUtil.printLog(DEBUG, TAG, "[setEditTextConstraints]");
        InputFilter nameFilterArray[][] = new InputFilter[6][1];
        nameFilterArray[0][0] = new InputFilter.LengthFilter(CM.MAX_LEN_USER_ID);
        nameFilterArray[1][0] = new InputFilter.LengthFilter(CM.MAX_LEN_USER_NAME);
        nameFilterArray[2][0] = new InputFilter.LengthFilter(CM.MAX_LEN_USER_PW);
        nameFilterArray[3][0] = new InputFilter.LengthFilter(CM.MAX_LEN_USER_PW);
        nameFilterArray[4][0] = new InputFilter.LengthFilter(CM.MAX_LEN_PHONE_NUMBER);
        nameFilterArray[5][0] = new InputFilter.LengthFilter(CM.AUTH_CODE_LEN);
        mEditUserId.setFilters(nameFilterArray[0]);
        mEditUserName.setFilters(nameFilterArray[1]);
        mEditUserPw.setFilters(nameFilterArray[2]);
        mEditUserPwConfirm.setFilters(nameFilterArray[3]);
        mEditUserPhoneNumber.setFilters(nameFilterArray[4]);
        mEditAuthCode.setFilters(nameFilterArray[5]);
    }

    private void setTextWatcher() {
        mTextWatcherUserName = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                mUtil.printLog(DEBUG, TAG, "[mTextWatcherUserName] [afterTextChanged] s : " + s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mUtil.printLog(DEBUG, TAG, "[mTextWatcherUserName] [beforeTextChanged] start : " + start +
                        ", count : " + count + ", after : " + after + ", s : " + s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mUtil.printLog(DEBUG, TAG, "[mTextWatcherUserName] [onTextChanged] start : " + start +
                        ", count : " + count + ", before : " + before + ", s : " + s.toString());
                if (s.length() < CM.MIN_LEN_USER_NAME ||
                        s.length() > CM.MAX_LEN_USER_NAME) {
                    mImgUserNameValidation.setImageResource(R.drawable.btn_cb_red);
                } else {
                    mImgUserNameValidation.setImageResource(R.drawable.btn_cb_blue);
                }
            }
        };
        mTextWatcherUserPw = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                mUtil.printLog(DEBUG, TAG, "[mTextWatcherUserPw] [afterTextChanged] s : " + s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mUtil.printLog(DEBUG, TAG, "[mTextWatcherUserPw] [beforeTextChanged] start : " + start + ""
                        + ", count : " + count + ", after : " + after + ", s : " + s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mUtil.printLog(DEBUG, TAG, "[mTextWatcherUserPw] [onTextChanged] start : " + start +
                        ", count : " + count + ", before : " + before + ", s : " + s.toString());
                if (s.length() < CM.MIN_LEN_USER_PW ||
                        s.length() > CM.MAX_LEN_USER_PW ||
                        !mUtil.isValidPassword("" + s)) {// || ! m_editUserPwConfirm.getText().toString().equals("" + s)) {
                    mImgUserPwValidation.setImageResource(R.drawable.btn_cb_red);
                } else {
                    mImgUserPwValidation.setImageResource(R.drawable.btn_cb_blue);
                }
            }
        };
        mTextWatcherUserPwConfirm = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                mUtil.printLog(DEBUG, TAG, "[mTextWatcherUserPwConfirm] [afterTextChanged] s : " + s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mUtil.printLog(DEBUG, TAG, "[mTextWatcherUserPwConfirm [beforeTextChanged] start : " + start + ", count : " + count + ", after : " + after + ", s : " + s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mUtil.printLog(DEBUG, TAG, "[mTextWatcherUserPwConfirm] [onTextChanged] start : " + start +
                        ", count : " + count + ", before : " + before + ", s : " + s.toString() + ", m_editUserPw : " + mEditUserPw.getText().toString());
                if (s.length() < CM.MIN_LEN_USER_PW ||
                        s.length() > CM.MAX_LEN_USER_PW ||
                        !mUtil.isValidPassword("" + s) ||
                        !mEditUserPw.getText().toString().equals("" + s)) {
                    mImgUserPwConfirmValidation.setImageResource(R.drawable.btn_cb_red);
                } else {
                    mImgUserPwConfirmValidation.setImageResource(R.drawable.btn_cb_blue);
                }
            }
        };
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_user_id_dup_check:
                mUtil.printLog(DEBUG, TAG, "[setBtnClickListener] mBtnDuplicationCheckUserId pressed");
                mInputUserID = mEditUserId.getText().toString();
                if (mImgLoadingAnim.getVisibility() != View.GONE) {
                    return;
                }
                if (mInputUserID.length() < CM.MIN_LEN_USER_ID || mInputUserID.length() > CM.MAX_LEN_USER_ID) {
                    Toast.makeText(this, getString(R.string.msg_invalid_user_id), Toast.LENGTH_SHORT).show();
                } else {
                    new DoCheckUserIdDuplication().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
                break;
            case R.id.btn_user_email_dup_check:
                mInputUserID = mEditUserId.getText().toString();
                mInputUserEmail = mEditEmail.getText().toString();
                mUtil.printLog(DEBUG, TAG, "[setBtnClickListener] mBtnDuplicationCheckUserEmail pressed");
                if (mImgLoadingAnim.getVisibility() != View.GONE) {
                    return;
                }
                if (mUtil.isValidEmailAddress(mInputUserEmail)) {
                    new DoCheckUserEmailDuplication().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    Toast.makeText(this, getString(R.string.msg_invalid_email_address), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_request_auth_code:
                mUtil.printLog(DEBUG, TAG, "[setBtnClickListener] mBtnRequestAuthCode pressed");
                mUserPhoneNumber = mUtil.getRefinedNumber(mEditUserPhoneNumber.getText().toString());
                if (mImgLoadingAnim.getVisibility() != View.GONE) {
                    return;
                }
                if (mUserPhoneNumber.length() < CM.MIN_LEN_PHONE_NUMBER || mUserPhoneNumber.length() > CM.MAX_LEN_PHONE_NUMBER) {
                    Toast.makeText(this, getString(R.string.msg_invalid_phone_number), Toast.LENGTH_SHORT).show();
                } else {
                    new DoRequestAuthCode().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
                break;
            case R.id.btn_send_auth_code:
                mUtil.printLog(DEBUG, TAG, "[setBtnClickListener] mBtnSendAuthCode pressed");
                if (mImgLoadingAnim.getVisibility() != View.GONE) {
                    return;
                }
                mAuthCode = mEditAuthCode.getText().toString();
                if (mAuthCode.length() != CM.AUTH_CODE_LEN) {
                    Toast.makeText(this, getString(R.string.msg_invalid_auth_code), Toast.LENGTH_SHORT).show();
                } else {
                    new DoSendAuthCode().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
                break;
            case R.id.btn_ok:
                mUtil.printLog(DEBUG, TAG, "[setBtnClickListener] mBtnJoin pressed");
                if (mImgLoadingAnim.getVisibility() != View.GONE) {
                    return;
                }

                if (checkJoinInfo()) {
                    mInputUserID = mEditUserId.getText().toString();
                    mInputUserName = mEditUserName.getText().toString();
                    mInputUserEmail = mEditEmail.getText().toString();
                    mInputUserPW = mEditUserPw.getText().toString();
                    mUserPhoneNumber = mUtil.getRefinedNumber(mEditUserPhoneNumber.getText().toString());
                    if(mUserSchoolGrade.equals(getString(R.string.elementary_student))){
                        mUserSchoolGrade = MemberInfo.ELEMENTARY_SCHOOL;
                    }else if(mUserSchoolGrade.equals(getString(R.string.middle_school_student))){
                        mUserSchoolGrade = MemberInfo.MIDDLE_SCHOOL;
                    }else if(mUserSchoolGrade.equals(getString(R.string.high_school_student))){
                        mUserSchoolGrade = MemberInfo.HIGH_SCHOOL;
                    }
                    mUserGrade = mSpinnerGrade.getSelectedItem().toString();
                    mUserSchoolName = mTxtSchoolName.getText().toString();
                    new DoJoin().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
                break;
            case R.id.btn_find_school:
                int grade = 0;
                mUserSchoolGrade = mSpinnerSchool.getSelectedItem().toString();
                if (mUserSchoolGrade.equals(getString(R.string.elementary_student))) {
                    grade = DialogSearchSchool.ELEMENTARY_SCHOOL;
                } else if (mUserSchoolGrade.equals(getString(R.string.middle_school_student))) {
                    grade = DialogSearchSchool.MIDDLE_SCHOOL;
                } else {
                    grade = DialogSearchSchool.HIGH_SCHOOL;
                }

                new DialogSearchSchool(this, grade, new DialogSearchSchool.ISchoolNameListener() {
                    @Override
                    public void getSchoolName(String schoolName) {
                        mTxtSchoolName.setText(schoolName);
                    }
                }).show();
                //Toast.makeText(this,getString(R.string.msg_wait_please),Toast.LENGTH_SHORT).show();
                break;
        }

    }

    public boolean checkJoinInfo() {
        mUtil.printLog(DEBUG, TAG, "[checkJoinInfo]");

        String userId = mEditUserId.getText().toString();
        if (userId.length() < CM.MIN_LEN_USER_ID || userId.length() > CM.MAX_LEN_USER_ID) {
            Toast.makeText(this, getString(R.string.msg_invalid_user_id), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!mVerifiedUserId.equals(userId)) {
            Toast.makeText(this, getString(R.string.msg_check_user_id_duplication), Toast.LENGTH_SHORT).show();
            return false;
        }

        String userName = mEditUserName.getText().toString();
        if (userName.length() < CM.MIN_LEN_USER_NAME || userName.length() > CM.MAX_LEN_USER_NAME) {
            Toast.makeText(this, getString(R.string.msg_invalid_user_name), Toast.LENGTH_SHORT).show();
            return false;
        }

        String userEmail = mEditEmail.getText().toString();
        if (!mUtil.isValidEmailAddress(userEmail)) {
            Toast.makeText(this, getString(R.string.msg_invalid_email_address), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!mVerifiedUserEmail.equals(userEmail)) {
            Toast.makeText(this, getString(R.string.msg_check_user_email_duplication), Toast.LENGTH_SHORT).show();
            return false;
        }

        String userPw = mEditUserPw.getText().toString();
        if (userPw.length() < CM.MIN_LEN_USER_PW || userPw.length() > CM.MAX_LEN_USER_PW || !mUtil.isValidPassword("" + userPw)) {
            Toast.makeText(this, getString(R.string.msg_invalid_user_pw), Toast.LENGTH_SHORT).show();
            return false;
        }

        String userPwConfirm = mEditUserPwConfirm.getText().toString();
        if (!userPwConfirm.equals(userPw)) {
            Toast.makeText(this, getString(R.string.msg_mismatch_user_pw_and_pw_confirm), Toast.LENGTH_SHORT).show();
            return false;
        }

        String userPhoneNumber = mUtil.getRefinedNumber(mEditUserPhoneNumber.getText().toString());
        if (userPhoneNumber.length() < CM.MIN_LEN_PHONE_NUMBER || userPhoneNumber.length() > CM.MAX_LEN_PHONE_NUMBER) {
            Toast.makeText(this, getString(R.string.msg_invalid_phone_number), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!mIsAuthenticated) {
            Toast.makeText(this, getString(R.string.msg_invalid_phone_number), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);

    }


    private class DoCheckUserIdDuplication extends AsyncTask<String, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            mUtil.printLog(DEBUG, TAG, "[DoCheckUserIdDuplication] [onPreExecute]");
            startLoadingAnimation();
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... strData) {
            mUtil.printLog(DEBUG, TAG, "[DoCheckUserIdDuplication] [doInBackground]");

            return DM.getInstance().checkUserId(mInputUserID);
        }

        @Override
        protected void onPostExecute(Integer result) {
            mUtil.printLog(DEBUG, TAG, "[DoCheckUserIdDuplication] [onPostExecute] result : " + result);
            if (result != DM.RES_SUCCESS) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopLoadingAnimation();
                        mVerifiedUserId = "";
                        Toast.makeText(ActJoin.this, getString(R.string.msg_user_id_duplicated), Toast.LENGTH_SHORT).show();

                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopLoadingAnimation();
                        mVerifiedUserId = mEditUserId.getText().toString();
                        Toast.makeText(ActJoin.this, getString(R.string.msg_user_id_not_duplicated), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        @Override
        protected void onCancelled() {
            mUtil.printLog(DEBUG, TAG, "[DoCheckUserIdDuplication] [onCancelled]");
            stopLoadingAnimation();
            super.onCancelled();
        }
    }

    private class DoCheckUserEmailDuplication extends AsyncTask<String, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            mUtil.printLog(DEBUG, TAG, "[DoCheckUserEmailDuplication][onPreExecute]");
            startLoadingAnimation();
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... strData) {
            mUtil.printLog(DEBUG, TAG, "[DoCheckUserEmailDuplication] [doInBackground]");

            return DM.getInstance().checkUserEmail(mInputUserID, mInputUserEmail);
        }

        @Override
        protected void onPostExecute(Integer result) {
            mUtil.printLog(DEBUG, TAG, "[DoCheckUserEmailDuplication] [onPostExecute] result : " + result);
            if (result != DM.RES_SUCCESS) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopLoadingAnimation();
                        mVerifiedUserEmail = "";
                        Toast.makeText(ActJoin.this, getString(R.string.msg_user_email_duplicated), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopLoadingAnimation();
                        mVerifiedUserEmail = mEditEmail.getText().toString();
                        Toast.makeText(ActJoin.this, getString(R.string.msg_user_email_not_duplicated), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        @Override
        protected void onCancelled() {
            mUtil.printLog(DEBUG, TAG, "[DoCheckUserEmailDuplication] [onCancelled]");
            stopLoadingAnimation();
            super.onCancelled();
        }
    }

    private class DoRequestAuthCode extends AsyncTask<String, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            mUtil.printLog(DEBUG, TAG, "[DoRequestAuthCode] [onPreExecute]");
            startLoadingAnimation();
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... strData) {
            mUtil.printLog(DEBUG, TAG, "[DoRequestAuthCode] [doInBackground]");

            mHashMapResult = new HashMap<String, String>();
            mHashMapResult.put("phonenumber", mUserPhoneNumber);
            return DM.getInstance().requestSMSAuth(mUserPhoneNumber, "0", mHashMapResult);
        }

        @Override
        protected void onPostExecute(Integer result) {
            mUtil.printLog(DEBUG, TAG, "[DoRequestAuthCode] [onPostExecute] result : " + result);
            /**
             * What ? 2022
             * **/
            if (result == 2022) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopLoadingAnimation();
                        Toast.makeText(ActJoin.this, getString(R.string.msg_exceed_request_auth_code), Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (result == DM.RES_SUCCESS) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopLoadingAnimation();
                        for (String key : mHashMapResult.keySet()) {
                            mUtil.printLog(DEBUG, TAG, "[DoRequestAuthCode] [onCancelled] [m_hashMapResult] " + key + " : " + mHashMapResult.get(key));
                        }
                        mEditUserPhoneNumber.setEnabled(false);
                        mBtnRequestAuthCode.setEnabled(false);
                        mBtnSendAuthCode.setEnabled(true);
                        mEditAuthCode.setEnabled(true);
                    }
                });
            }
        }

        @Override
        protected void onCancelled() {
            mUtil.printLog(DEBUG, TAG, "[DoRequestAuthCode] [onCancelled]");
            stopLoadingAnimation();
            super.onCancelled();
        }
    }

    private class DoSendAuthCode extends AsyncTask<String, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            mUtil.printLog(DEBUG, TAG, "[DoSendAuthCode] [onPreExecute]");
            startLoadingAnimation();
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... strData) {
            mUtil.printLog(DEBUG, TAG, "[DoSendAuthCode] [doInBackground]");
//			mUtil.printLog(DEBUG, TAG, "[DoSendAuthCode][doInBackground] authCode : " + authCode + ", phoneNumber : " + phoneNumber);
            return DM.getInstance().requestSMSConfirm(
                    mHashMapResult.get("RES_SEQ"),
                    mHashMapResult.get("REQ_SEQ"),
                    mAuthCode,
                    mHashMapResult
            );
        }

        @Override
        protected void onPostExecute(Integer result) {
            mUtil.printLog(DEBUG, TAG, "[DoSendAuthCode] [onPostExecute] result : " + result);
            if (result != DM.RES_SUCCESS) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopLoadingAnimation();
                        Toast.makeText(ActJoin.this, getString(R.string.msg_wrong_auth_code), Toast.LENGTH_SHORT).show();
                        mEditUserPhoneNumber.setEnabled(true);
                        mBtnRequestAuthCode.setEnabled(true);
                        mBtnSendAuthCode.setEnabled(false);
                        mEditAuthCode.setEnabled(false);
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopLoadingAnimation();
                        Toast.makeText(ActJoin.this, getString(R.string.msg_success_auth_code), Toast.LENGTH_SHORT).show();
                        mIsAuthenticated = true;
                        mEditUserPhoneNumber.setEnabled(false);
                        mBtnRequestAuthCode.setEnabled(false);
                        mBtnSendAuthCode.setEnabled(false);
                        mEditAuthCode.setEnabled(false);
                    }
                });
            }
        }

        @Override
        protected void onCancelled() {
            mUtil.printLog(DEBUG, TAG, "[DoSendAuthCode] [onCancelled]");
            stopLoadingAnimation();
            super.onCancelled();
        }
    }

    private class DoJoin extends AsyncTask<String, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            mUtil.printLog(DEBUG, TAG, "[DoJoin] [onPreExecute]");
            startLoadingAnimation();
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... strData) {
            mUtil.printLog(DEBUG, TAG, "[DoJoin] [doInBackground]");

            return DM.getInstance().memberJoin(
                    mInputUserID,
                    mInputUserName,
                    mInputUserPW,
                    "",
                    mUserPhoneNumber,
                    mInputUserEmail,
                    mUserSchoolGrade,
                    mUserGrade,
                    mUserSchoolName
            );
        }

        @Override
        protected void onPostExecute(Integer result) {
            mUtil.printLog(DEBUG, TAG, "[DoJoin] [onPostExecute] result : " + result);
            if (result == DM.RES_SUCCESS) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopLoadingAnimation();
                        Toast.makeText(ActJoin.this, getString(R.string.msg_join_success_alert_title), Toast.LENGTH_SHORT).show();
                        CM.getInstance().setUserId(mEditUserId.getText().toString().trim());
                        CM.getInstance().setUserPwd(RSASecurity.getInstance().RSAEncrypt(mEditUserPw.getText().toString().trim()));
                        finish();
                    }
                });
            } else if (result == DM.NRC_EXCEED_PHONENUM) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopLoadingAnimation();
                        Toast.makeText(ActJoin.this, getString(R.string.nrc_exceed_phonenum), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopLoadingAnimation();
                        Toast.makeText(ActJoin.this, getString(R.string.msg_join_failed), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        @Override
        protected void onCancelled() {
            mUtil.printLog(DEBUG, TAG, "[DoJoin] [onCancelled]");
            stopLoadingAnimation();
            super.onCancelled();
        }
    }
}
