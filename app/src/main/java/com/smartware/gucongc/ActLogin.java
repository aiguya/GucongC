package com.smartware.gucongc;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smartware.common.RSASecurity;
import com.smartware.common.Utils;
import com.smartware.container.UserData;
import com.smartware.manager.CM;
import com.smartware.manager.DM;

import java.util.UUID;

/**
 * Created by Juho.S on 2017-06-08.
 */
public class ActLogin extends Activity {
    private static final boolean DEBUG = true;
    private static final String TAG = "ActLogin";
    private Utils mUtils = Utils.getInstance();
    private CM mCm = CM.getInstance();

    private EditText mEditUserId;
    private EditText mEditUserPw;
    private ImageView mImgLoadingAnim;
    private Button mBtnLogin;
    private String mInputID;
    private String mInputPW;

    private AnimationDrawable mAnimLoading;

    private InputMethodManager mInputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RSASecurity.getInstance().setContext(ActLogin.this);
        mCm.setContext(this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        initView();
        setEditTextConstraints();
        setEditTextEnterKeyListener();
        if (mCm.getDeviceId().equals("")) {
            String uuid = makeDeviceId();
            mCm.setDeviceId(uuid);
        }



    }
    private void initView(){
        setContentView(R.layout.act_login);
        mEditUserId = (EditText) findViewById(R.id.edit_user_id);
        mEditUserPw = (EditText) findViewById(R.id.edit_user_pwd);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mImgLoadingAnim = (ImageView) findViewById(R.id.img_loading);
        mAnimLoading = (AnimationDrawable) mImgLoadingAnim.getBackground();
        stopLoadingAnimation();
    }

    private void startLoadingAnimation() {
        mUtils.printLog(DEBUG, TAG, "[startLoadingAnimation]");
        mImgLoadingAnim.setVisibility(View.VISIBLE);
        mAnimLoading.start();
        mEditUserId.setEnabled(false);
        mEditUserPw.setEnabled(false);
    }

