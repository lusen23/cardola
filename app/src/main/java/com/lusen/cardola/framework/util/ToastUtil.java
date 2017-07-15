package com.lusen.cardola.framework.util;

import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by leo on 2017/7/15.
 */

public class ToastUtil {

    private static Toast sToastMutex;
    private static final long TOAST_DURATION = 1000;

    private static Runnable mRunnableCancelToast = new Runnable() {
        @Override
        public void run() {
            // 取消当前toast
            cancelToast();
        }
    };

    private static void showToast(Toast toast) {
        if (null != toast) {
            // 取消当前toast
            cancelToast();
            // 移除当前任务
            ThreadUtil.MAIN_THREAD_HANDLER.removeCallbacks(mRunnableCancelToast);
            // 弹出新toast
            sToastMutex = toast;
            sToastMutex.setDuration(Toast.LENGTH_LONG);
            sToastMutex.show();
            ThreadUtil.MAIN_THREAD_HANDLER.postDelayed(mRunnableCancelToast, TOAST_DURATION);
        }
    }

    private static void cancelToast() {
        if (null != sToastMutex) {
            sToastMutex.cancel();
        }
    }

    public static void toast(@StringRes int msg) {
        toast(ContextUtil.getContext().getResources().getString(msg));
    }

    public static void toast(String msg) {
        if (!TextUtils.isEmpty(msg) && ThreadUtil.isUIThread()) {
            Toast toast = Toast.makeText(ContextUtil.getContext(), msg, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            showToast(toast);
        }
    }

}
