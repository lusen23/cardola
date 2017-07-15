/**
 * 
 */
package com.lusen.cardola.framework.uikit.pulltorefresh;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import java.lang.ref.SoftReference;


/**
 * 滚动后，自动加载数据
 *
 */
public class AutoLoadScrollListener implements OnScrollListener {

	boolean mIsEnable = true;
	int mTotalCount = 0;
	int mLastItem = 0;
    SoftReference<PullToRefreshAdapterViewBase> mOnRefreshViewReference;
    private int leftCount = 6;

	public AutoLoadScrollListener(){
    }

    public AutoLoadScrollListener(PullToRefreshAdapterViewBase pullToRefreshAdapterViewBase){
        mOnRefreshViewReference = new SoftReference<PullToRefreshAdapterViewBase>(pullToRefreshAdapterViewBase);
    }

    /** set to true when first time data loaded , set to false when no more data
	 * to load
	 * 
	 * @param e */
	public void setEnable(boolean e) {
		mIsEnable = e;
	}

    public void setLeftCount(int leftCount){
        this.leftCount = leftCount;
    }

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		mLastItem = firstVisibleItem + visibleItemCount - 1;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE && mIsEnable && mLastItem >= view.getCount() - leftCount && mOnRefreshViewReference != null && mOnRefreshViewReference.get()!=null) {
            if (mOnRefreshViewReference.get() != null){
                PullToRefreshAdapterViewBase pullToRefreshAdapterViewBase = mOnRefreshViewReference.get();
                if (pullToRefreshAdapterViewBase.getMode() == PullToRefreshBase.Mode.PULL_FROM_END || pullToRefreshAdapterViewBase.getMode() == PullToRefreshBase.Mode.BOTH){
                    pullToRefreshAdapterViewBase.setCurrentMode(PullToRefreshBase.Mode.PULL_FROM_END);
                    pullToRefreshAdapterViewBase.setRefreshing(false);
                }
            }
		}
	}

}