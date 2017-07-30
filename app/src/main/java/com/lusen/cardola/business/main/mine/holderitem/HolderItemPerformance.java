package com.lusen.cardola.business.main.mine.holderitem;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.lusen.cardola.R;
import com.lusen.cardola.business.main.mine.data.PerformanceData;
import com.lusen.cardola.framework.adapter.HolderViewItem;
import com.lusen.cardola.framework.adapter.IAdapterData;
import com.lusen.cardola.framework.util.ContextUtil;
import com.lusen.cardola.framework.util.DayTimeUtil;
import com.lusen.cardola.framework.util.UiUtil;

/**
 * Created by leo on 2017/7/30.
 */

public class HolderItemPerformance extends HolderViewItem {

    private TextView mCustomerName;
    private TextView mDayTime;
    private TextView mMoney;
    private TextView mStateBuy;
    private TextView mStateExchange;

    public HolderItemPerformance(@NonNull Context context) {
        super(context);
    }

    @Override
    public void bindView(View rootView) {
        mCustomerName = UiUtil.findViewById(rootView, R.id.customer_name, TextView.class);
        mDayTime = UiUtil.findViewById(rootView, R.id.day_time, TextView.class);
        mMoney = UiUtil.findViewById(rootView, R.id.money, TextView.class);
        mStateBuy = UiUtil.findViewById(rootView, R.id.state_buy, TextView.class);
        mStateExchange = UiUtil.findViewById(rootView, R.id.state_exchange, TextView.class);
    }

    @Override
    public void bindData(IAdapterData data, int position) {
        if (data instanceof PerformanceData) {
            mCustomerName.setText(((PerformanceData) data).mCustomerName);
            mDayTime.setText(DayTimeUtil.timeStamp2YMD(((PerformanceData) data).mDayTimeStamp));
            mMoney.setText(ContextUtil.getContext().getResources().getString(R.string.money_commission, ((PerformanceData) data).mMoney + "元"));
            mStateBuy.setText(((PerformanceData) data).mHasBuy ? "已购买" : "未购买");
            mStateExchange.setText(((PerformanceData) data).mHasExchange ? "已兑换" : "未兑换");
        }
    }

    @Override
    public int getLayoutRes() {
        return R.layout.holder_item_performance;
    }

}
