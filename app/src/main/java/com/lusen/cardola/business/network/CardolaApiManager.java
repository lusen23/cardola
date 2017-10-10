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

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by leo on 2017/7/15.
 */

public class CardolaApiManager {

    private static CardolaApiManager sInstance;
    private static final byte[] mLock = new byte[0];

    private static String BASE_URL = "http://huhansan.iego.cn:11778";

    private Retrofit mRetrofit;

    public static CardolaApiManager getInstance() {
        if (null == sInstance) {
            synchronized (mLock) {
                if (null != sInstance) {
                    return sInstance;
                }
                sInstance = new CardolaApiManager();
            }
        }
        return sInstance;
    }

    private CardolaApiManager() {
        init();
    }

    private void init() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)//日志拦截器
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        //Interceptor的典型使用场景，就是对request和response的Headers进行编辑
                        return chain.proceed(chain.request());
                    }
                })//网络拦截器,进行重定向等操作
                .connectTimeout(15, TimeUnit.SECONDS)//设置连接超时
                .build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())//json数据转换
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//将Callable接口转换成Observable接口
                .client(client)//网络请求客户端为okhttp
                .build();
    }

    private ICardolaApiService getService() {
        return mRetrofit.create(ICardolaApiService.class);
    }

    private <T> void execute(Observable<T> observable, Subscriber<T> subscriber) {
        observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getOrderInfoList(String userId, Subscriber<BaseResponse<HomeResp>> subscriber) {
        execute(getService().getOrderInfoList("admin", "123456", "", 2), subscriber);
    }

    public void login(String account, String password, Subscriber<BaseResponse<LoginResp>> subscriber) {
        execute(getService().login(account, password), subscriber);
    }

    public void getCustomerList(String userId, int page, Subscriber<BaseResponse<List<GetCustomerListResp>>> subscriber) {
        execute(getService().getCustomerList(userId, page), subscriber);
    }

    public void getProductAssortList(Subscriber<BaseResponse<GetProductAssortListResp>> subscriber) {
        execute(getService().getProductAssortList(), subscriber);
    }

    public void getProductAssortProductList(String assortId, Subscriber<BaseResponse<GetProductAssortProductListResp>> subscriber) {
        execute(getService().getProductAssortProductList(assortId), subscriber);
    }

    public void getProductDetail(String productId, Subscriber<BaseResponse<GetProductDetail>> subscriber) {
        execute(getService().getProductDetail(productId), subscriber);
    }

    public void getPerformanceList(String userId, int page, Subscriber<BaseResponse<GetPerformanceListResp>> subscriber) {
        execute(getService().getPerformanceList(userId, page), subscriber);
    }

    public void getMessageList(String userId, int page, Subscriber<BaseResponse<GetMessageListResp>> subscriber) {
        execute(getService().getMessageList(userId, page), subscriber);
    }

}
