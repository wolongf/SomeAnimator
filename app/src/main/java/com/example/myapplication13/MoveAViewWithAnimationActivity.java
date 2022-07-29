package com.example.myapplication13;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MoveAViewWithAnimationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_a_view_with_animation);

        // 使用 ObjectAnimator 更改视图位置
        findViewById(R.id.changeTheViewPositionWithObjectAnimator).setOnClickListener(v -> {
            Intent intent = new Intent(this, BasicAnimationActivity.class);
            startActivity(intent);
        });

        // 添加曲线动作
        findViewById(R.id.addCurvedMotion).setOnClickListener(v -> {
            Intent intent = new Intent(this, AddCurvedMotionActivity.class);
            startActivity(intent);
        });

        //添加自转动作
        findViewById(R.id.addRotateMotion).setOnClickListener(v -> {
            Intent intent = new Intent(this, AddRotateMotionActivity.class);
            startActivity(intent);
        });
    }
}