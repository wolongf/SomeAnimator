package com.example.myapplication13;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class StartAnActivityUsingOverridePendingTransitionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_an_using_override_pending_transition);
    }

    /**
     * 关闭当前页面
     *
     * @param view
     */
    public void close(View view) {
        finish();
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top);
    }
}