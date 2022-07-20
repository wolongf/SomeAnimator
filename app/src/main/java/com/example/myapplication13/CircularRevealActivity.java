package com.example.myapplication13;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;

public class CircularRevealActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circular_reveal);
    }

    /**
     * 圆形揭露动画
     *
     * @param view
     */
    public void circularReveal(View view) {
        ImageView imageView = findViewById(R.id.circleRevealImageView);

        // Check if the runtime version is at least Lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Animator anim = null;
            int cx = imageView.getWidth() / 2;
            int cy = imageView.getHeight() / 2;
            switch (imageView.getVisibility()) {
                case View.VISIBLE:
                    float initialRadius = (float) Math.hypot(cx, cy);
                    // 由里到外
                    anim = ViewAnimationUtils.createCircularReveal(imageView, cx, cy, initialRadius, 0f);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imageView.setVisibility(View.INVISIBLE);
                        }
                    });
                    break;
                case View.INVISIBLE:
                    float finalRadius = (float) Math.hypot(imageView.getWidth(), imageView.getHeight());
                    // 由左上角到右下角
                    anim = ViewAnimationUtils.createCircularReveal(imageView, 0, 0, 0f, finalRadius);
                    imageView.setVisibility(View.VISIBLE);
                    break;
            }
            anim.setDuration(2000);
            anim.start();
        } else {
            switch (imageView.getVisibility()) {
                case View.VISIBLE:
                    imageView.setVisibility(View.INVISIBLE);
                    break;
                case View.INVISIBLE:
                    imageView.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }
}