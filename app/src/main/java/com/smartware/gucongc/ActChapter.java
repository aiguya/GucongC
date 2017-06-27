
package com.smartware.gucongc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.smartware.common.Utils;
import com.smartware.container.Chapter;
import com.smartware.container.Workbook;
import com.smartware.manager.CM;
import com.smartware.manager.DM;

import java.util.ArrayList;

public class ActChapter extends AppCompatActivity {

    private static final boolean DEBUG = true;
    private static final String TAG = "ActChapter";

    public static final String EXTRA_WORKBOOK_DETAILS = "workbook_details";
    public static final String EXTRA_PREVIOUS_TEST = "previous_test";
    public static final String EXTRA_UNIT_ID = "unit_id";
    public static final String EXTRA_CONTENT_ID = "content_id";

    private Utils mUtil = Utils.getInstance();

    private ListView mListViewChapter;
    private ImageView mImgLoadingAnim;

    private AnimationDrawable mAnimLoading;

    private ChapterListViewAdapter mChapterListViewAdapter;

    private Workbook mWorkbook;
    private ArrayList<Chapter> mListChapter;
    private int mCheckedItemPosition = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUtil.printLog(DEBUG, TAG, "[onCreate]");

        mWorkbook = getIntent().getExtras().getParcelable(ActFindAssignment.EXTRA_WORKBOOK);

        mListChapter = new ArrayList<Chapter>();

