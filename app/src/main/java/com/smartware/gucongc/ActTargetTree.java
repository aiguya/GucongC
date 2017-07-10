package com.smartware.gucongc;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smartware.common.Utils;
import com.smartware.container.TargetTreeData;
import com.smartware.container.TargetTreeDate;
import com.smartware.manager.CM;
import com.smartware.manager.DM;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Juho.S on 2017-05-24.
 */
public class ActTargetTree extends AppCompatActivity implements View.OnClickListener {

    private final static boolean DEBUG = Utils.DEBUG;
    private final static String TAG = "ActTargetTree";

    private TextView mTxtTarget;
    private TextView mTxtMyScore;
    private TextView mTxtStartedDay;
    private ImageView mTargetTree;
    private GridViewAdapter mGridViewAdapter;
    private ConfirmAdapter mConfirmAdapter;
    private GridView mGridViewDay;
    private GridView mGridViewCheck;
    private ArrayList<Integer> mDayList;
    private ArrayList<TargetTreeDate> mScoreDayList;
    //private ArrayList<Integer> mCheckList;
    private ArrayList<TargetTreeDate> mCheckList;
    private int[] checkArray = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1}; //테스트용


    private CM mCM = CM.getInstance();
    private Utils mUtils = Utils.getInstance();
    private TargetTreeData mData;
    private ArrayList<TargetTreeData> arrayTTDatas;
    private Calendar mCal;

    private ImageView mImgLoadingAnim;
    private AnimationDrawable mAnimLoading;

    private int mWeekCount;
    private DialogEditScore mDialogScore;
    private DialogEditTarget mDialogTarget;
    private DialogCheck mDialogCheck;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_target_tree);
        initView();
        mCal = Calendar.getInstance();
    }

    private void initView() {
        mUtils.printLog(DEBUG, TAG, "[initView]");
        getSupportActionBar().setTitle(getText(R.string.target_tree_mng));
        //액션바 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF56aed2));
        //홈버튼 표시
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_navigate_before_white_36dp);
        long now = System.currentTimeMillis();
        final Date date = new Date(now);
        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);

        mTxtTarget = (TextView) findViewById(R.id.text_target);
        mTxtMyScore = (TextView) findViewById(R.id.txt_my_score);
        mTxtStartedDay = (TextView) findViewById(R.id.txt_start_date);
        mTargetTree = (ImageView) findViewById(R.id.img_targettree);
        mGridViewDay = (GridView) findViewById(R.id.calendar);
        mGridViewCheck = (GridView) findViewById(R.id.confirm);
        mImgLoadingAnim = (ImageView) findViewById(R.id.img_loading);
        mAnimLoading = (AnimationDrawable) mImgLoadingAnim.getDrawable();
        stopLoadingAnimation();
        mTargetTree.setOnClickListener(this);
        mDayList = new ArrayList<>();
        mCal = Calendar.getInstance();
        mCal.set(Integer.parseInt(curYearFormat.format(date)), Integer.parseInt(curMonthFormat.format(date)) - 1, 1);
        int dayNum = mCal.get(Calendar.DAY_OF_WEEK);
        for (int i = 1; i < dayNum; i++) {
            mDayList.add(0);
        }
        mCheckList = new ArrayList<>();

        setCalendarDate(mCal.get(Calendar.MONTH) + 1);
        mGridViewAdapter = new GridViewAdapter(getApplicationContext(), mDayList, mScoreDayList);
        mConfirmAdapter = new ConfirmAdapter(getApplicationContext(), mCheckList);
        mGridViewCheck.setAdapter(mConfirmAdapter);
        mGridViewDay.setAdapter(mGridViewAdapter);
        setOnItemClickListener();


    }

    private void setOnItemClickListener() {

        mGridViewDay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mUtils.printLog(DEBUG, TAG, "[setOnItemClickListener] [onItemClick] index i : " + i);
                if ((int) mGridViewAdapter.getItem(i) < 1 || mData.getGoalSeq().length() < 1) return;

                String strToday = "" + mGridViewAdapter.getItem(i);
                int today = (int) mGridViewAdapter.getItem(i);
                int score = mGridViewAdapter.getDateItem(today - 1).getScore();
                Bundle bundle = new Bundle();
                bundle.putString(DialogEditScore.DIALOG_DATE, strToday);
                if (score > -1) {
                    bundle.putString(DialogEditScore.DIALOG_SCORE, "" + score);
                }
                bundle.putString(DialogEditScore.GOAL_SEQ, mData.getGoalSeq());
                mDialogScore = new DialogEditScore(ActTargetTree.this, bundle);
                mDialogScore.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        new DoGetTargetTreeData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                });
                mDialogScore.setCanceledOnTouchOutside(false);
                mDialogScore.show();


            }
        });
        mGridViewCheck.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i % 2 == 0 || mData.getGoalSeq().length() < 1) return;

                Bundle bundle = new Bundle();
                bundle.putString(DialogCheck.GOAL_SEQ, mData.getGoalSeq());
                bundle.putInt(DialogCheck.DIALOG_INDEX, i);
                mDialogCheck = new DialogCheck(ActTargetTree.this, bundle);
                mDialogCheck.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        new DoGetTargetTreeData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                });
                mDialogCheck.setCanceledOnTouchOutside(false);
                mDialogCheck.show();
            }
        });
    }

    /**
     * 해당 월에 표시할 일 수 구함
     *
     * @param month
     */

    private void setCalendarDate(int month) {
        boolean isNull = false;
        if (mData == null) {
            mData = new TargetTreeData();
            mData.setMonthData(new ArrayList<TargetTreeDate>());
            mScoreDayList = mData.getMonthData();
            mCheckList = new ArrayList<>();
            isNull = true;
        }
        mCal.set(Calendar.MONTH, month - 1);
        for (int i = 0; i < mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            mDayList.add(i + 1);
            if (isNull) {
                mScoreDayList.add(new TargetTreeDate(i + 1, -1, TargetTreeDate.SCORE_DATE, (mDayList.size() / 7 + 1)));
            }
        }
        if (mDayList.size() % 7 > 0) {
            for (int i = 0; i < mDayList.size() % 7; i++) {
                mDayList.add(0);
            }
        }
        if (isNull) {
            int type;
            int week;
            for (int i = 0; i < (mDayList.size() / 7) * 2; i++) {
                if (i % 2 == 0) {
                    type = TargetTreeDate.TEACHER_CHECK;
                } else {
                    type = TargetTreeDate.PARENT_CHECK;
                }
                week = (i) / 2 + 1;
                mCheckList.add(new TargetTreeDate(0, checkArray[i], type, week));
            }
        }
        mWeekCount = mDayList.size() / 7;


    }


    @Override
    protected void onResume() {
        super.onResume();
        new DoGetTargetTreeData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        mTxtMyScore.setText("" + mData.getAvMyMonthScore());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {

        }
    }

    //액션버튼 메뉴 액션바에 집어 넣기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.target_tree_menu, menu);
        return true;
    }

    //액션버튼을 클릭했을때의 동작
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            Toast.makeText(this, "뒤로 가기", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.action_search) {
            startActivity(new Intent(this, ActFindSystem.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_target:
                Bundle bundle = new Bundle();
                bundle.putString(DialogEditTarget.TITLE, mData.getTarget());
                bundle.putString(DialogEditTarget.GOAL_SEQ, mData.getGoalSeq());
                mDialogTarget = new DialogEditTarget(this, bundle);
                mDialogTarget.setCanceledOnTouchOutside(false);
                mDialogTarget.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        new DoGetTargetTreeData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                });
                mDialogTarget.show();
                break;
            case R.id.img_targettree:
                startActivity(new Intent(this, ActTreeTip.class));
                break;
            default:

                break;
        }

    }

    private class ViewHolder {
        public TextView textView;
        public ImageView imgView;
    }

    private class GridViewAdapter extends BaseAdapter {
        private List<Integer> list;
        private ArrayList<TargetTreeDate> targetList;
        private LayoutInflater inflater;


        public GridViewAdapter(Context context, List<Integer> list, ArrayList<TargetTreeDate> targetList) {
            this.list = list;
            this.inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            this.targetList = targetList;
        }

        public void setDataList(ArrayList<TargetTreeDate> targetList) {
            this.targetList = targetList;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        public TargetTreeDate getDateItem(int i) {
            return targetList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            view = inflater.inflate(R.layout.item_calendar, viewGroup, false);
            holder = new ViewHolder();
            holder.textView = (TextView) view.findViewById(R.id.tv_item_gridview);
            view.setTag(holder);


            if (list.get(i) == 0) {
                holder.textView.setBackgroundResource(R.drawable.shape_ring_gray);
                holder.textView.setText("");
            } else if (i % 7 == 6) {
                holder.textView.setBackgroundResource(R.drawable.shape_ring_blue);
                holder.textView.setText("" + list.get(i));
            } else if (i % 7 == 0) {
                holder.textView.setBackgroundResource(R.drawable.shape_ring_red);
                holder.textView.setText("" + list.get(i));
            } else {
                holder.textView.setBackgroundResource(R.drawable.shape_ring_light_blue);
                holder.textView.setText("" + list.get(i));
            }
            holder.textView.setTextColor(getResources().getColor(R.color.silver));

            if (list.get(i) > 0 && targetList.get(list.get(i) - 1).getScore() > -1) {
                holder.textView.setText("" + targetList.get(list.get(i) - 1).getScore());
                holder.textView.setTextColor(getResources().getColor(R.color.chocolate));
            }


            mCal = Calendar.getInstance();

            int today = mCal.get(Calendar.DAY_OF_MONTH);
            String sToday = "" + today;
            if (sToday.equals("" + getItem(i))) {
                holder.textView.setBackgroundResource(R.drawable.shape_ring_light_blue_solid);
            }
            return view;
        }
    }

    private class ConfirmAdapter extends BaseAdapter {
        private ArrayList<TargetTreeDate> list;
        private LayoutInflater inflater;


        public ConfirmAdapter(Context context, ArrayList<TargetTreeDate> list) {
            this.list = list;
            this.inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        }

        public void getDataList(ArrayList<TargetTreeDate> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;

            view = inflater.inflate(R.layout.item_confirm, viewGroup, false);
            holder = new ViewHolder();
            holder.textView = (TextView) view.findViewById(R.id.item_confirm);
            holder.imgView = (ImageView) view.findViewById(R.id.item_confirm_icon);
            view.setTag(holder);
            if (i / 2 >= mWeekCount) {
                holder.textView.setVisibility(View.GONE);
                holder.imgView.setVisibility(View.GONE);
            } else if (list.get(i).getScore() > -1) {
                holder.textView.setVisibility(View.INVISIBLE);
                holder.imgView.setVisibility(View.VISIBLE);
                switch (list.get(i).getScore()) {
                    case 1:
                        holder.imgView.setImageResource(R.drawable.icon01_24dp);
                        break;
                    case 2:
                        holder.imgView.setImageResource(R.drawable.icon02_24dp); //임시아이콘
                        break;
                    case 3:
                        holder.imgView.setImageResource(R.drawable.icon03_24dp); //아이콘
                        break;
                }
            } else {
                holder.textView.setVisibility(View.VISIBLE);
                holder.imgView.setVisibility(View.INVISIBLE);
                if (list.get(i).getType() == TargetTreeDate.TEACHER_CHECK) {
                    holder.textView.setText("T");
                } else if (list.get(i).getType() == TargetTreeDate.PARENT_CHECK) {
                    holder.textView.setText("P");
                }
            }
            return view;
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


    private class DoGetTargetTreeData extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            mUtils.printLog(DEBUG, TAG, "[DoGetTargetTreeData] [onPreExecute]");
            startLoadingAnimation();
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... strings) {
            mUtils.printLog(DEBUG, TAG, "[DoGetTargetTreeData] [doInBackground]");
            arrayTTDatas = new ArrayList<>();
            return DM.getInstance().getTargetTreeData(arrayTTDatas);
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (arrayTTDatas.size() > 0 && result == DM.RES_SUCCESS) {
                mData = arrayTTDatas.get(0);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTxtTarget.setText(mData.getTarget());
                        stopLoadingAnimation();
                        mGridViewAdapter.setDataList(mData.getMonthData());
                        mGridViewAdapter.notifyDataSetChanged();
                        mTxtMyScore.setText("" + mData.getAvMyMonthScore());
                        mConfirmAdapter.getDataList(mData.getCheckData());
                        mConfirmAdapter.notifyDataSetChanged();
                        mTxtStartedDay.setText(mData.getStartedDate());
                    }
                });
            } else {
                mTxtTarget.setText(R.string.input_target);

            }
            stopLoadingAnimation();
            mUtils.printLog(DEBUG, TAG, "[DoGetTargetTreeData] [onPostExecute] result : " + result);

        }

        @Override
        protected void onCancelled() {
            mUtils.printLog(DEBUG, TAG, "[DoGetTargetTreeData] [onCancelled]");
            stopLoadingAnimation();
            super.onCancelled();
        }
    }


    private class DoSetTarget extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            mUtils.printLog(DEBUG, TAG, "[DoSetTarget] [onPreExecute]");
            startLoadingAnimation();
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            mUtils.printLog(DEBUG, TAG, "[DoSetTarget] [onPostExecute] result : " + result);
        }

        @Override
        protected void onCancelled() {
            mUtils.printLog(DEBUG, TAG, "[DoSetTarget] [onCancelled]");
            stopLoadingAnimation();
            super.onCancelled();
        }
    }

    private class DoEditTarget extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            mUtils.printLog(DEBUG, TAG, "[DoEditTarget] [onPreExecute]");
            startLoadingAnimation();
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            mUtils.printLog(DEBUG, TAG, "[DoEditTarget] [onPostExecute] result : " + result);
        }

        @Override
        protected void onCancelled() {
            mUtils.printLog(DEBUG, TAG, "[DoEditTarget] [onCancelled]");
            stopLoadingAnimation();
            super.onCancelled();
        }
    }

}
