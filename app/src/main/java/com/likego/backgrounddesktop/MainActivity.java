package com.likego.backgrounddesktop;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecyclerView();
    }

    String[] strings = new String[]{"1","2","3","4","5"};
    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.launcher_rv_id);
        CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL,true);//true循环轮播
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this,strings);

        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        recyclerView.setHasFixedSize(true);//item不会去改变RecyclerView宽高时调用
        recyclerView.addOnScrollListener(new CenterScrollListener());//给CarouselLayoutManager使用的,监听滑动状态变化

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

}