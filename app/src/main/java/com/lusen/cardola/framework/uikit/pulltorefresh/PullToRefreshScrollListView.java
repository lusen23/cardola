/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.lusen.cardola.framework.uikit.pulltorefresh;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.lusen.cardola.R;
import com.lusen.cardola.framework.uikit.pulltorefresh.internal.EmptyViewMethodAccessor;
import com.lusen.cardola.framework.uikit.pulltorefresh.internal.LoadingLayout;

import java.util.ArrayList;

public class PullToRefreshScrollListView extends PullToRefreshAdapterViewBase<ListView> {

    private LoadingLayout mHeaderLoadingView;
    private LoadingLayout mFooterLoadingView;

    private FrameLayout mLvFooterLoadingFrame;

    private boolean mListViewExtrasEnabled;

    /**
     * 阻尼系数,越小阻力就越大.
     */
    public static final float SCROLL_RATIO = 0.5f;

    public PullToRefreshScrollListView(Context context) {
        super(context);
    }

    public PullToRefreshScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshScrollListView(Context context, Mode mode) {
        super(context, mode);
    }

    public PullToRefreshScrollListView(Context context, Mode mode, AnimationStyle style) {
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

    public void setHeaderView(final View headerView) {
        if (mRefreshableView instanceof InternalListViewSDK9) {
            final InternalListViewSDK9 listViewSDK9 = (InternalListViewSDK9) mRefreshableView;
            if (mScaleBgInterface != null) {
                listViewSDK9.setScaleBackbroundListener(mScaleBgInterface);
            }
            if (shareInterface != null) {
                listViewSDK9.setShareInterface(shareInterface);
            }
            if (mScrollListener != null) {
                listViewSDK9.setScrollListener(mScrollListener);
            }
            listViewSDK9.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (headerView != null) {
                        //向上滚动，headview.getTop是负值
                        mScrollListener.OnScroll(firstVisibleItem, -listViewSDK9.getHeaderCurTop());
                    } else {
                        mScrollListener.OnScroll(firstVisibleItem, -1);
                    }

                }
            });

