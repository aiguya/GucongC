package com.smartware.gucongc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartware.common.Utils;
import com.smartware.container.GraphValue;

import java.util.ArrayList;

/**
 * Created by Juho.S on 2017-05-18.
 */
public class FragmentGraph extends Fragment {

    private final static boolean DEBUG = Utils.DEBUG;
    private final static String TAG = "FragmentGraph";

    public final static String TITLE = "title";
    public final static String GRAPH_VALUES = "graph values";
    public final static String DATES = "dates";

    private Utils mUtils = Utils.getInstance();

    private TextView mTextTitle;
    private ArrayList<GraphValue> mGraphDataSet;
    private ArrayList<TextView> mDateList;
    private ArrayList<View> mMyGraphList;
    private ArrayList<View> mAvGraphList;
    private LinearLayout mGraphLayout;

    private int mMaxGraphHeight;
    private boolean isShowing = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mng_record, container, false);
        setContentData();
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {

        mGraphLayout = (LinearLayout) rootView.findViewById(R.id.layout_graph_details);
        mTextTitle = (TextView) rootView.findViewById(R.id.txt_title);
        mTextTitle.setText(getArguments().getString(TITLE));
        mDateList = new ArrayList<>();
        mDateList.add((TextView) rootView.findViewById(R.id.txt_graph_value_month1));
        mDateList.add((TextView) rootView.findViewById(R.id.txt_graph_value_month2));
        mDateList.add((TextView) rootView.findViewById(R.id.txt_graph_value_month3));
        mDateList.add((TextView) rootView.findViewById(R.id.txt_graph_value_month4));
        mDateList.add((TextView) rootView.findViewById(R.id.txt_graph_value_month5));
        mMyGraphList = new ArrayList<>();
        mAvGraphList = new ArrayList<>();
        mMyGraphList.add(rootView.findViewById(R.id.view_my_graph1));
        mMyGraphList.add(rootView.findViewById(R.id.view_my_graph2));
        mMyGraphList.add(rootView.findViewById(R.id.view_my_graph3));
        mMyGraphList.add(rootView.findViewById(R.id.view_my_graph4));
        mMyGraphList.add(rootView.findViewById(R.id.view_my_graph5));
        mAvGraphList.add(rootView.findViewById(R.id.view_av_graph1));
        mAvGraphList.add(rootView.findViewById(R.id.view_av_graph2));
        mAvGraphList.add(rootView.findViewById(R.id.view_av_graph3));
        mAvGraphList.add(rootView.findViewById(R.id.view_av_graph4));
        mAvGraphList.add(rootView.findViewById(R.id.view_av_graph5));
        ViewTreeObserver viewTreeObserver = mGraphLayout.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(isShowing){

                    mMaxGraphHeight = (int) (mGraphLayout.getHeight() * 0.8f);
                    mUtils.printLog(DEBUG, TAG, "[onGlobalLayout] mMaxGraphHeight = " + mMaxGraphHeight);
                    for (int i = 0; i < mMyGraphList.size(); i++) {
                        if(i < mGraphDataSet.size()){
                            mDateList.get(i).setText(mGraphDataSet.get(i).getDate());
                            setGraphValues(mMyGraphList.get(i), mGraphDataSet.get(i).getPercent(), mMaxGraphHeight);
                            setGraphValues(mAvGraphList.get(i), mGraphDataSet.get(i).getAll_av(), mMaxGraphHeight);
                        }else{
                            mDateList.get(i).setVisibility(View.INVISIBLE);
                            mMyGraphList.get(i).setVisibility(View.GONE);
                            mAvGraphList.get(i).setVisibility(View.GONE);
                        }

                    }
                    isShowing = false;
                }
            }
        });

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUtils.printLog(DEBUG, TAG, "[onViewCreated]");
        isShowing = true;
    }

    public void setContentData() {
        mUtils.printLog(DEBUG, TAG, "[setContentData] + title = " + getArguments().getString(TITLE));
        mGraphDataSet = (ArrayList<GraphValue>)getArguments().get(GRAPH_VALUES);
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


}
