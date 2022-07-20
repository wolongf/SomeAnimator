package com.example.myapplication13;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class CardFlipActivity extends AppCompatActivity {
    private boolean showingBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_flip);

        // 默认显示卡片正面
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new CardFrontFragment())
                    .commit();
        }
    }

    /**
     * 翻转卡片
     *
     * @param view
     */
    public void flipCard(View view) {
        // 翻到反面
        if (!showingBack) {
            showingBack = true;
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.animator.card_flip_right_in,
                            R.animator.card_flip_right_out,
                            R.animator.card_flip_left_in,
                            R.animator.card_flip_left_out)
                    .replace(R.id.container, new CardBackFragment())
                    .commit();
        }
        // 翻到正面
        else {
            showingBack = false;
            getSupportFragmentManager()
                    .beginTransaction()
                    // enter, exit, popEnter, popExit
                    .setCustomAnimations(
                            R.animator.card_flip_left_in,
                            R.animator.card_flip_left_out,
                            R.animator.card_flip_right_in,
                            R.animator.card_flip_right_out)
                    .replace(R.id.container, new CardFrontFragment())
                    .commit();
        }
    }

}