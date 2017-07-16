package com.lusen.cardola.demo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.lusen.cardola.R;
import com.lusen.cardola.framework.adapter.HolderViewItem;
import com.lusen.cardola.framework.adapter.IAdapterData;
import com.lusen.cardola.framework.uikit.RemoteImageView;
import com.lusen.cardola.framework.util.UiUtil;

/**
 * Created by leo on 2017/7/16.
 */

public class DemoHolderView extends HolderViewItem {

    private RemoteImageView mLogo;
    private TextView mTitle;

    public DemoHolderView(@NonNull Context context) {
        super(context);
    }

    @Override
    public void bindView(View rootView) {
        mLogo = UiUtil.findViewById(rootView, R.id.logo, RemoteImageView.class);
        mTitle = UiUtil.findViewById(rootView, R.id.title, TextView.class);
    }

    @Override
    public void bindData(IAdapterData data, int position) {
        DemoData demoData = (DemoData) data;
        mLogo.load(demoData.logo);
        mTitle.setText(demoData.title);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_demo;
    }

}
