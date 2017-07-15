package com.lusen.cardola.framework.util;

import android.content.Context;

/**
 * Created by leo on 2017/7/15.
 */

public class ContextUtil {

    private static Context sContext;

    public static Context getContext() {
        return sContext;
    }

    public static void setContext(Context sContext) {
        ContextUtil.sContext = sContext;
    }

}
