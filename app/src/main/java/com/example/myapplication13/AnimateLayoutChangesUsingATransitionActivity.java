package com.example.myapplication13;

import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.Fade;
import androidx.transition.Scene;
import androidx.transition.Transition;
import androidx.transition.TransitionInflater;
import androidx.transition.TransitionManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AnimateLayoutChangesUsingATransitionActivity extends AppCompatActivity {
    ViewGroup sceneRoot;
    Scene aScene;
    Scene anotherScene;
    boolean isCurrentAtScene1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animate_layout_changes_using_atransition);

        // 获取 ViewGroup 和 Scene 实例
        sceneRoot = (ViewGroup) findViewById(R.id.scene_root);
        aScene = Scene.getSceneForLayout(sceneRoot, R.layout.a_scene, this);
        anotherScene = Scene.getSceneForLayout(sceneRoot, R.layout.another_scene, this);
        isCurrentAtScene1 = true;

        // 从资源文件创建过渡实例
        Transition fadeTransition = TransitionInflater.from(this).inflateTransition(R.transition.fade_transition);
        // 从资源文件创建过渡集实例
        Transition transitionSet = TransitionInflater.from(this).inflateTransition(R.transition.transition_set);
        // 在代码中创建过渡实例
//        Transition fadeTransition = new Fade();

        // 过渡动画
        findViewById(R.id.transitionAnimation).setOnClickListener(v -> {
            if (isCurrentAtScene1) {
                TransitionManager.go(anotherScene, fadeTransition);
            } else {
                TransitionManager.go(aScene, transitionSet);
            }
            isCurrentAtScene1 = !isCurrentAtScene1;
        });

        // 应用没有场景的过渡
        findViewById(R.id.applyATransitionWithoutScenes).setOnClickListener(v -> {
            // 创建新的 View
            TextView labelText = new TextView(this);
            labelText.setText("应用没有场景的过渡……");
            labelText.setWidth(2000);
            labelText.setTextSize(22f);
            labelText.setTextColor(getResources().getColor(R.color.teal_200));
            labelText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            ViewGroup rootView = (ViewGroup) findViewById(R.id.container);

            // 创建过渡实例
            Fade mFade = new Fade(Fade.IN);
            // 记录视图层的变化
            TransitionManager.beginDelayedTransition(rootView, mFade);
            // 添加 View 到视图层
            rootView.addView(labelText);
        });

        // 创建自定义过渡动画
        findViewById(R.id.createACustomTransitionAnimation).setOnClickListener(v -> {
            startActivity(new Intent(this, CreateACustomTransitionAnimationActivity.class));
        });
    }
}