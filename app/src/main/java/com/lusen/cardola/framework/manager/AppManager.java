package com.lusen.cardola.framework.manager;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.lusen.cardola.framework.uibase.util.UIBaseUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by leo on 16/8/4.
 * AppManager管理器
 * >>监控App前后台状态
 * >>Activity堆栈管理
 * <p>
 * Activity创建及销毁周期说明
 * onCreate
 * onActivityCreated
 * 销毁时,存在页面已退出,但destroy方法仍未执行的情况,需要在finish时,即将Activity移除
 * onActivityDestroyed
 * onDestroy
 */
public class AppManager {

    private Set<AppStateChangedListener> mAppStateChangedListeners = new LinkedHashSet<>();
    private List<WeakReference<Activity>> mActivitys = new LinkedList<>();

    private static AppManager sInstance;
    private boolean mInited = false;
    private Application mApplication;

    private boolean mForeground = false;
    private volatile int mCount = 0;

    private Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            UIBaseUtil.log("AppManager onActivityCreated (activity) = %s", activity);
            addActivity(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
            if (mCount == 0) {
                // 切到前台
                UIBaseUtil.log("AppManager 切到前台");
                mForeground = true;
                dispatchAppStateChanged(mForeground);
            }
            mCount++;
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
            mCount--;
            if (mCount == 0) {
                // 切到后台
                UIBaseUtil.log("AppManager 切到后台");
                mForeground = false;
                dispatchAppStateChanged(mForeground);
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            UIBaseUtil.log("AppManager onActivityDestroyed (activity) = %s", activity);
            removeActivity(activity);
        }
    };

    private AppManager() {
    }

