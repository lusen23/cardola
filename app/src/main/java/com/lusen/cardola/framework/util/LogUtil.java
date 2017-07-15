package com.lusen.cardola.framework.util;

import android.util.Log;

/**
 * Created by leo on 2017/7/15.
 */

public class LogUtil {

    private static final String DEFAULT_TAG = "cardola";

    public static void log(String msg) {
        Log.d(DEFAULT_TAG, msg);
    }

    public static void log(String msg, Object... args) {
        Log.d(DEFAULT_TAG, String.format(msg, args));
    }

}
