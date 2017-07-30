package com.lusen.cardola.business.main.component;

import com.lusen.cardola.R;
import com.lusen.cardola.business.base.CardolaBaseListPageActivity;
import com.lusen.cardola.business.main.mine.data.PerformanceData;
import com.lusen.cardola.business.main.mine.holderitem.HolderItemPerformance;
import com.lusen.cardola.business.main.user.UserManager;
import com.lusen.cardola.business.network.CardolaApiManager;
import com.lusen.cardola.business.network.resp.GetPerformanceListResp;
import com.lusen.cardola.framework.adapter.HolderViewItem;
import com.lusen.cardola.framework.util.ContextUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leo on 2017/7/30.
 */

public class MinePerformanceActivity extends CardolaBaseListPageActivity<PerformanceData, GetPerformanceListResp> {

    @Override
    protected String initActionBarTitle() {
        return ContextUtil.getContext().getResources().getString(R.string.actionbar_title_mine_performance);
    }

    @Override
    public Class<? extends HolderViewItem>[] getHolderViews() {
        return new Class[]{HolderItemPerformance.class};
    }

    @Override
    public void onListItemClick(int position, PerformanceData data) {

    }

    @Override
    public void requestData(int requestPage, ListPageSubscriber subscriber) {
        CardolaApiManager.getInstance().getPerformanceList(UserManager.getInstance().getUserId(), requestPage, subscriber);
    }

    @Override
    public List<PerformanceData> convertData(GetPerformanceListResp getPerformanceListResp) {
        List<PerformanceData> datas = new ArrayList<>();
        datas.add(new PerformanceData("商家0", 1000, System.currentTimeMillis(), true, true));
        datas.add(new PerformanceData("商家1", 1001, System.currentTimeMillis(), false, true));
        datas.add(new PerformanceData("商家2", 1002, System.currentTimeMillis(), true, true));
        datas.add(new PerformanceData("商家3", 1003, System.currentTimeMillis(), true, true));
        datas.add(new PerformanceData("商家4", 1004, System.currentTimeMillis(), true, false));
        datas.add(new PerformanceData("商家5", 1005, System.currentTimeMillis(), true, true));
        datas.add(new PerformanceData("商家6", 1006, System.currentTimeMillis(), false, false));
        datas.add(new PerformanceData("商家7", 1007, System.currentTimeMillis(), true, false));
        datas.add(new PerformanceData("商家8", 1008, System.currentTimeMillis(), true, true));
        datas.add(new PerformanceData("商家9", 1009, System.currentTimeMillis(), true, true));
        return datas;
    }

}
