package com.example.myapplication13.Liquid;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.example.myapplication13.R;

public class LiquidWidget extends AppWidgetProvider {
    private Context mContext;
    private Intent autoUpdateWPService = new Intent();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        mContext = context;
        ComponentName componentName = new ComponentName(mContext, LiquidWidget.class);
        RemoteViews views = new RemoteViews(mContext.getPackageName(),
                R.layout.liquid_widget_view);
        views.setImageViewBitmap(R.id.liquidview,
                getLiquidViewBMForWP(mContext, 20));
        appWidgetManager.updateAppWidget(componentName,views);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    public static Bitmap getLiquidViewBMForWP(Context context, float usedPercent) {
        int liquidViewWidth = context.getResources().getDimensionPixelSize(
                R.dimen.traffic_screen_liquiView_width);
        final LiquidView liquidView = new LiquidView(context, liquidViewWidth,
                liquidViewWidth);
        final Bitmap bitmap = Bitmap.createBitmap(liquidViewWidth, liquidViewWidth,
                Bitmap.Config.ARGB_8888);

        liquidView.addRing(Color.rgb(216, 100, 23), context.getResources()
                .getDimensionPixelOffset(R.dimen.traffic_wp_liquidview_ring_borderwidth));
        liquidView.setLiquidPercent(100 - usedPercent);
        liquidView.setLiquidBackgroundColor(Color.rgb(216, 100, 23));
        liquidView.setLiquidColor(Color.rgb(239, 125, 29));
        liquidView.setLiquidSpeed(1.0f);
        liquidView.setLiquidAmplitude(15);
        liquidView.setGravityEnable(true);

        Canvas canvas = new Canvas(bitmap);
        liquidView.drawCanvas(canvas);
        return bitmap;
    }

}