    public static AppManager getInstance() {
        if (null == sInstance) {
            synchronized (AppManager.class) {
                if (null == sInstance) {
                    sInstance = new AppManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 初始化
     *
     * @param application application对象
     */
    public void init(Application application) {
        if (null == application)
            throw new RuntimeException("AppManager init application must not be null");
        if (!mInited) {
            mInited = true;
            mApplication = application;
            application.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
        }
    }

    public void registerActivityLifecycleCallback(Application.ActivityLifecycleCallbacks callback) {
        if (null != mApplication && null != callback) {
            mApplication.registerActivityLifecycleCallbacks(callback);
        }
    }

    public void unregisterActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback) {
        if (null != mApplication && null != callback) {
            mApplication.unregisterActivityLifecycleCallbacks(callback);
        }
    }

    /**
     * 注册App监听器
     *
     * @param listener 监听器
     */
    public void registerAppListener(AppStateChangedListener listener) {
        if (null != listener) {
            mAppStateChangedListeners.add(listener);
        }
    }

    /**
     * 注销App监听器
     *
     * @param listener 监听器
     */
    public void unregisterAppListener(AppStateChangedListener listener) {
        if (null != listener) {
            mAppStateChangedListeners.remove(listener);
        }
    }

    /**
     * 获取所有Activity
     *
     * @return 所有Activity
     */
    public List<Activity> getAllActivity() {
        List<Activity> activities = new ArrayList<>();
        if (null != mActivitys) {
            for (WeakReference<Activity> weakReference : mActivitys) {
                Activity refActivity = weakReference.get();
                if (null != refActivity) {
                    activities.add(refActivity);
                }
            }
        }
        return activities;
    }

    /**
     * 获取Activity索引
     *
     * @param activity activity对象
     * @return activity索引
     */
    public int getActivityIndex(Activity activity) {
        int index = -1;
        if (null != activity) {
            List<Activity> activityList = getAllActivity();
            if (null != activityList) {
                index = activityList.indexOf(activity);
            }
        }
        return index;
    }

    /**
     * 获取Activity通过索引值
     *
     * @param index 索引值
     * @return activity对象
     */
    public Activity getActivityByIndex(int index) {
        Activity activity = null;
        if (index >= 0) {
            List<Activity> activityList = getAllActivity();
            if (null != activityList && index < activityList.size()) {
                activity = activityList.get(index);
            }
        }
        return activity;
    }

    /**
     * 获取Activity总数
     *
     * @return activity总数
     */
    public int getActivityCount() {
        List<Activity> activityList = getAllActivity();
        if (null != activityList) {
            return activityList.size();
        }
        return 0;
    }

    /**
     * 获取Activity通过偏移量
     *
     * @param referActivity activity参照对象
     * @param offset        偏移量
     * @return activity对象
     */
    public Activity getActivityByOffset(Activity referActivity, int offset) {
        Activity activity = null;
        int referIndex = getActivityIndex(referActivity);
        if (referIndex >= 0) {
            referIndex += offset;
            activity = getActivityByIndex(referIndex);
        }
        return activity;
    }

    /**
     * 获取当前Activity
     *
     * @return 当前activity
     */
    public Activity getCurrentActivityIfExist() {
        return getCurrentActivityIfExistIgnoreFinishing();
    }

    /**
     * 获取当前Activity(忽略正在finish中的Activity)
     *
     * @return 当前Activity
     */
    private Activity getCurrentActivityIfExistIgnoreFinishing() {
        if (null != mActivitys && !mActivitys.isEmpty()) {
            int count = mActivitys.size();
            for (int index = count - 1; index >= 0; index--) {
                WeakReference<Activity> weakReference = mActivitys.get(index);
                Activity refActivity = weakReference.get();
                // 忽略finishing状态Activity
                if (null == refActivity || refActivity.isFinishing()) {
                    continue;
                }
                return refActivity;
            }
        }
        return null;
    }

    /**
     * Activity是否存在
     *
     * @param activity activity对象
     * @return 是否存在
     */
    public boolean isActivityExist(Activity activity) {
        WeakReference<Activity> weakReference = getWeakActivityIfExist(activity);
        return (null != weakReference ? true : false);
    }

    /**
     * 是否当前Activity
     *
     * @param activity activity对象
     * @return 是否当前activity
     */
    public boolean isCurrentActivity(Activity activity) {
        boolean result = false;
        if (null != activity) {
            Activity curActivity = getCurrentActivityIfExist();
            if (null != curActivity && curActivity == activity) {
                result = true;
            }
        }
        return result;
    }

    /**
     * Activity是否命中
     *
     * @param targetActivity Activity目标类
     * @param activity       Activity对象
     * @param inherit        继承性
     * @return 是否命中
     */
    public boolean isActivityHit(Class<? extends Activity> targetActivity, Activity activity, boolean inherit) {
        if (null != targetActivity && null != activity) {
            if (inherit) {
                return targetActivity.isAssignableFrom(activity.getClass());
            } else {
                return targetActivity == activity.getClass();
            }
        }
        return false;
    }

//    /**
//     * 寻找指定Activity类实例
//     *
//     * @param activityClazz activity类
//     * @param inherit       继承性
//     * @param fromTop       顶部寻找
//     * @return 结果activity
//     */
//    public <T extends Activity> T findActivity(Class<T> activityClazz, boolean inherit, boolean fromTop) {
//        T activity = null;
//        if (null != activityClazz) {
//            List<Activity> activityList = getAllActivity();
//            int count = activityList.size();
//            if (fromTop) {
//                for (int index = count - 1; index >= 0; index--) {
//                    Activity findActivity = activityList.get(index);
//                    boolean isHit = isActivityHit(activityClazz, findActivity, inherit);
//                    if (isHit) {
//                        activity = (T) findActivity;
//                        break;
//                    }
//                }
//            } else {
//                for (int index = 0; index < count; index++) {
//                    Activity findActivity = activityList.get(index);
//                    boolean isHit = isActivityHit(activityClazz, findActivity, inherit);
//                    if (isHit) {
//                        activity = (T) findActivity;
//                        break;
//                    }
//                }
//            }
//        }
//        return activity;
//    }

    /**
     * App是否处于前台
     *
     * @return 是否处于前台
     */
    public boolean isAppInForeground() {
        return mForeground;
    }

    /**
     * 获取application
     *
     * @return application对象
     */
    public Application getApplication() {
        return mApplication;
    }

    private void dispatchAppStateChanged(boolean foreground) {
        for (AppStateChangedListener listener : mAppStateChangedListeners) {
            if (null != listener) {
                if (foreground) {
                    listener.onAppInForeground();
                } else {
                    listener.onAppInBackground();
                }
            }
        }
    }

    public void addActivity(Activity activity) {
        if (null != getWeakActivityIfExist(activity))
            return;
        WeakReference<Activity> weakReference = new WeakReference<>(activity);
        mActivitys.add(weakReference);
    }

    public void removeActivity(Activity activity) {
        WeakReference<Activity> weakReference = getWeakActivityIfExist(activity);
        if (null == weakReference)
            return;
        mActivitys.remove(weakReference);
    }

    private WeakReference<Activity> getWeakActivityIfExist(Activity activity) {
        if (null == activity || mActivitys.isEmpty())
            return null;
        for (WeakReference<Activity> weakReference : mActivitys) {
            if (null != weakReference) {
                Activity refActivity = weakReference.get();
                if (null != refActivity && refActivity == activity) {
                    return weakReference;
                }
            }
        }
        return null;
    }

    public interface AppStateChangedListener {
        void onAppInForeground();

        void onAppInBackground();
    }

}