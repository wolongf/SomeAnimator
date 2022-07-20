package com.example.myapplication13;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CrossfadeActivity extends AppCompatActivity {
    private View contentView;
    private View loadingView;
    private int shortAnimationDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crossfade);

        contentView = findViewById(R.id.content);
        loadingView = findViewById(R.id.loading_spinner);

        // Initially hide the content view.
        contentView.setVisibility(View.GONE);

        // Retrieve and cache the system's default "short" animation time.
        shortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

        findViewById(R.id.crossFade).setOnClickListener(v -> {
            switch (contentView.getVisibility()) {
                case View.GONE:
                    crossfadeIn();
                    break;
                case View.VISIBLE:
                    crossfadeOut();
                    break;
            }
        });
    }

    /**
     * 淡入动画
     */
    private void crossfadeIn() {

        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        contentView.setAlpha(0f);
        contentView.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        contentView.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(null);

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        loadingView.animate()
                .alpha(0f)
                .setDuration(shortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        loadingView.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * 淡出动画
     */
    private void crossfadeOut() {

        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        loadingView.setAlpha(0f);
        loadingView.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        contentView.animate()
                .alpha(0f)
                .setDuration(shortAnimationDuration)
                .setListener(null);

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        loadingView.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        contentView.setVisibility(View.GONE);
                    }
                });
    }
}