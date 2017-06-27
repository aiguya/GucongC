package com.smartware.gucongc;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smartware.common.Utils;
import com.smartware.container.MemberInfo;
import com.smartware.manager.CM;
import com.smartware.manager.DM;

/**
 * Created by Juho.S on 2017-06-15.
 */
public class ActFindUserIDPW extends AppCompatActivity {

    private static final boolean DEBUG = true;
    private static final String TAG = "ActFindUserIDPW";
    private Utils mUtil = Utils.getInstance();

    private EditText mEditTextFindUserIdUserName;
    private EditText mEditTextFindUserIdUserEmail;
    private EditText mEditTextFindUserPwUserId;
    private EditText mEditTextFindUserPwUserEmail;
    private ImageButton mBtnClearEditFIndUserIdUserName;
    private ImageButton mBtnClearEditFIndUserIdUserEmail;
    private ImageButton mBtnClearEditFIndUserPwUserId;
    private ImageButton mBtnClearEditFIndUserPwUserEmail;
    private Button mBtnFindUserId;
    private Button mBtnFindUserPw;
    private ImageView mImgLoadingAnim;

    private AnimationDrawable mAnimLoading;

    private InputMethodManager mInputMethodManager;

    private String mUserID = new String();
    private String mUserName;
    private String mUserEmail;
    private String mUserPW;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setEditTextEnterKeyListener();
    }

    private void initView() {
        mUtil.printLog(DEBUG, TAG, "[initView]");
        setContentView(R.layout.act_find_user_id_pw);
        getSupportActionBar().setTitle(getText(R.string.find_idpw));
        //액션바 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF56aed2));
        //홈버튼 표시
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_navigate_before_white_36dp);
        mEditTextFindUserIdUserName = (EditText) findViewById(R.id.edit_find_user_id_user_name);
        mBtnClearEditFIndUserIdUserName = (ImageButton) findViewById(R.id.btn_clear_find_id_user_name);
        mEditTextFindUserIdUserEmail = (EditText) findViewById(R.id.edit_find_user_id_user_email);
        mBtnClearEditFIndUserIdUserEmail = (ImageButton) findViewById(R.id.btn_clear_find_id_user_email);
        mEditTextFindUserPwUserId = (EditText) findViewById(R.id.edit_find_user_pw_user_id);
        mBtnClearEditFIndUserPwUserId = (ImageButton) findViewById(R.id.btn_clear_find_pw_user_id);
        mEditTextFindUserPwUserEmail = (EditText) findViewById(R.id.edit_find_user_pw_email);
        mBtnClearEditFIndUserPwUserEmail = (ImageButton) findViewById(R.id.btn_clear_find_pw_user_email);
        mBtnFindUserId = (Button) findViewById(R.id.btn_user_id_by_email);
        mBtnFindUserPw = (Button) findViewById(R.id.btn_user_pw_by_email);
        mImgLoadingAnim = (ImageView) findViewById(R.id.img_loading);

        mAnimLoading = (AnimationDrawable) mImgLoadingAnim.getBackground();
        stopLoadingAnimation();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    private void hideKeyboard() {
        mUtil.printLog(DEBUG, TAG, "[hideKeyboard]");
        mInputMethodManager.hideSoftInputFromWindow(mEditTextFindUserIdUserName.getWindowToken(), 0);
        mInputMethodManager.hideSoftInputFromWindow(mEditTextFindUserIdUserEmail.getWindowToken(), 0);
        mInputMethodManager.hideSoftInputFromWindow(mEditTextFindUserPwUserId.getWindowToken(), 0);
        mInputMethodManager.hideSoftInputFromWindow(mEditTextFindUserPwUserEmail.getWindowToken(), 0);
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
            case R.id.btn_clear_find_id_user_name:
                mEditTextFindUserIdUserName.setText("");
                break;
            case R.id.btn_clear_find_id_user_email:
                mEditTextFindUserIdUserEmail.setText("");
                break;
            case R.id.btn_clear_find_pw_user_id:
                mEditTextFindUserPwUserId.setText("");
                break;
            case R.id.btn_clear_find_pw_user_email:
                mEditTextFindUserPwUserEmail.setText("");
                break;
            case R.id.btn_user_id_by_email:
                mUserName = mEditTextFindUserIdUserName.getText().toString();
                mUserEmail = mEditTextFindUserIdUserEmail.getText().toString();
                if (checkUserNameAndEmail()) {
                    new DoFindUserId().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
                break;
            case R.id.btn_user_pw_by_email:
                mUserID = mEditTextFindUserPwUserId.getText().toString();
                mUserEmail = mEditTextFindUserPwUserEmail.getText().toString();
                if (checkUserNameAndEmail()) {
                    new DoFindUserPw().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
                break;
        }
    }

    public boolean checkUserNameAndEmail() {
        mUtil.printLog(DEBUG, TAG, "[checkUserNameAndEmail]");
        String userName = mEditTextFindUserIdUserName.getText().toString();
        if (userName.length() < CM.MIN_LEN_USER_NAME || userName.length() > CM.MAX_LEN_USER_NAME) {
            Toast.makeText(this, R.string.msg_invalid_user_name, Toast.LENGTH_SHORT).show();
            return false;
        }
        String userEmail = mEditTextFindUserIdUserEmail.getText().toString();
        if (!mUtil.isValidEmailAddress(userEmail)) {
            Toast.makeText(this, R.string.msg_invalid_email_address, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean checkUserIdAndEmail() {
        mUtil.printLog(DEBUG, TAG, "[checkUserIdAndEmail]");
        String userId = mEditTextFindUserPwUserId.getText().toString();
        if (userId.length() < CM.MIN_LEN_USER_ID || userId.length() > CM.MAX_LEN_USER_ID) {
            Toast.makeText(this, R.string.msg_invalid_user_id, Toast.LENGTH_SHORT).show();
            return false;
        }
        String userEmail = mEditTextFindUserPwUserEmail.getText().toString();
        if (!mUtil.isValidEmailAddress(userEmail)) {
            Toast.makeText(this, R.string.msg_invalid_email_address, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showWarningDlg(String title, String msg) {
        mUtil.printLog(DEBUG, TAG, "[showWarningDlg] title : " + title + ", msg : " + msg);
        showWarningDlg(title, msg, false);
    }

    private void showWarningDlg(String title, String msg, final boolean bActFinish) {
        mUtil.printLog(DEBUG, TAG, "[showWarningDlg] title : " + title + ", msg : " + msg + ", bActFinish : " + bActFinish);
        showWarningDlg(title, msg, bActFinish, RESULT_CANCELED);
    }

    private void showWarningDlg(String title, String msg, final boolean bActFinish, final int resultCode) {
        mUtil.printLog(DEBUG, TAG, "[showWarningDlg] title : " + title + ", msg : " + msg + ", bActFinish : " + bActFinish + ", resultCode : " + resultCode);

        final Dialog dialog = new Dialog(this,  android.R.style.Theme_Translucent_NoTitleBar);
        WindowManager.LayoutParams dlWindow = new WindowManager.LayoutParams();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        dlWindow.dimAmount = 0.5f;
        dialog.getWindow().setAttributes(dlWindow);
        dialog.setContentView(R.layout.dialog_no_title_bar_one_btn);


        final TextView textTitle = (TextView) dialog.findViewById(R.id.txt_dialog_title);
        textTitle.setText(title);

        final TextView		textMessage = (TextView) dialog.findViewById(R.id.txt_dialog_contents);
        textMessage.setText(msg + "\n");

        final Button			btnDlgOk = (Button) dialog.findViewById(R.id.btn_ok);
        btnDlgOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (bActFinish) {
                    finish();
                }
            }
        });
        dialog.show();
    }

    private void setEditTextEnterKeyListener() {
        mUtil.printLog(DEBUG, TAG, "[setEditTextEnterKeyListener]");
        mEditTextFindUserIdUserEmail.setOnEditorActionListener( new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ( actionId == EditorInfo.IME_ACTION_DONE ) {
                    mUtil.printLog(DEBUG, TAG, "[setEditTextEnterKeyListener] mEditTextFindUserIdUserEmail - EditorInfo.IME_ACTION_DONE");
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });
        mEditTextFindUserPwUserEmail.setOnEditorActionListener( new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ( actionId == EditorInfo.IME_ACTION_DONE ) {
                    mUtil.printLog(DEBUG, TAG, "[setEditTextEnterKeyListener] mEditTextFindUserPwUserEmail - EditorInfo.IME_ACTION_DONE");
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });
    }
    private class DoFindUserId extends AsyncTask<String, Integer, Integer> {
        private MemberInfo 			mMemberInfo = new MemberInfo();

        @Override
        protected void onPreExecute() {
            mUtil.printLog(DEBUG, TAG, "[DoFindUserId] [onPreExecute]");
            startLoadingAnimation();
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... strData) {
            mUtil.printLog(DEBUG, TAG, "[DoFindUserId] [doInBackground]");
            return DM.getInstance().findId(mUserName, mUserEmail, mMemberInfo);
        }

        @Override
        protected void onPostExecute(Integer result) {
            mUtil.printLog(DEBUG, TAG, "[DoFindUserId] [onPostExecute] result : " + result);
            stopLoadingAnimation();
            if ( result == DM.RES_SUCCESS ) {
                /** HARD_CODED **/
                showWarningDlg(
                        getString(R.string.find_id),
                        getString(R.string.msg_find_id_sussess_result) + mMemberInfo.getMemberId() + " 입니다.",
                        false
                );
            } else {
                showWarningDlg(
                        getString(R.string.find_id),
                        getString(R.string.msg_find_id_error_result),
                        false
                );
            }
        }

        @Override
        protected void onCancelled() {
            mUtil.printLog(DEBUG, TAG, "[DoFindUserId] [onCancelled]");
            stopLoadingAnimation();
            super.onCancelled();
        }
    }

    private class DoFindUserPw extends AsyncTask<String, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            mUtil.printLog(DEBUG, TAG, "[DoFindUserPw] [onPreExecute]");
            startLoadingAnimation();
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... strData) {
            mUtil.printLog(DEBUG, TAG, "[DoFindUserPw] [doInBackground]");
            return DM.getInstance().findPw(mUserID, mUserEmail);
        }

        @Override
        protected void onPostExecute(Integer result) {
            mUtil.printLog(DEBUG, TAG, "[DoFindUserPw] [onPostExecute] result : " + result);
            stopLoadingAnimation();
            if ( result == DM.RES_SUCCESS ) {
                showWarningDlg(
                        getString(R.string.find_pw),
                        getString(R.string.msg_send_user_pw_to_email),
                        false,
                        RESULT_CANCELED
                );
            } else {
                showWarningDlg(
                        getString(R.string.find_pw),
                        getString(R.string.msg_find_pw_error_result),
                        false
                );
            }
        }

        @Override
        protected void onCancelled() {
            mUtil.printLog(DEBUG, TAG, "[DoFindUserPw] [onCancelled]");
            stopLoadingAnimation();
            super.onCancelled();
        }
    }
}
