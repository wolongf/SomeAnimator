package com.example.myapplication13;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AddCurvedMotionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_curved_motion);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ImageView translationImageView = findViewById(R.id.translationImageView);

            Path path = new Path();
            path.arcTo(200f, 200f, 800f, 800f, 270f, -270f, true);
            ObjectAnimator animator = ObjectAnimator.ofFloat(translationImageView, View.X, View.Y, path);
            animator.setDuration(2000);
            animator.setRepeatCount(ObjectAnimator.INFINITE);
            animator.setRepeatMode(ObjectAnimator.REVERSE);
            animator.start();
        } else {
            // Create animator without using curved path
        }
    }
}