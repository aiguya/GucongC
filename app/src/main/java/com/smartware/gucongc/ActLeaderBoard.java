package com.smartware.gucongc;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smartware.common.Utils;
import com.smartware.container.LeaderBoard;
import com.smartware.manager.DM;

import java.util.ArrayList;

/**
 * Created by Juho.S on 2017-06-21.
 */
public class ActLeaderBoard extends AppCompatActivity {

    private final static boolean DEBUG = Utils.DEBUG;
    private final static String TAG = "ActLeaderBoard";

    private final static int GOLD = 0;
    private final static int ATTENDANCE = 1;
    private final static int PLAN_COMPLETION = 2;
    private final static int THIS_WEEK = 0;
    private final static int THIS_MONTH = 1;
    private final static int ACCUMULATE = 2;

    private ArrayList<Button> mButtonGroup1;
    private ArrayList<Button> mButtonGroup2;
    private ArrayList<Button> mButtonGroup3;
    private ArrayList<TextView> mTxtNameGroup;
    private ArrayList<TextView> mTxtPointGroup;
    private ArrayList<TextView> mTxtHistoryGroup;
    private ArrayList<TextView> mTxtGraphGroup;
    private ArrayList<View> mGraphGroup;
    private int[] mTxtNameIds = {
            R.id.txt_first_reward,
            R.id.txt_second_reward,
            R.id.txt_third_reward,
            R.id.txt_fourth_reward,
            R.id.txt_fifth_reward
    };
    private int[] mTxtPointIds = {
            R.id.txt_first_value,
            R.id.txt_second_value,
            R.id.txt_third_value,
            R.id.txt_fourth_value,
            R.id.txt_fifth_value
    };
    private int[] mTxtGraphIds = {
            R.id.txt_my_rank1,
            R.id.txt_my_rank2,
            R.id.txt_my_rank3,
            R.id.txt_my_rank4,
    };
    private int[] mTxtHistoryIds = {
            R.id.txt_graph_value_1,
            R.id.txt_graph_value_2,
            R.id.txt_graph_value_3,
            R.id.txt_graph_value_4,
    };
    private int[] mGraphIds = {
            R.id.view_graph_value_1,
            R.id.view_graph_value_2,
            R.id.view_graph_value_3,
            R.id.view_graph_value_4,
    };
    private int mTabSelection[] = {0, 0, 0};
    private int mMaxPlayer;
    private int mMaxGraphHeight;
    private LeaderBoard mLeaderBoardData;

    private ImageView mImgLoadingAnim;
    private TextView mTxtMyColumn;
    private TextView mTxtMyPoint;
    private TextView mTxtMyRank;
    private View mViewHeight;

    private AnimationDrawable mAnimLoading;

