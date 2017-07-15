package com.lusen.cardola.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.lusen.cardola.R;
import com.lusen.cardola.business.network.CardolaApiManager;
import com.lusen.cardola.framework.network.BaseSubscriber;

/**
 * Created by leo on 2017/7/13.
 */

public class DemoActivity extends FragmentActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        CardolaApiManager.getInstance().getHome(1, new BaseSubscriber() {
            @Override
            public void onNext(Object o) {

            }
        });

    }
}
