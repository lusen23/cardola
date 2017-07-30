package com.lusen.cardola.business.network.resp;

import com.lusen.cardola.business.network.model.MessagePo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by leo on 2017/7/30.
 */

public class GetMessageListResp implements Serializable {

    public List<MessagePo> list;

}
