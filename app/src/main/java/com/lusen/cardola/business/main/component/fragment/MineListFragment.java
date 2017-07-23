package com.lusen.cardola.business.main.component.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lusen.cardola.R;
import com.lusen.cardola.business.base.CardolaBaseFragment;

/**
 * Created by leo on 2017/7/23.
 */

public class MineListFragment extends CardolaBaseFragment {
    @Override
    protected View onContentViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflaterView(inflater, R.layout.fragment_mine_list, container);
    }
}
