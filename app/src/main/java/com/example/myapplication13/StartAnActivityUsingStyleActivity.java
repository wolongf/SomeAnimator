package com.example.myapplication13;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class StartAnActivityUsingStyleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_an_using_style);
    }

    /**
     * 关闭当前页面
     *
     * @param view
     */
    public void close(View view) {
        finish();
    }
}