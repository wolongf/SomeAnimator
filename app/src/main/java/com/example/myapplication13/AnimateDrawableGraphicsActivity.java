package com.example.myapplication13;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;

public class AnimateDrawableGraphicsActivity extends AppCompatActivity {
    AnimationDrawable animationDrawable;
    AnimatedVectorDrawable animatedVectorDrawable;
    ImageView animationDrawableImageView;
    ImageView animatedVectorDrawableImageView;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animate_drawable_graphics);

        animationDrawableImageView = findViewById(R.id.animationDrawableImageView);
        animatedVectorDrawableImageView = findViewById(R.id.animatedVectorDrawableImageView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 使用 AnimationDrawable 播放帧动画
        animationDrawableImageView.setBackgroundResource(R.drawable.animation_list);
        animationDrawable = (AnimationDrawable) animationDrawableImageView.getBackground();
        // 对 AnimationDrawable 调用的 start() 方法不能在 Activity 的 onCreate() 方法期间调用，因为 AnimationDrawable 尚未完全附加到窗口
        animationDrawable.start();

        // 使用 AnimatedVectorDrawable 手动绘制矢量图
        animatedVectorDrawableImageView.setBackgroundResource(R.drawable.animator_vector_drawable);
        animatedVectorDrawable = (AnimatedVectorDrawable) animatedVectorDrawableImageView.getBackground();
        animatedVectorDrawable.start();
    }
}