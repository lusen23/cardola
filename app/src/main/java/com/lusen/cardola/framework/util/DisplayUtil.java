package com.lusen.cardola.framework.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by leo on 2017/7/15.
 */

public class DisplayUtil {

    static public DisplayMetrics metrics;

    static {
        DisplayMetrics dm = new DisplayMetrics();
        try {
            ((WindowManager) ContextUtil.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        metrics = dm;
        if (metrics.widthPixels > metrics.heightPixels) {
            int oldWidth = metrics.widthPixels;
            metrics.widthPixels = metrics.heightPixels;
            metrics.heightPixels = oldWidth;
        }
    }

    public static int getDisplayHeight() {
        return metrics.heightPixels;
    }

    public static DisplayMetrics getMetrics() {
        return metrics;
    }

    public static int px2dip(float value) {
        return (int) (value / metrics.density + 0.5f);
    }

    public static int dip2px(float value) {
        return (int) (value * metrics.density + 0.5f);
    }

    public static int px2sp(float value) {
        return (int) (value / metrics.scaledDensity + 0.5f);
    }

    public static int sp2px(float value) {
        return (int) (value * metrics.scaledDensity + 0.5f);
    }

}
