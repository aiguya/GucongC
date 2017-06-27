package com.smartware.gucongc;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.smartware.common.Utils;
import com.smartware.manager.CM;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Juho.S on 2017-06-13.
 */
public class DialogSearchSchool extends Dialog implements View.OnClickListener{
    private final static boolean DEBUG = Utils.DEBUG;
    private final static String TAG = "DialogSearchSchool";
    public final static int ELEMENTARY_SCHOOL = 1;
    public final static int MIDDLE_SCHOOL = 2;
    public final static int HIGH_SCHOOL = 3;

    private int mGrade = 0;

    private Context mContext;

    public DialogSearchSchool(Context context, int grade, ISchoolNameListener onSchoolNameListener) {
        super(context);
        mContext = context;
        mGrade = grade;
        this.onSchoolNameListener = onSchoolNameListener;
    }

    public interface ISchoolNameListener {
        void getSchoolName(String schoolName);
    }
    private ISchoolNameListener onSchoolNameListener;


    private TextView mTitle;
    private EditText mEditSearch;
    private Button mBtn_Search;
    private Button mBtn_Cancel;
    private ListView mListSchool;
    private SchoolListViewAdapter mAdapter;
    private ArrayList<String> mArraySchool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams dlWindow = new WindowManager.LayoutParams();
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        dlWindow.dimAmount = 0.5f;
        getWindow().setAttributes(dlWindow);
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_search_school);
        mTitle = (TextView) findViewById(R.id.txt_dialog_title);
        mEditSearch = (EditText) findViewById(R.id.edit_search_school);
        mBtn_Search = (Button) findViewById(R.id.btn_search_school);
        mListSchool = (ListView) findViewById(R.id.list_school_name);
        int arrayID = 0;
        switch (mGrade){
            case ELEMENTARY_SCHOOL:
                arrayID = R.array.elementary_list;
                break;
            case MIDDLE_SCHOOL:
                arrayID = R.array.middleschool_list;
                break;
            case HIGH_SCHOOL:
                arrayID = R.array.highschool_list;
                break;
            default:
                arrayID = R.array.elementary_list;
                break;
        }
        mArraySchool = new ArrayList<>();

        for(String s: mContext.getResources().getStringArray(arrayID)){
            mArraySchool.add(s);
        }

        mAdapter = new SchoolListViewAdapter(mContext, mArraySchool);
        mListSchool.setAdapter(mAdapter);
        mListSchool.setVisibility(View.INVISIBLE);

        mBtn_Cancel = (Button) findViewById(R.id.btn_cancel);
        mBtn_Cancel.setOnClickListener(this);
        mEditSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = mEditSearch.getText().toString().toLowerCase(Locale.getDefault());
                mAdapter.filter(text);
                if(mAdapter.getCount() < 10) {
                    mListSchool.setVisibility(View.VISIBLE);
                }else{
                    mListSchool.setVisibility(View.INVISIBLE);
                }
            }
        });
        mListSchool.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = mAdapter.getItem(i);
                onSchoolNameListener.getSchoolName(name);
                Utils.getInstance().hideSoftKeyboard(mContext, mEditSearch);
                dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_ok:
                dismiss();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }

    }

    public class SchoolListViewAdapter extends BaseAdapter {

        Context context;
        LayoutInflater inflater;
        private List<String> schoolList = null;
        private ArrayList<String> arrayList;
        public SchoolListViewAdapter(Context context, List<String> schoolList) {
            this.context = context;
            this.schoolList = schoolList;
            inflater = LayoutInflater.from(context);
            this.arrayList = new ArrayList<>();
            this.arrayList.addAll(schoolList);
        }

        public class ViewHolder {
            TextView text_name;
        }

        @Override
        public int getCount() {
            return schoolList.size();
        }

        @Override
        public String getItem(int position) {
            return schoolList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            final ViewHolder holder;
            final String text = schoolList.get(position);

            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(android.R.layout.simple_list_item_1, null);
                holder.text_name = (TextView) view.findViewById(android.R.id.text1);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.text_name.setText(text);
            return view;
        }

        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            schoolList.clear();
            if (charText.length() == 0) {
                schoolList.addAll(arrayList);
            } else {
                for (String text : arrayList) {
                    String name = text;
                    if (name.toLowerCase().contains(charText)) {
                        schoolList.add(name);
                    }
                }
            }
            notifyDataSetChanged();
        }

    }



}
