package com.smartware.gucongc;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartware.common.Utils;
import com.smartware.container.TargetTreeDate;
import com.smartware.manager.DM;

/**
 * Created by Juho.S on 2017-06-27.
 */
public class DialogCheck extends Dialog implements View.OnClickListener {
    private final static boolean DEBUG = Utils.DEBUG;
    private final static String TAG = "DialogCheck";

    public final static String DIALOG_INDEX = "dialog_index";
    public final static String DIALOG_CHECK_TYPE = "dialog_check_type";
    public final static String GOAL_SEQ = "goal_seq";

    private Context mContext;
    private Bundle mBundle;

    public DialogCheck(Context context, Bundle bundle) {
        super(context);
        mContext = context;
        mBundle = bundle;
    }


    private TextView mTitle;
    private TextView mTxtDate;
    private Button mBtn_Cancel;
    private ImageButton mBtn_Good;
    private ImageButton mBtn_NotBad;
    private ImageButton mBtn_Bad;
    private ImageView mImgLoadingAnim;
    private AnimationDrawable mAnimLoading;


    private Utils mUtils = Utils.getInstance();

    private String mGoalSeq;
    private int mIndex;
    private int mCheckedIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams dlWindow = new WindowManager.LayoutParams();
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        dlWindow.dimAmount = 0.5f;
        getWindow().setAttributes(dlWindow);
        setContentView(R.layout.dialog_check);
        mGoalSeq = mBundle.getString(GOAL_SEQ);
        mIndex = mBundle.getInt(DIALOG_INDEX);

        initView();
    }

    private void initView() {
        mImgLoadingAnim = (ImageView) findViewById(R.id.img_loading);
        mAnimLoading = (AnimationDrawable) mImgLoadingAnim.getDrawable();
        stopLoadingAnimation();

        mTitle = (TextView) findViewById(R.id.txt_dialog_title);
        mBtn_Cancel = (Button) findViewById(R.id.btn_cancel);
        mBtn_Good = (ImageButton) findViewById(R.id.btn_good);
        mBtn_NotBad = (ImageButton) findViewById(R.id.btn_not_bad);
        mBtn_Bad = (ImageButton) findViewById(R.id.btn_bad);
        mBtn_Cancel.setOnClickListener(this);
        mBtn_Good.setOnClickListener(this);
        mBtn_NotBad.setOnClickListener(this);
        mBtn_Bad.setOnClickListener(this);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_good:
                mCheckedIcon = 1;
                new DoWeekCheck().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case R.id.btn_not_bad:
                mCheckedIcon = 2;
                new DoWeekCheck().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case R.id.btn_bad:
                mCheckedIcon = 3;
                new DoWeekCheck().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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

    protected class DoWeekCheck extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            startLoadingAnimation();
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... strings) {
            int weekNum = mIndex / 2 + 1;
            int type = mIndex % 2 == 0 ? TargetTreeDate.TEACHER_CHECK : TargetTreeDate.PARENT_CHECK;
            return DM.getInstance().setWeekCheck(mBundle.getString(GOAL_SEQ), weekNum, type, mCheckedIcon);
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
