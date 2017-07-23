package com.lusen.cardola.business.network.resp;

import com.lusen.cardola.business.network.model.CustomerPo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by leo on 2017/7/23.
 */

public class GetCustomerListResp implements Serializable {

    public List<CustomerPo> list;

}