    private Utils mUtils = Utils.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUtils.printLog(DEBUG, TAG, "[onCreate]");
        setContentView(R.layout.act_leaderboard);
        initView();
    }

    public void initView() {
        mUtils.printLog(DEBUG, TAG, "[initView]");
        getSupportActionBar().setTitle(getText(R.string.leaderboard));
        //액션바 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF56aed2));
        //홈버튼 표시
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_navigate_before_white_36dp);
        mButtonGroup1 = new ArrayList<>();
        mButtonGroup2 = new ArrayList<>();
        mButtonGroup3 = new ArrayList<>();
        mTxtNameGroup = new ArrayList<>();
        mTxtPointGroup = new ArrayList<>();
        mTxtGraphGroup = new ArrayList<>();
        mTxtHistoryGroup = new ArrayList<>();
        mGraphGroup = new ArrayList<>();

        mImgLoadingAnim = (ImageView) findViewById(R.id.img_loading);
        mAnimLoading = (AnimationDrawable) mImgLoadingAnim.getBackground();
        stopLoadingAnimation();

        mButtonGroup1.add((Button) findViewById(R.id.btn_tab_gold));
        mButtonGroup1.add((Button) findViewById(R.id.btn_tab_attend));
        mButtonGroup1.add((Button) findViewById(R.id.btn_tab_achieve));

        mButtonGroup2.add((Button) findViewById(R.id.btn_tab_guild));
        mButtonGroup2.add((Button) findViewById(R.id.btn_tab_all));

        mButtonGroup3.add((Button) findViewById(R.id.btn_tab_week));
        mButtonGroup3.add((Button) findViewById(R.id.btn_tab_month));
        mButtonGroup3.add((Button) findViewById(R.id.btn_tab_cumulative));
        mTxtMyColumn = (TextView) findViewById(R.id.txt_my_column);
        mTxtMyPoint = (TextView) findViewById(R.id.txt_my_point);
        mTxtMyRank = (TextView) findViewById(R.id.txt_my_rank);
        mViewHeight = findViewById(R.id.view_graph_height);
        for(int i = 0; i < mTxtNameIds.length ; i++ ){
            mTxtNameGroup.add((TextView)findViewById(mTxtNameIds[i]));
            mTxtPointGroup.add((TextView)findViewById(mTxtPointIds[i]));
            if(i < mTxtHistoryIds.length){
                mTxtHistoryGroup.add((TextView)findViewById(mTxtHistoryIds[i]));
                mTxtGraphGroup.add((TextView)findViewById(mTxtGraphIds[i]));
                mGraphGroup.add(findViewById(mGraphIds[i]));
            }
        }
        mUtils.printLog(DEBUG, TAG, "[initView] mTxtHistoryGroup size : " + mTxtHistoryGroup.size());

        new DoLoadLeaderboard().executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR,
                mTabSelection);
    }


    //액션버튼을 클릭했을때의 동작
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            Toast.makeText(this, "뒤로 가기", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setGraphValues(View view, int value, int maxGraphHight) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        //mUtils.printLog(DEBUG, TAG, "[setGraphValues] score = " + value);
        if (value > 100) {
            layoutParams.height = maxGraphHight;
        } else {
            layoutParams.height = (int) (maxGraphHight * (value / 100f));
        }
        //mUtils.printLog(DEBUG, TAG, "[setGraphValues] width = " + layoutParams.width);
        view.setLayoutParams(layoutParams);
    }

    public void onClick(View v) {
        if (mButtonGroup1.contains(v)) {
            if (mButtonGroup1.indexOf(v) != mTabSelection[0]){
                mButtonGroup1.get(mTabSelection[0]).setBackgroundResource(R.drawable.shape_button_blue_line);
                mButtonGroup1.get(mTabSelection[0]).setTextColor(getResources().getColor(R.color.color_action_bar_background));
                v.setBackgroundResource(R.drawable.shape_button_blue);
                ((Button)v).setTextColor(getResources().getColor(R.color.color_white));
                mTabSelection[0] = mButtonGroup1.indexOf(v);
            }else{
                return;
            }
        }else if(mButtonGroup2.contains(v)){
            if (mButtonGroup2.indexOf(v) != mTabSelection[1]) {
                mButtonGroup2.get(mTabSelection[1]).setBackgroundResource(R.drawable.shape_button_blue_line);
                mButtonGroup2.get(mTabSelection[1]).setTextColor(getResources().getColor(R.color.color_action_bar_background));
                v.setBackgroundResource(R.drawable.shape_button_blue);
                ((Button) v).setTextColor(getResources().getColor(R.color.color_white));
                mTabSelection[1] = mButtonGroup2.indexOf(v);
            }else{
                return;
            }
        }else if(mButtonGroup3.contains(v)){
            if (mButtonGroup3.indexOf(v) != mTabSelection[2]) {
                mButtonGroup3.get(mTabSelection[2]).setBackgroundResource(R.drawable.shape_button_blue_line);
                mButtonGroup3.get(mTabSelection[2]).setTextColor(getResources().getColor(R.color.color_action_bar_background));
                v.setBackgroundResource(R.drawable.shape_button_blue);
                ((Button) v).setTextColor(getResources().getColor(R.color.color_white));
                mTabSelection[2] = mButtonGroup3.indexOf(v);
            }else{
                return;
            }
        }
        new DoLoadLeaderboard().executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR,
                mTabSelection);
        mUtils.printLog(DEBUG, TAG, "[onClick] mTabSelection = " + mTabSelection[0] + "," +  mTabSelection[1] + "," + mTabSelection[2]);

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

    private class DoLoadLeaderboard extends AsyncTask<int[], Void, Integer> {

        @Override
        protected void onPreExecute() {
            mUtils.printLog(DEBUG, TAG, "[DoLoadLeaderboard] [onPreExecute]");
            startLoadingAnimation();
            mLeaderBoardData = new LeaderBoard();
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(int[]... ints) {
            mUtils.printLog(DEBUG, TAG, "[DoLoadLeaderboard] [doInBackground]");
            return DM.getInstance().getLeaderboardData(ints[0], mLeaderBoardData);
        }

        @Override
        protected void onPostExecute(Integer result) {
            mUtils.printLog(DEBUG, TAG, "[DoLoadLeaderboard] [onPostExecute] result : " + result);
            if(result == DM.RES_SUCCESS){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mUtils.printLog(DEBUG, TAG, "[runOnUiThread] [run] ");
                        stopLoadingAnimation();
                        String rank_value = getString(R.string.rank_unit);
                        String unit1 = "";
                        String unit2 ="";
                        switch (mTabSelection[0]){
                            case GOLD:
                                unit1 = getString(R.string.en_gold);
                                mTxtMyColumn.setText(R.string.my_gold);
                                break;
                            case ATTENDANCE:
                                unit1 = getString(R.string.point_unit);
                                mTxtMyColumn.setText(R.string.my_attendance);
                                break;
                            case PLAN_COMPLETION:
                                unit1 = getString(R.string.point_unit);
                                mTxtMyColumn.setText(R.string.my_plan_completion);
                                break;
                        }
                        switch (mTabSelection[2]){
                            case THIS_WEEK:
                                unit2 = getString(R.string.week_unit);
                                break;
                            case THIS_MONTH:
                                unit2 = getString(R.string.month_unit);
                                break;
                            case ACCUMULATE:
                                unit2 = getString(R.string.month_unit);
                                break;
                        }

                        mMaxPlayer = mLeaderBoardData.getMaxPlayer();
                        mUtils.printLog(DEBUG, TAG, "[runOnUiThread] [run] getRankUserPoints size : " + mLeaderBoardData.getRankUserPoints().size() );
                        mUtils.printLog(DEBUG, TAG, "[runOnUiThread] [run] mTxtGraphGroup size : " + mTxtGraphGroup.size() );
                        mTxtMyPoint.setText(mLeaderBoardData.getmyPoint() + " " + unit1);
                        mTxtMyRank.setText(mLeaderBoardData.getmyPointRank() + rank_value);
                        mMaxGraphHeight = mViewHeight.getHeight();
                        for(int i = 0 ; i < mLeaderBoardData.getRankUserNames().size() ; i++){
                            mTxtNameGroup.get(i).setText(mLeaderBoardData.getRankUserNames().get(i));
                            mTxtPointGroup.get(i).setText(mLeaderBoardData.getRankUserPoints().get(i) + " " + unit1);
                            if(i < mTxtGraphGroup.size()){
                                int percent = 0;
                                mTxtGraphGroup.get(i).setText(mLeaderBoardData.getRankHistory().get(i) + rank_value);
                                mTxtHistoryGroup.get(i).setText(mLeaderBoardData.getRankHistoryPeriod().get(i));
                                mUtils.printLog(DEBUG, TAG, "[runOnUiThread] [run] getRankHistory : " + mLeaderBoardData.getRankHistory().get(i) );
                                mUtils.printLog(DEBUG, TAG, "[runOnUiThread] [run] mMaxGraphHeight : " + mMaxGraphHeight );
                                percent = 101 - (int)((Integer.parseInt(mLeaderBoardData.getRankHistory().get(i)) * 100f) / mMaxPlayer );
                                mUtils.printLog(DEBUG, TAG, "[runOnUiThread] [run] percent : " + percent );
                                setGraphValues(mGraphGroup.get(i), percent, mMaxGraphHeight);
                            }
                        }
                    }
                });
            }else{
                stopLoadingAnimation();
            }
        }
        @Override
        protected void onCancelled() {
            mUtils.printLog(DEBUG, TAG, "[DoLoadLeaderboard] [onCancelled]");
            stopLoadingAnimation();
            super.onCancelled();
        }
    }


}
