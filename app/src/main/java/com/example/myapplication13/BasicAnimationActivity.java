package com.example.myapplication13;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class BasicAnimationActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_animation);

        textView = findViewById(R.id.textView);
    }

    /**
     * 位移动画
     *
     * @param view
     */
    public void translation(View view) {
        ObjectAnimator objectAnimation = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator
                .animator_translation);
        objectAnimation.setTarget(textView);
        objectAnimation.start();
    }

    /**
     * 缩放动画
     *
     * @param view
     */
    public void scale(View view) {
        ObjectAnimator objectAnimation = (ObjectAnimator) AnimatorInflater.loadAnimator(this,
                R.animator.animator_scale);
        objectAnimation.setTarget(textView);
        objectAnimation.start();
    }

    /**
     * 旋转动画
     *
     * @param view
     */
    public void rotate(View view) {
        ObjectAnimator objectAnimation = (ObjectAnimator) AnimatorInflater.loadAnimator(this,
                R.animator.animator_rotate);
        objectAnimation.setTarget(textView);
        objectAnimation.start();
    }

    /**
     * 透明动画
     *
     * @param view
     */
    public void alpha(View view) {
        ObjectAnimator objectAnimation = (ObjectAnimator) AnimatorInflater.loadAnimator(this,
                R.animator.animator_alpha);
        objectAnimation.setTarget(textView);
        objectAnimation.start();
    }

    /**
     * 组合动画
     *
     * @param view
     */
    public void combine(View view) {
        AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.animator_combine);
        animatorSet.setTarget(textView);
        animatorSet.start();
    }
}