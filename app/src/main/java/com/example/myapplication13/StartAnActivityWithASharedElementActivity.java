package com.example.myapplication13;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class StartAnActivityWithASharedElementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_an_with_a_shared_element);
    }

    /**
     * 关闭当前页面
     *
     * @param view
     */
    public void close(View view) {
        finishAfterTransition();
    }
}