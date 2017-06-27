package com.smartware.gucongc;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smartware.common.Utils;
import com.smartware.manager.CM;
import com.smartware.manager.DM;

/**
 * Created by Juho.S on 2017-06-02.
 */
public class DialogEditTarget extends Dialog implements View.OnClickListener{
    private final static boolean DEBUG = Utils.DEBUG;
    private final static String TAG = "DialogEditScore";

    public final static String TITLE = "title";
    public final static String GOAL_SEQ = "goal_seq";

    private Context mContext;
    private Bundle mBundle;


    public DialogEditTarget(Context context, Bundle bundle) {
        super(context);
        mContext = context;
        mBundle = bundle;

    }


    private TextView mTxtTitle;
    private EditText mEditTarget;
    private Button mBtn_OK;
    private Button mBtn_Cancel;
    private ImageView mImgLoadingAnim;
    private AnimationDrawable mAnimLoading;

    private String mTarget;
    private String mGoal_SEQ = "";


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
        setContentView(R.layout.dialog_edit_target);
        mTarget = mBundle.getString(TITLE);
        mGoal_SEQ = mBundle.getString(GOAL_SEQ);
        mCM = CM.getInstance();
        initView();
    }

    private void initView() {
        mTxtTitle = (TextView) findViewById(R.id.txt_dialog_title);
        mEditTarget = (EditText) findViewById(R.id.edit_dialog_target);
        mBtn_OK = (Button) findViewById(R.id.btn_ok);
        mBtn_Cancel = (Button) findViewById(R.id.btn_cancel);
        mBtn_OK.setOnClickListener(this);
        mEditTarget.setText(mBundle.getString(TITLE));
        mBtn_Cancel.setOnClickListener(this);
        mImgLoadingAnim = (ImageView) findViewById(R.id.img_loading);
        mAnimLoading = (AnimationDrawable) mImgLoadingAnim.getDrawable();
        stopLoadingAnimation();

        mEditTarget.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                mUtils.printLog(DEBUG, TAG, "[onKey] keyevent : " + keyEvent.toString());
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                    mUtils.printLog(DEBUG, TAG, "[onKey] KEYCODE_ENTER");
                    mUtils.hideSoftKeyboard(mContext, mEditTarget);
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
            mEditTarget.requestFocus();
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_ok:
                mTarget = mEditTarget.getText().toString().trim();
                if(mTarget.length() > 0){
                    new DoSetTarget().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mTarget, mGoal_SEQ);
                }else{
                    Toast.makeText(mContext, R.string.input_target, Toast.LENGTH_SHORT).show();
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


    public class DoSetTarget extends AsyncTask<String, Void, Integer>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startLoadingAnimation();
        }

        @Override
        protected Integer doInBackground(String... strings) {
            int[] res = DM.getInstance().setTarget(strings[0], strings[1]);
            return res[0];
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            stopLoadingAnimation();
            if(integer == DM.RES_SUCCESS){
                dismiss();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
