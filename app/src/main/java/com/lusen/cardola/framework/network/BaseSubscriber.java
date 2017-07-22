package com.lusen.cardola.framework.network;

import rx.Subscriber;

/**
 * Created by leo on 2017/7/15.
 */

public abstract class BaseSubscriber<T> extends Subscriber<T> {

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(T response) {

    }

}
