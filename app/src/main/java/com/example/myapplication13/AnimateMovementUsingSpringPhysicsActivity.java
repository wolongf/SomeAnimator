package com.example.myapplication13;

import androidx.appcompat.app.AppCompatActivity;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.ImageView;

public class AnimateMovementUsingSpringPhysicsActivity extends AppCompatActivity {
    private ImageView imageView;
    private SpringAnimation springAnimX;
    private SpringAnimation springAnimY;
    private VelocityTracker velocityTracker;
    private float dX;
    private float dY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animate_movement_using_spring_physics);

        imageView = findViewById(R.id.ball);
        springAnimX = new SpringAnimation(imageView, SpringAnimation.TRANSLATION_X, 0);
        springAnimY = new SpringAnimation(imageView, SpringAnimation.TRANSLATION_Y, 0);

        // 为图片注册触摸事件
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        // 取消动画
                        if (springAnimX != null) {
                            springAnimX.cancel();
                        }
                        if (springAnimY != null) {
                            springAnimY.cancel();
                        }
                        if (velocityTracker == null) {
                            // 获取速度跟踪器对象
                            velocityTracker = VelocityTracker.obtain();
                        } else {
                            // 重置速度跟踪器
                            velocityTracker.clear();
                        }
                        // 设置触摸时图片与触摸点水平和竖直方向距离的差值
                        dX = event.getRawX() - v.getX();
                        dY = event.getRawY() - v.getY();
                        // 添加被跟踪的事件
                        velocityTracker.addMovement(event);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 跟随手指移动图片
                        imageView.setX(event.getRawX() - dX);
                        imageView.setY(event.getRawY() - dY);
                        // 添加被跟踪的事件
                        velocityTracker.addMovement(event);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        velocityTracker.computeCurrentVelocity(1000);
                        if (event.getRawX() - dX != 0) {
                            // 设置起始速度
                            springAnimX.setStartVelocity(velocityTracker.getXVelocity());
                            // 设置阻尼比
                            springAnimX.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
                            // 设置刚度
                            springAnimX.getSpring().setStiffness(SpringForce.STIFFNESS_MEDIUM);
                            springAnimX.start();
                        }
                        if (event.getRawY() - dY != 0) {
                            // 设置起始速度
                            springAnimY.setStartVelocity(velocityTracker.getYVelocity());
                            // 设置阻尼比
                            springAnimY.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
                            // 设置刚度
                            springAnimY.getSpring().setStiffness(SpringForce.STIFFNESS_MEDIUM);
                            springAnimY.start();
                        }
                        velocityTracker.clear();
                        velocityTracker = null;
                        break;
                }
                return true;
            }
        });
    }
}