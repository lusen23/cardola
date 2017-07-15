package com.lusen.cardola.business.network;

import com.lusen.cardola.business.network.resp.HomeResp;

import java.io.IOException;
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

    private static String BASE_URL = "https://api.heweather.com";

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

    private class ComposeThread<T> implements Observable.Transformer<T, T> {
        @Override
        public Observable<T> call(Observable<T> observable) {
            return observable
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }

    /**
     * 首页信息
     *
     * @param id
     * @param subscriber
     */
    public void getHome(int id, Subscriber subscriber) {
        getService().getHome(id).compose(new ComposeThread<HomeResp>()).subscribe(subscriber);
    }

}
