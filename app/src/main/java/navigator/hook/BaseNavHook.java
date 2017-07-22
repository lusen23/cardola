package navigator.hook;

import android.net.Uri;

/**
 * NavRegister  -->  BaseNavHook
 * <p>
 * <p>
 * 默认实现的Hook。 用于拦截schema，替换默认的navigator跳转实现。
 * <p>
 * Created by leo on 16/5/25.
 * 导航注册器(用于注册接收特定Url)
 */
public abstract class BaseNavHook implements NavHook {

    private final HookType mHookType;

    /**
     * 用于导航器匹配注册器
     * Url正则、Scheme、Host
     * 匹配规则:
     * >>scheme、host都存在,则全匹配
     * >>scheme存在,则scheme匹配
     * >>host存在,则host匹配
     * >>pattern存在,则pattern匹配
     */
    public String mUrlPattern;
    public String mUrlScheme;
    public String mUrlHost;


    public BaseNavHook(String urlPattern) {
        if (urlPattern == null) {
            throw new IllegalArgumentException("参数不能为null");
        }

        mUrlPattern = urlPattern;

        mHookType = computeHookType();
    }

    public BaseNavHook(String urlScheme, String urlHost) {
        if (urlScheme == null && urlHost == null) {
            throw new IllegalArgumentException("参数不能都为null");
        }

        mUrlScheme = urlScheme;
        mUrlHost = urlHost;

        mHookType = computeHookType();
    }

    /**
     * Uri匹配
     *
     * @param uri 源uri
     * @return 是否匹配成功
     */
    @Override
    public boolean canHook(Uri uri) {
        if (null != uri) {
            String uriScheme = uri.getScheme();
            String uriHost = uri.getHost();
            // Scheme、Host全匹配
            if (null != mUrlScheme && null != mUrlHost) {
                if (null != uriScheme && null != uriHost) {
                    return mUrlScheme.equals(uriScheme) && mUrlHost.equals(uriHost);
                }
            }
            // Scheme匹配
            else if (null != mUrlScheme) {
                if (null != uriScheme) {
                    return mUrlScheme.equals(uriScheme);
                }
            }
            // Host匹配
            else if (null != mUrlHost) {
                if (null != uriHost) {
                    return mUrlHost.equals(uriHost);
                }
            }
            // 正则匹配
            else if (null != mUrlPattern) {
                return uri.toString().matches(mUrlPattern);
            }
        }
        return false;
    }

    @Override
    public HookType getType() {
        return mHookType;
    }

    private HookType computeHookType() {
        if (null != mUrlScheme && null != mUrlHost) {
            return HookType.SCHEME_HOST;
        }
        if (null != mUrlScheme) {
            return HookType.SCHEME;
        }
        if (null != mUrlHost) {
            return HookType.HOST;
        }

        return HookType.PATTERN;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseNavHook that = (BaseNavHook) o;

        if (mHookType != that.mHookType) return false;
        if (mUrlPattern != null ? !mUrlPattern.equals(that.mUrlPattern) : that.mUrlPattern != null)
            return false;
        if (mUrlScheme != null ? !mUrlScheme.equals(that.mUrlScheme) : that.mUrlScheme != null)
            return false;
        return mUrlHost != null ? mUrlHost.equals(that.mUrlHost) : that.mUrlHost == null;
    }

    @Override
    public int hashCode() {
        int result = mHookType != null ? mHookType.hashCode() : 0;
        result = 31 * result + (mUrlPattern != null ? mUrlPattern.hashCode() : 0);
        result = 31 * result + (mUrlScheme != null ? mUrlScheme.hashCode() : 0);
        result = 31 * result + (mUrlHost != null ? mUrlHost.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format("NavRegister [scheme,host,pattern,type]=%s,%s,%s,%s", mUrlScheme, mUrlHost, mUrlPattern, mHookType);
    }

}
