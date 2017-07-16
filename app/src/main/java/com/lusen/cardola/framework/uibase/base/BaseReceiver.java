package com.lusen.cardola.framework.uibase.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by leo on 16/8/5.
 * 基类BroadcastReceiver
 */
public abstract class BaseReceiver extends BroadcastReceiver {

    @Override
    public final void onReceive(Context context, Intent intent) {
        onReceived(context, intent);
    }

    abstract void onReceived(Context context, Intent intent);

}
