package com.lusen.cardola.framework.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.lusen.cardola.framework.util.ThreadUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by leo on 2017/7/15.<br><br>
 * 绑定数据源及HolderView的Adapter,实现数据及UI分离
 */
public class HolderViewAdapter extends BaseAdapter {

    private final Context mContext;
    private List<? extends IAdapterData> mDatas;
    private Class<? extends HolderViewItem>[] mHolderViews;
    private HolderViewCallback mHolderViewCallback;
    private AbsListView mAbsListView;

    public HolderViewAdapter(Context context) {
        this.mContext = context;
    }

    public HolderViewAdapter(Context context, List<? extends IAdapterData> datas, Class<? extends HolderViewItem>... holderViews) {
        this.mContext = context;
        this.mDatas = datas;
        this.mHolderViews = holderViews;
    }

    public void setHolderViews(Class<? extends HolderViewItem>... holderViews) {
        this.mHolderViews = holderViews;
    }

    public void setDatas(List<? extends IAdapterData> datas) {
        this.mDatas = datas;
    }

    public void appendData(List<? extends IAdapterData> datas) {
        List<IAdapterData> dataList = (List<IAdapterData>) getDatas();
        if (dataList != null) {
            ArrayList<IAdapterData> tempDataList = new ArrayList<>();
            for (IAdapterData data : datas) {
                tempDataList.add(data);
            }
            dataList.addAll(tempDataList);
        }
    }

    public List<? extends IAdapterData> getDatas() {
        return this.mDatas;
    }

    public boolean isDataEmpty() {
        return null == mDatas || mDatas.size() == 0;
    }

    @Override
    public int getCount() {
        return null != mDatas ? mDatas.size() : 0;
    }

    @Override
    public IAdapterData getItem(int position) {
        return null != mDatas && position >= 0 && position < mDatas.size() ? mDatas.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return null != mHolderViews ? mHolderViews.length : 1;
    }

    @Override
    public int getItemViewType(int position) {
        // 数据源对象实现IAdapterView接口,定义数据所需要展现的视图类型
        // 上层也可无需实现IAdapterView接口,直接重写getItemViewType方法,自定义数据展现视图类型
        IAdapterData data = getItem(position);
        if (null != data && data instanceof IAdapterDataViewModel) {
            Class<? extends HolderViewItem> viewModelType = ((IAdapterDataViewModel) data).getViewModelType();
            if (null != viewModelType) {
                int index = Arrays.asList(mHolderViews).indexOf(viewModelType);
                return index >= 0 ? index : 0;
            }
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == mAbsListView) {
            if (parent instanceof AbsListView) {
                mAbsListView = (AbsListView) parent;
            }
        }
        // 构建生成HolderViewItem
        HolderViewItem holderView = null;
        if (null != convertView && convertView.getClass().getName().equals(mHolderViews[getItemViewType(position)].getName())) {
            holderView = (HolderViewItem) convertView;
        } else {
            try {
                // HolderView类
                Class<? extends HolderViewItem> holderViewClass = mHolderViews[getItemViewType(position)];
                // 获取外部类
                Class enclosingClass = holderViewClass.getEnclosingClass();
                // HolderView是内部类
                if (null != holderViewClass.getEnclosingClass()) {
                    // 静态内部类
                    if (Modifier.isStatic(holderViewClass.getModifiers())) {
                        Constructor constructor = holderViewClass.getDeclaredConstructor(Context.class);
                        constructor.setAccessible(true);
                        holderView = (HolderViewItem) constructor.newInstance(mContext);
                    }
                    // 非静态内部类
                    else {
                        Constructor constructor = holderViewClass.getDeclaredConstructor(enclosingClass, Context.class);
                        constructor.setAccessible(true);
                        holderView = (HolderViewItem) constructor.newInstance(enclosingClass.newInstance(), mContext);
                    }
                }
                // HolderView非内部类
                else {
                    Constructor constructor = holderViewClass.getDeclaredConstructor(Context.class);
                    constructor.setAccessible(true);
                    holderView = (HolderViewItem) constructor.newInstance(mContext);
                }
            } catch (Exception e) {
            }
        }

        // HolderView回调接口
        if (null != mHolderViewCallback) {
            mHolderViewCallback.onHolderViewInvalidate(holderView, position);
        }
        // HolderView数据绑定
        if (null != getItem(position) && null != holderView) {
            holderView.bindData(getItem(position), position);
        }
        if (holderView == null) {
            return new View(mContext);
        }
        return holderView;
    }

    @Override
    public void notifyDataSetChanged() {
        if (ThreadUtil.isUIThread()) {
            super.notifyDataSetChanged();
        } else {
            ThreadUtil.MAIN_THREAD_HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    HolderViewAdapter.super.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void notifyDataSetInvalidated() {
        if (ThreadUtil.isUIThread()) {
            super.notifyDataSetChanged();
        } else {
            ThreadUtil.MAIN_THREAD_HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    HolderViewAdapter.super.notifyDataSetInvalidated();
                }
            });
        }
    }

    protected int getHolderViewType(Class<? extends HolderViewItem> viewModelType) {
        try {
            return Arrays.asList(mHolderViews).indexOf(viewModelType);
        } catch (Exception e) {
        }
        return -1;
    }

    public void setHolderViewCallback(HolderViewCallback callback) {
        this.mHolderViewCallback = callback;
    }

    public interface HolderViewCallback {
        void onHolderViewInvalidate(HolderViewItem holderView, int position);
    }


    /**
     * 更新指定位置视图
     *
     * @param position 数据位置
     */
    public void updateView(int position) {
        if (null != mAbsListView) {
            int visiblePosition = mAbsListView.getFirstVisiblePosition();
            int headerViewCount = (mAbsListView instanceof ListView ? ((ListView) mAbsListView).getHeaderViewsCount() : 0);
            View view = mAbsListView.getChildAt(position - visiblePosition + headerViewCount);
            HolderViewItem holderView = null;
            if (null != view && view.getClass().getName().equals(mHolderViews[getItemViewType(position)].getName())) {
                holderView = (HolderViewItem) view;
            }
            // HolderView回调接口
            if (null != mHolderViewCallback) {
                mHolderViewCallback.onHolderViewInvalidate(holderView, position);
            }
            // HolderView数据绑定
            if (null != getItem(position) && null != holderView) {
                holderView.bindData(getItem(position), position);
            }
        }
    }
}
