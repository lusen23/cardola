package com.lusen.cardola.business.main.home;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by leo on 2017/7/23.
 */

public class HomeViewPager extends ViewPager {

    public HomeViewPager(Context context) {
        super(context);
    }

    public HomeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // 屏蔽ViewPager处理
        return false;
    }

}
