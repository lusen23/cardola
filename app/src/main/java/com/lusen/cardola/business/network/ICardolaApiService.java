package com.lusen.cardola.business.network;

import com.lusen.cardola.business.network.resp.BaseResponse;
import com.lusen.cardola.business.network.resp.GetCustomerListResp;
import com.lusen.cardola.business.network.resp.GetMessageListResp;
import com.lusen.cardola.business.network.resp.GetPerformanceListResp;
import com.lusen.cardola.business.network.resp.GetProductAssortListResp;
import com.lusen.cardola.business.network.resp.GetProductAssortProductListResp;
import com.lusen.cardola.business.network.resp.GetProductDetail;
import com.lusen.cardola.business.network.resp.HomeResp;
import com.lusen.cardola.business.network.resp.LoginResp;

import java.util.List;

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

    // 登录
    @GET("admin/appLogin")
    Observable<BaseResponse<LoginResp>> login(@Query("username") String username, @Query("password") String password);

    // 重置密码
    @GET("admin/appLogin")
    Observable<BaseResponse<LoginResp>> resetPassword(@Query("token") String token, @Query("password") String password);

    @GET("admin/appLogin")
    Observable<BaseResponse<LoginResp>> sendVerifyCode(@Query("token") String token, @Query("phone") String phone);

    // 获取商户列表
    @GET("admin/app/seller/getMerchantList")
    Observable<BaseResponse<List<GetCustomerListResp>>> getCustomerList(@Query("userId") String userId, @Query("pageNum") int page);

    // 获取商品专区列表
    @GET("admin/appLogin")
    Observable<BaseResponse<GetProductAssortListResp>> getProductAssortList();

    // 获取商品专区商品列表
    @GET("admin/appLogin")
    Observable<BaseResponse<GetProductAssortProductListResp>> getProductAssortProductList(@Query("commodityZoneId") String assortId);

    // 获取商品详情
    @GET("admin/appLogin")
    Observable<BaseResponse<GetProductDetail>> getProductDetail(@Query("goodsId") String productId);

    // 获取绩效列表
    @GET("admin/appLogin")
    Observable<BaseResponse<GetPerformanceListResp>> getPerformanceList(@Query("userId") String userId, @Query("pageNum") int page);

    // 获取消息列表
    @GET("admin/appLogin")
    Observable<BaseResponse<GetMessageListResp>> getMessageList(@Query("userId") String userId, @Query("pageNum") int page);

}
