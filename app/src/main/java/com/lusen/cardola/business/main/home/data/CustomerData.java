package com.lusen.cardola.business.main.home.data;

import com.lusen.cardola.business.main.home.holderitem.HolderItemCustomer;
import com.lusen.cardola.business.network.resp.GetCustomerListResp;
import com.lusen.cardola.framework.adapter.IAdapterDataViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leo on 2017/7/23.
 */

public class CustomerData implements IAdapterDataViewModel {

    public String mId;
    public String mName;

    @Override
    public Class getViewModelType() {
        return HolderItemCustomer.class;
    }

    public static List<CustomerData> convert(List<GetCustomerListResp> resp) {
        List<CustomerData> datas = new ArrayList<>();
        if (null != resp) {
            for (GetCustomerListResp po : resp) {
                if (null != po) {
                    CustomerData data = new CustomerData();
                    data.mId = po.id;
                    data.mName = po.name;
                    datas.add(data);
                }
            }
        }
        return datas;
    }

}
