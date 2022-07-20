package com.example.myapplication13;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class AutoAnimateLayoutUpdatesActivity extends AppCompatActivity {
    private LinearLayoutCompat linearLayoutCompat;
    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_animate_layout_updates);

        linearLayoutCompat = findViewById(R.id.linearLayoutCompat);

        // 添加布局
        findViewById(R.id.addLayout).setOnClickListener(v -> {
            // 定义新按钮
            Button button = new Button(this);
            button.setText("button " + index++);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            button.setLayoutParams(params);
            // 添加按钮到布局中
            linearLayoutCompat.addView(button, 0);
        });

        // 删除布局
        findViewById(R.id.removeLayout).setOnClickListener(v -> {
            if (index > 0) {
                linearLayoutCompat.removeViewAt(index - 1);
                index--;
            }
        });
    }
}