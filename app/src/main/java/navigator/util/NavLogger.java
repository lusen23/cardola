package navigator.util;

import com.lusen.cardola.framework.util.LogUtil;

/**
 * Created by leo on 16/6/1.
 * Log日志打印
 */
public class NavLogger {

    private static final String TAG = "NavLogger";
    public static boolean mEnable = true;

    public static void log(String msg, Object... args) {
        if (mEnable) {
            LogUtil.log(msg, args);
        }
    }

    public static void setEnable(boolean enable) {
        mEnable = enable;
    }

}
