package com.smartware.gucongc;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartware.common.Utils;
import com.smartware.container.TargetTreeData;
import com.smartware.manager.CM;
import com.smartware.manager.DM;

/**
 * Created by Juho.S on 2017-06-02.
 */
public class DialogEditScore extends Dialog implements View.OnClickListener {
    private final static boolean DEBUG = Utils.DEBUG;
    private final static String TAG = "DialogEditScore";

    public final static String DIALOG_DATE = "dialog_date";
    public final static String DIALOG_SCORE = "dialog_score";
    public final static String GOAL_SEQ = "goal_seq";

    private Context mContext;
    private Bundle mBundle;
    private TargetTreeData mData;

    public DialogEditScore(Context context, Bundle bundle/*, TargetTreeData data*/) {
        super(context);
        mContext = context;
        mBundle = bundle;
        /*mData = data;*/
    }


    private TextView mTitle;
    private TextView mTxtDate;
    private EditText mEditScore;
    private Button mBtn_OK;
    private Button mBtn_Cancel;
    private ImageView mImgLoadingAnim;
    private AnimationDrawable mAnimLoading;

    private String mDate;
    private String mScore;


    private CM mCM;
    private Utils mUtils = Utils.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams dlWindow = new WindowManager.LayoutParams();
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        dlWindow.dimAmount = 0.5f;
        getWindow().setAttributes(dlWindow);
        setContentView(R.layout.dialog_edit_score);
        mDate = mBundle.getString(DIALOG_DATE);
        mScore = mBundle.getString(DIALOG_SCORE);

        initView();
    }

    private void initView() {
        mImgLoadingAnim = (ImageView) findViewById(R.id.img_loading);
        mAnimLoading = (AnimationDrawable) mImgLoadingAnim.getDrawable();
        stopLoadingAnimation();

        mTitle = (TextView) findViewById(R.id.txt_dialog_title);
        mTxtDate = (TextView) findViewById(R.id.txt_dialog_date);
        mEditScore = (EditText) findViewById(R.id.edit_dialog_score);
        mBtn_OK = (Button) findViewById(R.id.btn_ok);
        mBtn_Cancel = (Button) findViewById(R.id.btn_cancel);
        mTxtDate.setText(mDate + mContext.getString(R.string.day));
        mBtn_OK.setOnClickListener(this);
        mBtn_Cancel.setOnClickListener(this);
        mEditScore.setText(mScore);
        mEditScore.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 1) {
                    if (s.length() > 1 && s.toString().startsWith("0")) {
                        mEditScore.setText("" + Integer.parseInt(s.toString()));
                        mEditScore.setSelection(mEditScore.length());
                    }
                    if (Integer.parseInt(s.toString()) > 100) {
                        mEditScore.setText("" + 100);
                        mEditScore.setSelection(mEditScore.length());
                    }

                }

            }
        });
        mEditScore.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                mUtils.printLog(DEBUG, TAG, "[onKey] keyevent : " + keyEvent.toString());
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                    mUtils.printLog(DEBUG, TAG, "[onKey] KEYCODE_ENTER");
                    if (mEditScore.getText().toString().equals("")) {
                        mEditScore.setText("" + 0);
                    } else if (Integer.parseInt(String.valueOf(mEditScore.getText())) > 101) {
                        mEditScore.setText("" + 100);
                    }
                    mUtils.hideSoftKeyboard(mContext, mEditScore);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            mEditScore.requestFocus();
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                mScore = mEditScore.getText().toString();
                if (mScore.length() > 0) {
                    new DoInputDateScore().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }

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

    protected class DoInputDateScore extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            startLoadingAnimation();
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... strings) {
            return DM.getInstance().setDayScore(mBundle.getString(GOAL_SEQ), Integer.parseInt(mDate), Integer.parseInt(mScore));
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if (integer == DM.RES_SUCCESS) {
                stopLoadingAnimation();
                dismiss();
            } else if (integer == DM.RES_FAIL) {
                stopLoadingAnimation();
                dismiss();
            }
            super.onPostExecute(integer);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
