package com.lusen.cardola.business.main.component;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lusen.cardola.R;
import com.lusen.cardola.business.base.CardolaBaseActivity;
import com.lusen.cardola.business.scheme.SchemeUrlConstant;
import com.lusen.cardola.framework.uibase.UiModel;
import com.lusen.cardola.framework.util.ThreadUtil;

import navigator.Nav;

/**
 * Created by leo on 2017/7/16.
 */

public class SplashActivity extends CardolaBaseActivity {

    @Override
    protected View onContentViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflaterView(inflater, R.layout.activity_splash, container);
    }

    @Override
    protected int initUiModel() {
        return UiModel.UI_MODEL_NONE;
    }

    @Override
    protected void onContentViewCreated(View view) {
        super.onContentViewCreated(view);
        ThreadUtil.MAIN_THREAD_HANDLER.postDelayed(new Runnable() {
            @Override
            public void run() {
                Nav.fromHost(SchemeUrlConstant.Host.HOME).nav();
                finishSelfActivity();
            }
        }, 3000);
    }
}
