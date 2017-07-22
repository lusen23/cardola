package navigator.err;

import navigator.param.NavParam;

/**
 * Create on 2017/03/01.
 *
 * @author xinpeng.xiao
 * @version 1.0.0
 */
public interface NavErrHandler {

    /**
     * 进行跳转出错的处理
     *
     * @param navParam  参数
     * @param errReason 出错原因 {@link NavErrReason}
     */
    void onErr(NavParam navParam, NavErrReason errReason);
}
