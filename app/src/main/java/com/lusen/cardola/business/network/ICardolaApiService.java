package com.lusen.cardola.business.network;

import com.lusen.cardola.business.network.resp.HomeResp;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by leo on 2017/7/15.
 */

public interface ICardolaApiService {

    @GET("X/X")
    Observable<HomeResp> getHome(@Query("id") int id);

}
