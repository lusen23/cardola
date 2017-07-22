package navigator.param;

import android.net.Uri;
import android.os.Bundle;

/**
 * NavEntity  -->    NavParam
 *
 * Created by leo on 16/6/1.
 * 封装导航的各种行为参数
 */
public class NavParam {

    /**
     * 导航的Uri
     */
    public Uri mUri;
    /**
     * Bundle数据(提供给Native设置)
     */
    public Bundle mBundleData;
    /**
     * 全局参数(提供给框架层使用)
     */
    public NavGlobalParam mGlobal = new NavGlobalParam();

    public NavParam(Uri uri) {
        mUri = uri;
    }

    public NavParam(String url) {
        if (null != url) {
            mUri = Uri.parse(url);
        } else {
            mUri = null;
        }
    }

    public String getUrl() {
        return null != mUri ? mUri.toString() : null;
    }

    @Override
    public String toString() {
        return String.format("NavEntity [uri,bundle]=%s,%s", mUri, mBundleData);
    }

}
