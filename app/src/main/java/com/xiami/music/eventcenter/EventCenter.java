package com.xiami.music.eventcenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusBuilder;
import org.greenrobot.eventbus.meta.SubscriberInfoIndex;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created by leo on 16/6/6.
 * EventCenter消费分发
 * 提供订阅对象注册/注销、事件发布(实时及粘性)
 */
public class EventCenter {

    private static EventCenter sInstance;
    private static final byte[] mLock = new byte[0];
    private EventBus mEventBus;

    /**
     * 配置
     */
    private static EventInitConfig mEventInitConfig;

    /**
     * 获取EventCenter实例
     *
     * @return EventCenter实例
     */
    public static EventCenter getInstance() {
        if (null == sInstance) {
            //增加同步锁，避免多线程同时调用实例化多个，导致覆盖之前的实例
            synchronized (mLock) {
                if (null != sInstance) {
                    return sInstance;
                }
                sInstance = new EventCenter(mEventInitConfig);
            }
        }
        return sInstance;
    }

    /**
     * 设置自定义配置(未设置,则初始化时,采用默认)
     *
     * @param eventInitConfig 配置
     */
    public static void setEventInitConfig(EventInitConfig eventInitConfig) {
        mEventInitConfig = eventInitConfig;
    }

    /**
     * 默认构造器
     *
     * @param eventInitConfig 配置
     */
    private EventCenter(EventInitConfig eventInitConfig) {
        initInternal(eventInitConfig);
    }

    /**
     * 内部初始化(根据InitConfig初始化各种EventCenter行为)
     *
     * @param eventInitConfig 配置
     */
    private void initInternal(EventInitConfig eventInitConfig) {
        Boolean logSubscriberExceptions = null;
        Boolean logNoSubscriberMessages = null;
        Boolean sendSubscriberExceptionEvent = null;
        Boolean sendNoSubscriberEvent = null;
        Boolean throwSubscriberException = null;
        Boolean eventInheritance = null;
        Boolean ignoreGeneratedIndex = null;
        Boolean strictMethodVerification = null;
        ExecutorService executorService = null;
        List<SubscriberInfoIndex> subscriberInfoIndexes = null;
        if (null != eventInitConfig) {
            logSubscriberExceptions = eventInitConfig.logSubscriberExceptions;
            logNoSubscriberMessages = eventInitConfig.logNoSubscriberMessages;
            sendSubscriberExceptionEvent = eventInitConfig.sendSubscriberExceptionEvent;
            sendNoSubscriberEvent = eventInitConfig.sendNoSubscriberEvent;
            throwSubscriberException = eventInitConfig.throwSubscriberException;
            eventInheritance = eventInitConfig.eventInheritance;
            ignoreGeneratedIndex = eventInitConfig.ignoreGeneratedIndex;
            strictMethodVerification = eventInitConfig.strictMethodVerification;
            executorService = eventInitConfig.executorService;
            subscriberInfoIndexes = eventInitConfig.subscriberInfoIndexes;
        }

        EventLogger.log("event center init ... ");
        EventLogger.log("logSubscriberExceptions = " + logSubscriberExceptions);
        EventLogger.log("logNoSubscriberMessages = " + logNoSubscriberMessages);
        EventLogger.log("sendSubscriberExceptionEvent = " + sendSubscriberExceptionEvent);
        EventLogger.log("sendNoSubscriberEvent = " + sendNoSubscriberEvent);
        EventLogger.log("throwSubscriberException = " + throwSubscriberException);
        EventLogger.log("eventInheritance = " + eventInheritance);
        EventLogger.log("ignoreGeneratedIndex = " + ignoreGeneratedIndex);
        EventLogger.log("strictMethodVerification = " + strictMethodVerification);
        EventLogger.log("executorService = " + executorService);
        EventLogger.log("subscriberInfoIndexes = " + subscriberInfoIndexes);

        EventBusBuilder builder = EventBus.builder();
        if (null != logSubscriberExceptions) {
            builder.logSubscriberExceptions(logSubscriberExceptions);
        }
        if (null != logNoSubscriberMessages) {
            builder.logNoSubscriberMessages(logNoSubscriberMessages);
        }
        if (null != sendSubscriberExceptionEvent) {
            builder.sendSubscriberExceptionEvent(sendSubscriberExceptionEvent);
        }
        if (null != sendNoSubscriberEvent) {
            builder.sendNoSubscriberEvent(sendNoSubscriberEvent);
        }
        if (null != throwSubscriberException) {
            builder.throwSubscriberException(throwSubscriberException);
        }
        if (null != eventInheritance) {
            builder.eventInheritance(eventInheritance);
        }
        if (null != ignoreGeneratedIndex) {
            builder.ignoreGeneratedIndex(ignoreGeneratedIndex);
        }
        if (null != strictMethodVerification) {
            builder.strictMethodVerification(strictMethodVerification);
        }
        if (null != executorService) {
            builder.executorService(executorService);
        }
        if (null != subscriberInfoIndexes) {
            for (SubscriberInfoIndex subscriberInfoIndex : subscriberInfoIndexes) {
                if (null != subscriberInfoIndex) {
                    builder.addIndex(subscriberInfoIndex);
                }
            }
        }
        mEventBus = builder.build();
    }

