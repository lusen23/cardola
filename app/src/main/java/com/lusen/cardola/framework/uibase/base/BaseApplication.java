package com.lusen.cardola.framework.uibase.base;

import android.app.Application;

import com.lusen.cardola.framework.manager.AppManager;
import com.lusen.cardola.framework.util.ContextUtil;

/**
 * Created by leo on 16/7/29.
 * 基类Application
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ContextUtil.setContext(this);
        AppManager.getInstance().init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