    private void stopLoadingAnimation() {
        mUtils.printLog(DEBUG, TAG, "[stopLoadingAnimation]");
        mImgLoadingAnim.setVisibility(View.GONE);
        mAnimLoading.stop();
        mEditUserId.setEnabled(true);
        mEditUserPw.setEnabled(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        mUtils.printLog(DEBUG, TAG, "[onKeyDown] keyCode : " + keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                finish();
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                new DoUserLogin().executeOnExecutor(
                        AsyncTask.THREAD_POOL_EXECUTOR,
                        mEditUserId.getText().toString().trim(),
                        RSASecurity.getInstance().RSAEncrypt( mEditUserPw.getText().toString().trim() ) );

                break;
            case R.id.btn_join:
                startActivity(new Intent(this, ActJoin.class));
                break;
            case R.id.btn_find_idpw:
                startActivity(new Intent(this, ActFindUserIDPW.class));
                break;

        }

    }
    private void hideKeyboard() {
        mUtils.printLog(DEBUG, TAG, "[hideKeyboard]");
        mInputMethodManager.hideSoftInputFromWindow(mEditUserId.getWindowToken(), 0);
        mInputMethodManager.hideSoftInputFromWindow(mEditUserPw.getWindowToken(), 0);
    }

    private void setEditTextConstraints() {
        mUtils.printLog(DEBUG, TAG, "[setEditTextConstraints]");
        InputFilter nameFilterArray[][] = new InputFilter[2][1];
        nameFilterArray[0][0] = new InputFilter.LengthFilter(CM.MAX_LEN_USER_ID);
        nameFilterArray[1][0] = new InputFilter.LengthFilter(CM.MAX_LEN_USER_PW);
        mEditUserId.setFilters(nameFilterArray[0]);
        mEditUserPw.setFilters(nameFilterArray[1]);
    }

    private void setEditTextEnterKeyListener() {
        mUtils.printLog(DEBUG, TAG, "[setEditTextEnterKeyListener]");
        mEditUserPw.setOnEditorActionListener( new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ( actionId == EditorInfo.IME_ACTION_DONE ) {
                    mUtils.printLog(DEBUG, TAG, "[setEditTextEnterKeyListener] m_editUserPw - EditorInfo.IME_ACTION_DONE");
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus && !mCm.getUserId().equals("") && !mCm.getUserPwd().equals("")){
            new DoUserLogin().executeOnExecutor(
                    AsyncTask.THREAD_POOL_EXECUTOR,
                    mCm.getUserId(),
                    mCm.getUserPwd() );
        }else if(hasFocus){
            mEditUserId.setText(mCm.getUserId());
        }
    }

    private String makeDeviceId() {
        mUtils.printLog(DEBUG, TAG, "[makeDeviceId]");

        final TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        final String                     	tmDevice = tm.getDeviceId();
        final String                     	tmSerial = tm.getSimSerialNumber();
        final String                     	androidId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid;
        if (tmSerial != null && tmSerial.length() > 0) {
            deviceUuid = new UUID(
                    androidId.hashCode(),
                    ( (long) tmDevice.hashCode() << 32 ) | tmSerial.hashCode() );
        } else {
            if (tmDevice != null && tmDevice.length() > 0) {
                deviceUuid = new UUID(
                        androidId.hashCode(),
                        ( (long) tmDevice.hashCode() << 32 ) );
            } else {
                deviceUuid = new UUID(
                        androidId.hashCode(),
                        ( (long) 0x00 ) );
            }
        }

        String								deviceUUID = deviceUuid.toString();
        mUtils.printLog(DEBUG, TAG, "[makeDeviceId] deviceUUID : " + deviceUUID);

        return deviceUUID;
    }

    private class DoUserLogin extends AsyncTask<String, Integer, Integer> {

        private String 			mUserId = "";
        private String 			mUserPwd = "";

        @Override
        protected void onPreExecute() {
            mUtils.printLog(DEBUG, TAG, "[DoUserLogin] [onPreExecute]");
            startLoadingAnimation();
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... strData) {
            mUtils.printLog(DEBUG, TAG, "[DoUserLogin] [doInBackground] strData[0] : " + strData[0] + ", strData[1] : " + strData[1]);
            mUserId = strData[0];
            mUserPwd = strData[1];
            return DM.getInstance().requestLogin(strData[0], strData[1], "i");
        }

        @Override
        protected void onPostExecute(Integer result) {
            mUtils.printLog(DEBUG, TAG, "[DoUserLogin] [onPostExecute] result : " + result);

            stopLoadingAnimation();

            if ( result == DM.NRC_SUCCESS || result == DM.NRC_LOGIN_DEVICE_EXCEED ) {
                Toast.makeText(ActLogin.this, "로그인 성공.", Toast.LENGTH_SHORT).show();
                mCm.setLoginStatus(true);
                mCm.setUserId(mUserId);
                mCm.setUserPwd(mUserPwd);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mEditUserPw.setText("");
                        stopLoadingAnimation();
                        startActivity(new Intent(ActLogin.this, ActMain.class));
                    }
                });

            } else if ( result == DM.NRC_USER_NOT_EXIST ) {
                //아이디없음
//                mUtils.showToast(ActLogin.this, DM.getInstance().getErrorString(ActLogin.this, DM.NRC_USER_NOT_EXIST));
                Toast.makeText(ActLogin.this, "ID를 확인하세요.", Toast.LENGTH_SHORT).show();

            } else if ( result == DM.NRC_PASSWORD_MISMATCH ) {
                //mismatch
//                mUtils.showToast(ActLogin.this, DM.getInstance().getErrorString(ActLogin.this, DM.NRC_PASSWORD_MISMATCH));
                Toast.makeText(ActLogin.this, "비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show();

            }
        }

        @Override
        protected void onCancelled() {
            mUtils.printLog(DEBUG, TAG, "[DoUserLogin] [onCancelled]");
            stopLoadingAnimation();
            super.onCancelled();
        }
    }
}
