package navigator.preprocessor;

import navigator.param.NavParam;

/**
 * Create on 2017/03/01.
 *
 * @author xinpeng.xiao
 * @version 1.0.0
 */
public interface NavPreprocessor {

    /**
     * 导航前置处理器。在Navigator进行导航前，可以对参数进行处理
     *
     * @param navParam 导航参数
     * @return true表示拦截。如果拦截，Navigator本身不进行跳转了。
     */
    boolean beforeNavTo(NavParam navParam);

}
