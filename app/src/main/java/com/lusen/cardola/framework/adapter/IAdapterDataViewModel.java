package com.lusen.cardola.framework.adapter;

/**
 * Created by leo on 2014/11/11.<br><br>
 */
public interface IAdapterDataViewModel<T extends HolderViewItem> extends IAdapterData {

    /**
     * 获取数据对应的视图模型
     * @return
     */
    Class<T> getViewModelType();

}
