package com.archaeanx.libx;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.archeanx.libx.appupdate.AppUpdateManager;
import com.archeanx.libx.appupdate.OnAppUpdateStatusListener;
import com.archeanx.libx.util.ToastUtil;
import com.archeanx.libx.widget.dialog.XProgressDialog;
import com.archeanx.libx.widget.divider.XRvVerDivider;

import java.util.ArrayList;
import java.util.List;

public class TestMainActivity extends AppCompatActivity {
    private static final int ITEM_1 = 111;
    private static final int ITEM_2 = 222;
    private static final int ITEM_3 = 333;
    private static final int ITEM_4 = 444;
    private static final int ITEM_5 = 555;
    private static final int ITEM_6 = 666;
    private static final int ITEM_7 = 777;

    static List<Object> lists = new ArrayList<>();

    static {
        lists.add(ITEM_1);
        lists.add(ITEM_2);
        lists.add(ITEM_2);
        lists.add(ITEM_2);
        lists.add(ITEM_2);
        lists.add(ITEM_2);
        lists.add(ITEM_2);
        lists.add(ITEM_2);
        lists.add(ITEM_2);
        lists.add(ITEM_2);
        lists.add(ITEM_2);
        lists.add(ITEM_2);
        lists.add(ITEM_3);
        lists.add(ITEM_4);
        lists.add(ITEM_4);
        lists.add(ITEM_4);
        lists.add(ITEM_4);
        lists.add(ITEM_4);
        lists.add(ITEM_4);
        lists.add(ITEM_4);
        lists.add(ITEM_5);
        lists.add(ITEM_5);
        lists.add(ITEM_5);
        lists.add(ITEM_5);
        lists.add(ITEM_5);
        lists.add(ITEM_5);
        lists.add(ITEM_5);
        lists.add(ITEM_5);
        lists.add(ITEM_5);
        lists.add(ITEM_6);
        lists.add(ITEM_6);
        lists.add(ITEM_6);
        lists.add(ITEM_6);
        lists.add(ITEM_6);
        lists.add(ITEM_6);
        lists.add(ITEM_7);
        lists.add(ITEM_7);
        lists.add(ITEM_7);
        lists.add(ITEM_7);
        lists.add(ITEM_7);
        lists.add(ITEM_7);
        lists.add(ITEM_7);
        lists.add(ITEM_7);
        lists.add(ITEM_7);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppUpdateManager.getInstance().init(this);
        Log.e("showLogn_begin", Long.toString(System.currentTimeMillis()));
        ToastUtil.init(this);
        ToastUtil.show("aljfsdlfjalsjljljljagl;difjasnc");
        Log.e("showLogn_endee", Long.toString(System.currentTimeMillis()));

        findViewById(R.id.am_show_toast).setOnClickListener(new View.OnClickListener() {

            private XProgressDialog mProgressDialog;

            @Override
            public void onClick(View v) {
                XProgressDialog.show(TestMainActivity.this, "不在加载中");

                App.sAppHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        XProgressDialog.show(TestMainActivity.this, "1111");
                    }
                },2000);
                //AppUpdateManager.getInstance().inspectVersion( "更新App", "http://news.wisdomforcloud.com/17.apk");
            }
        });

        RecyclerView recyclerView = findViewById(R.id.am_rv);
        recyclerView.setItemViewCacheSize(10);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new XRvVerDivider.Builder(this).build());
        TestAdapter1 adapter = new TestAdapter1();

        recyclerView.setAdapter(adapter);

        adapter.setDatas(lists, true);

        AppUpdateManager.getInstance().setOnAppUpdateStatusListener(new OnAppUpdateStatusListener() {

            @Override
            public void onRunning() {
                ToastUtil.show("正在下载App更新包");
            }
        });


        XProgressDialog.show(this, "加载中");
    }


}
