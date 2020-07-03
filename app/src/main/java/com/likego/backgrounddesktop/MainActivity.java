package com.likego.backgrounddesktop;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: -------------------------------");
        findViewById(R.id.open_setting_id).setOnClickListener(this);
        findViewById(R.id.update_id).setOnClickListener(this);
        findViewById(R.id.reset_id).setOnClickListener(this);
        findViewById(R.id.cut_layoutManager_id).setOnClickListener(this);

        long startTime = System.currentTimeMillis();
        mAppInfoModels = AppInfoUtils.getLauncherInfo(this);
        initRecyclerView();
        Toast.makeText(this,"加载耗时"+(System.currentTimeMillis() - startTime)+" ms",Toast.LENGTH_SHORT).show();

    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.launcher_rv_id);
        CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL,true);//true循环轮播
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this,mAppInfoModels,R.layout.rc_item);

        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        mRecyclerView.setHasFixedSize(true);//item不会去改变RecyclerView宽高时调用
        mRecyclerView.addOnScrollListener(new CenterScrollListener());//给CarouselLayoutManager使用的,监听滑动状态变化

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.setOnItemClickListener(onItemClickListener);
    }

    private void initMoreRecyclerView() {
        StaggeredGridLayoutManager staggeredGridLayoutManager
                = new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL);
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
        }

        @Override
        public void onItemLongClick(int position) {
            Uri packageUri = Uri.parse("package:" + mAppInfoModels.get(position).getPackageName());
            Intent intent = new Intent(Intent.ACTION_DELETE, packageUri);
            startActivity(intent);
        }
    };

    private boolean mCurLayoutManageFlag = false;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.open_setting_id:
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.update_id:
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
//                sendBroadcast(new Intent("android.intent.action.MASTER_CLEAR"));
                break;
            case R.id.cut_layoutManager_id:
                TextView textView = (TextView) v;
                if (mCurLayoutManageFlag){
                    mCurLayoutManageFlag = false;
                    initRecyclerView();
                    textView.setText("切换瀑布模式");
                }else {
                    mCurLayoutManageFlag = true;
                    initMoreRecyclerView();
                    textView.setText("切换画廊模式");
                }
                break;
            default:
                break;
        }
    }
}