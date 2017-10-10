package com.lusen.cardola.business.main.home.holderitem;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.lusen.cardola.R;
import com.lusen.cardola.business.main.home.data.CustomerData;
import com.lusen.cardola.framework.adapter.HolderViewItem;
import com.lusen.cardola.framework.adapter.IAdapterData;
import com.lusen.cardola.framework.uikit.CommonItemView;
import com.lusen.cardola.framework.util.UiUtil;

/**
 * Created by leo on 2017/7/23.
 */

public class HolderItemCustomer extends HolderViewItem {

    private CommonItemView mItemView;

    public HolderItemCustomer(@NonNull Context context) {
        super(context);
    }

    @Override
    public void bindView(View rootView) {
        mItemView = UiUtil.findViewById(rootView, R.id.item_view, CommonItemView.class);
    }

    @Override
    public void bindData(IAdapterData data, int position) {
        if (data instanceof CustomerData) {
            mItemView.mItemNameView.setText(((CustomerData) data).mName);
        }
    }

    @Override
    public int getLayoutRes() {
        return R.layout.holder_item_customer;
    }

}
