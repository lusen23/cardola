package com.lusen.cardola.business.main.component;

import com.lusen.cardola.R;
import com.lusen.cardola.business.base.CardolaBaseListPageActivity;
import com.lusen.cardola.business.main.mine.data.MessageData;
import com.lusen.cardola.business.main.mine.holderitem.HolderItemMessage;
import com.lusen.cardola.business.main.user.UserManager;
import com.lusen.cardola.business.network.CardolaApiManager;
import com.lusen.cardola.business.network.resp.GetMessageListResp;
import com.lusen.cardola.framework.adapter.HolderViewItem;
import com.lusen.cardola.framework.uibase.UiModelActionBarHelper;
import com.lusen.cardola.framework.uikit.choicedialogxm.cmd.AccsDialogFactory;
import com.lusen.cardola.framework.util.ContextUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leo on 2017/7/30.
 */

public class MineMessageActivity extends CardolaBaseListPageActivity<MessageData, GetMessageListResp> {

    @Override
    protected String initActionBarTitle() {
        return ContextUtil.getContext().getResources().getString(R.string.actionbar_title_mine_message);
    }

    @Override
    public Class<? extends HolderViewItem>[] getHolderViews() {
        return new Class[]{HolderItemMessage.class};
    }

    @Override
    public void onListItemClick(int position, MessageData data) {
        AccsDialogFactory.showMessageDialog(data.mMessage);
    }

    @Override
    public void requestData(int requestPage, ListPageSubscriber subscriber) {
        CardolaApiManager.getInstance().getMessageList(UserManager.getInstance().getUserId(), requestPage, subscriber);
    }

    @Override
    public List<MessageData> convertData(GetMessageListResp getPerformanceListResp) {
        List<MessageData> datas = new ArrayList<>();
        datas.add(new MessageData(System.currentTimeMillis(), "我就讲大话啥"));
        datas.add(new MessageData(System.currentTimeMillis(), "27473678啊办事的"));
        datas.add(new MessageData(System.currentTimeMillis(), "啊啊发哈积分好看"));
        datas.add(new MessageData(System.currentTimeMillis(), "的积分兑换放假时间"));
        return datas;
    }

}
