package com.lusen.cardola.business.main.mine.holderitem;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.lusen.cardola.R;
import com.lusen.cardola.business.main.mine.data.MessageData;
import com.lusen.cardola.framework.adapter.HolderViewItem;
import com.lusen.cardola.framework.adapter.IAdapterData;
import com.lusen.cardola.framework.uikit.CommonItemView;
import com.lusen.cardola.framework.util.DayTimeUtil;
import com.lusen.cardola.framework.util.UiUtil;

/**
 * Created by leo on 2017/7/30.
 */

public class HolderItemMessage extends HolderViewItem {

    private CommonItemView mItemView;

    public HolderItemMessage(@NonNull Context context) {
        super(context);
    }

    @Override
    public void bindView(View rootView) {
        mItemView = UiUtil.findViewById(rootView, R.id.item_view, CommonItemView.class);
    }

    @Override
    public void bindData(IAdapterData data, int position) {
        if (data instanceof MessageData) {
            mItemView.mItemNameView.setText(DayTimeUtil.timeStamp2YMD(((MessageData) data).mTimeStamp));
            mItemView.mItemSubNameView.setText(((MessageData) data).mMessage);
        }
    }

    @Override
    public int getLayoutRes() {
        return R.layout.holder_item_message;
    }

}
