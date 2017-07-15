package com.lusen.cardola.framework.util;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by leo on 2017/7/15.
 */

public class ThreadUtil {

    public static final Handler MAIN_THREAD_HANDLER = new Handler(Looper.getMainLooper());

    public static boolean isUIThread() {
        Looper mLooper = Looper.myLooper();
        if (null != mLooper && mLooper == Looper.getMainLooper()) {
            return true;
        }
        return false;
    }

}
