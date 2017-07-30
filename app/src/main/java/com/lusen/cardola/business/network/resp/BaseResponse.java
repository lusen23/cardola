package com.lusen.cardola.business.network.resp;

import java.io.Serializable;

/**
 * Created by leo on 2017/7/16.
 */

public class BaseResponse<T> implements Serializable {

    public static final int STATE_SUCCESS = 0;

    public int success;
    public String msg;
    public T data;

    public boolean isSuccess() {
        return success == STATE_SUCCESS;
    }

}
