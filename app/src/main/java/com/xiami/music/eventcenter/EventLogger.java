package com.xiami.music.eventcenter;

/**
 * Created by leo on 16/6/15.
 * 日志打印器
 */
public class EventLogger {

    private static final String TAG = EventLogger.class.getSimpleName();
    private static boolean mEnable = true;

    public static void log(String msg) {
        if (mEnable) {
            android.util.Log.d(TAG, msg);
        }
    }

    public static void setEnable(boolean enable) {
        mEnable = enable;
    }

}
