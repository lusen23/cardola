package navigator.param;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.Map;
import java.util.Set;

/**
 * Created by leo on 16/6/20.
 * 导航Url构建器
 * 主要用于Url各种条件下,参数的附加
 * <p>
 * 关于Encode及Decode说明
 * 构造:不做任何操作
 * 参数:所有addParam方法进行的param都会进行encode操作
 * build:已经进行encode后的uri
 */
@Deprecated
public class NavUrlBuilder {

    /**
     * 目标uri
     */
    private Uri mUri;

    public NavUrlBuilder(String url) throws IllegalArgumentException {
        if (null == url || url.trim().equals("")) {
            throw new IllegalArgumentException("NavUrlBuilder url must not be null or empty");
        }
        mUri = Uri.parse(url);
    }

    public NavUrlBuilder(@NonNull String scheme, @NonNull String host) throws Exception {
        generateUri(scheme, host, null);
    }

    public NavUrlBuilder(@NonNull String scheme, @NonNull String host, String[] segments) throws Exception {
        StringBuilder path = new StringBuilder();
        if (null != segments) {
            for (String segment : segments) {
                if (null != segment && !segment.trim().equals("")) {
                    path.append(segment).append("/");
                }
            }
        }
        generateUri(scheme, host, path.toString());
    }

    private void generateUri(String scheme, String host, String path) throws Exception {
        if (null == scheme || scheme.trim().equals("")) {
            throw new Exception("NavUrlBuilder scheme must not be null or empty");
        }
        if (null == host || host.trim().equals("")) {
            throw new Exception("NavUrlBuilder host must not be null or empty");
        }
        StringBuilder builder = new StringBuilder();
        builder.append(scheme).append("://");
        builder.append(host);
        if (null != path && !path.trim().equals("")) {
            builder.append("/").append(path);
        }
        mUri = Uri.parse(builder.toString());
    }

    /**
     * 增加参数
     * 目前支持八大基本类型及String的参数附加
     * 后续需根据需求扩展,如JSON、Object等类型
     *
     * @param key   参数key
     * @param value 参数value
     * @return 是否成功
     */
    public boolean addParam(String key, Object value) {
        boolean result = false;
        if (null != key && null != value) {
            if (value instanceof String) {
                mUri = mUri.buildUpon().appendQueryParameter(key, (String) value).build();
                result = true;
            } else if (value instanceof Byte) {
                Byte param = (Byte) value;
                mUri = mUri.buildUpon().appendQueryParameter(key, param.byteValue() + "").build();
                result = true;
            } else if (value instanceof Character) {
                Character param = (Character) value;
                mUri = mUri.buildUpon().appendQueryParameter(key, param.charValue() + "").build();
                result = true;
            } else if (value instanceof Boolean) {
                Boolean param = (Boolean) value;
                mUri = mUri.buildUpon().appendQueryParameter(key, param.booleanValue() + "").build();
                result = true;
            } else if (value instanceof Short) {
                Short param = (Short) value;
                mUri = mUri.buildUpon().appendQueryParameter(key, param.shortValue() + "").build();
                result = true;
            } else if (value instanceof Integer) {
                Integer param = (Integer) value;
                mUri = mUri.buildUpon().appendQueryParameter(key, param.intValue() + "").build();
                result = true;
            } else if (value instanceof Long) {
                Long param = (Long) value;
                mUri = mUri.buildUpon().appendQueryParameter(key, param.longValue() + "").build();
                result = true;
            } else if (value instanceof Float) {
                Float param = (Float) value;
                mUri = mUri.buildUpon().appendQueryParameter(key, param.floatValue() + "").build();
                result = true;
            } else if (value instanceof Double) {
                Double param = (Double) value;
                mUri = mUri.buildUpon().appendQueryParameter(key, param.doubleValue() + "").build();
                result = true;
            }
        }
        return result;
    }

    /**
     * 增加参数
     *
     * @param map 参数map
     */
    public void addParam(Map<String, String> map) {
        if (null != map) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (null != entry) {
                    addParam(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    /**
     * 增加参数
     *
     * @param bundle 参数bundle
     */
    public void addParam(Bundle bundle) {
        if (null != bundle) {
            Set<String> keys = bundle.keySet();
            if (null != keys) {
                for (String key : keys) {
                    addParam(key, bundle.get(key));
                }
            }
        }
    }

    /**
     * 构建url
     *
     * @return 目标url
     */
    public String build() {
        return mUri.toString();
    }

    /**
     * 构建uri
     *
     * @return 目标uri
     */
    public Uri buildUri() {
        return mUri;
    }

    /**
     * 解码uri
     *
     * @return 解码后的uri
     */
    public Uri decodeUri() {
        return Uri.parse(decodeUrl());
    }

    /**
     * 解码uri
     *
     * @return 解码后的uri
     */
    public String decodeUrl() {
        return Uri.decode(mUri.toString());
    }

}
