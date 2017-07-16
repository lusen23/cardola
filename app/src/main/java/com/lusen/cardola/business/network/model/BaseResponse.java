package com.lusen.cardola.business.network.model;

import java.io.Serializable;

/**
 * Created by leo on 2017/7/16.
 */

public class BaseResponse<T> implements Serializable {

    public int success;
    public String msg;
    public T data;

}
