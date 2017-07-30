package com.lusen.cardola.business.network;

import com.lusen.cardola.business.network.resp.BaseResponse;
import com.lusen.cardola.business.network.resp.GetCustomerListResp;
import com.lusen.cardola.business.network.resp.GetProductAssortResp;
import com.lusen.cardola.business.network.resp.HomeResp;
import com.lusen.cardola.business.network.resp.LoginResp;

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

    @GET("admin/appLogin")
    Observable<BaseResponse<LoginResp>> login(@Query("username") String username, @Query("password") String password);

    @GET("admin/appLogin")
    Observable<BaseResponse<LoginResp>> resetPassword(@Query("token") String token, @Query("password") String password);

    @GET("admin/appLogin")
    Observable<BaseResponse<LoginResp>> sendVerifyCode(@Query("token") String token, @Query("phone") String phone);

    @GET("admin/appLogin")
    Observable<BaseResponse<GetCustomerListResp>> getCustomerList(@Query("token") String token);

    @GET("admin/appLogin")
    Observable<BaseResponse<GetProductAssortResp>> getProductAssort();

}