        initView();
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
        super.onResume();
        mUtil.printLog(DEBUG, TAG, "[onResume] m_workbook : " + (mWorkbook != null ? mWorkbook.toString() : "null"));
        new DoLoadChapterList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initView() {
        mUtil.printLog(DEBUG, TAG, "[initView]");
        getSupportActionBar().setTitle(mWorkbook.getTitle());
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF56aed2));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_navigate_before_white_36dp);


        setContentView(R.layout.act_chapter);
        mListViewChapter = (ListView) findViewById(R.id.list_chapter);
        mImgLoadingAnim = (ImageView) findViewById(R.id.img_loading);
        mChapterListViewAdapter = new ChapterListViewAdapter(ActChapter.this, R.layout.tc_chapter, mListChapter);
        mListViewChapter.setAdapter(mChapterListViewAdapter);
        mAnimLoading = (AnimationDrawable) mImgLoadingAnim.getBackground();
        stopLoadingAnimation();
    }


    //액션버튼 메뉴 액션바에 집어 넣기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.schedule_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                actFinish(RESULT_CANCELED);
                Toast.makeText(this, "뒤로 가기", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_ok:
                if (mCheckedItemPosition < 0) {
                    Toast.makeText(this, "단원을 선택하세요", Toast.LENGTH_SHORT).show();
                    break;
                }
                Intent intent = new Intent();
                intent.putExtra(EXTRA_WORKBOOK_DETAILS, mListChapter.get(mCheckedItemPosition));
                setResult(RESULT_OK, intent);
                finish();
                Toast.makeText(this, "확인", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void actFinish(int type) {
        mUtil.printLog(DEBUG, TAG, "[actFinish] type : " + type);
        setResult(type);
        finish();
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


    private class DoLoadChapterList extends AsyncTask<String, Integer, Long> {

        @Override
        protected void onPreExecute() {
            mUtil.printLog(DEBUG, TAG, "[DoLoadChapterList] [onPreExecute]");
            startLoadingAnimation();
            mWorkbook.getListChapter().clear();
            super.onPreExecute();
        }

        @Override
        protected Long doInBackground(String... strData) {
            mUtil.printLog(DEBUG, TAG, "[DoLoadChapterList] [doInBackground]");
//			if ( DM.getInstance().IsTextBookViewAuth( String.valueOf( mWorkbook.getIdx() ) ) == 0 ) {
//				mIsViewAuth = true;
//			} else {
//				mIsViewAuth = false;
//			}
            DM.getInstance().getMyChapterList(
                    mWorkbook.getIdx(),
                    "",
                    mWorkbook.getListChapter()
            );
            return (long) DM.RES_SUCCESS;
        }

        @Override
        protected void onPostExecute(Long result) {
            mUtil.printLog(DEBUG, TAG, "[DoLoadChapterList] [onPostExecute] result : " + result);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mListChapter.clear();
                    for (Chapter item : mWorkbook.getListChapter()) {
                        if (item.getCellOpen() || item.getCellType() == Chapter.CT_UNIT) {
                            mListChapter.add(item);
                        }
                    }
//					m_chapterListViewAdapter.addAll(m_listChapter);
                    mChapterListViewAdapter.notifyDataSetChanged();
                    stopLoadingAnimation();
                }
            });
        }

        @Override
        protected void onCancelled() {
            mUtil.printLog(DEBUG, TAG, "[DoLoadChapterList] [onCancelled]");
            stopLoadingAnimation();
            super.onCancelled();
        }
    }

    private class ChapterListViewHolder {

        public LinearLayout layoutUnit;
        public LinearLayout layoutUnitDetail;
        public TextView textChapterNumber;
        public TextView textChapterName;
        public TextView textChapterNameDetail;
        public CheckBox checkBox;
        public ImageButton btnOpen;
    }

    private class ChapterListViewAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private ArrayList<Chapter> mList;
        private Activity mContext;
        private int mListLayoutResId;

        public ChapterListViewAdapter(Activity tContext, int listLayoutResId, ArrayList<Chapter> list) {
            mContext = tContext;
            mListLayoutResId = listLayoutResId;
            mList = list;
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int pos = position;
            Chapter row = mList.get(pos);
            ChapterListViewHolder vh = null;
            if (convertView == null) {
                vh = new ChapterListViewHolder();
                convertView = mInflater.inflate(mListLayoutResId, parent, false);

                vh.layoutUnit = (LinearLayout) convertView.findViewById(R.id.layout_cell_unit);
                vh.textChapterNumber = (TextView) convertView.findViewById(R.id.text_chapter_number);
                vh.textChapterName = (TextView) convertView.findViewById(R.id.text_chapter_name);
                vh.checkBox = (CheckBox) convertView.findViewById(R.id.cb_chapter);
                vh.layoutUnitDetail = (LinearLayout) convertView.findViewById(R.id.layout_cell_unit_detail);
                vh.textChapterNameDetail = (TextView) convertView.findViewById(R.id.text_chapter_name_detail);
                vh.btnOpen = (ImageButton) convertView.findViewById(R.id.btn_tc_opener);

                convertView.setTag(vh);
            } else {
                try {
                    vh = (ChapterListViewHolder) convertView.getTag();
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    return convertView;
                }
            }
            vh.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCheckedItemPosition = pos;
                    notifyDataSetChanged();
                }
            });

            if( pos != mCheckedItemPosition) vh.checkBox.setChecked(false);

            vh.layoutUnitDetail.setVisibility(View.VISIBLE);
            switch (row.getCellType()) {
                case Chapter.CT_UNIT: {
                    vh.layoutUnitDetail.setVisibility(View.GONE);
                    vh.layoutUnit.setVisibility(View.VISIBLE);

                    if (row.getCellOpen()) {

                        vh.btnOpen.setImageResource(R.drawable.ic_expand_less_black);
                    } else {

                        vh.btnOpen.setImageResource(R.drawable.ic_expand_more_black);
                    }

                    vh.btnOpen.setTag(position);
                    vh.btnOpen.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Integer idx = (Integer) v.getTag();
                            mUtil.printLog(DEBUG, TAG, "[ChapterListViewAdapter] [btnOpen] [onClick] idx : " + idx);
                            Chapter item = mListChapter.get(idx);
                            if (item.getCellOpen()) {
                                CM.getInstance().setWorkBookOpenedChapter(0);
                                item.setCellOpen(false);
                                for (Chapter cItem : mWorkbook.getListChapter()) {
                                    if (cItem.getChapterId() == item.getChapterId()) {
                                        cItem.setCellOpen(false);
                                    }
                                }
                                @SuppressWarnings("unchecked")
                                ArrayList<Chapter> tmpList = (ArrayList<Chapter>) mListChapter.clone();
                                for (Chapter cItem : tmpList) {
                                    if (cItem.getChapterId() == item.getChapterId() && cItem.getCellType() != Chapter.CT_UNIT) {
                                        mListChapter.remove(cItem);
                                    }
                                }
                                mChapterListViewAdapter.notifyDataSetChanged();
                            } else {
                                CM.getInstance().setWorkBookOpenedChapter(item.getChapterId());
                                for (Chapter cItem : mWorkbook.getListChapter()) {
                                    if (cItem.getChapterId() == item.getChapterId()) {
                                        cItem.setCellOpen(true);
                                    } else {
                                        cItem.setCellOpen(false);
                                    }
                                }
                                for (Chapter cItem : mWorkbook.getListChapter()) {
                                    if (cItem.getChapterId() != item.getChapterId() && cItem.getCellType() != Chapter.CT_UNIT) {
                                        mListChapter.remove(cItem);
                                    }
                                }
                                int basicrow = 0;
                                int cnt = 0;
                                for (Chapter cItem : mListChapter) {
                                    if (cItem.getChapterId() == item.getChapterId() && cItem.getCellType() == Chapter.CT_UNIT) {
                                        basicrow = cnt;
                                        break;
                                    }
                                    cnt++;
                                }
                                for (Chapter cItem : mWorkbook.getListChapter()) {
                                    if (cItem.getChapterId() == item.getChapterId() && cItem.getCellType() != Chapter.CT_UNIT) {
                                        mListChapter.add(++basicrow, cItem);
                                    }
                                }
                                mChapterListViewAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                    vh.textChapterNumber.setText(row.getChapterNumber());
                    vh.textChapterName.setText(row.getChapterName());
                }
                break;
                case Chapter.CT_UNIT_DETAIL: {
                    vh.layoutUnit.setVisibility(View.GONE);

                    vh.textChapterNameDetail.setText(row.getFolderName());
                }
                break;
            }

            return convertView;
        }
    }
}








































