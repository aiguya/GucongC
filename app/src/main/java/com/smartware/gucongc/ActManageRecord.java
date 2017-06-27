package com.smartware.gucongc;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smartware.container.GraphCache;
import com.smartware.common.Utils;
import com.smartware.container.GraphData;
import com.smartware.container.GraphValue;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juho.S on 2017-05-18.
 */
public class ActManageRecord extends AppCompatActivity implements View.OnClickListener {

    private final static boolean DEBUG = Utils.DEBUG;
    private final static String TAG = "ActManageRecord";

    private Utils mUtils = Utils.getInstance();

    private String[] mGraphTitles;
    private int mPrePagePos = 0;
    private int mGraphMaxSize;

    private ViewPager mViewPager;
    private CustomPagerAdapter mPageAdapter;

    private ArrayList<RelativeLayout> mGraphBackgrounds = new ArrayList<>();
    private ArrayList<TextView> mTextValues = new ArrayList<>();
    private ArrayList<View> mGraphLists = new ArrayList<>();

    private Bundle mBundle;

    private ImageButton mBtnNavLeft;
    private ImageButton mBtnNavRight;

    private ArrayList<GraphValue> mGraphValues_1;
    private ArrayList<GraphValue> mGraphValues_2;
    private ArrayList<GraphValue> mGraphValues_3;
    private ArrayList<GraphValue> mGraphValues_4;
    private ArrayList<GraphValue> mGraphValues_5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_manage_record);
        mBundle = getIntent().getBundleExtra(Utils.BUNDLE);

        initView();
        initViewPager();
    }

    private void initView() {
        getSupportActionBar().setTitle(getText(R.string.nav_score));
        //액션바 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF56aed2));
        //홈버튼 표시
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_navigate_before_white_36dp);
        mGraphTitles = getResources().getStringArray(R.array.graph_titles);
        mViewPager = (ViewPager) findViewById(R.id.mng_record_viewpager);
        mGraphBackgrounds.add((RelativeLayout) findViewById(R.id.layout_graph1));
        mGraphBackgrounds.add((RelativeLayout) findViewById(R.id.layout_graph2));
        mGraphBackgrounds.add((RelativeLayout) findViewById(R.id.layout_graph3));
        mGraphBackgrounds.add((RelativeLayout) findViewById(R.id.layout_graph4));
        mGraphBackgrounds.add((RelativeLayout) findViewById(R.id.layout_graph5));
        mTextValues.add((TextView) findViewById(R.id.txt_my_score_value1));
        mTextValues.add((TextView) findViewById(R.id.txt_moverall_av_value1));
        mTextValues.add((TextView) findViewById(R.id.txt_my_score_value2));
        mTextValues.add((TextView) findViewById(R.id.txt_moverall_av_value2));
        mTextValues.add((TextView) findViewById(R.id.txt_my_score_value3));
        mTextValues.add((TextView) findViewById(R.id.txt_moverall_av_value3));
        mTextValues.add((TextView) findViewById(R.id.txt_my_score_value4));
        mTextValues.add((TextView) findViewById(R.id.txt_moverall_av_value4));
        mTextValues.add((TextView) findViewById(R.id.txt_my_score_value5));
        mTextValues.add((TextView) findViewById(R.id.txt_moverall_av_value5));
        mGraphLists.add(findViewById(R.id.view_my_score_bar1));
        mGraphLists.add(findViewById(R.id.view_overall_av_bar1));
        mGraphLists.add(findViewById(R.id.view_my_score_bar2));
        mGraphLists.add(findViewById(R.id.view_overall_av_bar2));
        mGraphLists.add(findViewById(R.id.view_my_score_bar3));
        mGraphLists.add(findViewById(R.id.view_overall_av_bar3));
        mGraphLists.add(findViewById(R.id.view_my_score_bar4));
        mGraphLists.add(findViewById(R.id.view_overall_av_bar4));
        mGraphLists.add(findViewById(R.id.view_my_score_bar5));
        mGraphLists.add(findViewById(R.id.view_overall_av_bar5));
        mBtnNavLeft = (ImageButton) findViewById(R.id.btn_nav_left);
        mBtnNavRight = (ImageButton) findViewById(R.id.btn_nav_right);

        graphDataSort();

    }

    private void graphDataSort() {
        ArrayList<GraphValue> detailList = (ArrayList<GraphValue>)mBundle.getSerializable(GraphData.GRAPH_DATA_2ND);
        mGraphValues_1 = new ArrayList<>();
        mGraphValues_2 = new ArrayList<>();
        mGraphValues_3 = new ArrayList<>();
        mGraphValues_4 = new ArrayList<>();
        mGraphValues_5 = new ArrayList<>();

        for (int i = 0; i < detailList.size(); i++) {
            if(detailList.get(i).getGubun().startsWith("1")){
                mGraphValues_1.add(detailList.get(i));
            }else if(detailList.get(i).getGubun().startsWith("2")){
                mGraphValues_2.add(detailList.get(i));
            }else if(detailList.get(i).getGubun().startsWith("3")){
                mGraphValues_3.add(detailList.get(i));
            }else if(detailList.get(i).getGubun().startsWith("4")){
                mGraphValues_4.add(detailList.get(i));
            }else if(detailList.get(i).getGubun().startsWith("5")){
                mGraphValues_5.add(detailList.get(i));
            }
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {

            LinearLayout layout = (LinearLayout) findViewById(R.id.layout_graph_base);
            mGraphMaxSize = layout.getWidth();
            ArrayList<GraphValue> mainGraph = (ArrayList<GraphValue>) mBundle.getSerializable(GraphData.GRAPH_DATA);
            for (int i = 0; i < mainGraph.size(); i++) {
                mTextValues.get(i).setText("" + mainGraph.get(i).getPercent());
                mUtils.setGraph(mGraphLists.get(i), mainGraph.get(i).getPercent(), mGraphMaxSize);
            }
        }

    }

    private void initViewPager() {
        List<Fragment> fragments = new ArrayList<Fragment>();
        FragmentGraph frame1 = new FragmentGraph();
        FragmentGraph frame2 = new FragmentGraph();
        FragmentGraph frame3 = new FragmentGraph();
        FragmentGraph frame4 = new FragmentGraph();
        FragmentGraph frame5 = new FragmentGraph();
        Bundle bundle1 = new Bundle(3);
        Bundle bundle2 = new Bundle(3);
        Bundle bundle3 = new Bundle(3);
        Bundle bundle4 = new Bundle(3);
        Bundle bundle5 = new Bundle(3);

        bundle1.putString(FragmentGraph.TITLE, mGraphTitles[0]);
        bundle1.putSerializable(FragmentGraph.GRAPH_VALUES, mGraphValues_1);
        frame1.setArguments(bundle1);
        bundle2.putString(FragmentGraph.TITLE, mGraphTitles[1]);
        bundle2.putSerializable(FragmentGraph.GRAPH_VALUES, mGraphValues_2);
        frame2.setArguments(bundle2);
        bundle3.putString(FragmentGraph.TITLE, mGraphTitles[2]);
        bundle3.putSerializable(FragmentGraph.GRAPH_VALUES, mGraphValues_3);
        frame3.setArguments(bundle3);
        bundle4.putString(FragmentGraph.TITLE, mGraphTitles[3]);
        bundle4.putSerializable(FragmentGraph.GRAPH_VALUES, mGraphValues_4);
        frame4.setArguments(bundle4);
        bundle5.putString(FragmentGraph.TITLE, mGraphTitles[4]);
        bundle5.putSerializable(FragmentGraph.GRAPH_VALUES, mGraphValues_5);
        frame5.setArguments(bundle5);
        fragments.add(frame1);
        fragments.add(frame2);
        fragments.add(frame3);
        fragments.add(frame4);
        fragments.add(frame5);

        mPageAdapter = new CustomPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mPageAdapter);
        ;
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mUtils.printLog(DEBUG, TAG, "[onPageSelected] position : " + position);
                mGraphBackgrounds.get(position).setBackgroundResource(R.drawable.shape_bg_blue);
                mGraphBackgrounds.get(mPrePagePos).setBackground(null);
                mPrePagePos = position;
                if (position == 0) {
                    mBtnNavLeft.setVisibility(View.GONE);
                    mBtnNavRight.setVisibility(View.VISIBLE);
                } else if (position == mPageAdapter.getCount() - 1) {
                    mBtnNavLeft.setVisibility(View.VISIBLE);
                    mBtnNavRight.setVisibility(View.GONE);
                } else {
                    mBtnNavLeft.setVisibility(View.VISIBLE);
                    mBtnNavRight.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(0, false);
        mGraphBackgrounds.get(0).setBackgroundResource(R.drawable.shape_bg_blue);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_nav_left:
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
                break;
            case R.id.btn_nav_right:
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                break;
            case R.id.layout_graph1:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.layout_graph2:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.layout_graph3:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.layout_graph4:
                mViewPager.setCurrentItem(3);
                break;
            case R.id.layout_graph5:
                mViewPager.setCurrentItem(4);
                break;
        }
    }

    //액션버튼 메뉴 액션바에 집어 넣기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mng_record_menu, menu);
        return true;
    }

    //액션버튼을 클릭했을때의 동작
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            Toast.makeText(this, "뒤로 가기", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.action_leaderboard) {
            startActivity(new Intent(this, ActLeaderBoard.class));
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * PagerAdapter
     */
    private class CustomPagerAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> fragments;


        public CustomPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
