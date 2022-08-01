package com.example.myapplication13.Glow;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.myapplication13.R;

import java.util.ArrayList;

public class GlowView extends View {
    private static final String TAG = GlowView.class.getSimpleName();
    private static final boolean DEBUG = false;
    public static final String ANIM_STATUS_STOP = "stop";
    public static final String ANIM_STATUS_RUNNING = "running";
    public static final String ANIM_STATUS_DYING = "dying";
    private String mRunningStatus = ANIM_STATUS_STOP;
    private static final long GLOW_ANIM_DRUGTION_TOTAL = 1017;// ms
    private static final long GLOW_ANIM_DRUGTION_PART_ONE = 133;// ms
    private static final long GLOW_ANIM_DRUGTION_PART_TWO = GLOW_ANIM_DRUGTION_TOTAL
            - GLOW_ANIM_DRUGTION_PART_ONE;// ms
    private static final long GLOW_VIEW_BORN_CIRCLE_TIME = 1067;// ms
    private static final long GLOW_VIEW_BORN_HALF_TIME = 530;// ms
    private static int GLOW_VIEW_START_SIZE;
    private static int GLOW_VIEW_SUBJOIN_SIZE; //
    //
    //
    private long mNewGlowViewDelay = GLOW_VIEW_BORN_HALF_TIME;
    private ValueAnimator mAnimator;
    private static final long VALUE_ANIMATOR_CIRCLE = 10 * 1000;// 10s
    private static final int MAX_GLOW_VIEW_NUM = 5;

    private Paint mPaint;
    private int mStrokeWidth;

    private static int mGlowViewColor = Color.GREEN;
    private static int cx;
    private static int cy;
    private ArrayList<GlowViewInfo> mGlowViewList = new ArrayList<GlowViewInfo>(MAX_GLOW_VIEW_NUM);

    class GlowViewInfo {
        long mBornTime;
        int mRadius;
        int mAlpha;
        boolean isAlive;

        public GlowViewInfo() {
            isAlive = false;
        }
    }

    private Callback mCallback;

