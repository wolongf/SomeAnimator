package com.example.myapplication13;

import android.animation.Animator;
import android.animation.ObjectAnimator;

import androidx.transition.Transition;
import androidx.transition.TransitionValues;

import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.textview.MaterialTextView;

public class CustomTransition extends Transition {
    private final static String PROPNAME_TEXT_COLOR = "com.example.myapplication13:textColor";

    /**
     * 记录下起始状态时的属性值
     *
     * @param transitionValues
     */
    public void captureStartValues(TransitionValues transitionValues) {
        if (transitionValues != null && transitionValues.view instanceof MaterialTextView) {
            captureValues(transitionValues);
        }
    }

    /**
     * 记录下结束状态时的属性值
     *
     * @param transitionValues
     */
    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        if (transitionValues != null && transitionValues.view instanceof MaterialTextView) {
            captureValues(transitionValues);
        }
    }

    /**
     * 获取视图属性值
     *
     * @param transitionValues
     */
    private void captureValues(TransitionValues transitionValues) {
        MaterialTextView view = (MaterialTextView) transitionValues.view;
        transitionValues.values.put(PROPNAME_TEXT_COLOR, view.getCurrentTextColor());
    }

    /**
     * 创建动画
     *
     * @param sceneRoot
     * @param startValues
     * @param endValues
     * @return
     */
    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues,
                                   final TransitionValues endValues) {
        if (startValues == null || endValues == null) {
            return null;
        }
        if (!(endValues.view instanceof MaterialTextView)) {
            return super.createAnimator(sceneRoot, startValues, endValues);
        }
        // 获取动画结束后的视图
        TextView endView = (TextView) endValues.view;
        // 获取动画执行前后 TextView 的字体颜色
        int startTextColor = (int) startValues.values.get(PROPNAME_TEXT_COLOR);
        int endTextColor = (int) endValues.values.get(PROPNAME_TEXT_COLOR);
        // 设置动画
        ObjectAnimator animator = ObjectAnimator.ofArgb(endView, "TextColor", startTextColor, endTextColor);
        animator.setDuration(2000);
        return animator;
    }
}
