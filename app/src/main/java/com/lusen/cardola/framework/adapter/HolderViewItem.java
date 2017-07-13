package com.lusen.cardola.framework.adapter;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by leo on 2014/11/9.<br><br>
 * 基础HolderView绑定器<br><br>
 * BaseHolderView生成注意事项:<br><br>
 * 1.BaseHolderView具体实现类务必提供:构造函数(Context context),在此构造函数中调用super(Context context, int layoutResId)或super(Context context, View view)<br><br>
 * 2.BaseHolderView尽量采用单独类实现,避免采用内部类方式,如采用内部类方式实现(非静态),则务必在外部类中提供默认无参构造函数
 */
public abstract class HolderViewItem extends FrameLayout {

    protected Context mContext;
    private int mPosition;

    public HolderViewItem(Context context) {
        this(context, null);
    }

    protected HolderViewItem(Context context, int layoutResId) {
        this(context, null, layoutResId);
    }

    protected HolderViewItem(Context context, View view) {
        this(context, view, 0);
    }

    private HolderViewItem(Context context, View childView, int layoutResId) {
        super(context);
        this.mContext = context;
        if (childView != null) {
            addView(childView);
        } else if (layoutResId != 0) {
            inflate(context, layoutResId, this);
        }
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

}
