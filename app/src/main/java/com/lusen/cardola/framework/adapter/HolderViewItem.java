package com.lusen.cardola.framework.adapter;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by leo on 2017/7/15.<br><br>
 * 基础HolderView绑定器<br><br>
 * 尽量采用单独类实现,避免采用内部类方式,如采用内部类方式实现(非静态),则务必在外部类中提供默认无参构造函数
 */
public abstract class HolderViewItem extends FrameLayout {

    public HolderViewItem(@NonNull Context context) {
        super(context);
        init();
    }

    public HolderViewItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HolderViewItem(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), getLayoutRes(), this);
        bindView(this);
    }

    /**
     * 初始化HolderView内部控件
     *
     * @param rootView 顶层View
     */
    public abstract void bindView(View rootView);

    /**
     * 绑定数据
     *
     * @param data     数据源
     * @param position 数据位置
     */
    public abstract void bindData(IAdapterData data, int position);

    /**
     * 布局
     *
     * @return 布局资源XML
     */
    @LayoutRes
    public abstract int getLayoutRes();

}