    public GlowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /* 初始化参数 */
        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GlowView);
            mGlowViewColor = a.getColor(R.styleable.GlowView_gv_color, mGlowViewColor); //画笔颜色
            mStrokeWidth = (int) a.getDimension(R.styleable.GlowView_gv_stroke_width, 80);  //画笔宽度
            GLOW_VIEW_START_SIZE = (int) a.getDimension(R.styleable.GlowView_gv_radius,
                    150);
            GLOW_VIEW_SUBJOIN_SIZE = (int) a.getDimension(R.styleable.GlowView_gv_subjoin_radius,
                    150);
            a.recycle();
        }

        for (int i = 0; i < MAX_GLOW_VIEW_NUM; i++) {
            mGlowViewList.add(new GlowViewInfo());
        }
        if (mPaint == null) {
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mStrokeWidth);
            mPaint.setAntiAlias(true); // 设置画笔为无锯齿
            mPaint.setColor(mGlowViewColor);
        }
    }

    public GlowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GlowView(Context context) {
        this(context, null);
    }

    private static final int BORN_GLOW_VIEW = 0;
    private static final int WAITINT_FOR_STOP_ANIM = 1;
    Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case BORN_GLOW_VIEW:
                    mNewGlowViewDelay = GLOW_VIEW_BORN_CIRCLE_TIME - mNewGlowViewDelay;
                    mHandler.sendEmptyMessageDelayed(BORN_GLOW_VIEW, mNewGlowViewDelay);
                    reliveGlowView(mGlowViewList);
                    break;
                case WAITINT_FOR_STOP_ANIM:
                    if (isAllGlowViewDied(mGlowViewList)) {
                        if (mAnimator != null && mAnimator.isRunning())
                            mAnimator.cancel();
                        mCallback.onAnimEnd();
                    } else {
                        mHandler.sendEmptyMessageDelayed(WAITINT_FOR_STOP_ANIM, 500);
                    }
                    break;
            }
            super.dispatchMessage(msg);
        }
    };

    /**
     * 开始做动画
     */
    public void startAnim() {
        synchronized (mRunningStatus) {
            if (ANIM_STATUS_RUNNING.equals(mRunningStatus))
                return;
            mRunningStatus = ANIM_STATUS_RUNNING;
        }
        mHandler.removeMessages(WAITINT_FOR_STOP_ANIM);
        reliveGlowView(mGlowViewList);
        mHandler.sendEmptyMessageDelayed(BORN_GLOW_VIEW, mNewGlowViewDelay);
        if (mAnimator == null) {
            mAnimator = new ValueAnimator();
            mAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mAnimator.setTarget(this);
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    updateGlowView(mGlowViewList, System.currentTimeMillis());
                }
            });
        }
        if (mAnimator.isRunning()) {
            mAnimator.cancel();
        }
        mAnimator.setIntValues(0, 255);
        mAnimator.setDuration(VALUE_ANIMATOR_CIRCLE);
        mAnimator.start();
    }

    /**
     * 请求停止动画
     *
     *
     */
    public void stopAnim(boolean animToEnd) {
        mHandler.removeMessages(BORN_GLOW_VIEW);
        mNewGlowViewDelay = GLOW_VIEW_BORN_HALF_TIME;
        if (animToEnd) {
            mHandler.sendEmptyMessage(WAITINT_FOR_STOP_ANIM);
            synchronized (mRunningStatus) {
                mRunningStatus = ANIM_STATUS_DYING;
            }
        } else {
            if (mAnimator != null && mAnimator.isRunning())
                mAnimator.cancel();
            for (GlowViewInfo mGlowViewInfo : mGlowViewList) {
                mGlowViewInfo.isAlive = false;
            }
            synchronized (mRunningStatus) {
                mRunningStatus = ANIM_STATUS_STOP;
            }
        }
    }

    /**
     * 检查动画是否结束
     *
     * @param mGlowViewList
     * @return
     */
    private boolean isAllGlowViewDied(ArrayList<GlowViewInfo> mGlowViewList) {
        for (GlowViewInfo mGlowViewInfo : mGlowViewList) {
            if (mGlowViewInfo.isAlive) {
                return false;
            }
        }
        return true;
    }

    /**
     * 更新一帧 计算每个圆形的半径、透明度、死亡 计算title、delay的透明度变化(%/alpha) frame 0 33 44 72 title
     * 100/255 60/153 60/153 100/255 delay 60/153 30/77 30/77 60/153
     */
    private void updateGlowView(ArrayList<GlowViewInfo> mGlowViewList, long currentTime) {
        // glow view
        for (GlowViewInfo mGlowViewInfo : mGlowViewList) {
            if (mGlowViewInfo.isAlive) {
                final long mAge = currentTime - mGlowViewInfo.mBornTime;
                // set old viewinfo false
                mGlowViewInfo.isAlive = mAge < GLOW_ANIM_DRUGTION_TOTAL;
                if (mGlowViewInfo.isAlive) {
                    mGlowViewInfo.mRadius = (int) (GLOW_VIEW_START_SIZE + GLOW_VIEW_SUBJOIN_SIZE
                            * mAge / GLOW_ANIM_DRUGTION_TOTAL);
                    mGlowViewInfo.mAlpha = (int) ((mAge < GLOW_ANIM_DRUGTION_PART_ONE) ?
                            26 * mAge / GLOW_ANIM_DRUGTION_PART_ONE
                            : 26 * (GLOW_ANIM_DRUGTION_TOTAL - mAge) / GLOW_ANIM_DRUGTION_PART_TWO);
                    // if(DEBUG)
                    // Log.d(TAG,"  mRadius:"+mGlowViewInfo.mRadius+" alpha:"+mGlowViewInfo.mAlpha);
                }
            }
        }

        // if(DEBUG)
        // Log.d(TAG,"  textAnimTime:"+textAnimAge+" \tmtitleAlpha:"+mtitleAlpha+" \tmDelayAlpha:"+mDelayAlpha);
        invalidate();
    }

    /**
     * 复活一个glowview
     *
     * @param mGlowViewList
     */
    private void reliveGlowView(ArrayList<GlowViewInfo> mGlowViewList) {
        for (GlowViewInfo mGlowViewInfo : mGlowViewList) {
            if (!mGlowViewInfo.isAlive) {
                mGlowViewInfo.isAlive = true;
                mGlowViewInfo.mBornTime = System.currentTimeMillis();
                if (DEBUG)
                    Log.d(TAG, "bornGlowView()，mNewGlowViewDelay：" + mNewGlowViewDelay);
                break; // if have one is fasle set ture and break
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // if(DEBUG) Log.d(TAG,"onDraw(),canvas:"+canvas.getClipBounds());
        for (GlowViewInfo mGlowViewInfo : mGlowViewList) {
            if (mGlowViewInfo.isAlive) {
                mPaint.setAlpha(mGlowViewInfo.mAlpha);
                canvas.drawCircle(cx, cy, mGlowViewInfo.mRadius, mPaint);
                // if(DEBUG)
                // Log.d(TAG,"onDraw(),mRadius:"+mGlowViewInfo.mRadius+" alpha:"+mGlowViewInfo.mAlpha);
            }
        }

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (DEBUG)
            Log.d(TAG, "onLayout(),changed:" + changed + " left:" + left + " top:" + top
                    + " right:" + right + " bottom:" + bottom);
        cx = (right - left) / 2;
        cy = (bottom - top) / 2;
    }

    public interface Callback {
        void onAnimEnd();
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAnimator != null && mAnimator.isRunning())
            mAnimator.cancel();
        if (mHandler != null) {
            mHandler.removeMessages(BORN_GLOW_VIEW);
            mHandler.removeMessages(WAITINT_FOR_STOP_ANIM);
        }
    }
}

