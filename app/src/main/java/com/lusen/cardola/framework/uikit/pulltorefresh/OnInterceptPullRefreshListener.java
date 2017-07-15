package com.lusen.cardola.framework.uikit.pulltorefresh;


import com.lusen.cardola.R;
import com.lusen.cardola.framework.util.NetworkUtil;

/**
 * 刷新之前拦截判断
 * Created by ak on 2015/1/22.
 */
public interface OnInterceptPullRefreshListener {

    public static final int NORMAL_STATE = 0x01;
    public static final int NO_NETWORK = 0x02;
    public static final int NO_MORE_DATA = 0x03;

    public boolean isPullBeforeToRefreshEnabled();

    public int interceptState();

    public void setHasMore(boolean hasMore);

    public int getBeforeRefreshLabelResourceId();

    public static class SampleOnInterceptPullRefreshListener implements OnInterceptPullRefreshListener {

        private int state = NORMAL_STATE;
        private boolean hasMore = true;


        @Override
        public boolean isPullBeforeToRefreshEnabled() {
            return interceptState() == NORMAL_STATE;
        }

        @Override
        public int interceptState() {
            if (NetworkUtil.isNoNetWork()) {
                return NO_NETWORK;
            } else if (!hasMore) {
                return NO_MORE_DATA;
            }
            return state;
        }

        public void setHasMore(boolean hasMore) {
            this.hasMore = hasMore;
        }

        @Override
        public int getBeforeRefreshLabelResourceId() {
            if (interceptState() == NO_NETWORK) {
                return R.string.now_none_network;
            } else if (interceptState() == NO_MORE_DATA) {
                return R.string.no_more_data;
            }
            return 0;
        }
    }

}