            listViewSDK9.setmHeaderView(headerView);
            listViewSDK9.setContentView(mContentView);
        }
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

    public enum PullState {
        /**
         * 顶部
         */
        UP,
        /**
         * 底部
         */
        DOWN,
        /**
         * 正常
         */
        NORMAL
    }

    /**
     * 向下滚动时回调
     */
    public interface PullToRefreshScrollViewInterface {
        void scaleBackground(int l, int oldl, PullState mState);
    }

    private PullToRefreshScrollViewInterface mScaleBgInterface;

    /**
     * 向下滚动时回调
     * visibleItem:当前可见的ITEM,从0开始，不使用，传-1
     * distance:手指移动距离
     */
    public interface PullToRefreshScrollListener {
        void OnScroll(int visibleItem, int distance);
    }

    private PullToRefreshScrollListener mScrollListener;

    /**
     * 设置滚动回调函数
     *
     * @param scrollListener
     */
    public void setScrollListener(PullToRefreshScrollListener scrollListener) {
        mScrollListener = scrollListener;
    }

    /**
     * 分享时回调
     */
    public interface PullToShareInterface {
        void onShare();
    }

    private PullToShareInterface shareInterface;

    /**
     * 设置分享回调
     *
     * @param shareListener
     */
    public void setShareInterface(PullToShareInterface shareListener) {
        shareInterface = shareListener;
    }

    /**
     * 设置下滑需要滚动背景时的监听
     *
     * @param listener
     */
    public void setScaleBackgroundListener(PullToRefreshScrollViewInterface listener) {
        mScaleBgInterface = listener;
    }

    public void setContentView(View view) {
        mContentView = view;
    }

    private View mContentView;

    @TargetApi(9)
    public final class InternalListViewSDK9 extends InternalListView {

        //底部图片View
        private View mHeaderView;
        //头部图片的初始化位置
        private Rect mHeadInitRect = new Rect();
        //底部View
        private View mContentView;
        //ScrollView的contentView的初始化位置
        private Rect mContentInitRect = new Rect();

        //初始点击位置
        private Point mTouchPoint = new Point();


        //标识当前view是否移动
        boolean mIsMoving = false;

        //是否使用layout函数移动布局
        boolean mIsLayout = false;

        private int mContentTop;

        private int mHeaderCurTop;

        //用户定义的headview高度
        private int mContentMaxMoveHeight = 0;

        /**
         * 是否关闭ScrollView的滑动.
         */
        private boolean mEnableTouch = false;

        public InternalListViewSDK9(Context context, AttributeSet attrs) {
            super(context, attrs);
            mContentMaxMoveHeight = (int) ((getResources().getDimensionPixelOffset(R.dimen.pull_to_refresh_custom_distance)) * SCROLL_RATIO);
        }

        private PullToRefreshScrollViewInterface internalScracleBgInterface;
        private PullToShareInterface shareInterface;
        private PullToRefreshScrollListener scrollListener;
        private ArrayList<View> mHeadViews = new ArrayList<View>();

        /**
         * 设置回调函数
         *
         * @param listener
         */
        public void setScrollListener(PullToRefreshScrollListener listener) {
            this.scrollListener = listener;
        }

        /**
         * 设置分享回调
         *
         * @param shareListener
         */
        public void setShareInterface(PullToShareInterface shareListener) {
            shareInterface = shareListener;
        }

        /**
         * 设置下滑监听
         *
         * @param listener
         */
        public void setScaleBackbroundListener(PullToRefreshScrollViewInterface listener) {
            internalScracleBgInterface = listener;
        }

        @Override
        protected void onFinishInflate() {
            super.onFinishInflate();
        }

        public void setContentView(View view) {
            mContentView = view;
        }


        /**
         * 获取当前headview的top位置,由于使用的是getChildAt（）
         * 所以并不准确，必须与firstVisiblePosition共同来判断
         *
         * @return
         */
        private int getHeaderCurTop() {
            View c = getChildAt(0);
            if (c == null) {
                return 0;
            }
            return c.getTop();
        }

        @Override
        public void addHeaderView(View v) {
            super.addHeaderView(v);
            if (v != null) {
                mHeadViews.add(v);
            }
        }


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
            int firstVisiblePos = list.getFirstVisiblePosition() - 1;
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


        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE: {
                    int moveHeight = (int) event.getY() - mTouchPoint.y;
                    int scrolledHeight = 0;
                    try {
                        scrolledHeight = getScrollHeight(this, mHeadViews);
                    } catch (Exception e) {
                        Log.e("", e.getMessage());
                    }
                    if (moveHeight > 0 && mIsLayout && scrolledHeight == 0) {
                        float headerMoveHeight = moveHeight * 0.5f * SCROLL_RATIO;
                        mHeaderCurTop = (int) (mHeadInitRect.top + headerMoveHeight);
                        float contentMoveHeight = moveHeight * SCROLL_RATIO;
                        mContentTop = (int) (mContentInitRect.top + contentMoveHeight);

                        if (contentMoveHeight <= mContentMaxMoveHeight) {
                            internalScracleBgInterface.scaleBackground((int) contentMoveHeight, (int) contentMoveHeight, PullState.DOWN);
                            mHeaderView.setTranslationY(headerMoveHeight);
                            mContentView.setTranslationY(contentMoveHeight);

                            mIsMoving = true;
                            mEnableTouch = true;
                        }
                    } else {
                        mEnableTouch = false;
                    }
                }
                break;
                case MotionEvent.ACTION_UP: {
                    //反弹
                    if (mIsMoving) {
                        shareInterface.onShare();
                        mHeaderView.setTranslationY(0);
                        TranslateAnimation headAnim = new TranslateAnimation(0, 0, mHeaderCurTop - mHeadInitRect.top, 0);
                        headAnim.setDuration(200);
                        mHeaderView.startAnimation(headAnim);

                        mContentView.setTranslationY(0);
                        TranslateAnimation contentAnim = new TranslateAnimation(0, 0, mContentTop - mContentInitRect.top, 0);
                        contentAnim.setDuration(200);
                        mContentView.startAnimation(contentAnim);
                        mIsMoving = false;
                    }
                    mIsLayout = false;
                    mEnableTouch = false;
                }
                break;
                case MotionEvent.ACTION_CANCEL: {
                    mEnableTouch = false;
                }
                break;
            }
            // 禁止控件本身的滑动.
            //这句厉害,如果mEnableMoving返回TRUE,那么就不会执行super.onTouchEvent(event)
            //只有返回FALSE的时候,才会执行super.onTouchEvent(event)
            //禁止控件本身的滑动，就会让它，本来应有的滑动就不会滑动了，比如向上滚动
            //！！！！！这点对于listview控件尤为重要。因为在上滑时，如果不禁止控件本身的向上移动，
            // 就会乱套，因为你本不需要利用setTranslationY（）上移的地方，他仍然会上移
            return mEnableTouch || super.onTouchEvent(event);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                //保存原始位置

                mTouchPoint.set((int) event.getX(), (int) event.getY());
                mHeadInitRect.set(mHeaderView.getLeft(), mHeaderView.getTop(), mHeaderView.getRight(), mHeaderView.getBottom());
                mContentInitRect.set(mContentView.getLeft(), mContentView.getTop(), mContentView.getRight(), mContentView.getBottom());
                mIsMoving = false;
                mEnableTouch = false;
                //如果当前不是从初始化位置开始滚动的话，就不让用户拖拽
                if (getScrollY() == 0) {
                    mIsLayout = true;
                }

            }
            return super.onInterceptTouchEvent(event);
        }

        public void setmHeaderView(View view) {
            mHeaderView = view;
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
            PullToRefreshScrollListView.this.setEmptyView(emptyView);
        }

        @Override
        public void setEmptyViewInternal(View emptyView) {
            super.setEmptyView(emptyView);
        }

    }

}
