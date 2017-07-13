package com.lusen.cardola.framework.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.xiami.basic.rtenviroment.AppRuntime;
import com.xiami.music.util.ThreadUtil;
import com.xiami.music.util.logtrack.AppLog;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fm.xiami.main.BuildConfig;

/**
 * Created by leo on 2014/11/9.<br><br>
 * 绑定数据源及HolderView的Adapter,实现数据及UI分离
 */
public class HolderViewAdapter extends BaseAdapter {

    private final Context mContext;
    private List<? extends IAdapterData> mDatas;
    private Class<? extends HolderViewItem>[] mHolderViews;
    private HolderViewCallback mHolderViewCallback;
    private ListView listView;

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
        if (null == listView) {
            if (null != parent && parent instanceof ListView) {
                listView = (ListView) parent;
            }
        }
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
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
                AppLog.w(e.getMessage());
                AppLog.w("HolderViewAdapter error = " + e.getMessage());
            }
        }

        // HolderView回调接口
        if (null != mHolderViewCallback) {
            mHolderViewCallback.onHolderViewInvalidate(holderView, position);
        }

        // HolderView数据绑定
        if (null != getItem(position) && null != holderView) {
            //为避免干扰bindData逻辑，所以单独开了个口子去写入position，给埋点用...
            holderView.setPosition(position);
            holderView.bindData(getItem(position), position);
        }

        if (holderView == null) {
            //FIXME 这里绝不能存在holderView == null的情况，否则listView会报NPE无法正常工作。需要将这个漏洞补上。
            return new View(AppRuntime.context);
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
            AppLog.w(e.getMessage());
        }
        return -1;
    }

    public void setHolderViewCallback(HolderViewCallback callback) {
        this.mHolderViewCallback = callback;
    }

    @Override
    public void destroyObject(boolean focus) {
        mDatas.clear();
    }

    @Override
    public boolean isDestroyed() {
        return false;
    }

    @Override
    public void setState(DestroyableObjectState state) {

    }

    public interface HolderViewCallback {
        void onHolderViewInvalidate(HolderViewItem holderView, int position);
    }


    /**
     * 更新指定位置视图
     *
     * @param position
     */
    public void updateView(int position) {
        if (listView != null) {
            int visiblePosition = listView.getFirstVisiblePosition();
            View view = listView.getChildAt(position - visiblePosition + listView.getHeaderViewsCount());
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
