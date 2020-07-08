package com.likego.backgrounddesktop;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.likego.backgrounddesktop.model.AppInfoModel;
import com.likego.backgrounddesktop.utils.AppInfoUtils;

import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private ArrayList<AppInfoModel> mAppInfoModels = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private boolean mScreenFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: -------------------------------");
        initWindow();//沉浸式

        findViewById(R.id.open_setting_id).setOnClickListener(this);
        findViewById(R.id.update_id).setOnClickListener(this);
        findViewById(R.id.reset_id).setOnClickListener(this);
        TextView textView = findViewById(R.id.cut_layoutManager_id);
        textView.setOnClickListener(this);
        findViewById(R.id.start_Development_id).setOnClickListener(this);
        findViewById(R.id.show_xml_id).setOnClickListener(this);
        findViewById(R.id.hide_xml_id).setOnClickListener(this);
        mRecyclerView = findViewById(R.id.launcher_rv_id);

        long startTime = System.currentTimeMillis();

        mAppInfoModels = AppInfoUtils.getLauncherInfo(this);//获取所有APP信息
        mScreenFlag = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;//获取横竖屏状态

        if (savedInstanceState != null && savedInstanceState.getBoolean("status")) {//获取旋转屏幕前的状态
            mCurLayoutManageFlag = true;
            textView.setText("画廊模式");
            initMoreRecyclerView();
        }else {
            initRecyclerView();
        }

        Toast.makeText(this,"加载耗时"+(System.currentTimeMillis() - startTime)+" ms",Toast.LENGTH_SHORT).show();

    }

    private void initWindow() {
        Window window = getWindow();
        View decorView = window.getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void initRecyclerView() {
        Log.d(TAG, "initMoreRecyclerView: 切换画廊模式");
        CarouselLayoutManager layoutManager;
        if (mScreenFlag){//横屏
            layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL,true);//true循环轮播
        }else {//竖屏
            layoutManager = new CarouselLayoutManager(CarouselLayoutManager.VERTICAL,true);//true循环轮播
        }
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this,mAppInfoModels,R.layout.rc_item);

        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        mRecyclerView.setHasFixedSize(true);//item不会去改变RecyclerView宽高时调用
        mRecyclerView.addOnScrollListener(new CenterScrollListener());//给CarouselLayoutManager使用的,监听滑动状态变化

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.setOnItemClickListener(onItemClickListener);
    }

    private void initMoreRecyclerView() {
        Log.d(TAG, "initMoreRecyclerView: 切换方格模式");
        StaggeredGridLayoutManager staggeredGridLayoutManager;
        if (mScreenFlag){//横屏
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.HORIZONTAL);
        }else {
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        }
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this,mAppInfoModels,R.layout.staggered_item);
        mRecyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.setOnItemClickListener(onItemClickListener);
    }

    private RecyclerViewAdapter.OnItemClickListener onItemClickListener = new RecyclerViewAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            AppInfoModel appInfoModel = mAppInfoModels.get(position);
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setComponent(new ComponentName(appInfoModel.getPackageName(), appInfoModel.getPackageClassName()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            startActivity(intent);
            Log.d(TAG, "onItemClick: 打开应用 "+appInfoModel.getAppName());
        }

        @Override
        public void onItemLongClick(int position) {
            Uri packageUri = Uri.parse("package:" + mAppInfoModels.get(position).getPackageName());
            Intent intent = new Intent(Intent.ACTION_DELETE, packageUri);
            startActivity(intent);
            Log.d(TAG, "onItemClick: 卸载应用 "+mAppInfoModels.get(position).getAppName());
        }
    };

    private boolean mCurLayoutManageFlag = false;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.open_setting_id:
                Log.d(TAG, "onClick: 打开设置");
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.update_id:
                Log.d(TAG, "onClick: 打开升级程序");
                try {
                    Intent upgradeIntent = new Intent("android.com.likego.hph03_update.Main");
                    upgradeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(upgradeIntent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this,"没有升级程序",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.reset_id:
                Log.d(TAG, "onClick: 恢复出厂设置");
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("恢复出厂设置");
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "onClick: 确认");
                        sendBroadcast(new Intent("android.intent.action.MASTER_CLEAR"));
                    }
                });
                builder.create().show();
                break;
            case R.id.cut_layoutManager_id:
                Log.d(TAG, "onClick: 切换模式");
                TextView textView = (TextView) v;
                if (mCurLayoutManageFlag){
                    mCurLayoutManageFlag = false;
                    initRecyclerView();
                    textView.setText("方格模式");
                }else {
                    mCurLayoutManageFlag = true;
                    initMoreRecyclerView();
                    textView.setText("画廊模式");
                }
                break;
            case R.id.start_Development_id:
                AppInfoUtils.startDevelopmentActivity(MainActivity.this);
                break;
            case R.id.show_xml_id:
                boolean status = AppInfoUtils.startUiTest(true);
                if (status){
                    Toast.makeText(MainActivity.this,"显示成功,稍后生效",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(MainActivity.this,"显示失败",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.hide_xml_id:
                status = AppInfoUtils.startUiTest(false);
                if (status){
                    Toast.makeText(MainActivity.this,"关闭成功,稍后生效",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(MainActivity.this,"关闭失败",Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("status", mCurLayoutManageFlag);//旋转屏幕时保存当前模式
    }
}