package com.example.myapplication13;

import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.Scene;
import androidx.transition.TransitionManager;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.transition.Transition;

public class CreateACustomTransitionAnimationActivity extends AppCompatActivity {
    private boolean isCurrentAtScene1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acustom_transition_animation);

        // 获取 ViewGroup 和 Scene 实例
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.scene_container);
        Scene customScene1 = Scene.getSceneForLayout(viewGroup, R.layout.custom_scene_1, this);
        Scene customScene2 = Scene.getSceneForLayout(viewGroup, R.layout.custom_scene_2, this);
        isCurrentAtScene1 = true;

        // 创建自定义过渡实例
        Transition customTransition = new CustomTransition();

        // 自定义过渡动画
        findViewById(R.id.createACustomTransitionAnimation).setOnClickListener(v -> {
            if (isCurrentAtScene1) {
                TransitionManager.go(customScene2, customTransition);
            } else {
                TransitionManager.go(customScene1, customTransition);
            }
            isCurrentAtScene1 = !isCurrentAtScene1;
        });
    }
}