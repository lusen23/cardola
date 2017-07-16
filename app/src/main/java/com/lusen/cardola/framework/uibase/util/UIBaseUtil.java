package com.lusen.cardola.framework.uibase.util;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lusen.cardola.framework.util.ContextUtil;
import com.lusen.cardola.framework.util.LogUtil;

/**
 * Created by leo on 16/7/27.
 * UiBase工具类
 */
public class UIBaseUtil {

    public static boolean mLogEnable = false;

    public static void log(String msg) {
        if (mLogEnable) {
            LogUtil.log(msg);
        }
    }

    public static void log(String msg, Object... args) {
        if (mLogEnable) {
            LogUtil.log(msg, args);
        }
    }

    public static boolean isUIThread() {
        Looper mLooper = Looper.myLooper();
        if (null != mLooper && mLooper == Looper.getMainLooper()) {
            return true;
        }
        return false;
    }

    public static void checkUIThreadThrowException() {
        if (!isUIThread()) {
            throw new RuntimeException("not in UI Thread!!!");
        }
    }

    public static String generateFragmentTag(Fragment fragment) {
        return "Fragment>>" + fragment;
    }

    public static String generateActivityTag(Activity activity) {
        return "Activity>>" + activity;
    }

    public static String generateDialogFragmentTag(DialogFragment dialogFragment) {
        return "DialogFragment>>" + dialogFragment;
    }

    public static View inflaterView(@LayoutRes int layoutRes, ViewGroup container) {
        return inflaterView(null, layoutRes, container);
    }

    public static View inflaterView(LayoutInflater inflater, @LayoutRes int layoutRes, ViewGroup container) {
        if (null == inflater) {
            inflater = LayoutInflater.from(ContextUtil.getContext());
        }
        return inflater.inflate(layoutRes, container, false);
    }

}
