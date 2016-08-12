package com.shizhefei.mutitypedemo.util;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.RectF;
import android.os.Build;
import android.provider.Settings.Secure;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.util.Locale;

public class DisplayUtils {

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    private static int deviceType = -1;


    /**
     * 根据dip值转化成px值
     *
     * @param context
     * @param dip
     * @return
     */
    public static int dipToPix(Context context, float dip) {
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
        return size;
    }

    public static float spToPx(Context context, float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public static int getDimenValue(Context context, int dimenId) {
        return (int) context.getResources().getDimension(dimenId);
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        // DisplayMetrics dm = new DisplayMetrics();
        // activity.getResources().getDisplayMetrics();
        // activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return context.getResources().getDisplayMetrics();
    }

    /**
     * 判断设备是否是模拟器
     *
     * @return
     */
    public static boolean isEmulator() {
        return "sdk".equals(Build.PRODUCT) || "google_sdk".equals(Build.PRODUCT) || "generic".equals(Build.BRAND.toLowerCase(Locale.getDefault()));
    }

    /**
     * 得到设备id
     *
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {
        String android_id = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
        return android_id;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static Bitmap createTextBitmap(Context context, String str, int bgColor, int fontColot, float fontSizePx) {
        Paint paint = new Paint();
        paint.setTextSize(fontSizePx);
        int w = (int) paint.measureText(str);// 计算文字实际占用的宽度
        int margin = (int) (fontSizePx * 0.2f);
        int bgHeight = (int) (fontSizePx + margin * 2);// 将高度+10防止绘制椭圆时左右的文字超出椭圆范围
        int bgWitdh = w + margin * 2;
        Bitmap bm = Bitmap.createBitmap(bgWitdh, bgHeight, Config.ARGB_4444);
        Canvas c = new Canvas(bm);

        Paint p2 = new Paint();
        RectF re = new RectF(0, 0, bgWitdh, bgHeight);// 矩形
        float roundPx = margin * 2;
        p2.setAntiAlias(true);// 设置Paint为无锯齿
        c.drawARGB(0, 0, 0, 0);// 透明色
        p2.setColor(bgColor);
        c.drawRoundRect(re, roundPx, roundPx, p2);// 绘制圆角矩形
        Paint p1 = new Paint();
        p1.setColor(fontColot);
        p1.setTextSize(fontSizePx);
        p1.setTextAlign(Paint.Align.CENTER);
        FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int baseline = (int) (re.top + (re.bottom - re.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top);
        c.drawText(str, re.centerX(), baseline, p1);
        c.save(Canvas.ALL_SAVE_FLAG);
        c.restore();
        return bm;
    }

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void copyToClipboard(Context context, String text) {
        // if()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setPrimaryClip(ClipData.newPlainText(null, text));
        }
    }
}
