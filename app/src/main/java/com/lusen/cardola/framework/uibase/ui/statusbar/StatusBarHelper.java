package com.lusen.cardola.framework.uibase.ui.statusbar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lusen.cardola.framework.util.Phone;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by leo on 17/2/13.
 */

public class StatusBarHelper {

    //YUNOS状态栏用的flag
    private static final int SYSTEM_UI_FLAG_TEXT_DARK = 0x00000010;

    /**
     * 设置状态栏字体是否Dark
     *
     * @param activity activity对象
     * @param dark     是否dark
     * @return 是否成功
     */
    public static boolean setStatusBarFont(Activity activity, boolean dark) {
        if (null != activity) {
            return setStatusBarFont(activity.getWindow(), dark);
        }
        return false;
    }

    /**
     * 设置状态栏字体是否Dark
     *
     * @param window window对象
     * @param dark   是否dark
     * @return 是否成功
     */
    public static boolean setStatusBarFont(Window window, boolean dark) {
        boolean result = false;
        if (null != window) {
            if (Phone.isFlyme4()) {
                result = setStatusBarFontForFlyme(window, dark);
            } else if (Phone.isMiUiV6()) {
                result = setStatusBarFontForMiui(window, dark);
            } else if (Phone.isYUNOSSystem()) {
                result = setStatusBarFontForYunos(window, dark);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                result = setStatusBarModeFor6_0(window, dark);
            }
        }
        return result;
    }

    /**
     * 设置状态栏背景是否透明
     *
     * @param activity    activity对象
     * @param transparent 是否透明
     * @return 是否成功
     */
    public static boolean setStatusBarBackground(Activity activity, boolean transparent) {
        if (null != activity) {
            return setStatusBarBackground(activity.getWindow(), transparent);
        }
        return false;
    }

    /**
     * 设置状态栏背景是否透明
     *
     * @param window      window对象
     * @param transparent 是否透明
     * @return 是否成功
     */
    public static boolean setStatusBarBackground(Window window, boolean transparent) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            if (!transparent) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                result = true;
            }
        }
        // 5.0+
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (transparent) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.setStatusBarColor(Color.TRANSPARENT);
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
            result = true;
        }
        return result;
    }

    /**
     * 设置状态栏字体图标为深色,FlymeV4以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    private static boolean setStatusBarFontForFlyme(Window window, boolean dark) {
        boolean result = false;
        if (null != window) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 设置状态栏字体图标为深色,MIUIV6以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    private static boolean setStatusBarFontForMiui(Window window, boolean dark) {
        boolean result = false;
        if (null != window) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 设置状态栏字体图标为深色,yunos
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    private static boolean setStatusBarFontForYunos(Window window, boolean dark) {
        boolean result = false;
        if (null != window) {
            try {
                View decorView = window.getDecorView();
                int current = decorView.getSystemUiVisibility();
                if (dark) {
                    decorView.setSystemUiVisibility(current | SYSTEM_UI_FLAG_TEXT_DARK);
                } else {
                    decorView.setSystemUiVisibility(current & (~SYSTEM_UI_FLAG_TEXT_DARK));
                }
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 设置状态栏字体图标为深色,Android6.0
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    private static boolean setStatusBarModeFor6_0(Window window, boolean dark) {
        boolean result = false;
        if (null != window) {
            try {
                View decorView = window.getDecorView();
                int ui = decorView.getSystemUiVisibility();
                if (dark) {
                    ui |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else {
                    ui &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                decorView.setSystemUiVisibility(ui);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 状态栏字体是否支持Dark
     * 特殊机型及版本[MiUiV6+,Flyme,Yunos,23+]支持修改状态栏字体成Dark黑色模式
     *
     * @return 是否支持
     */
    public static boolean isStatusBarSupportFontDark() {
        return (Phone.isFlyme4() || Phone.isMiUiV6() || Phone.isYUNOSSystem() || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    /**
     * 状态栏背景是否支持全透明
     * [19-21]沉浸式后支持半透明,附带渐变效果;特殊机型如[小米/魅族]等支持全透明
     * [21~]沉浸式后系统支持修改成全透明
     *
     * @return 是否支持
     */
    public static boolean isStatusBarSupportBackgroundTransparent() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP || Phone.isMiUiV6() || Phone.isFlyme4());
    }

    /**
     * 状态栏背景是否总是透明
     * (部分厂商手机设置FLAG_TRANSLUCENT_STATUS后,是全透明,而不是渐变[4.4~5.0)或纯色[5.0+))
     *
     * @return 是否总是透明
     */
    public static boolean isStatusBarBackgroundAlwaysTransparent() {
        return Phone.isMiUiV6() || Phone.isFlyme4();
    }

}
