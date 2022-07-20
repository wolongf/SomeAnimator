package com.example.myapplication13;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

public class EnlargeAViewUsingAZoomAnimationActivity extends AppCompatActivity {
    private Animator currentAnimator;
    private int shortAnimationDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enlarge_a_view_using_a_zoom_animation);

        final View thumb1View = findViewById(R.id.thumb_button_1);

        // 绑定缩放事件
        thumb1View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomImageFromThumb(thumb1View, R.drawable.woman);
            }
        });

        // 设置动画时长
        shortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

    }

    /**
     * 缩放动画
     *
     * @param thumbView
     * @param imageResId
     */
    private void zoomImageFromThumb(final View thumbView, int imageResId) {
        // 当前有动画时取消动画
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        // 为大图设置图片路径，因为图片默认的 scaleType 值是 fit_center，所以图片会被等比缩放到能够填充控件大小
        final ImageView expandedImageView = (ImageView) findViewById(R.id.expanded_image);
        expandedImageView.setImageResource(imageResId);

        // 定义大图的起始和结束边界(left, top, right, bottom)
        // 起始边界是缩略图的全局可见矩形，结束边界是容器的全局可见矩形
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // 获取缩略图的位置并赋值给变量
        thumbView.getGlobalVisibleRect(startBounds);
        // 获取顶层容器和偏移量的位置并赋值给变量,偏移量是指容器左上角的点相对于屏幕(顶层容器 + actionBar + 状态栏)的距离,用坐标表示
        // 详情见 https://stackoverflow.com/questions/35789319/what-exactly-is-getglobalvisiblerect
        findViewById(R.id.container).getGlobalVisibleRect(finalBounds, globalOffset);
        // 用偏移量来校正边界值,保证边界值是相对于屏幕的
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // 使用 center_crop 将起始边界调整为与最终边界相同的纵横比边界，可以防止在动画期间的不良拉伸
        // 比较缩略图与容器宽高的比值，分成两种情况，通过缩放后的大图来反推其起始和结束边界
        float startScale;
        if ((float) startBounds.width() / startBounds.height() > (float) finalBounds.width() / finalBounds.height()) {
            // 纵向放大(拉伸高度)
            // 换成 宽度的比值 < 高度的比值(Y/y < X/x) 更容易理解，其中 XY 分别对应容器的宽高, xy 分别对应缩略图的宽高

            // 计算缩略图与大图宽度的比值
            startScale = (float) startBounds.width() / finalBounds.width();
            // 用相同的缩放比例来计算大图的高度
            float startHeight = startScale * finalBounds.height();
            // 计算大图与缩略图高度的差值
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            // 拉高顶部
            startBounds.top -= deltaHeight;
            // 拉低底部
            startBounds.bottom += deltaHeight;
        } else {
            // 横向放大(拉伸宽度)
            // 换成宽度的比值 > 高度的比值(Y/y > X/x) 更容易理解，其中 XY 分别对应容器的宽高, xy 分别对应缩略图的宽高

            // 计算缩略图与大图高度的比值
            startScale = (float) startBounds.height() / finalBounds.height();
            // 用相同的缩放比例来计算大图的宽度
            float startWidth = startScale * finalBounds.width();
            // 计算大图与缩略图宽度的差值
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            // 缩窄左边
            startBounds.left -= deltaWidth;
            // 拉宽右边
            startBounds.right += deltaWidth;
        }

        // 隐藏缩略图，同时设置大图可见
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // 设置左上角为放大后视图的轴心点,默认是在中心
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // 设置联合动画并运行(放大)
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale, 1f));
        set.setDuration(shortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                currentAnimator = null;
            }
        });
        set.start();
        currentAnimator = set;

        // 给大图注册点击监听事件，实现缩小功能
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentAnimator != null) {
                    currentAnimator.cancel();
                }

                // 设置联合动画并运行(缩小)
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top))
                        .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScaleFinal));
                set.setDuration(shortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        currentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        currentAnimator = null;
                    }
                });
                set.start();
                currentAnimator = set;
            }
        });
    }

}