package com.lusen.cardola.demo;

import com.lusen.cardola.framework.adapter.IAdapterDataViewModel;

/**
 * Created by leo on 2017/7/16.
 */

public class DemoData implements IAdapterDataViewModel {

    public String logo;
    public String title;

    public DemoData(String logo, String title) {
        this.logo = logo;
        this.title = title;
    }

    @Override
    public Class getViewModelType() {
        return DemoHolderView.class;
    }

}
