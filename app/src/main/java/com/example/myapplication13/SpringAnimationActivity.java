package com.example.myapplication13;

import android.os.Bundle;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class SpringAnimationActivity extends AppCompatActivity {

    private static final String TAG = "SpringAnimationActivity";
    private SpringAnimation xAnimation;
    private SpringAnimation yAnimation;
    ImageView imageView;

    private float dX;
    private float dY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spring);

        imageViewDragSpringAnimation();
        chainedSpringAnimation();

    }

    private void imageViewDragSpringAnimation() {

        imageView = findViewById(R.id.imageView);
        imageView.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
        imageView.setOnTouchListener(touchListener);
    }

    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            xAnimation = createSpringAnimation(imageView, SpringAnimation.X, imageView.getX(),
                    SpringForce.STIFFNESS_MEDIUM, SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
            yAnimation = createSpringAnimation(imageView, SpringAnimation.Y, imageView.getY(),
                    SpringForce.STIFFNESS_MEDIUM, SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        }
    };

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    // capture the difference between view's top left corner and touch point
                    dX = v.getX() - event.getRawX();
                    dY = v.getY() - event.getRawY();
                    // cancel animations
                    xAnimation.cancel();
                    yAnimation.cancel();
                    break;
                case MotionEvent.ACTION_MOVE:
                    //  a different approach would be to change the view's LayoutParams.
                    imageView.animate()
                            .x(event.getRawX() + dX)
                            .y(event.getRawY() + dY)
                            .setDuration(0)
                            .start();
                    break;
                case MotionEvent.ACTION_UP:
                    xAnimation.start();
                    yAnimation.start();
                    break;
            }
            return true;
        }
    };

    public SpringAnimation createSpringAnimation(View view,
                                                 DynamicAnimation.ViewProperty property,
                                                 float finalPosition,
                                                 float stiffness,
                                                 float dampingRatio) {
        SpringAnimation animation = new SpringAnimation(view, property);
        SpringForce springForce = new SpringForce(finalPosition);
        springForce.setStiffness(stiffness);
        springForce.setDampingRatio(dampingRatio);
        animation.setSpring(springForce);
        return animation;
    }


    public SpringAnimation createSpringAnimation(View view,
                                                 DynamicAnimation.ViewProperty property,
                                                 float stiffness,
                                                 float dampingRatio) {
        SpringAnimation animation = new SpringAnimation(view, property);
        SpringForce springForce = new SpringForce();
        springForce.setStiffness(stiffness);
        springForce.setDampingRatio(dampingRatio);
        animation.setSpring(springForce);
        return animation;
    }


    private void chainedSpringAnimation() {

        final FloatingActionButton fab4 = findViewById(R.id.fab4);
        final FloatingActionButton fab5 = findViewById(R.id.fab5);
        final FloatingActionButton fab6 = findViewById(R.id.fab6);

        final SpringAnimation firstXAnim = createSpringAnimation(fab5, DynamicAnimation.X, SpringForce.STIFFNESS_LOW, SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        final SpringAnimation firstYAnim = createSpringAnimation(fab5, DynamicAnimation.Y, SpringForce.STIFFNESS_MEDIUM, SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        final SpringAnimation secondXAnim = createSpringAnimation(fab6, DynamicAnimation.X, SpringForce.STIFFNESS_LOW, SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        final SpringAnimation secondYAnim = createSpringAnimation(fab6, DynamicAnimation.Y, SpringForce.STIFFNESS_LOW, SpringForce.DAMPING_RATIO_HIGH_BOUNCY);


        final ViewGroup.MarginLayoutParams fab5Params = (ViewGroup.MarginLayoutParams) fab5.getLayoutParams();
        final ViewGroup.MarginLayoutParams fab6Params = (ViewGroup.MarginLayoutParams) fab6.getLayoutParams();


        firstXAnim.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {
            @Override
            public void onAnimationUpdate(DynamicAnimation dynamicAnimation, float v, float v1) {

                secondXAnim.animateToFinalPosition(v + ((fab5.getWidth() -
                        fab6.getWidth()) / 2));

            }
        });

        firstYAnim.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {
            @Override
            public void onAnimationUpdate(DynamicAnimation dynamicAnimation, float v, float v1) {
                secondYAnim.animateToFinalPosition(v + fab5.getHeight() +
                        fab6Params.topMargin);
            }
        });


        fab4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = view.getX() - motionEvent.getRawX();
                        dY = view.getY() - motionEvent.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:

                        float newX = motionEvent.getRawX() + dX;
                        float newY = motionEvent.getRawY() + dY;

                        view.animate().x(newX).y(newY).setDuration(0).start();
                        firstXAnim.animateToFinalPosition(newX + ((fab4.getWidth() -
                                fab5.getWidth()) / 2));
                        firstYAnim.animateToFinalPosition(newY + fab4.getHeight() +
                                fab5Params.topMargin);
                        Log.d(TAG, "view.getX(): "+view.getX()+" view.getY(): "+view.getY()+
                                " motionEvent.getRawX(): "+motionEvent.getRawX()+ " motionEvent.getRawY(): "+motionEvent.getRawY());

                        break;
                }
                return true;
            }
        });

    }
}