package com.lusen.cardola.framework.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by leo on 2015/5/27.<br><br>
 * 键盘Util
 */
public class KeyboardUtil {

    /**
     * 显示键盘
     *
     * @param context 上下文
     * @param view    锚点View
     * @return 是否执行成功
     */
    public static boolean showKeybBoard(final Context context, final View view) {
        if (null == view) return false;
        try {
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (context != null && (view.requestFocus() || view.requestFocusFromTouch())) {
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
                    }
                }
            }, 500);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public static void showKeybBoardForced(final Context context, final View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 隐藏键盘
     *
     * @param context 上下文
     * @param view    锚点View
     * @return 是否执行成功
     */
    public static boolean hideKeybBoard(Context context, View view) {
        if (null == view) return false;
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

}
