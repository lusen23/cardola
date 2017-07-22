package navigator.hook;

import android.net.Uri;

import navigator.param.NavParamParser;

/**
 * Create on 2017/03/01.
 * <p>
 * 用于拦截schema，替换默认的navigator跳转实现。
 *
 * @author xinpeng.xiao
 * @version 1.0.0
 */
public interface NavHook {
    /**
     * 是否要hook当前的schema uri。
     *
     * @param uri 当前要跳转的uri
     * @return 如何return true， 就会用此钩子的onHook取代默认的导航逻辑。
     */
    boolean canHook(Uri uri);

    /**
     * 核心方法,用于替换Navigator默认的控制逻辑
     *
     * @param navParamParser NavObject封装所有导航数据
     * @return 处理结果:接受(true)/拒绝(false). 这个值会告诉Navigator的调用者，本次nav是否成功。
     */
    boolean hook(NavParamParser navParamParser);

    /**
     * @return hook的匹配类型
     */
    HookType getType();

    enum HookType {
        //scheme 和host都匹配
        SCHEME_HOST,
        //scheme匹配
        SCHEME,
        //host匹配
        HOST,
        //正则匹配
        PATTERN,
    }
}
