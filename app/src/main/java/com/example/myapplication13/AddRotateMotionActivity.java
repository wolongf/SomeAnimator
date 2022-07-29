package com.example.myapplication13;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.PathInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class AddRotateMotionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_curved_motion);

            ImageView translationImageView = findViewById(R.id.translationImageView);

            RotateAnimation mRadarAnimation = new RotateAnimation(0, 360f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            mRadarAnimation.setDuration(1600);
            mRadarAnimation.setRepeatCount(Animation.INFINITE);
            mRadarAnimation.setInterpolator(new PathInterpolator(.42f, .156f, .58f, .844f));
            translationImageView.startAnimation(mRadarAnimation);
    }

}
