package com.lusen.cardola.business.main.mine.data;

import com.lusen.cardola.business.main.mine.holderitem.HolderItemMessage;
import com.lusen.cardola.framework.adapter.IAdapterDataViewModel;

/**
 * Created by leo on 2017/7/30.
 */

public class MessageData implements IAdapterDataViewModel {

    public long mTimeStamp;
    public String mMessage;

    public MessageData(long timeStamp, String message) {
        mTimeStamp = timeStamp;
        mMessage = message;
    }

    @Override
    public Class getViewModelType() {
        return HolderItemMessage.class;
    }

}
