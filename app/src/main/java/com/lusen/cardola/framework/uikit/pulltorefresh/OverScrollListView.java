package com.lusen.cardola.framework.uikit.pulltorefresh;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ListView;

import com.lusen.cardola.framework.util.DisplayUtil;

/**
 * 弹性效果ListView
 * Created by sky on 15/1/6.
 */
public class OverScrollListView extends ListView {

    private int mMaxOverScrollDistance = 200;

    public OverScrollListView(Context context) {
        super(context);
        mMaxOverScrollDistance = DisplayUtil.dip2px(75);
    }

    public int getmMaxOverScrollDistance() {
        return mMaxOverScrollDistance;
    }

    public void setmMaxOverScrollDistance(int mMaxOverScrollDistance) {
        this.mMaxOverScrollDistance = mMaxOverScrollDistance;
    }

    public OverScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mMaxOverScrollDistance = DisplayUtil.dip2px(75);
    }

    public OverScrollListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mMaxOverScrollDistance = DisplayUtil.dip2px(75);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, mMaxOverScrollDistance, isTouchEvent);
    }
}
