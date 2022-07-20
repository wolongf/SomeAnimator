package com.example.myapplication13;

import androidx.appcompat.app.AppCompatActivity;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FlingAnimation;
import androidx.dynamicanimation.animation.FloatPropertyCompat;

import android.graphics.PointF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.ImageView;

public class MoveViewsUsingAFlingAnimationActivity extends AppCompatActivity {
    private ImageView imageView;
    private PointF deltaMove = new PointF();
    private VelocityTracker velocityTracker;
    private FlingAnimation flingX;
    private FlingAnimation flingY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_views_using_a_fling_animation);

        imageView = findViewById(R.id.flingAnimationImageView);

        // 为图片注册触摸事件
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 获取触摸动作的类型
                int action = event.getActionMasked();
                // 获取触摸点的索引
                int index = event.getActionIndex();
                // 获取触摸点的 ID
                int pointerId = event.getPointerId(index);
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // 取消动画
                        if (flingX != null) {
                            flingX.cancel();
                        }
                        if (flingY != null) {
                            flingY.cancel();
                        }
                        if (velocityTracker == null) {
                            // 获取速度跟踪器对象
                            velocityTracker = VelocityTracker.obtain();
                        } else {
                            // 重置速度跟踪器
                            velocityTracker.clear();
                        }
                        // 添加被跟踪的事件
                        velocityTracker.addMovement(event);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 计算需要移动的距离
                        float distanceX = event.getRawX() - deltaMove.x;
                        float distanceY = event.getRawY() - deltaMove.y;
                        // 设置图片水平和竖直方向的位移距离
                        imageView.setTranslationX(imageView.getTranslationX() + distanceX);
                        imageView.setTranslationY(imageView.getTranslationY() + distanceY);
                        // 添加被跟踪的事件
                        velocityTracker.addMovement(event);
                        // 计算速度，设置跟踪的时间长度
                        velocityTracker.computeCurrentVelocity(1000);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // 获取 pointerId 对应的手指在水平方向的速度，在 computeCurrentVelocity() 之后调用
                        float velocityX = velocityTracker.getXVelocity(pointerId);
                        // 获取 pointerId 对应的手指在垂直方向的速度，在 computeCurrentVelocity() 之后调用
                        float velocityY = velocityTracker.getYVelocity(pointerId);
                        // 开启投掷动画（普通版 - 到达边界后不会反弹）
//                        startSampleFlingAnimation(velocityX, velocityY);
                        // 开启投掷动画
                        startFlingAnimation(velocityX, velocityY);
                        // 释放速度跟踪器对象
                        velocityTracker.recycle();
                        velocityTracker = null;
                        break;
                }
                // 更新每次移动后的位置
                deltaMove.set(event.getRawX(), event.getRawY());
                return true;
            }
        });
    }

    /**
     * Fling 动画（普通版 - 到达边界后不会反弹）
     *
     * @param velocityX vx
     * @param velocityY vy
     */
    public void startSampleFlingAnimation(float velocityX, float velocityY) {
        new FlingAnimation(imageView, DynamicAnimation.TRANSLATION_X)
                .setStartVelocity(velocityX)
                .start();
        new FlingAnimation(imageView, DynamicAnimation.TRANSLATION_Y)
                .setStartVelocity(velocityY)
                .start();
    }

    /**
     * Fling 动画
     *
     * @param velocityX vx
     * @param velocityY vy
     */
    public void startFlingAnimation(float velocityX, float velocityY) {
        // 取消动画
        if (flingX != null) {
            flingX.cancel();
        }
        if (flingY != null) {
            flingY.cancel();
        }
        // 初始化水平方向 fling
        flingX = new FlingAnimation(imageView, new FloatPropertyCompat<View>("") {
            @Override
            public float getValue(View object) {
                return imageView.getTranslationX();
            }

            @Override
            public void setValue(View object, float value) {
                imageView.setTranslationX(value);
            }
        });
        // 为水平方向 fling 设置速度跟踪器
        flingX.setStartVelocity(velocityX)
                .addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {
                    @Override
                    public void onAnimationUpdate(DynamicAnimation animation, float value, float velocity) {
                        // 定位当前位置(左右)
                        float leftNow = imageView.getLeft() + imageView.getTranslationX();
                        float rightNow = imageView.getRight() + imageView.getTranslationX();
                        // 左边界
                        boolean leftBounce = leftNow < 0 && velocity < 0;
                        // 右边界
                        boolean rightBounce = rightNow > ((View) imageView.getParent()).getRight() && velocity > 0;
                        if (leftBounce || rightBounce) {
                            // 到达边界后反弹
                            flingX.setStartVelocity(-velocity);
                        }
                    }
                })
                .start();
        // 初始化垂直方向 fling
        flingY = new FlingAnimation(imageView, new FloatPropertyCompat<View>("") {
            @Override
            public float getValue(View object) {
                return object.getTranslationY();
            }

            @Override
            public void setValue(View object, float value) {
                object.setTranslationY(value);
            }
        });
        // 为垂直方向 fling 设置速度跟踪器
        flingY.setStartVelocity(velocityY)
                .addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {
                    @Override
                    public void onAnimationUpdate(DynamicAnimation animation, float value, float velocity) {
                        // 定位当前位置(上下)
                        float topNow = imageView.getTop() + imageView.getTranslationY();
                        float bottomNow = imageView.getBottom() + imageView.getTranslationY();
                        // 上边界
                        boolean topBounce = topNow < 0 && velocity < 0;
                        // 下边界
                        boolean bottomBounce = bottomNow > ((View) imageView.getParent()).getBottom() && velocity > 0;
                        if (topBounce || bottomBounce) {
                            // 到达边界后反弹
                            flingY.setStartVelocity(-velocity);
                        }
                    }
                })
                .start();
    }
}