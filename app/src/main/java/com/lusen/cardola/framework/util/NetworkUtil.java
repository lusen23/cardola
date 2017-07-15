package com.lusen.cardola.framework.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by leo on 2017/7/15.
 */

public class NetworkUtil {

    private static ConnectivityManager mConnectManager;

    static {
        mConnectManager = (ConnectivityManager) ContextUtil.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static boolean isNoNetWork() {
        if (mConnectManager == null) {
            return true;
        }
        NetworkInfo networkInfo = mConnectManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return true;
        }
        return !networkInfo.isConnected();
    }

}
