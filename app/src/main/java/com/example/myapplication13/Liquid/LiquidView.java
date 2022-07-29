package com.example.myapplication13.Liquid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LiquidView extends View {

    private static final String TAG = "LiquidView";

    private static final int DEFAULT_BACKGROUND_COLOR = 0x0;//Color.rgb(255, 255, 255);
    private static final int DEFAULT_RING_BORDER_COLOR = Color.rgb(53, 147, 65);
    private static final int DEFAULT_LIQUID_COLOR = Color.rgb(73, 177, 83);
    private static final int DEFAULT_LIQUID_PERCENT = 100;
    private static final int DEFAULT_LIQUID_AMPLITUDE = 14;
    private static final int DEFAULT_LIQUID_SPEED = 6;
    private static final int LIQUID_AMPLITUDE_MAX_VALUE = 42;
    private static final int LIQUID_SPEED_MAX = 40;
    private static final int LIQUID_ONDRAW_FRAME_RATE = 33;// 液体刷新帧数
    private static final long EVERY_FRAME_FINISH_TIME = 1000 / LIQUID_ONDRAW_FRAME_RATE;//每帧耗时

    private ScheduledExecutorService mScheduledExecutorService;
    private UpdateTimerTask mUpdateTimerTask;

    private class UpdateTimerTask implements Runnable {
        @Override
        public void run() {
            //do not need to reDraw when percent is over than 99% or less than 0%
            if (mLiquid.getPercent() > 99 || mLiquid.getPercent() <= 0) {
                return;
            }

            postInvalidate();
        }
    }

    private class Ring {
        private int mRingCenterX;
        private int mRingCenterY;
        private int mRingRadius;
        private int mRingBorderColor;
        private int mRingBorderWidth;

        public Ring() {
            mRingCenterX = 0;
            mRingCenterY = 0;
            mRingRadius = 0;
            mRingBorderColor = DEFAULT_RING_BORDER_COLOR;
            mRingBorderWidth = 0;
        }

        public void init(int cx, int cy, int radius) {
            mRingCenterX = cx;
            mRingCenterY = cy;
            mRingRadius = radius;
        }

        public void setBorderColor(int borderColor) {
            mRingBorderColor = borderColor;
        }

        public void setBorderWidth(int borderWidth) {
            mRingBorderWidth = borderWidth;
        }

        public int getBorderWidth() {
            return mRingBorderWidth;
        }

        public void draw(Canvas canvas, Paint paint) {
            if (null != canvas && null != paint) {
                paint.setColor(mRingBorderColor);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(mRingBorderWidth);
                if (!mIsStaticDraw) {
                    canvas.drawCircle(mRingCenterX, mRingCenterY, mRingRadius - mRingBorderWidth / 2 - 2.0f,
                            paint);
                } else {
                    canvas.drawCircle(mRingCenterX, mRingCenterY, mRingRadius - mRingBorderWidth / 2,
                            paint);
                }
            }
        }
    }

    private class Liquid {
        private int mLiquidCenterX;
        private int mLiquidCenterY;
        private int mLiquidRadius;
        private int mLiquidOuterRadius;
        private int mLiquidColor;
        private int mLiquidBackgroundColor;
        // private float mAlpha;
        private float mLiquidPlaneLeft;
        private float mLiquidPlaneRight;
        private float mLiquidPlanePercent;
        private float mLiquidPlaneAmplitude;
        private float mLiquidWaveAngle;
        private float mLiquidWaveSpeed;
        private float mCurrentLiquidPlaneHeight;
        private float mCurrentLiquidPlaneAmplitude;
        private float mCurrentLiquidWaveBottom;
        private float mCurrentWaveStep;
        private float mTopOffset;
        private final float[] mLiquidWave = new float[360];
        private final float[] mLiquidWavePoints = new float[360 * 4];// Array of points to draw [x0 y0 x1 y1 x2 y2 ...]
        private final PorterDuffXfermode mPorterDuffXfermode = new PorterDuffXfermode(Mode.CLEAR);

        public Liquid() {
            mLiquidCenterX = 0;
            mLiquidCenterY = 0;
            mLiquidRadius = 0;
            mLiquidOuterRadius = 0;
            mLiquidColor = DEFAULT_LIQUID_COLOR;
            mLiquidBackgroundColor = DEFAULT_BACKGROUND_COLOR;
            mLiquidPlaneLeft = 0;
            mLiquidPlaneRight = 0;
            mTopOffset = 0;
            mLiquidPlanePercent = DEFAULT_LIQUID_PERCENT;
            mLiquidPlaneAmplitude = DEFAULT_LIQUID_AMPLITUDE;
            mLiquidWaveSpeed = DEFAULT_LIQUID_SPEED;
            //mCurrentWaveStep = mLiquidWaveSpeed * 2;
            mLiquidWaveAngle = 0;
            mCurrentWaveStep = mLiquidWaveSpeed * 2;
            for (int i = mLiquidWave.length - 1; i >= 0; i--) {
                mLiquidWaveAngle = (mLiquidWaveAngle + mLiquidWaveSpeed) % 360;
                mLiquidWave[i] = (float) Math.sin((float) (mLiquidWaveAngle * Math.PI / 180));
            }

        }

        public void init(int cx, int cy, int radius, int outerRadius, int topOffset) {
            mLiquidCenterX = cx;
            mLiquidCenterY = cy;
            mLiquidRadius = radius;
            mLiquidOuterRadius = outerRadius;
            mLiquidPlaneLeft = mLiquidCenterX - mLiquidRadius;
            mLiquidPlaneRight = mLiquidCenterX + mLiquidRadius;
            mTopOffset = topOffset;
        }

        public void setColor(int color) {
            mLiquidColor = color;
        }

        public int getColor() {
            return mLiquidColor;
        }

        public void setBackgroundColor(int color) {
            mLiquidBackgroundColor = color;
        }


        public void setPercent(float percent) {
            mLiquidPlanePercent = percent;
        }

        public float getPercent() {
            return mLiquidPlanePercent;
        }

        public void setAmplitude(float amplitude) {
            mLiquidPlaneAmplitude = amplitude;
        }

        public float getAmplitude() {
            return mLiquidPlaneAmplitude;
        }

        public void setSpeed(float speed) {
            mLiquidWaveSpeed = speed;
            mCurrentWaveStep = mLiquidWaveSpeed * 2;
            mLiquidWaveAngle = 100;
            for (int i = 0; i < mLiquidWave.length - 1; i++) {
                mLiquidWaveAngle = (mLiquidWaveAngle + mLiquidWaveSpeed * 3) % 360;
                mLiquidWave[i] = (float) Math.sin((float) (mLiquidWaveAngle * Math.PI / 180));
            }
        }

        public float getSpeed() {
            return mLiquidWaveSpeed;
        }

        public void draw(Canvas canvas, Paint paint) {
            float diff = 0.0f;
            if (!mIsStaticDraw) {
                if (null != canvas && null != paint) {
                    for (int i = mLiquidWave.length - 1; i > 0; i--) {
                        mLiquidWave[i] = mLiquidWave[i - 1];
                    }
                    mLiquidWaveAngle = (mLiquidWaveAngle + mLiquidWaveSpeed) % 360;
                    mLiquidWave[0] = (float) Math.sin((float) (mLiquidWaveAngle * Math.PI / 180));
                }
                //因为桌面显示的流量球是透明的，画线不能有任何重叠
                //这样在某些宽度下，主界面的流量球会有小于1pixel的间隙
                diff = 1.0f;
            }
            float h = (100 - mLiquidPlanePercent) * mLiquidRadius / 50;
            float lh = Math.abs(h - mLiquidRadius);
            float lw = (float) Math.sqrt(mLiquidRadius * mLiquidRadius - lh * lh);

            mCurrentLiquidPlaneHeight = mTopOffset + h;// + mLiquidWave[0] * 2;
            mCurrentLiquidPlaneAmplitude = mLiquidPlaneAmplitude * (lw + 1) / mLiquidRadius;
            mCurrentLiquidWaveBottom = mCurrentLiquidPlaneHeight + mCurrentLiquidPlaneAmplitude;
            mCurrentLiquidWaveBottom = mCurrentLiquidWaveBottom > mLiquidCenterY
                    + mLiquidRadius ? mLiquidCenterY + mLiquidRadius : mCurrentLiquidWaveBottom;
            mCurrentLiquidWaveBottom = (int) (mCurrentLiquidWaveBottom + 0.5);
            paint.setColor(mLiquidBackgroundColor);
            paint.setStrokeWidth(1);
            paint.setStyle(Paint.Style.FILL);
            // 绘制背景圆
            canvas.drawCircle(mLiquidCenterX, mLiquidCenterY, mLiquidRadius, paint);

            // Jerry
            paint.setColor(mLiquidColor);
            // 绘制液体下方
            if (mIsStaticDraw) {
                canvas.drawRect(mLiquidPlaneLeft, mCurrentLiquidWaveBottom - diff,
                        mLiquidPlaneRight, mLiquidCenterY * 2,
                        paint);
            }
            paint.setColor(mLiquidColor);
            paint.setStrokeWidth(1);
            paint.setStyle(Paint.Style.FILL);
            float uoff = 0;
            for (int i = 0, offset = 0; i < mLiquidWave.length - 1; i++, offset += 4) {
                uoff = mLiquidPlaneLeft + i * mCurrentWaveStep;
                mLiquidWavePoints[offset] = uoff;
                mLiquidWavePoints[offset + 1] = mCurrentLiquidPlaneHeight + mLiquidWave[i]
                        * mCurrentLiquidPlaneAmplitude;
                mLiquidWavePoints[offset + 2] = uoff + mCurrentWaveStep + 1;
                mLiquidWavePoints[offset + 3] = mCurrentLiquidPlaneHeight + mLiquidWave[i + 1]
                        * mCurrentLiquidPlaneAmplitude;
                if (mIsStaticDraw) {
                    canvas.drawRect(uoff, mCurrentLiquidPlaneHeight + mLiquidWave[i]
                                    * mCurrentLiquidPlaneAmplitude,
                            uoff + mCurrentWaveStep + diff, mCurrentLiquidWaveBottom, paint);
                }
            }
            if (!mIsStaticDraw) {
                Path path = new Path();
                path.moveTo(mLiquidWavePoints[0], mLiquidWavePoints[1]);
                for (int i = 0, viewWidth = getWidth(); (mLiquidWavePoints[i] < viewWidth); i += 2) {
                    path.lineTo(mLiquidWavePoints[i], mLiquidWavePoints[i + 1]);
                }
                path.lineTo(getWidth(), getHeight());
                path.lineTo(mLiquidPlaneLeft, getHeight());
                path.lineTo(mLiquidWavePoints[0], mLiquidWavePoints[1]);
                paint.setAntiAlias(true);
                // for bugFix#281054 : 在M80机器上, canvas.drawPath()在path改变的时候会有native内存泄漏.
                // canvas.drawPath(path, paint);
                canvas.clipPath(path);
                canvas.drawColor(paint.getColor());
            }

            // 把液体下方多出圆外的部分设置透明
            paint.setStrokeWidth(4);
            paint.setColor(mLiquidBackgroundColor);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth((float) (mLiquidOuterRadius * Math.sqrt(2) - mLiquidRadius) * 2.0f);
            paint.setXfermode(mPorterDuffXfermode);
            canvas.drawCircle(mLiquidCenterX, mLiquidCenterY,
                    (float) (mLiquidOuterRadius * Math.sqrt(2)), paint);
            paint.setXfermode(null);

        }
    }

    private int mViewWidth;
    private int mViewHeight;
    private int mCenterX;
    private int mCenterY;
    private int mRadius;
    private int mInnerCircleRadius;
    private int mTopOffset;
    private Paint mRingsPaint;
    private Paint mLiquidPaint;

    private boolean mIsSensorRegistered = false;
    private SensorManager mSensorManager;
    private LiquidViewSensorEventListener mSensorEventListener;

    /*package*/ float mAngle;
    /*package*/ float mMaxAngle = 30;
    private float mLiquidAngle = 0;
    private float mLiquidAngleSpeed = 0;
    private boolean mIsUpdateRings = false;
    private boolean mIsUpdateLiquid = false;
    private boolean mGravityEnable = false;
    // 是否开启静态绘制，供SafeWidgetProvider使用
    private boolean mIsStaticDraw = false;

    private final ArrayList<Ring> mRings = new ArrayList<Ring>();
    private final Liquid mLiquid = new Liquid();

    public LiquidView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLiquidView();
    }

    public LiquidView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScheduledExecutorService = new ScheduledThreadPoolExecutor(2);
        mUpdateTimerTask = new UpdateTimerTask();
        initLiquidView();
    }

    public LiquidView(Context context) {
        super(context);
        initLiquidView();
    }

    public LiquidView(Context context, int w, int h) {
        super(context);
        initLiquidView();
        mViewWidth = w;
        mViewHeight = h;
        mCenterX = mViewWidth / 2;
        mCenterY = mViewHeight / 2;
        mRadius = Math.min(mCenterX, mCenterY);
        mIsUpdateRings = true;
        mIsUpdateLiquid = true;
        mIsStaticDraw = true;
    }

    private void initLiquidView() {
        mViewWidth = 0;
        mViewHeight = 0;
        mCenterX = 0;
        mCenterY = 0;
        mRadius = 0;
        mInnerCircleRadius = 0;
        mTopOffset = 0;

        mRingsPaint = new Paint();
        mRingsPaint.setAntiAlias(true);

        mLiquidPaint = new Paint();
        mLiquidPaint.setAntiAlias(true);

        setBackgroundColor(DEFAULT_BACKGROUND_COLOR);
        // setAlpha(0);
    }


    public void setLiquidBackgroundColor(int color) {
        mLiquid.setBackgroundColor(color);
    }

    // add by HJX
    @Override
    public void setAlpha(float alpha) {
        // super.setAlpha(alpha);
        // mLiquid.setBackgroundAlpha(alpha);
    }

    // 设置 圆环接口
    public int addRing(int borderWidth) {
        mIsUpdateRings = true;
        Ring ring = new Ring();
        ring.setBorderWidth(borderWidth);
        mRings.add(ring);
        return mRings.size() - 1;
    }

    public void addRings(int[] borderWidths) {
        mIsUpdateRings = true;
        if (null != borderWidths) {
            for (int i = 0; i < borderWidths.length; i++) {
                Ring ring = new Ring();
                ring.setBorderWidth(borderWidths[i]);
                mRings.add(ring);
            }
        }
    }

    public int addRing(int borderColor, int borderWidth) {
        mIsUpdateRings = true;
        Ring ring = new Ring();
        ring.setBorderColor(borderColor);
        ring.setBorderWidth(borderWidth);
        mRings.add(ring);
        return mRings.size() - 1;
    }

    public void addRings(int[] borderColors, int[] borderWidths) {
        int size = 0;
        mIsUpdateRings = true;
        if (null != borderColors && null != borderWidths) {
            size = Math.min(borderColors.length, borderWidths.length);
            for (int i = 0; i < size; i++) {
                Ring ring = new Ring();
                ring.setBorderColor(borderColors[i]);
                ring.setBorderWidth(borderWidths[i]);
                mRings.add(ring);
            }
        }
    }

    public void removeRings(int index) {
        if (index >= 0 && index < mRings.size()) {
            mRings.remove(index);
            mIsUpdateRings = true;
        }
    }

    public void setRingColor(int index, int borderColor) {
        if (index >= 0 && index < mRings.size()) {
            Ring ring = mRings.get(index);
            ring.setBorderColor(borderColor);
        }
    }

    public void setRingWidth(int index, int borderWidth) {
        if (index >= 0 && index < mRings.size()) {
            Ring ring = mRings.get(index);
            ring.setBorderWidth(borderWidth);
            mIsUpdateRings = true;
        }
    }

    public void clearRings() {
        mRings.clear();
    }

    // add by HJX
    public int getRingCount() {
        return mRings.size();
    }

    // 设置 液体接口
    public void setLiquidColor(int liquidColor) {
        mLiquid.setColor(liquidColor);
    }

    public int getLiquidColor() {
        return mLiquid.getColor();
    }

    public void setLiquidPercent(float liquidPercent) {
        mIsUpdateLiquid = true;
        mLiquid.setPercent(liquidPercent);
        invalidate();
    }

    public float getLiquidPercent() {
        return mLiquid.getPercent();
    }

    public void setLiquidAmplitude(float liquidAmplitude) {
        mIsUpdateLiquid = true;
        mLiquid.setAmplitude(liquidAmplitude < 0 ? 0
                : liquidAmplitude > LIQUID_AMPLITUDE_MAX_VALUE ? LIQUID_AMPLITUDE_MAX_VALUE
                : liquidAmplitude);
    }

    public float getLiquidAmplitude() {
        return mLiquid.getAmplitude();
    }

    public void setLiquidSpeed(float liquidSpeed) {
        mIsUpdateLiquid = true;
        mLiquid.setSpeed(liquidSpeed < 0 ? 0 : liquidSpeed > LIQUID_SPEED_MAX ? LIQUID_SPEED_MAX
                : liquidSpeed);
    }

    public float getLiquidSpeed() {
        return mLiquid.getSpeed();
    }

    public void setLiquid(int liquidColor, float liquidPercent) {
        mIsUpdateLiquid = true;
        mLiquid.setColor(liquidColor);
        mLiquid.setPercent(liquidPercent);
    }

    public void setLiquid(int liquidColor, float liquidPercent, float liquidAmplitude) {
        mIsUpdateLiquid = true;
        mLiquid.setColor(liquidColor);
        mLiquid.setPercent(liquidPercent);
        mLiquid.setAmplitude(liquidAmplitude < 0 ? 0
                : liquidAmplitude > LIQUID_AMPLITUDE_MAX_VALUE ? LIQUID_AMPLITUDE_MAX_VALUE
                : liquidAmplitude);
    }

    public void setLiquid(int liquidColor, float liquidPercent, float liquidAmplitude,
                          float liquidSpeed) {
        mIsUpdateLiquid = true;
        mLiquid.setColor(liquidColor);
        mLiquid.setPercent(liquidPercent);
        mLiquid.setAmplitude(liquidAmplitude < 0 ? 0
                : liquidAmplitude > LIQUID_AMPLITUDE_MAX_VALUE ? LIQUID_AMPLITUDE_MAX_VALUE
                : liquidAmplitude);
        mLiquid.setSpeed(liquidSpeed < 0 ? 0 : liquidSpeed > LIQUID_SPEED_MAX ? LIQUID_SPEED_MAX
                : liquidSpeed);
    }

    public int getLiquidAmplitudeMinValue() {
        return 0;
    }

    public int getLiquidAmplitudeMaxValue() {
        return LIQUID_AMPLITUDE_MAX_VALUE;
    }

    public int getLiquidSpeedMinValue() {
        return 0;
    }

    public int getLiquidSpeedMaxValue() {
        return LIQUID_SPEED_MAX;
    }

    // 设置重力感应
    public void setGravityEnable(boolean enable) {
        if (mGravityEnable && !enable) {
            unregisterSensorListener();
        } else if (!mGravityEnable && enable) {
            registerSensorListener();
        }

        mGravityEnable = enable;
    }

    private void registerSensorListener() {
        if (mIsSensorRegistered) {
            return;
        }
        mIsSensorRegistered = true;
        if (null == mSensorManager) {
            mSensorManager = (SensorManager) getContext().getSystemService(Activity.SENSOR_SERVICE);
        }
        if (mSensorEventListener == null) {
            mSensorEventListener = new LiquidViewSensorEventListener(LiquidView.this);
        }
        Log.d(TAG, "registerSensorListener()" + this.hashCode());
        new RegisterSensorThread(this).start();
    }

    private void unregisterSensorListener() {
        if (null == mSensorManager || null == mSensorEventListener) {
            return;
        }
        Log.d(TAG, "unregisterSensorListener()" + this.hashCode());
        mSensorManager.unregisterListener(mSensorEventListener);
        mSensorEventListener = null;
        mIsSensorRegistered = false;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.d(TAG, "onAttachedToWindow");
        if (mGravityEnable) {
            registerSensorListener();
        }
        if (!mIsStaticDraw) {
            mScheduledExecutorService.scheduleAtFixedRate(mUpdateTimerTask, 0, EVERY_FRAME_FINISH_TIME, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d(TAG, "onDetachedFromWindow");
        if (mGravityEnable) {
            unregisterSensorListener();
        }
        if (!mIsStaticDraw) {
            mScheduledExecutorService.shutdown();
        }
    }

    private int mT = 0;

    public void drawCanvas(Canvas canvas) {
        if (mViewWidth != getWidth() || mViewHeight != getHeight()) {
            if (!mIsStaticDraw) {
                mViewWidth = getWidth();
                mViewHeight = getHeight();
                mCenterX = mViewWidth / 2;
                mCenterY = mViewHeight / 2;
                mRadius = Math.min(mCenterX, mCenterY);
                mIsUpdateRings = true;
                mIsUpdateLiquid = true;
            }
        }

        // canvas.saveLayerAlpha(0, 0, mViewWidth, mViewHeight, 255, Canvas.ALL_SAVE_FLAG);
        canvas.save();
        if (mIsUpdateRings) {
            mIsUpdateRings = false;
            mTopOffset = 0;
            for (Ring ring : mRings) {
                ring.init(mCenterX, mCenterY, mRadius - mTopOffset);
                mTopOffset += ring.getBorderWidth();
            }
            mInnerCircleRadius = mRadius - mTopOffset;
        }
        if (mIsUpdateLiquid) {
            mIsUpdateLiquid = false;
            mLiquid.init(mCenterX, mCenterY, mInnerCircleRadius, mRadius, mTopOffset);
        }

        // draw liquid
        if (mGravityEnable) {
            if (Math.abs(mAngle - mLiquidAngle) > 1.4f) {
                mLiquidAngleSpeed = (mAngle - mLiquidAngle) / 15.0f;
            } else {
                mLiquidAngleSpeed = 0;
            }
            mLiquidAngle += mLiquidAngleSpeed;
            // canvas.setBitmap(BitmapFactory.decodeResource(getResources(),
            // R.drawable.bg));
            canvas.save();
            canvas.rotate(mLiquidAngle, mCenterX, mCenterY);
            mLiquid.draw(canvas, mLiquidPaint);
            canvas.restore();
        } else {
            mLiquid.draw(canvas, mLiquidPaint);
        }

        // draw rings
        for (Ring ring : mRings) {
            ring.draw(canvas, mRingsPaint);
        }
        // int radius = Math.min(getWidth(), getHeight()) / 2;
        // mLiquidPaint.setColor(0xffff0000);
        // canvas.save();
        // canvas.rotate(mT, getWidth() / 2, getHeight() / 2);
        // canvas.clipRect(0, 0, getWidth(), 100);
        // canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius,
        // mLiquidPaint);
        // canvas.restore();
        // mT = (mT + 1) % 360;
        canvas.restore();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCanvas(canvas);
    }

    private static class LiquidViewSensorEventListener implements SensorEventListener {

        WeakReference<LiquidView> mWeakRefLiquidView = null;

        LiquidViewSensorEventListener(LiquidView liquidView) {
            mWeakRefLiquidView = new WeakReference<LiquidView>(liquidView);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (mWeakRefLiquidView == null) {
                return;
            }
            LiquidView liquidView = mWeakRefLiquidView.get();
            if (liquidView == null) {
                return;
            }

            double gx = event.values[SensorManager.DATA_X];
            double gy = event.values[SensorManager.DATA_Y];
            gx = gx > 10 ? 10 : gx < -10 ? -10 : gx;
            gy = gy > 10 ? 10 : gy < -10 ? -10 : gy;

            if (!(Math.abs(gx) < 0.5 && Math.abs(gy) < 0.5)) {
                double vectorABProduct = gy * 10;
                double vectorALen = 10;
                double vectorBLen = Math.sqrt(gx * gx + gy * gy);
                double cosValue = vectorABProduct / (vectorALen * vectorBLen);
                liquidView.mAngle = (float) (Math.acos(cosValue) * 180 / Math.PI);
                if (liquidView.mAngle > 90) {
                    liquidView.mAngle = 180 - liquidView.mAngle;
                }
                liquidView.mAngle = (int) liquidView.mAngle / 2;
                if (liquidView.mAngle < 10) {
                    liquidView.mAngle = 0;
                }
                liquidView.mAngle = liquidView.mAngle > liquidView.mMaxAngle ? liquidView.mMaxAngle : liquidView.mAngle;
                liquidView.mAngle = (float) (gx > 0 ? liquidView.mAngle : -liquidView.mAngle);
            } else {
                liquidView.mAngle = 0;
            }
        }
    }

    private static class RegisterSensorThread extends Thread {

        private WeakReference<LiquidView> refLiquidView = null;

        public RegisterSensorThread(LiquidView liquidView) {
            super();
            if (null != liquidView) {
                refLiquidView = new WeakReference<>(liquidView);
            }
        }

        @Override
        public void run() {
            LiquidView liquidView = null;
            if (null != refLiquidView) {
                liquidView = refLiquidView.get();
            }
            if (null == liquidView || null == liquidView.mSensorManager ||
                    null == liquidView.mSensorEventListener) {
                return;
            }
            Log.d(TAG, "RegisterSensorThread.run()" + liquidView.hashCode());
            Sensor sensor = liquidView.mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            liquidView.mSensorManager.registerListener(liquidView.mSensorEventListener, sensor,
                    SensorManager.SENSOR_DELAY_GAME);
        }
    }
}