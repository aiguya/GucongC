package com.smartware.gucongc;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.smartware.common.Utils;

/**
 * Created by userpc on 2017-06-07.
 */
public class ActFindSystem extends AppCompatActivity{

    private final static boolean DEBUG = Utils.DEBUG;
    private final static String TAG = "ActFindSystem";

    private Utils mUtils = Utils.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_find_system);
        initView();
    }

    private void initView() {
        mUtils.printLog(DEBUG, TAG, "[initView]");
        getSupportActionBar().setTitle(getText(R.string.find_target_system));
        //액션바 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF56aed2));
        //홈버튼 표시
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_navigate_before_white_36dp);
    }

    //액션버튼 메뉴 액션바에 집어 넣기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.find_taget_system, menu);
        return true;
    }

    //액션버튼을 클릭했을때의 동작
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        /*if (id == R.id.action_home) {
            finish();
            Toast.makeText(this, "홈 이동", Toast.LENGTH_SHORT).show();
            return true;
        } else */
        if (id == android.R.id.home) {
            finish();
            Toast.makeText(this, "뒤로 가기", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
