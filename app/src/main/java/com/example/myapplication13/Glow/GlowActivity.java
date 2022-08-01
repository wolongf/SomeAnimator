package com.example.myapplication13.Glow;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication13.R;

public class GlowActivity extends AppCompatActivity {
    GlowView mGlowView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glow_view);
        mGlowView = findViewById(R.id.glow_view);
    }

    public void start(View view) {
        mGlowView.startAnim();
    }

    public void stop(View view) {
        mGlowView.stopAnim(false);
    }
}
