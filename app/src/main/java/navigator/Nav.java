package navigator;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.AnimRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.SparseArray;

import com.lusen.cardola.framework.manager.AppManager;
import com.lusen.cardola.framework.manager.JumperManager;
import com.lusen.cardola.framework.util.ToastUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import navigator.err.NavErrHandler;
import navigator.err.NavErrReason;
import navigator.extend.NavSwitcherMemoryCache;
import navigator.hook.NavHook;
import navigator.hook.NavHookPriorityQueue;
import navigator.param.NavParam;
import navigator.param.NavParamParser;
import navigator.param.NavUrlBuilder;
import navigator.preprocessor.NavPreprocessor;
import navigator.util.NavLogger;

/**
 * Created by leo on 16/7/13.
 * SchemeUrl管理器
 * 用于SchemeUrl导航逻辑
 */
public class Nav {


    /**
     * @param url 比如服务器下发的url
     * @return Nav
     */
    public static Nav fromUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return new NullNav();
        }

        final Nav nav = new Nav();
        nav.mNavUri = Uri.parse(url).buildUpon();
        return nav;
    }

    /**
     * @param host 要去的native host
     * @return Nav
     */
    public static Nav fromHost(String host) {
        if (TextUtils.isEmpty(host)) {
            return new NullNav();
        }

        Nav nav = new Nav();
        nav.mNavUri = new Uri.Builder().scheme(Nav.SCHEME_DEFAULT).authority(host);
        return nav;
    }

    public Nav scheme(String scheme) {
        mNavUri.scheme(scheme);
        return this;
    }

    public Nav param(String key, String value) {
        mNavUri.appendQueryParameter(key, value);
        return this;
    }

    public Nav param(String key, Number value) {
        // TODO: xxp 2017/3/31 不丢失类型才加的。 与下面的uri是重复的。后面有优先级，所以没问题，后续需要优化
        if (value instanceof Long) {
            mBundle.putLong(key, (Long) value);
        } else if (value instanceof Integer) {
            mBundle.putInt(key, (Integer) value);
        }

        mNavUri.appendQueryParameter(key, String.valueOf(value));
        return this;
    }

    public Nav param(String key, boolean value) {
        // TODO: xxp 2017/3/31 不丢失类型才加的。 与下面的uri是重复的。后面有优先级，所以没问题，后续需要优化
        mBundle.putBoolean(key, value);

        mNavUri.appendQueryParameter(key, String.valueOf(value));
        return this;
    }

    /**
     * 兼容性api，会破坏页面动态性, 不推荐使用
     */
    @Deprecated
    public Nav param(String key, Parcelable value) {
        mBundle.putParcelable(key, value);
        return this;
    }

    /**
     * 兼容性api，会破坏页面动态性, 不推荐使用
     */
    @Deprecated
    public Nav param(String key, Serializable value) {
        mBundle.putSerializable(key, value);
        return this;
    }

    /**
     * 兼容性api，会破坏页面动态性, 不推荐使用
     */
    @Deprecated
    public Nav param(String key, Parcelable[] value) {
        mBundle.putParcelableArray(key, value);
        return this;
    }

    /**
     * 兼容性api，会破坏页面动态性, 不推荐使用
     */
    @Deprecated
    public Nav param(String key, ArrayList<? extends Parcelable> value) {
        mBundle.putParcelableArrayList(key, value);
        return this;
    }

    /**
     * 兼容性api，会破坏页面动态性, 不推荐使用
     */
    @Deprecated
    public Nav param(String key, SparseArray<? extends Parcelable> value) {
        mBundle.putSparseParcelableArray(key, value);
        return this;
    }

    public Nav segment(String segment) {
        mNavUri.appendEncodedPath(segment);
        return this;
    }

    public Nav segment(int segment) {
        mNavUri.appendEncodedPath(String.valueOf(segment));
        return this;
    }

    public Nav segment(long segment) {
        mNavUri.appendEncodedPath(String.valueOf(segment));
        return this;
    }

    public Nav path(String path) {
        mNavUri.path(path);
        return this;
    }

    /**
     * 不要有序列化对象
     */
    public Nav withExtras(Bundle value) {
        mBundle.putAll(value);
        return this;
    }

    /**
     * like  {@link Intent#FLAG_ACTIVITY_CLEAR_TOP}
     */
    public Nav withFlags(int flag) {
        mIntent.setFlags(flag);
        return this;
    }

    /**
     * like  {@link Intent#CATEGORY_BROWSABLE}
     */
    public Nav withCategory(String category) {
        mIntent.addCategory(category);
        return this;
    }

    /**
     * 设置  {@link Activity#startActivityForResult(Intent, int)} 的第二个参数。
     * 用于在{@link Activity#onActivityResult(int, int, Intent)} 中获取跳转页面的返回值。
     */
    public Nav forResult(int requestCode) {
        mRequestCode = requestCode;
        return this;
    }

    /**
     * 设置动画
     */
    public Nav setTransition(@AnimRes int enterAnim, @AnimRes int exitAnim) {
        mTransition = new int[2];
        mTransition[0] = enterAnim;
        mTransition[1] = exitAnim;
        return this;
    }

    /**
     * 关闭activity动画
     */
    public Nav disableTransition() {
        mDisableTransition = true;
        return this;
    }

    /**
     * 跳过navigator 钩子。
     */
    public Nav skipHooker() {
        mSkipHooker = true;
        return this;
    }

    /**
     * 跳过navigator 预处理
     */
    public Nav skipPreprocessor() {
        mSkipPreprocessor = true;
        return this;
    }

    /**
     * 允许外跳其他app
     */
    public Nav allowEscape() {
        mAllowLeaving = true;
        return this;
    }

    public Uri getUri() {
        final NavParam navParam = new NavParam(mNavUri.build().toString());
        navParam.mBundleData = mBundle;

        return new NavParamParser(navParam).getFullUri();
    }

    /**
     * 触发
     */
    public boolean nav() {
        long start = SystemClock.elapsedRealtime();

        if (NavLogger.mEnable) {
            NavLogger.log("---------------  navigate %s  ---------------", TAG);
        }
        //参数组装。
        final NavParam navParam = new NavParam(mNavUri.build().toString());
        navParam.mBundleData = mBundle;

        //触发导航逻辑
        boolean success = navigate(navParam);

        //耗时日志
        if (NavLogger.mEnable) {
            NavLogger.log("<===  navigate cost %d, success = " + success, (SystemClock.elapsedRealtime() - start));
        }

        return success;
    }

    /**
     * 导航
     *
     * @param url 源url
     */
    @Deprecated
    public boolean navigate(String url) {
        return navigate(new NavParam(url));
    }

    /**
     * 导航
     *
     * @param navParam 导航实体
     */
    @Deprecated
    public boolean navigate(NavParam navParam) {
        if (navParam == null) {
            onNavigateErr(navParam, NavErrReason.RESULT_ERROR_ENTITY_INVALID);
            return false;
        }

        if (NavLogger.mEnable) {
            NavLogger.log("navigate (begin...) = %s", navParam);
        }

        if (null == navParam.mUri) {
            onNavigateErr(navParam, NavErrReason.RESULT_ERROR_URI_INVALID);
            return false;
        }

        // 导航前,尝试进行干预
        if (null != sPreprocessorHashSet) {
            for (NavPreprocessor preprocessor : sPreprocessorHashSet) {
                if (NavLogger.mEnable) {
                    NavLogger.log("navigate (preprocesssor before) = %s", navParam);
                }

                boolean handleByPreprocessor = preprocessor.beforeNavTo(navParam);

                if (NavLogger.mEnable) {
                    NavLogger.log("navigate (preprocesssor end...) and is handle by preprocessor=%b", handleByPreprocessor);
                }

                if (handleByPreprocessor) {
                    return false;
                }
            }
        }

        // 导航
        boolean navSuccess = innerNavigate(navParam);
        if (NavLogger.mEnable) {
            NavLogger.log("navigate end...(entity) = %s", navParam);
        }

        return navSuccess;
    }

    private boolean innerNavigate(NavParam navParam) {
        final NavParamParser navParamParser = new NavParamParser(navParam);

        // 遍历匹配hook
        if (!mSkipHooker) {
            Set<NavHook> queue = sNavHookPriorityQueue.getQueue();
            for (NavHook hook : queue) {
                // 匹配命中hook
                if (hook.canHook(navParam.mUri)) {

                    boolean hooked = hook.hook(navParamParser);
                    if (NavLogger.mEnable) {
                        NavLogger.log("navigate (hook) = **[ %s ]**", hook.getClass().getSimpleName());
                    }

                    if (hooked) {
                        return true;
                    } else {
                        onNavigateErr(navParam, NavErrReason.RESULT_NAV_REFUSE);
                        return false;
                    }
                }
            }
        }

        mIntent.setData(navParam.mUri);
        mIntent.putExtras(navParamParser.getNavParamParserResult().getParams());

        try {
            if (NavLogger.mEnable) {
                NavLogger.log("navigate (resolver) begin");
            }

            //外跳intent
            if (mAllowLeaving && !sSupportedSchemes.contains(navParam.mUri.getScheme())) {
                sendSchemeUrl2System(AppManager.getInstance().getApplication(), navParam.mUri);
                onNavigateErr(navParam, NavErrReason.RESULT_ERROR_SCHEME_INVALID);
                return false;
            }

            //设置intent的package
            mIntent.setPackage(mContext.getPackageName());

            //intent匹配activity的逻辑, 匹配后，设置className等信息
            final ResolveInfo info = mNavResolver.resolveActivity(mContext.getPackageManager(), mIntent,
                    PackageManager.MATCH_DEFAULT_ONLY);
            ResolveInfo finalInfo = info;
            if (info == null || "com.android.internal.app.ResolverActivity".equals(info.activityInfo.name)) {
                //for some special permission-check phone, resolveActivity will return null.
                //so we use queryIntentActivities to check again.
                final List<ResolveInfo> list = mNavResolver.queryIntentActivities(mContext.getPackageManager(), mIntent,
                        PackageManager.MATCH_DEFAULT_ONLY);

                final ResolveInfo rinfo = optimum(list);
                if (rinfo == null) {
                    throw new ActivityNotFoundException("No Activity found to handle " + mIntent);
                }
                finalInfo = rinfo;
            }

            mIntent.setClassName(finalInfo.activityInfo.packageName, finalInfo.activityInfo.name);

            if (NavLogger.mEnable) {
                NavLogger.log("navigate (resolver) end : " + finalInfo.activityInfo.name);
            }

            //启动activity
            boolean jumperResult = false;
            if (mRequestCode != null) {
                jumperResult = JumperManager.launchActivityForResult((Activity) mContext, mIntent, mRequestCode);
            } else {
                if (!(mContext instanceof Activity)) {
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }

                jumperResult = JumperManager.launchActivity(mIntent);
            }

            if (!mDisableTransition && mTransition != null && mContext instanceof Activity) {
                ((Activity) mContext).overridePendingTransition(mTransition[0], mTransition[1]);
            }

            if (!jumperResult) {
                onNavigateErr(navParam, NavErrReason.RESULT_ERROR_JUMPER_FAILED);
            }

            return jumperResult;
        } catch (Exception e) {
            e.printStackTrace();
            onNavigateErr(navParam, NavErrReason.RESULT_REGISTER_NOT_FIND);
        }

        return false;
    }

    void onNavigateErr(NavParam navParam, int failedReason) {
        final NavErrReason navErrReason = new NavErrReason(failedReason);

        if (sNavErrHandler != null) {
            sNavErrHandler.onErr(navParam, navErrReason);
        }

        if (NavLogger.mEnable) {
            NavLogger.log("navigate ERROR, %s", navErrReason.toString());
            ToastUtil.toast("Nav ERROR:\n" + navErrReason.toString() + "\nparam:\n" + navParam.toString());
        }
    }

    /**
     * 发送SchemeUrl
     *
     * @param context context上下文
     * @param uri     uri资源
     * @return 是否成功
     */
    public static boolean sendSchemeUrl2System(Context context, Uri uri) {
        boolean result = false;
        if (null != uri) {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            result = JumperManager.launchActivity(context, intent);
        }

        if (NavLogger.mEnable) {
            NavLogger.log("sendSchemeUrl2System (result,context,uri) = %s,%s,%s", result, context, uri);
        }

        return result;
    }

    /**
     * 发送SchemeUrl
     *
     * @param context context上下文
     * @param url     url资源
     * @return 是否成功
     */
    public static boolean sendSchemeUrl2System(Context context, String url) {
        if (null != url) {
            return sendSchemeUrl2System(context, Uri.parse(url));
        }
        return false;
    }

    public static void setNavErrHandler(NavErrHandler navErrHandler) {
        sNavErrHandler = navErrHandler;
    }

    /**
     * 注册导航器
     *
     * @param hook 导航注册器
     * @return 是否成功
     */
    public static void registerNavHook(NavHook hook) {
        if (hook == null) {
            if (NavLogger.mEnable) {
                NavLogger.log("%s register failure since null", TAG);
            }
            return;
        }

        sNavHookPriorityQueue.add(hook);

        if (NavLogger.mEnable) {
            NavLogger.log("%s register (failure#NullOrInvaild) = %s", TAG, hook);
        }
    }

    /**
     * 注销导航器
     *
     * @param hook 导航注册器
     * @return 是否成功
     */
    public static void unregisterHook(@Nullable NavHook hook) {
        if (hook == null) {
            if (NavLogger.mEnable) {
                NavLogger.log("%s register failure since null", TAG);
            }
            return;
        }

        sNavHookPriorityQueue.remove(hook);
    }

    /**
     * 设置UriConverter用于Uri转换
     *
     * @param navPreprocessor uri转换器
     */
    public static void addNavPreprocessor(NavPreprocessor navPreprocessor) {
        sPreprocessorHashSet.add(navPreprocessor);
    }

    public static void removeNavPreprocessor(NavPreprocessor navPreprocessor) {
        sPreprocessorHashSet.remove(navPreprocessor);
    }

    /**
     * 尝试进行源url转换,用于url动态切换
     *
     * @param origin 源url
     * @param bundle 参数bundle
     * @return 转换后url, 如果null, 则表示未进行转换, 否则表示转换后url
     */
    public static String getSwitcher(String origin, Bundle bundle, boolean switchWithBundle) {
        if (NavLogger.mEnable) {
            NavLogger.log("%s trySwitcher (origin,bundle,switchWithBundle) = %s,%s,%s", TAG, origin, bundle, switchWithBundle);
        }

        String target = null;
        String switchOrigin = origin;

        // 带参数的转换,适用于源origin已无法满足单一匹配性,如一些统一页面,origin是一致的,是通过参数部分来识别的
        if (switchWithBundle) {
            if (null != bundle) {
                try {
                    NavUrlBuilder builder = new NavUrlBuilder(origin);
                    builder.addParam(bundle);
                    switchOrigin = builder.build();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // 从映射表中,尝试进行转换
        String switchResult = NavSwitcherMemoryCache.get(switchOrigin);

        if (NavLogger.mEnable) {
            NavLogger.log("%s trySwitcher (switchOrigin,switchResult) = %s,%s", TAG, switchOrigin, switchResult);
        }

        target = switchResult;
        if (null != switchResult) {
            try {
                NavUrlBuilder builder = new NavUrlBuilder(switchResult);
                builder.addParam(bundle);
                target = builder.build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (NavLogger.mEnable) {
            NavLogger.log("%s trySwitcher (target) = %s", TAG, target);
        }

        return target;
    }

    /**
     * Outer resolver provider for every navigation attempts
     */
    public interface NavResolver {

        /**
         * Provider outer resolver.
         *
         * @return {@link ResolveInfo} list.
         */
        List<ResolveInfo> queryIntentActivities(PackageManager pm, Intent intent, int flags);

        /**
         * Provider outer resolver.
         *
         * @return {@link ResolveInfo}.
         */
        ResolveInfo resolveActivity(PackageManager pm, Intent intent, int flags);
    }

    private final static class DefaultResolver implements NavResolver {

        @Override
        public List<ResolveInfo> queryIntentActivities(final PackageManager pm, final Intent intent, final int flags) {
            return pm.queryIntentActivities(intent, flags);
        }

        @Override
        public ResolveInfo resolveActivity(final PackageManager pm, final Intent intent, final int flags) {
            return pm.resolveActivity(intent, flags);
        }
    }

    private final class SortedResolveInfo implements Comparable<SortedResolveInfo> {

        public SortedResolveInfo(final ResolveInfo info, final int weight, final int same) {
            this.info = info;
            this.weight = weight;
            this.same = same;
        }

        private final ResolveInfo info;
        private int weight = 0;
        private int same = 0;

        @Override
        public int compareTo(final SortedResolveInfo other) {
            if (this == other)
                return 0;

            // order descending by priority
            if (other.weight != this.weight)
                return other.weight - this.weight;
                // order descending by same package
            else if (other.same != this.same)
                return other.same - this.same;
                // then randomly
            else if (System.identityHashCode(this) < System.identityHashCode(other))
                return -1;
            else
                return 1;
        }
    }

    private ResolveInfo optimum(final List<ResolveInfo> list) {

        if (list == null)
            return null;
        else if (list.size() == 1) {
            return list.get(0);
        }

        final ArrayList<SortedResolveInfo> resolveInfo = new ArrayList<SortedResolveInfo>();

        for (final ResolveInfo info : list) {

            if (!TextUtils.isEmpty(info.activityInfo.packageName)) {
                if (info.activityInfo.packageName.endsWith(mContext.getPackageName())) {
                    resolveInfo.add(new SortedResolveInfo(info, info.priority, 1));
                } else {
                    final String p1 = info.activityInfo.packageName;
                    final String p2 = mContext.getPackageName();
                    final String[] l1 = p1.split("\\.");
                    final String[] l2 = p2.split("\\.");
                    if (l1.length >= 2 && l2.length >= 2) {
                        if (l1[0].equals(l2[0]) && l1[1].equals(l2[1]))
                            resolveInfo.add(new SortedResolveInfo(info, info.priority, 0));
                    }
                }
            }
        }

        if (resolveInfo.size() > 0) {
            if (resolveInfo.size() > 1)
                Collections.sort(resolveInfo);
            final ResolveInfo ret = resolveInfo.get(0).info;
            resolveInfo.clear();
            return ret;
        } else
            return null;
    }

    @Deprecated
    public static Nav getInstance() {
        return new Nav();
    }

    private Nav() {
        mContext = AppManager.getInstance().getCurrentActivityIfExist();
        mIntent = new Intent(INTERNAL_ACTION);
        mBundle = new Bundle();
    }

    private static final String TAG = "Nav";
    private static NavHookPriorityQueue sNavHookPriorityQueue = new NavHookPriorityQueue();
    private static NavErrHandler sNavErrHandler = null;
    private static HashSet<NavPreprocessor> sPreprocessorHashSet = new HashSet<>();
    private static HashSet<String> sSupportedSchemes = new HashSet<>();

    public static final String SCHEME_DEFAULT = "cardola";
    public static final String SCHEME_HTTP = "http";
    public static final String SCHEME_HTTPS = "https";

    public static final String FILTER_ACTION = "com.ali.music.action.navigator.INTERNAL_NAVIGATION";

    static {
        Nav.sSupportedSchemes.add(SCHEME_DEFAULT);
        Nav.sSupportedSchemes.add(SCHEME_HTTP);
        Nav.sSupportedSchemes.add(SCHEME_HTTPS);
    }

    private static final NavResolver DEFAULT_RESOLVER = new DefaultResolver();
    private static volatile NavResolver mNavResolver = DEFAULT_RESOLVER;
    private boolean mAllowLeaving = false;

    public static final String INTERNAL_ACTION = "com.xiami.action.VIEW";

    public static final String KEY_ORIGIN_URL = "nav_key_origin_url";

    protected Uri.Builder mNavUri;

    private Bundle mBundle;
    private Integer mRequestCode;
    private boolean mSkipPreprocessor;
    private boolean mSkipHooker;
    private boolean mDisableTransition;
    private int mTransition[];
    private Context mContext;
    private Intent mIntent;

    static class NullNav extends Nav {
        NullNav() {
            super();
            mNavUri = Uri.parse("fuck://param.url.is.null").buildUpon();
        }

        @Override
        public boolean nav() {
            error();
            return false;
        }

        @Override
        public boolean navigate(String url) {
            error();
            return false;
        }

        @Override
        public boolean navigate(NavParam navParam) {
            error();
            return false;
        }

        private void error() {
            onNavigateErr(new NavParam(""), NavErrReason.RESULT_ERROR_URI_INVALID);
        }
    }
}
