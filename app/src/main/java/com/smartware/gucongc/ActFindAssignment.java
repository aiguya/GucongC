
package com.smartware.gucongc;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.smartware.common.ImageDownloader;
import com.smartware.common.Utils;
import com.smartware.container.Chapter;
import com.smartware.container.Workbook;
import com.smartware.manager.DM;

import java.util.ArrayList;

public class ActFindAssignment extends AppCompatActivity {

    private static final boolean DEBUG = true;
    private static final String TAG = "ActFindAssignment";

    public static final String EXTRA_SEARCH_TEXT = "search_text";
    public static final String EXTRA_STUDENT_GRADE = "student_grade";
    public static final String EXTRA_WORKBOOK = "workbook";


    private Utils mUtil = Utils.getInstance();

    @SuppressWarnings("unused")
    private TextView mTextNavTitle;

    private EditText mEditSearch;

    private ListView mListViewWorkbook;
    private ImageView mImgLoadingAnim;

    private AnimationDrawable mAnimLoading;

    private ArrayList<Workbook> mListWorkbookOriginal;
    private ArrayList<Workbook> mListWorkbookSearch;
    private WorkbookListViewAdapter mWorkbookListViewAdapter;

    private boolean mIsWorkbookLoaded = false;

    private int mWorkbookThumbWidth = 0;
    private int mWorkbookThumbHeight = 0;

    private TextWatcher mTextWatcher;
    private InputMethodManager mInputMethodManager;

    private String mStudentGrade = "";
    private int mCheckedItemPosition = -1;
    private Chapter mWorkbookDetails;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUtil.printLog(DEBUG, TAG, "[onCreate]");

        mListWorkbookOriginal = new ArrayList<Workbook>();
        mListWorkbookSearch = new ArrayList<Workbook>();

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().getString(EXTRA_SEARCH_TEXT) != null) {
                mEditSearch.setText(getIntent().getExtras().getString(EXTRA_SEARCH_TEXT));
            }
            if (getIntent().getExtras().getString(EXTRA_STUDENT_GRADE) != null) {
                mStudentGrade = getIntent().getExtras().getString(EXTRA_STUDENT_GRADE);
            }
        }

        initView();

        mWorkbookListViewAdapter = new WorkbookListViewAdapter(ActFindAssignment.this, R.layout.tc_find_assignment);
        mListViewWorkbook.setAdapter(mWorkbookListViewAdapter);

        setListViewWorkbookItemClickListener();
        setEditTextEnterKeyListener();
        setTextWatcher();
        mEditSearch.addTextChangedListener(mTextWatcher);

        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mIsWorkbookLoaded = false;
    }

    @Override
    protected void onPause() {
        mUtil.printLog(DEBUG, TAG, "[onPause]");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mUtil.printLog(DEBUG, TAG, "[onDestroy]");
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        mUtil.printLog(DEBUG, TAG, "[onResume]");
        super.onResume();
        if (!mIsWorkbookLoaded) {
            new DoLoadWorkbookList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    @Override
    protected void onStop() {
        mUtil.printLog(DEBUG, TAG, "[onStop]");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        mUtil.printLog(DEBUG, TAG, "[onRestart]");
        super.onRestart();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        mUtil.printLog(DEBUG, TAG, "[onKeyDown] keyCode : " + keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                actFinish(RESULT_CANCELED);
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mUtil.printLog(DEBUG, TAG, "[onActivityResult] requestCode : " + requestCode + ", resultCode : " + resultCode);
        switch (resultCode) {
            case RESULT_OK:
                mWorkbookDetails = data.getParcelableExtra(ActChapter.EXTRA_WORKBOOK_DETAILS);
                if (mCheckedItemPosition < 0) {
                    Toast.makeText(this, "교재를 선택하세요", Toast.LENGTH_SHORT).show();
                    break;
                } else if (mWorkbookDetails == null) {
                    Toast.makeText(this, "학습 내용을 선택하세요", Toast.LENGTH_SHORT).show();
                    break;
                }
                Intent intent = new Intent();
                intent.putExtra(EXTRA_WORKBOOK, mListWorkbookSearch.get(mCheckedItemPosition));
                intent.putExtra(ActChapter.EXTRA_WORKBOOK_DETAILS, mWorkbookDetails);
                setResult(RESULT_OK, intent);
                finish();
                Toast.makeText(this, "확인", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initView() {
        mUtil.printLog(DEBUG, TAG, "[initView]");

        getSupportActionBar().setTitle(getText(R.string.static_text_add_assignment));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF56aed2));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_navigate_before_white_36dp);

        setContentView(R.layout.act_find_assignment);

        mListViewWorkbook = (ListView) findViewById(R.id.list_workbook);
        mEditSearch = (EditText) findViewById(R.id.edit_search_key);

        mImgLoadingAnim = (ImageView) findViewById(R.id.img_loading);

        mAnimLoading = (AnimationDrawable) mImgLoadingAnim.getBackground();
        stopLoadingAnimation();

        mEditSearch.setText("");

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        mWorkbookThumbWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.workbook_thumb_width), metrics);
        mWorkbookThumbHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.workbook_thumb_height), metrics);
        mUtil.printLog(DEBUG, TAG, "[initView] mWorkbookThumbWidth : " + mWorkbookThumbWidth + ", mWorkbookThumbHeight : " + mWorkbookThumbHeight);
    }

    private void setTextWatcher() {
        mTextWatcher = new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                mUtil.printLog(DEBUG, TAG, "[setTextWatcher] [afterTextChanged] s : " + s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mUtil.printLog(DEBUG, TAG, "[setTextWatcher] [beforeTextChanged] start : " + start + ", count : " + count + ", after : " + after + ", s : " + s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mUtil.printLog(DEBUG, TAG, "[setTextWatcher] [onTextChanged] start : " + start + ", count : " + count + ", before : " + before + ", s : " + s.toString());
                searchItem();
            }
        };
    }
