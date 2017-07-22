package navigator.err;

import android.util.SparseArray;

/**
 * Created by leo on 16/5/27.
 * 导航出错原因
 */
public class NavErrReason {

    public final int mResult;

    public static final int RESULT_NAV_REFUSE = 1;              // 导航:拒绝
    public static final int RESULT_REGISTER_NOT_FIND = 2;       // 注册器未找到
    public static final int RESULT_REGISTER_IS_CLOSED = 3;      // 注册器已关闭
    public static final int RESULT_REGISTER_NEED_PERMIT = 4;    // 注册器需认证
    public static final int RESULT_ERROR_ENTITY_INVALID = 5;    // 错误:entity无效
    public static final int RESULT_ERROR_URI_INVALID = 6;       // 错误:url无效
    public static final int RESULT_ERROR_SCHEME_INVALID = 7;    // 错误:scheme无效
    public static final int RESULT_ERROR_JUMPER_FAILED = 8;    // 错误:uibase 中跳转失败
    /**
     * 如poplayer 会要求不拦截对应h5的http
     * 如h5容器内部跳转
     */
    public static final int RESULT_ERROR_PREPROCESOR_FAILED = 9;    // 错误:被预处理拦截了

    private static SparseArray<String> mReasonMap = new SparseArray<>();

    static {
        mReasonMap.put(RESULT_NAV_REFUSE, "导航:被hook拦截后，hook处理失败");
        mReasonMap.put(RESULT_REGISTER_NOT_FIND, "注册器未找到");
        mReasonMap.put(RESULT_REGISTER_IS_CLOSED, "注册器已关闭");
        mReasonMap.put(RESULT_REGISTER_NEED_PERMIT, "注册器需认证");
        mReasonMap.put(RESULT_ERROR_ENTITY_INVALID, "错误:导航参数为null");
        mReasonMap.put(RESULT_ERROR_URI_INVALID, "错误:导航uri为null");
        mReasonMap.put(RESULT_ERROR_SCHEME_INVALID, "错误:scheme无效");
        mReasonMap.put(RESULT_ERROR_JUMPER_FAILED, "错误:jumper跳转器跳转失败");
        mReasonMap.put(RESULT_ERROR_PREPROCESOR_FAILED, "错误:被预处理器拦截了.");
    }

    public NavErrReason(int result) {
        mResult = result;
    }

    @Override
    public String toString() {
        return String.format("NavResult [result]=%s,%s", mResult, mReasonMap.get(mResult));
    }

}
