package com.example.myapplication13.Liquid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication13.R;

public class LiquidActivity extends AppCompatActivity {
    /**
     * The view to show the traffic info in general with animation
     */
    private LiquidView mLiquidView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liquid_view);
        mLiquidView = (LiquidView) findViewById(R.id.liquidView);
        initLiquidView(mLiquidView);
        update(false,500,200);
    }

    private void initLiquidView(LiquidView liquidView) {
        liquidView.addRing(getResources().getDimensionPixelSize(R.dimen.traffic_liquidView_borderwidth));
        liquidView.setGravityEnable(true);// 开启重力感应
    }

    public void update(boolean isIdleMode, long limitBytes, long usedBytes) {
        long leftBytes = limitBytes - usedBytes;
        float percent = (float) usedBytes * 100 / (float) limitBytes;

        if (limitBytes == -1) {
            return;
        }
        mLiquidView.setLiquidPercent(100 - percent);
        switchLiquidColor(percent);
    }
    /**
     * 根据不同百分比设置LiquidView液体颜色
     *
     * @param usedPercent 已用流量百分比
     */
    private void switchLiquidColor(float usedPercent) {
        if (usedPercent < 0) {
            return;
        }


        //正常流量模式
        if (usedPercent >= 0 && usedPercent <= 60) {// 当使用0<=liquidUsedPercent<=60,液体为绿色
            mLiquidView.setLiquidBackgroundColor(Color.rgb(53, 147, 65));
            mLiquidView.setRingColor(0, Color.rgb(53, 147, 65));
            mLiquidView.setLiquidColor(Color.rgb(73, 177, 83));
        } else if (usedPercent > 60 && usedPercent < 75) {// 当使用60<liquidUsedPercent<75,液体为黄色
            mLiquidView.setLiquidBackgroundColor(Color.rgb(220, 134, 31));
            mLiquidView.setRingColor(0, Color.rgb(220, 134, 31));
            mLiquidView.setLiquidColor(Color.rgb(235, 164, 37));
        } else if (usedPercent >= 75 && usedPercent < 90) {// 当使用75<=liquidUsedPercent<90,液体为橙色
            mLiquidView.setLiquidBackgroundColor(Color.rgb(216, 100, 23));
            mLiquidView.setRingColor(0, Color.rgb(216, 100, 23));
            mLiquidView.setLiquidColor(Color.rgb(239, 125, 29));
        } else if (usedPercent >= 90 && usedPercent < 100) {// 当使用90<=liquidUsedPercent<100,液体为红色
            mLiquidView.setLiquidBackgroundColor(Color.rgb(205, 67, 51));
            mLiquidView.setRingColor(0, Color.rgb(205, 67, 51));
            mLiquidView.setLiquidColor(Color.rgb(231, 90, 72));
        } else {// 流量超出
            mLiquidView.setLiquidBackgroundColor(Color.rgb(205, 67, 51));
            mLiquidView.setRingColor(0, Color.rgb(205, 67, 51));
            mLiquidView.setLiquidColor(Color.rgb(205, 67, 51));
        }
    }



}
