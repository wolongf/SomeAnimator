package com.example.myapplication13;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ScreenSlidePageFragment extends Fragment {
    private String text;

    public ScreenSlidePageFragment(String text) {
        this.text = text;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
        ((TextView) view.findViewById(R.id.textView)).setText(text);
        return view;
    }

}