package com.lusen.cardola.framework.adapter;

/**
 * Created by leo on 2017/7/15.<br><br>
 */
public interface IAdapterDataViewModel<T extends HolderViewItem> extends IAdapterData {

    /**
     * 获取数据对应的视图模型
     *
     * @return 数据类型
     */
    Class<T> getViewModelType();

}
