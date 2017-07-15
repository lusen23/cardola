package com.lusen.cardola.framework.util;

import android.app.Activity;
import android.view.View;

/**
 * Created by leo on 2017/7/15.
 */

public class UiUtil {

    public static <T> T findViewById(Activity activity, int id, Class<T> viewType) {
        try {
            return (T) activity.findViewById(id);
        } catch (Exception e) {
        }
        return null;
    }

    public static <T> T findViewById(View view, int id, Class<T> viewType) {
        try {
            return (T) view.findViewById(id);
        } catch (Exception e) {
        }
        return null;
    }

    public static void bindClickListener(View.OnClickListener listener, View... views) {
        try {
            if (null != views) {
                for (View view : views) {
                    if (null != view) {
                        view.setOnClickListener(listener);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public static void bindLongClickListener(View.OnLongClickListener listener, View... views) {
        try {
            if (null != views) {
                for (View view : views) {
                    if (null != view) {
                        view.setOnLongClickListener(listener);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public static void bindKeyListener(View.OnKeyListener listener, View... views) {
        try {
            if (null != views) {
                for (View view : views) {
                    if (null != view) {
                        view.setOnKeyListener(listener);
                    }
                }
            }
        } catch (Exception e) {
        }
    }


}
