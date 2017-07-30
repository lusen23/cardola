package com.lusen.cardola.business.network.resp;

import com.lusen.cardola.business.network.model.PerformancePo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by leo on 2017/7/30.
 */

public class GetPerformanceListResp implements Serializable {

    public List<PerformancePo> list;

}
