package com.lusen.cardola.business.main.component;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lusen.cardola.R;
import com.lusen.cardola.business.base.CardolaBaseActivity;
import com.lusen.cardola.framework.util.ContextUtil;

/**
 * Created by leo on 2017/7/30.
 */

public class AddCustomerActivity extends CardolaBaseActivity {

    @Override
    protected View onContentViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflaterView(inflater, R.layout.activity_add_customer, container);
    }

    @Override
    protected String initActionBarTitle() {
        return ContextUtil.getContext().getResources().getString(R.string.actionbar_title_add_customer);
    }

}
