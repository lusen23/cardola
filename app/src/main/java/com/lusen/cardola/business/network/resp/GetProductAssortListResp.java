package com.lusen.cardola.business.network.resp;

import com.lusen.cardola.business.network.model.ProductAssortPo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by leo on 2017/7/23.
 */

public class GetProductAssortListResp implements Serializable {

    public List<ProductAssortPo> list;

}
