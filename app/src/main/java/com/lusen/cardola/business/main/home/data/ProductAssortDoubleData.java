package com.lusen.cardola.business.main.home.data;

import com.lusen.cardola.business.main.home.holderitem.HolderItemProductAssortDouble;
import com.lusen.cardola.framework.adapter.IAdapterDataViewModel;

/**
 * Created by leo on 2017/7/23.
 */

public class ProductAssortDoubleData extends ProductAssortBaseData implements IAdapterDataViewModel {

    @Override
    public Class getViewModelType() {
        return HolderItemProductAssortDouble.class;
    }

}