    /**
     * 注册订阅对象
     * 会将订阅对象(包含各级父类)内的所有方法(Public+非Abstract+非Static+Subscribe)进行注册
     * 唯一方法维度:method名+param名不一致即可;如一致,说明存在继承重写关系,需要判断isAssignableFrom
     * <p>
     * clazz.getDeclaredMethods方法:返回当前clazz内所有申明方法,不包含父类
     * clazz.getMethods方法:返回当前clazz及各级父类内所有Public方法
     *
     * @param subscriber 订阅对象
     * @return 是否成功
     */
    public boolean register(Object subscriber) {
        boolean result = false;
        if (null != subscriber) {
            try {
                mEventBus.register(subscriber);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        EventLogger.log("register (subscriber,result) = " + subscriber + "," + result);
        return result;
    }

    /**
     * 注销订阅对象
     *
     * @param subscriber 订阅对象
     * @return 是否成功
     */
    public boolean unregister(Object subscriber) {
        boolean result = false;
        if (null != subscriber) {
            try {
                mEventBus.unregister(subscriber);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        EventLogger.log("unregister (subscriber,result) = " + subscriber + "," + result);
        return result;
    }

    /**
     * 订阅对象是否已注册
     *
     * @param subscriber 订阅对象
     * @return 是否已注册
     */
    public boolean isRegistered(Object subscriber) {
        boolean result = false;
        if (null != subscriber) {
            result = mEventBus.isRegistered(subscriber);
        }
        EventLogger.log("isRegistered (subscriber,result) = " + subscriber + "," + result);
        return result;
    }

    /**
     * 发布实时事件
     * 事件继承原则:指的是原始Event(各级父类、接口)都作为集合列表参与EventClazz遍历发布,默认打开
     * 事件执行时序规则:Clazz严格匹配>>priority;注:具体event method在类中的顺序和执行顺序无直接关系
     *
     * @param event 事件
     * @return 是否成功
     */
    public boolean post(Object event) {
        boolean result = false;
        if (null != event) {
            try {
                mEventBus.post(event);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        EventLogger.log("post (event,result) = " + event + "," + result);
        return result;
    }

    /**
     * 发布粘性事件(原理同{@link #post(Object)})
     * 粘性事件发布,只作用在sticky=true的event method
     * 粘性事件,发布后,会一直常驻,可以在调用{@link #register(Object)}时,进行检查触发
     * 在{@link #register(Object)}中被触发的sticky event,是直接执行post,不受priority等影响
     *
     * @param event 事件
     * @return 是否成功
     */
    public boolean postSticky(Object event) {
        boolean result = false;
        if (null != event) {
            try {
                mEventBus.postSticky(event);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        EventLogger.log("postSticky (event,result) = " + event + "," + result);
        return result;
    }

    /**
     * 取消Event事件传递(一般用于高优先级取消Event传递)
     * 使用条件:event method必须是ThreadMode.POSTING的方法体内
     * (为何受限于POSTING模式? 因为只有保证发布+处理在同一个线程中,才能使用LocalThread去设置cancel标志位)
     *
     * @param event 事件
     * @return 是否成功
     */
    public boolean cancelEventDelivery(Object event) {
        boolean result = false;
        if (null != event) {
            try {
                mEventBus.cancelEventDelivery(event);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        EventLogger.log("cancelEventDelivery (event,result) = " + event + "," + result);
        return result;
    }

    /**
     * 获取粘性事件
     *
     * @param eventType 事件类型
     * @param <T>       事件泛型
     * @return 事件
     */
    public <T> T getStickyEvent(Class<T> eventType) {
        T result = null;
        if (null != eventType) {
            result = mEventBus.getStickyEvent(eventType);
        }
        EventLogger.log("getStickyEvent (eventType,result) = " + eventType + "," + result);
        return result;
    }

    /**
     * 是否存在订阅特定事件类型的订阅对象(考虑事件继承原则)
     *
     * @param eventType 事件类型
     * @return 是否存在
     */
    public boolean hasSubscriberForEvent(Class eventType) {
        boolean result = false;
        if (null != eventType) {
            result = mEventBus.hasSubscriberForEvent(eventType);
        }
        EventLogger.log("hasSubscriberForEvent (eventType,result) = " + eventType + "," + result);
        return result;
    }

    /**
     * 移除所有粘性事件
     */
    public void removeAllStickyEvents() {
        EventLogger.log("removeAllStickyEvents");
        mEventBus.removeAllStickyEvents();
    }

    /**
     * 移除粘性事件对象
     *
     * @param event 事件
     * @return 是否成功
     */
    public boolean removeStickyEvent(Object event) {
        boolean result = false;
        if (null != event) {
            result = mEventBus.removeStickyEvent(event);
        }
        EventLogger.log("removeStickyEvent (event,result) = " + event + "," + result);
        return result;
    }

    /**
     * 移除粘性事件类型的对象
     *
     * @param eventType 事件类型
     * @param <T>       事件泛型
     * @return 事件
     */
    public <T> T removeStickyEvent(Class<T> eventType) {
        T result = null;
        if (null != eventType) {
            result = mEventBus.removeStickyEvent(eventType);
        }
        EventLogger.log("removeStickyEvent (eventType,result) = " + eventType + "," + result);
        return result;
    }

}
