package navigator.param;

import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import navigator.Nav;

/**
 * NavObject  -->  NavParamParser
 * <p>
 * <p>
 * Created by leo on 16/6/1.
 * 导航对象,匹配成功后,返回给注册器
 */
public class NavParamParser {

    /**
     * uri,默认已经进行过encode操作,建议导航所用uri拼接采用类 {@link NavUrlBuilder}
     */
    private Uri mUri;
    private String mUrl;
    private NavGlobalParam mGlobal;

    private String mScheme;
    private String mHost;
    private int mPort;
    private String mPath;
    private List<String> mSegments;

    private NavParamParserResult mNavParamParserResult;

    private Uri mFullUri;
    private String mFullUrl;

    public NavParamParser(NavParam navParam) {
        init(navParam);
    }

    private void init(NavParam navParam) {
        // 初始参数生成
        mUri = navParam.mUri;
        mUrl = navParam.getUrl();
        mGlobal = navParam.mGlobal;
        // 解析Uri各部数据
        mScheme = mUri.getScheme();
        mHost = mUri.getHost();
        mPort = mUri.getPort();
        mPath = mUri.getPath();
        mSegments = mUri.getPathSegments();
        // 解析生成NavParam
        mNavParamParserResult = new NavParamParserResult();
        Set<String> paramKeys = mUri.getQueryParameterNames();
        Map<String, Object> paramFromUrl = new LinkedHashMap<>();
        if (null != paramKeys) {
            for (String paramKey : paramKeys) {
                if (null != paramKey) {
                    String paramValue = mUri.getQueryParameter(paramKey);
                    // 将url内参数部分进行decode后,返回给上层
                    paramFromUrl.put(Uri.decode(paramKey), Uri.decode(paramValue));
                }
            }
        }

        // 获取全Url及Uri,会将Bundle数据追加
        mFullUri = mUri;

        try {
            NavUrlBuilder navUrlBuilder = new NavUrlBuilder(mUrl);
            navUrlBuilder.addParam(navParam.mBundleData);
            mFullUrl = navUrlBuilder.build();
        } catch (IllegalArgumentException e) {
            //
        }

        paramFromUrl.put(Nav.KEY_ORIGIN_URL, mFullUri);

        mNavParamParserResult.putParamFromUrl(paramFromUrl);
        mNavParamParserResult.putParams(navParam.mBundleData);
    }

    public String getUrl() {
        return mUrl;
    }

    public Uri getUri() {
        return mUri;
    }

    public String getFullUrl() {
        return mFullUrl;
    }

    public Uri getFullUri() {
        return mFullUri;
    }

    public String getScheme() {
        return mScheme;
    }

    public String getHost() {
        return mHost;
    }

    public int getPort() {
        return mPort;
    }

    public String getPath() {
        return mPath;
    }

    public List<String> getSegments() {
        return mSegments;
    }

    public
    @NonNull
    NavParamParserResult getNavParamParserResult() {
        return mNavParamParserResult;
    }

    public NavGlobalParam getGlobal() {
        return mGlobal;
    }
}
