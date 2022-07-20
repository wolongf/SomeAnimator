package com.example.myapplication13;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class RevealOrHideAViewUsingAnimationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reveal_or_hide_a_view_using_animation);

        // 创建淡入淡出动画
        findViewById(R.id.createACrossfadeAnimation).setOnClickListener(v -> {
            Intent intent = new Intent(this, CrossfadeActivity.class);
            startActivity(intent);
        });

        // 创建卡片翻转动画
        findViewById(R.id.createACardFlipAnimation).setOnClickListener(v -> {
            Intent intent = new Intent(this, CardFlipActivity.class);
            startActivity(intent);
        });

        // 创建圆形揭露动画
        findViewById(R.id.createACircularRevealAnimation).setOnClickListener(v -> {
            Intent intent = new Intent(this, CircularRevealActivity.class);
            startActivity(intent);
        });
    }
}