package com.example.myapplication13;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication13.Liquid.LiquidActivity;
import com.example.myapplication13.Path.BoatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //使用重力传感器绘制动画
        findViewById(R.id.liquidAnimation).setOnClickListener(v -> {
            Intent intent = new Intent(this, LiquidActivity.class);
            startActivity(intent);
        });

        // 为可绘制图形添加动画
        findViewById(R.id.animateDrawableGraphics).setOnClickListener(v -> {
            Intent intent = new Intent(this, AnimateDrawableGraphicsActivity.class);
            startActivity(intent);
        });

        // 使用动画显示或隐藏视图
        findViewById(R.id.revealOrHideAViewUsingAnimation).setOnClickListener(v -> {
            Intent intent = new Intent(this, RevealOrHideAViewUsingAnimationActivity.class);
            startActivity(intent);
        });

        // 使用动画移动视图
        findViewById(R.id.moveAViewWithAnimation).setOnClickListener(v -> {
            Intent intent = new Intent(this, MoveAViewWithAnimationActivity.class);
            startActivity(intent);
        });

        // 使用 Fling 动画移动视图
        findViewById(R.id.moveViewsUsingAFlingAnimation).setOnClickListener(v -> {
            Intent intent = new Intent(this, MoveViewsUsingAFlingAnimationActivity.class);
            startActivity(intent);
        });

        // 使用缩放动画放大视图
        findViewById(R.id.enlargeAViewUsingAZoomAnimation).setOnClickListener(v -> {
            Intent intent = new Intent(this, EnlargeAViewUsingAZoomAnimationActivity.class);
            startActivity(intent);
        });

        // 运用弹簧物理学原理为图形运动添加动画
        findViewById(R.id.animateMovementUsingSpringPhysics).setOnClickListener(v -> {
            Intent intent = new Intent(this, SpringAnimationActivity.class);
            startActivity(intent);
        });

        // 自动为布局更新添加动画
        findViewById(R.id.autoAnimateLayoutUpdates).setOnClickListener(v -> {
            Intent intent = new Intent(this, AutoAnimateLayoutUpdatesActivity.class);
            startActivity(intent);
        });

        // 使用过渡为布局变化添加动画效果
        findViewById(R.id.animateLayoutChangesUsingATransition).setOnClickListener(v -> {
            Intent intent = new Intent(this, AnimateLayoutChangesUsingATransitionActivity.class);
            startActivity(intent);
        });

        // 使用动画启动 Activity
        findViewById(R.id.startAnActivityUsingAnAnimation).setOnClickListener(v -> {
            Intent intent = new Intent(this, StartAnActivityUsingAnAnimationActivity.class);
            startActivity(intent);
        });

        // 使用 ViewPager2 在 Fragment 之间滑动
        findViewById(R.id.slideBetweenFragmentsUsingViewPager2).setOnClickListener(v -> {
            Intent intent = new Intent(this, SlideBetweenFragmentsUsingViewPager2Activity.class);
            startActivity(intent);
        });

        //贝塞尔曲线原理与实战
        findViewById(R.id.boatWave).setOnClickListener(v -> {
            Intent intent = new Intent(this, BoatActivity.class);
            startActivity(intent);
        });
    }
}