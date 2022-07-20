package com.example.myapplication13;

import androidx.appcompat.app.AppCompatActivity;

import android.transition.Transition;
import android.transition.TransitionInflater;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class StartAnActivityUsingAnAnimationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 申请启用窗口内容过渡，必须在 setContentView() 之前，
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_start_an_using_an_animation);

        // 使用 OverridePendingTransition 方法实现 Activity 跳转动画
        findViewById(R.id.startAnActivityUsingOverridePendingTransition).setOnClickListener(v -> {
            Intent intent = new Intent(this, StartAnActivityUsingOverridePendingTransitionActivity.class);
            startActivity(intent);
            // 紧跟 startActivity() 之后
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
        });

        // 使用 Style 的方式定义 Activity 跳转动画
        findViewById(R.id.startAnActivityUsingStyle).setOnClickListener(v -> {
            Intent intent = new Intent(this, StartAnActivityUsingStyleActivity.class);
            startActivity(intent);
        });

        // 使用 ActivityOptions 实现 Activity 跳转动画
        findViewById(R.id.startAnActivityUsingActivityOptions).setOnClickListener(v -> {
            // 创建过渡实例
            Transition explode = TransitionInflater.from(this).inflateTransition(android.R.transition.explode);
            // 设置过渡方式
            getWindow().setEnterTransition(explode);
            // 若通过 style 的方式来跳转，则不用以上两步操作，在清单文件添加主题即可
            // 跳转使用过渡动画的 Activity
            startActivity(new Intent(this, StartAnActivityUsingActivityOptionsActivity.class),
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });

        // 使用 ActivityOptions 共享元素的方式实现 Activity 跳转动画
        findViewById(R.id.startAnActivityWithASharedElement).setOnClickListener(v -> {
            startActivity(new Intent(this, StartAnActivityWithASharedElementActivity.class),
                    ActivityOptions.makeSceneTransitionAnimation(this, findViewById(R.id.startAnActivityWithASharedElement), "sharedButton").toBundle());
        });
    }
}