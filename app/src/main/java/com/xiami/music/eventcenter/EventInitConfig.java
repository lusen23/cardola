package com.xiami.music.eventcenter;

import org.greenrobot.eventbus.meta.SubscriberInfoIndex;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created by leo on 16/6/15.
 * 初始化配置
 */
public class EventInitConfig {

    /**
     * EventCenter底层框架可支持的参数设置
     */
    public Boolean logSubscriberExceptions;
    public Boolean logNoSubscriberMessages;
    public Boolean sendSubscriberExceptionEvent;
    public Boolean sendNoSubscriberEvent;
    public Boolean throwSubscriberException;
    public Boolean eventInheritance;
    public Boolean ignoreGeneratedIndex;
    public Boolean strictMethodVerification;
    public ExecutorService executorService;
    public List<SubscriberInfoIndex> subscriberInfoIndexes;

}
