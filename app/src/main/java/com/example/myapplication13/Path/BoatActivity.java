package com.example.myapplication13.Path;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication13.R;

public class BoatActivity extends AppCompatActivity {

    BoatWaveView mBoatWaveView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boat_wave_view);
        mBoatWaveView = findViewById(R.id.boat_wave_view);
    }

    public void start(View view) {
        mBoatWaveView.startAnim();
    }

    public void stop(View view) {
        mBoatWaveView.stopAnim();
    }

}
