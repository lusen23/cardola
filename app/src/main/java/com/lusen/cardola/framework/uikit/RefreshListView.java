package com.lusen.cardola.framework.uikit;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.lusen.cardola.framework.uikit.pulltorefresh.PullToRefreshListView;

/**
 * Created by leo on 2017/7/16.
 */

public class RefreshListView extends PullToRefreshListView {

    private AdapterView.OnItemClickListener mCustomerOnItemClickListener;
    private AdapterView.OnItemLongClickListener mCustomerOnItemLongClickListener;

    public RefreshListView(Context context) {
        super(context);
        internalInit();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        internalInit();
    }

    public RefreshListView(Context context, Mode mode) {
        super(context, mode);
        internalInit();
    }

    public RefreshListView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
        internalInit();
    }

    private void internalInit() {
        super.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // id返回值原理: id = itemPosition - (headerViewCount + footerViewCount)
                // id<0说明点击的是Header或者Footer,否则点击的是AdapterView中的Position
                final int headerNum = getRefreshableView().getHeaderViewsCount();
                if (id >= 0) {
                    int dataPosition = position - headerNum;
                    // 回调上层
                    if (null != mCustomerOnItemClickListener) {
                        mCustomerOnItemClickListener.onItemClick(parent, view, dataPosition, id);
                    }
                }
            }
        });
        super.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int headerNum = getRefreshableView().getHeaderViewsCount();
                if (id >= 0) {
                    int dataPosition = position - headerNum;
                    // 回调上层
                    if (null != mCustomerOnItemLongClickListener) {
                        mCustomerOnItemLongClickListener.onItemLongClick(parent, view, dataPosition, id);
                    }
                }
                // 强行由底层返回True,表示LongClick事件后,无需再传递执行Click事件分发
                return true;
            }
        });
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
    }

    @Override
    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        mCustomerOnItemClickListener = onItemClickListener;
    }

    @Override
    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener onItemLongClickListener) {
        mCustomerOnItemLongClickListener = onItemLongClickListener;
    }

}
