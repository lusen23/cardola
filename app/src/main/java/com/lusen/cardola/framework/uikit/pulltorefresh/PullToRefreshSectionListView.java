package com.lusen.cardola.framework.uikit.pulltorefresh;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.lusen.cardola.R;
import com.lusen.cardola.framework.uikit.pulltorefresh.internal.EmptyViewMethodAccessor;
import com.lusen.cardola.framework.uikit.pulltorefresh.internal.LoadingLayout;

import java.util.ArrayList;

/**
 * Created by qijian on 15/8/24.
 */
public class PullToRefreshSectionListView extends PullToRefreshAdapterViewBase<ListView> {

    private LoadingLayout mHeaderLoadingView;
    private LoadingLayout mFooterLoadingView;

    private FrameLayout mLvFooterLoadingFrame;

    private boolean mListViewExtrasEnabled;

    public PullToRefreshSectionListView(Context context) {
        super(context);
    }

    public PullToRefreshSectionListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshSectionListView(Context context, Mode mode) {
        super(context, mode);
    }

    public PullToRefreshSectionListView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
    }


    @Override
    public final Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    @Override
    protected void onRefreshing(final boolean doScroll) {
        /**
         * If we're not showing the Refreshing view, or the list is empty, the
         * the header/footer views won't show so we use the normal method.
         */
        ListAdapter adapter = mRefreshableView.getAdapter();
        if (!mListViewExtrasEnabled || !getShowViewWhileRefreshing() || null == adapter || adapter.isEmpty()) {
            super.onRefreshing(doScroll);
            return;
        }

        super.onRefreshing(false);

        final LoadingLayout origLoadingView, listViewLoadingView, oppositeListViewLoadingView;
        final int selection, scrollToY;

        switch (getCurrentMode()) {
            case MANUAL_REFRESH_ONLY:
            case PULL_FROM_END:
                origLoadingView = getFooterLayout();
                listViewLoadingView = mFooterLoadingView;
                oppositeListViewLoadingView = mHeaderLoadingView;
                selection = mRefreshableView.getCount() - 1;
                scrollToY = getScrollY() - getFooterSize();
                break;
            case PULL_FROM_START:
            default:
                origLoadingView = getHeaderLayout();
                listViewLoadingView = mHeaderLoadingView;
                oppositeListViewLoadingView = mFooterLoadingView;
                selection = 0;
                scrollToY = getScrollY() + getHeaderSize();
                break;
        }

        // Hide our original Loading View
        origLoadingView.reset();
        origLoadingView.hideAllViews();

        // Make sure the opposite end is hidden too
        oppositeListViewLoadingView.setVisibility(View.GONE);

        // Show the ListView Loading View and set it to refresh.
        listViewLoadingView.setVisibility(View.VISIBLE);
        listViewLoadingView.refreshing();

        if (doScroll) {
            // We need to disable the automatic visibility changes for now
            disableLoadingLayoutVisibilityChanges();

            // We scroll slightly so that the ListView's header/footer is at the
            // same Y position as our normal header/footer
            setHeaderScroll(scrollToY);

            // Make sure the ListView is scrolled to show the loading
            // header/footer
            mRefreshableView.setSelection(selection);

            // Smooth scroll as normal
            smoothScrollTo(0);
        }
    }

    @Override
    protected void onReset() {
        /**
         * If the extras are not enabled, just call up to super and return.
         */
        if (!mListViewExtrasEnabled) {
            super.onReset();
            return;
        }

        final LoadingLayout originalLoadingLayout, listViewLoadingLayout;
        final int scrollToHeight, selection;
        final boolean scrollLvToEdge;

        switch (getCurrentMode()) {
            case MANUAL_REFRESH_ONLY:
            case PULL_FROM_END:
                originalLoadingLayout = getFooterLayout();
                listViewLoadingLayout = mFooterLoadingView;
                selection = mRefreshableView.getCount() - 1;
                scrollToHeight = getFooterSize();
                scrollLvToEdge = Math.abs(mRefreshableView.getLastVisiblePosition() - selection) <= 1;
                break;
            case PULL_FROM_START:
            default:
                originalLoadingLayout = getHeaderLayout();
                listViewLoadingLayout = mHeaderLoadingView;
                scrollToHeight = -getHeaderSize();
                selection = 0;
                scrollLvToEdge = Math.abs(mRefreshableView.getFirstVisiblePosition() - selection) <= 1;
                break;
        }

        // If the ListView header loading layout is showing, then we need to
        // flip so that the original one is showing instead
        if (listViewLoadingLayout.getVisibility() == View.VISIBLE) {

            // Set our Original View to Visible
            originalLoadingLayout.showInvisibleViews();

            // Hide the ListView Header/Footer
            listViewLoadingLayout.setVisibility(View.GONE);

            /**
             * Scroll so the View is at the same Y as the ListView
             * header/footer, but only scroll if: we've pulled to refresh, it's
             * positioned correctly
             */
            if (scrollLvToEdge && getState() != State.MANUAL_REFRESHING) {
                mRefreshableView.setSelection(selection);
                setHeaderScroll(scrollToHeight);
            }
        }

        // Finally, call up to super
        super.onReset();
    }

    @Override
    protected LoadingLayoutProxy createLoadingLayoutProxy(final boolean includeStart, final boolean includeEnd) {
        LoadingLayoutProxy proxy = super.createLoadingLayoutProxy(includeStart, includeEnd);

        if (mListViewExtrasEnabled) {
            final Mode mode = getMode();

            if (includeStart && mode.showHeaderLoadingLayout()) {
                proxy.addLayout(mHeaderLoadingView);
            }
            if (includeEnd && mode.showFooterLoadingLayout()) {
                proxy.addLayout(mFooterLoadingView);
            }
        }

        return proxy;
    }

    protected ListView createListView(Context context, AttributeSet attrs) {
        final ListView lv;
        lv = new InternalListViewSDK9(context, attrs);
        return lv;
    }

    @Override
    protected ListView createRefreshableView(Context context, AttributeSet attrs) {
        ListView lv = createListView(context, attrs);

        // Set it to this so it can be used in ListActivity/ListFragment
        lv.setId(android.R.id.list);
        return lv;
    }

    @Override
    protected void handleStyledAttributes(TypedArray a) {
        super.handleStyledAttributes(a);

        mListViewExtrasEnabled = a.getBoolean(R.styleable.PullToRefresh_ptrListViewExtrasEnabled, true);//默认需要

        if (mListViewExtrasEnabled) {
            final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);

            // Create Loading Views ready for use later
            FrameLayout frame = new FrameLayout(getContext());
            mHeaderLoadingView = createLoadingLayout(getContext(), Mode.PULL_FROM_START, a);
            mHeaderLoadingView.setVisibility(View.GONE);
            frame.addView(mHeaderLoadingView, lp);
            mRefreshableView.addHeaderView(frame, null, false);
            mRefreshableView.setHeaderDividersEnabled(false);

            mLvFooterLoadingFrame = new FrameLayout(getContext());
            mFooterLoadingView = createLoadingLayout(getContext(), Mode.PULL_FROM_END, a);
            mFooterLoadingView.setVisibility(View.GONE);
            mLvFooterLoadingFrame.addView(mFooterLoadingView, lp);

            /**
             * If the value for Scrolling While Refreshing hasn't been
             * explicitly set via XML, enable Scrolling While Refreshing.
             */
            if (!a.hasValue(R.styleable.PullToRefresh_ptrScrollingWhileRefreshingEnabled)) {
                setScrollingWhileRefreshingEnabled(true);
            }
        }


    }

    public void scrollToSceondHeader(){
        if (mRefreshableView instanceof InternalListViewSDK9) {
            final InternalListViewSDK9 listViewSDK9 = (InternalListViewSDK9) mRefreshableView;
            listViewSDK9.scrollToSecondHeader(1);
        }
    }

    @TargetApi(9)
    public final class InternalListViewSDK9 extends InternalListView {

        public InternalListViewSDK9(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        private ArrayList<View> mheaders = new ArrayList<View>();

        /**
         * 注意：
         * 1、在ListView中，使用getChildAt(index)的取值，只能是当前可见区域（列表可滚动）的子项！
         * 即取值范围在 >= ListView.getFirstVisiblePosition() &&  <= ListView.getLastVisiblePosition();
         * <p/>
         * 2、不要在listView中添加负值的marginTop，不然会导致getFirstVibisble（）得到的值不准
         * 正值的marginTop得到的getFirstVisible()是准确的
         * <p/>
         * 3、目前计算方法仅考虑heaerview与Item都全部显示的情况下，对于headerView的visible设为GONE的情况没有考虑
         *
         * @param list
         * @return
         */
        private int getScrollHeight(ListView list, ArrayList<? extends View> headviews) {
            int headCount = headviews.size();
            //由于在显示顶部加载时，顶部PullToRefresh顶部的下拉加载时，是不显示的，所以校正当前header的位置
            int firstVisiblePos = list.getFirstVisiblePosition();
            //说明已经下拉到pullToRefresh的本身的headview可见的区域。
            if (firstVisiblePos < 0) {
                return 0;
            }
            int scrollHeight = 0;
            if (firstVisiblePos < headCount) {
                //如果还在header内部，说明只需要逐个计算header的高度就好了。
                if (headviews == null) {
                    new Exception("内部含有headerView,请在函数入口处设置headview list");
                    return -1;
                }
                for (int i = 0; i <= firstVisiblePos; i++) {
                    View view = headviews.get(i);
                    if (view != null && i == firstVisiblePos) {
                        //注意，getTop()是负值，因为已经滚到不可见区域去了
                        scrollHeight += (-view.getTop());
                    } else if (i != firstVisiblePos) {
                        scrollHeight += view.getHeight();
                    }
                }
            } else {
                //这里假设除了headview以外的所有的正常ListItem的高度都是一样的。如果你的不一样，需要改写这一部分的计算方式了
                //已经滚出所以headView，只需要将所以headview高度相加，然后再加上其它所有list的高度即可
                if (headviews != null) {
                    for (View view : headviews) {
                        scrollHeight += view.getHeight();
                    }
                }
                //获取单个item的视图
                View itemView = list.getChildAt(0);
                //值得非常注意的是firstVisiblePos是从0开始算的，所以headCount正好对应listview的第一个item的索引
                if (itemView != null) {
                    //这里计算的是从headview到当前可见的item之间已经被完全滚过去的item的总高度
                    scrollHeight += (firstVisiblePos - headCount) * itemView.getHeight();
                }
                //最后加上当前可见的item，已经滚动的部分
                scrollHeight += (-itemView.getTop());
            }
            return scrollHeight;
        }



        /**
         * 滚动到第二个header处,不算PullToRefresh内置的下拉刷新headerview
         * @param firstVisibilePos  内置下拉的headerview索引为0
         */
        public void scrollToSecondHeader(int firstVisibilePos){
            //只向上滚动，不向下滚动，如果想要实现向下滚动到第二个header，请自行添加代码
            if (firstVisibilePos >=2){
                return;
            }
            if (mheaders == null || mheaders.size()<2){
                return;
            }
            View head_1 = mheaders.get(1);
            final  int headerHeight = head_1.getHeight();
            int scrollHeight = getScrollHeight(this, mheaders);
            int delta = scrollHeight - headerHeight;
            this.smoothScrollBy(-delta,300);
        }


        @Override
        public void addHeaderView(View v, Object data, boolean isSelectable) {
            super.addHeaderView(v, data, isSelectable);
            mheaders.add(v);
        }
        public ArrayList<View> getHeaderList(){
            return mheaders;
        }
    }

    protected class InternalListView extends ListView implements EmptyViewMethodAccessor {

        private boolean mAddedLvFooter = false;

        public InternalListView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected void dispatchDraw(Canvas canvas) {
            /**
             * This is a bit hacky, but Samsung's ListView has got a bug in it
             * when using Header/Footer Views and the list is empty. This masks
             * the issue so that it doesn't cause an FC. See Issue #66.
             */
            try {
                super.dispatchDraw(canvas);
            } catch (IndexOutOfBoundsException e) {
                Log.w("", e.getMessage());
            } catch (StackOverflowError e) {
                e.printStackTrace();
            }
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            /**
             * This is a bit hacky, but Samsung's ListView has got a bug in it
             * when using Header/Footer Views and the list is empty. This masks
             * the issue so that it doesn't cause an FC. See Issue #66.
             */
            try {
                return super.dispatchTouchEvent(ev);
            } catch (IndexOutOfBoundsException e) {
                Log.w("", e.getMessage());
                return false;
            }
        }

        @Override
        public void setAdapter(ListAdapter adapter) {
            // Add the Footer View at the last possible moment
            if (null != mLvFooterLoadingFrame && !mAddedLvFooter) {
                addFooterView(mLvFooterLoadingFrame, null, false);
                mAddedLvFooter = true;
            }

            super.setAdapter(adapter);
        }

        @Override
        public void setEmptyView(View emptyView) {
            PullToRefreshSectionListView.this.setEmptyView(emptyView);
        }

        @Override
        public void setEmptyViewInternal(View emptyView) {
            super.setEmptyView(emptyView);
        }

    }

}
