package com.lusen.cardola.business.network;

import com.lusen.cardola.business.network.model.BaseResponse;
import com.lusen.cardola.business.network.resp.HomeResp;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by leo on 2017/7/15.
 */

public interface ICardolaApiService {

    @GET("admin/appLogin")
    Observable<BaseResponse<HomeResp>> getOrderInfoList(
            @Query("username") String username,
            @Query("password") String password,
            @Query("captcha") String captcha,
            @Query("userFrom") int userFrom);

}
