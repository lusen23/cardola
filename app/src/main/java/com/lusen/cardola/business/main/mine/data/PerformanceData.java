package com.lusen.cardola.business.main.mine.data;

import com.lusen.cardola.business.main.mine.holderitem.HolderItemPerformance;
import com.lusen.cardola.framework.adapter.IAdapterDataViewModel;

/**
 * Created by leo on 2017/7/30.
 */

public class PerformanceData implements IAdapterDataViewModel {

    public String mCustomerName;
    public long mMoney;
    public long mDayTimeStamp;
    public boolean mHasBuy;
    public boolean mHasExchange;

    public PerformanceData(String customerName, long money, long dayTimeStamp, boolean hasBuy, boolean hasExchange) {
        mCustomerName = customerName;
        mMoney = money;
        mDayTimeStamp = dayTimeStamp;
        mHasBuy = hasBuy;
        mHasExchange = hasExchange;
    }

    @Override
    public Class getViewModelType() {
        return HolderItemPerformance.class;
    }

}
