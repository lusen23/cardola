package com.lusen.cardola.business.main.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.githang.viewpagerindicator.IconPagerAdapter;
import com.lusen.cardola.R;
import com.lusen.cardola.business.main.component.fragment.CheckInfoListFragment;
import com.lusen.cardola.business.main.component.fragment.CustomerListFragment;
import com.lusen.cardola.business.main.component.fragment.MineFragment;
import com.lusen.cardola.business.main.component.fragment.ProductAssortFragment;
import com.lusen.cardola.framework.util.ContextUtil;

/**
 * Created by leo on 2017/7/23.
 */

public class HomePagerAdapter extends FragmentPagerAdapter implements IconPagerAdapter {

    public static final int FRAGMENT_TAB_0 = 0;
    public static final int FRAGMENT_TAB_1 = 1;
    public static final int FRAGMENT_TAB_2 = 2;
    public static final int FRAGMENT_TAB_3 = 3;

    private String[] mTabTitle = new String[]{};
    private final SparseArray<Fragment> mMapFragments = new SparseArray<>();

    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
        // 解决not attached to Activity
        try {
            mTabTitle = ContextUtil.getContext().getResources().getStringArray(R.array.home_tab_title_array);
        } catch (Exception e) {
        }
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        Fragment fragment = (Fragment) object;
        if (null == fragment || null == fragment.getFragmentManager()) {
            return;
        }
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = mMapFragments.get(position);
        if (null == fragment) {
            // 歌曲
            if (position == FRAGMENT_TAB_0) {
                fragment = new CustomerListFragment();
            }
            // 艺人
            else if (position == FRAGMENT_TAB_1) {
                fragment = new CheckInfoListFragment();
            }
            // 专辑
            else if (position == FRAGMENT_TAB_2) {
                fragment = new ProductAssortFragment();
            }
            // 歌单
            else if (position == FRAGMENT_TAB_3) {
                fragment = new MineFragment();
            }
            mMapFragments.put(position, fragment);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return mTabTitle.length;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabTitle[position];
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == ((Fragment) obj).getView();
    }

    @Override
    public int getIconResId(int index) {
        return R.drawable.demo;
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        // 捕获异常:commitAllowingStateLoss java.lang.IllegalStateException: commit already called
        try {
            super.finishUpdate(container);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
