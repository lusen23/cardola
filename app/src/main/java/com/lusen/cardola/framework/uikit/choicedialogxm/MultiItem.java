package com.lusen.cardola.framework.uikit.choicedialogxm;

import com.lusen.cardola.framework.adapter.IAdapterData;

/**
 * Created by leo on 2014/12/3.<br><br>
 */
public class MultiItem implements IAdapterData {

    private String mItemTitle;
    private Object mExtra;
    public String mItemTxColor;
    public String mItemBgColor;
    public String mItemDivideColor;
    public boolean mShowDivide = true;

    public MultiItem(String itemTitle) {
        mItemTitle = itemTitle;
    }

    public String getItemTitle() {
        return mItemTitle;
    }

    public void setItemTitle(String itemTitle) {
        mItemTitle = itemTitle;
    }

    public Object getExtra() {
        return mExtra;
    }

    public void setExtra(Object extra) {
        mExtra = extra;
    }

}
