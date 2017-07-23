package com.lusen.cardola.business.main.component;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.githang.viewpagerindicator.IconTabPageIndicator;
import com.lusen.cardola.R;
import com.lusen.cardola.business.base.CardolaBaseActivity;
import com.lusen.cardola.business.main.home.HomePagerAdapter;
import com.lusen.cardola.business.main.home.HomeViewPager;
import com.lusen.cardola.framework.uibase.UiModel;
import com.lusen.cardola.framework.util.UiUtil;

/**
 * Created by leo on 2017/7/22.
 */

public class HomeActivity extends CardolaBaseActivity {

    private HomeViewPager mViewPager;
    private IconTabPageIndicator mIndicator;

    private HomePagerAdapter mPagerAdapter;

    @Override
    protected View onContentViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflaterView(inflater, R.layout.activity_home, container);
    }

    @Override
    protected void onContentViewCreated(View view) {
        super.onContentViewCreated(view);
        mViewPager = UiUtil.findViewById(this, R.id.viewpager, HomeViewPager.class);
        mIndicator = UiUtil.findViewById(this, R.id.indicator, IconTabPageIndicator.class);

        mPagerAdapter = new HomePagerAdapter(getOptimizedFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mIndicator.setOnPageChangeListener(null);
        mIndicator.setViewPager(mViewPager, HomePagerAdapter.FRAGMENT_TAB_0);

    }

    @Override
    protected int initUiModel() {
        return UiModel.UI_MODEL_IMMERSIVE;
    }

}