/*

    //액션버튼 메뉴 액션바에 집어 넣기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.schedule_edit_menu, menu);
        return true;
    }
*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                actFinish(RESULT_CANCELED);
                Toast.makeText(this, "뒤로 가기", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_ok:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void actFinish(int type) {
        mUtil.printLog(DEBUG, TAG, "[actFinish] type : " + type);
        setResult(type);
        finish();
    }

    private void searchItem() {
        mUtil.printLog(DEBUG, TAG, "[searchItem]");
        mListWorkbookSearch.clear();
        String searchKey = mEditSearch.getText().toString();
        if (searchKey.length() > 0) {
            for (Workbook item : mListWorkbookOriginal) {
                if (item.getTitle().contains(searchKey) ||
                        item.getSerieseName().contains(searchKey) ||
                        item.getTarget().contains(searchKey) ||
                        item.getSubject().contains(searchKey)) {

                    mListWorkbookSearch.add(new Workbook(item));
                }
            }
        } else {
            for (Workbook item : mListWorkbookOriginal) {
                mListWorkbookSearch.add(new Workbook(item));
            }
        }

        mWorkbookListViewAdapter.clear();
        mWorkbookListViewAdapter.addAll(mListWorkbookSearch);
        mWorkbookListViewAdapter.notifyDataSetChanged();
    }

    private void hideKeyboard() {
        mUtil.printLog(DEBUG, TAG, "[hideKeyboard]");
        mInputMethodManager.hideSoftInputFromWindow(mEditSearch.getWindowToken(), 0);
    }

    private void setEditTextEnterKeyListener() {
        mUtil.printLog(DEBUG, TAG, "[setListViewWorkbookItemClickListener]");
        mEditSearch.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mUtil.printLog(DEBUG, TAG, "[setListViewWorkbookItemClickListener] EditorInfo.IME_ACTION_DONE");
//	        		searchItem();
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });
    }

    private void setListViewWorkbookItemClickListener() {
        mUtil.printLog(DEBUG, TAG, "[setListViewWorkbookItemClickListener]");
        mListViewWorkbook.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mUtil.printLog(DEBUG, TAG, "[setListViewWorkbookItemClickListener] [onItemClick] position : " + position);
                mCheckedItemPosition = position;
                mWorkbookListViewAdapter.notifyDataSetChanged();
                if (mListWorkbookSearch != null && mListWorkbookSearch.size() > position) {
                    Workbook item = mListWorkbookSearch.get(position);
                    if (item != null) {
                        showActChapter(item);
                    }
                }
            }
        });
    }

    private void showActChapter(Workbook item) {
        mUtil.printLog(DEBUG, TAG, "[showActProductInfo] item : " + item.toString());
        Intent intent = new Intent(ActFindAssignment.this, ActChapter.class);
        intent.putExtra(EXTRA_WORKBOOK, item);
        startActivityForResult(intent, 0);
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

    private class WorkbookListViewHolder {

        public ImageView imgThumb;
        public TextView textTag;
        public TextView textAffiliation;
        public TextView textRelease;
        public TextView textSubject;
        public TextView textTitle;
        public CheckBox checkBox;

    }

    private class WorkbookListViewAdapter extends ArrayAdapter<Workbook> {

        private LayoutInflater mInflater;

        public WorkbookListViewAdapter(Context context, int layoutResource, ArrayList<Workbook> objects) {
            super(context, layoutResource, objects);
            mUtil.printLog(DEBUG, TAG, "[WorkbookListViewAdapter]");
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public WorkbookListViewAdapter(Context context, int layoutResource) {
            super(context, layoutResource);
            mUtil.printLog(DEBUG, TAG, "[WorkbookListViewAdapter]");
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return super.getCount();
        }

        @Override
        public Workbook getItem(int position) {
            return super.getItem(position);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            mUtil.printLog(!DEBUG, TAG, "[WorkbookListViewAdapter] [getView] position : " + position);
            Workbook row = getItem(position);
            WorkbookListViewHolder vh = null;
            if (convertView == null) {
                vh = new WorkbookListViewHolder();
                convertView = mInflater.inflate(R.layout.tc_find_assignment, parent, false);

                vh.imgThumb = (ImageView) convertView.findViewById(R.id.img_thumb);
                vh.textTag = (TextView) convertView.findViewById(R.id.text_seriese);
                vh.textAffiliation = (TextView) convertView.findViewById(R.id.text_target);
                vh.textRelease = (TextView) convertView.findViewById(R.id.text_release);
                vh.textSubject = (TextView) convertView.findViewById(R.id.text_subject);
                vh.textTitle = (TextView) convertView.findViewById(R.id.text_title);
                vh.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);

                convertView.setTag(vh);
            } else {
                try {
                    vh = (WorkbookListViewHolder) convertView.getTag();
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    return convertView;
                }
            }
            if (position == mCheckedItemPosition) {
                vh.checkBox.setChecked(true);
            } else {
                vh.checkBox.setChecked(false);
            }
            //vh.imgThumb.setId(R.drawable.no_workbook_thumb_nor);
            vh.imgThumb.setTag(ImageDownloader.SHAPE_RECTANGLE);
            if (row.getThumbUrl().length() > 0) {
                ImageDownloader.download(row.getThumbUrl(), vh.imgThumb, mWorkbookThumbWidth, mWorkbookThumbHeight);
            } else {
                vh.imgThumb.setImageResource(R.drawable.no_workbook_thumb_nor);
            }
            switch (row.getTargetType()) {
                case Workbook.TT_BLUE: {
                    vh.textTag.setBackgroundResource(R.drawable.bg_rounded_pure_blue);
                    vh.textTag.setTextColor(getResources().getColor(R.color.pure_blue));
                }
                break;
                case Workbook.TT_GREEN: {
                    vh.textTag.setBackgroundResource(R.drawable.bg_rounded_dark_cyan_lime_green);
                    vh.textTag.setTextColor(getResources().getColor(R.color.dark_cyan_lime_green));
                }
                break;
                case Workbook.TT_MAGENTA: {
                    vh.textTag.setBackgroundResource(R.drawable.bg_rounded_strong_magenta);
                    vh.textTag.setTextColor(getResources().getColor(R.color.strong_magenta));
                }
                break;
                case Workbook.TT_ORANGE: {
                    vh.textTag.setBackgroundResource(R.drawable.bg_rounded_pure_orange);
                    vh.textTag.setTextColor(getResources().getColor(R.color.pure_orange));
                }
                break;
                case Workbook.TT_VIOLET: {
                    vh.textTag.setBackgroundResource(R.drawable.bg_rounded_strong_violet);
                    vh.textTag.setTextColor(getResources().getColor(R.color.strong_violet));
                }
                break;
                case Workbook.TT_BROWN: {
                    vh.textTag.setBackgroundResource(R.drawable.bg_rounded_dark_orange);
                    vh.textTag.setTextColor(getResources().getColor(R.color.dark_orange));
                }
                break;
                case Workbook.TT_RED:
                default: {
                    vh.textTag.setBackgroundResource(R.drawable.bg_rounded_pure_red);
                    vh.textTag.setTextColor(getResources().getColor(R.color.pure_red));
                }
                break;
            }
            vh.textTag.setText(row.getSerieseName());
            switch (row.getStatus()) {
                case Workbook.WS_PREPARATION: {
                    vh.textRelease.setVisibility(View.VISIBLE);
                    vh.textRelease.setText(row.getRelease());
                    vh.textSubject.setTextColor(getResources().getColor(R.color.gray));
                    vh.textTitle.setTextColor(getResources().getColor(R.color.gray));
                }
                break;
                case Workbook.WS_REGISTRATION: {
                    vh.textRelease.setVisibility(View.VISIBLE);
                    vh.textRelease.setText(row.getRelease());
                    vh.textSubject.setTextColor(getResources().getColor(R.color.gray));
                    vh.textTitle.setTextColor(getResources().getColor(R.color.gray));
                }
                break;
                case Workbook.WS_AVAILABLE:
                default: {
                    vh.textRelease.setVisibility(View.GONE);
                    vh.textRelease.setText("");
                    vh.textSubject.setTextColor(getResources().getColor(R.color.very_dark_gray_2));
                    vh.textTitle.setTextColor(getResources().getColor(R.color.very_dark_gray));
                }
                break;
            }
            vh.textAffiliation.setText(row.getTarget());
            vh.textSubject.setText(row.getSubject());
            vh.textTitle.setText(row.getTitle());

            return convertView;
        }
    }

    private class DoLoadWorkbookList extends AsyncTask<String, Integer, Long> {

        @Override
        protected void onPreExecute() {
            mUtil.printLog(DEBUG, TAG, "[DoLoadWorkbookList] [onPreExecute]");
            startLoadingAnimation();
            mListWorkbookOriginal.clear();
            mListWorkbookSearch.clear();
            super.onPreExecute();
        }

        @Override
        protected Long doInBackground(String... strData) {
            mUtil.printLog(DEBUG, TAG, "[DoLoadWorkbookList] [doInBackground]");
            DM.getInstance().getWorkbookList(mListWorkbookOriginal, mStudentGrade);
            for (Workbook item : mListWorkbookOriginal) {
                mListWorkbookSearch.add(new Workbook(item));
            }
            return (long) DM.RES_SUCCESS;
        }

        @Override
        protected void onPostExecute(Long result) {
            mUtil.printLog(DEBUG, TAG, "[DoLoadWorkbookList] [onPostExecute] result : " + result);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    searchItem();
                    mIsWorkbookLoaded = true;
                    stopLoadingAnimation();
                }
            });
        }

        @Override
        protected void onCancelled() {
            mUtil.printLog(DEBUG, TAG, "[DoLoadWorkbookList] [onCancelled]");
            stopLoadingAnimation();
            mIsWorkbookLoaded = true;
            super.onCancelled();
        }
    }
}
