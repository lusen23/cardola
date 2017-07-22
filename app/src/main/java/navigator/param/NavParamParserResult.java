package navigator.param;

import android.os.Bundle;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * NavParam  -->  NavParamParserResult
 * 后续会进一步简化
 *
 * Created by leo on 16/5/25.
 * 导航参数,用于统一封装不同来源的参数(Url/Bundle)
 */
public class NavParamParserResult {

    /**
     * 参数
     */
    private Bundle mParams = new Bundle();
    /**
     * 来源JsonObject内的参数
     */
    private JSONObject mParamFromJsonObject;

    public NavParamParserResult() {
    }

    public NavParamParserResult(JSONObject paramFromJsonObject) {
        mParamFromJsonObject = paramFromJsonObject;
    }

    /*************************************
     * 以下各项参数获取函数(主要是针对Map<String,String>/Bundle/JsonObject之间的兼容方案)
     * 其他类型的参数获取,如获取ArrayXXX,Parcelable,仍需自行获取Bundle/JsonObject解析
     *
     * @param key          key值
     * @param defaultValue 默认Value值
     **************************************/
    public String getParamString(String key, String defaultValue) {
        if (null != mParams && mParams.containsKey(key)) {
            return mParams.getString(key, defaultValue);
        }
        if (null != mParamFromJsonObject && mParamFromJsonObject.containsKey(key)) {
            try {
                return mParamFromJsonObject.getString(key);
            } catch (Exception e) {
            }
        }
        return defaultValue;
    }

    public boolean getParamBoolean(String key, boolean defaultValue) {
        if (null != mParams && mParams.containsKey(key)) {
            return mParams.getBoolean(key, defaultValue);
        }
        if (null != mParamFromJsonObject && mParamFromJsonObject.containsKey(key)) {
            try {
                return mParamFromJsonObject.getBooleanValue(key);
            } catch (Exception e) {
            }
        }
        return defaultValue;
    }

    public int getParamInt(String key, int defaultValue) {
        if (null != mParams && mParams.containsKey(key)) {
            return mParams.getInt(key, defaultValue);
        }
        if (null != mParamFromJsonObject && mParamFromJsonObject.containsKey(key)) {
            try {
                return mParamFromJsonObject.getIntValue(key);
            } catch (Exception e) {
            }
        }
        return defaultValue;
    }

    public long getParamLong(String key, long defaultValue) {
        if (null != mParams && mParams.containsKey(key)) {
            return mParams.getLong(key, defaultValue);
        }
        if (null != mParamFromJsonObject && mParamFromJsonObject.containsKey(key)) {
            try {
                return mParamFromJsonObject.getLongValue(key);
            } catch (Exception e) {
            }
        }
        return defaultValue;
    }

    public float getParamFloat(String key, float defaultValue) {
        if (null != mParams && mParams.containsKey(key)) {
            return mParams.getFloat(key, defaultValue);
        }
        if (null != mParamFromJsonObject && mParamFromJsonObject.containsKey(key)) {
            try {
                return mParamFromJsonObject.getFloatValue(key);
            } catch (Exception e) {
            }
        }
        return defaultValue;
    }

    public JSONObject getParamJsonObject(String key, JSONObject defaultValue) {
        if (null != mParamFromJsonObject && mParamFromJsonObject.containsKey(key)) {
            try {
                return mParamFromJsonObject.getJSONObject(key);
            } catch (Exception e) {
            }
        }
        return defaultValue;
    }

    public JSONArray getParamJsonArray(String key, JSONArray defaultValue) {
        if (null != mParamFromJsonObject && mParamFromJsonObject.containsKey(key)) {
            try {
                return mParamFromJsonObject.getJSONArray(key);
            } catch (Exception e) {
            }
        }
        return defaultValue;
    }

    public boolean isParamExist(String key) {
        if (null != mParams && mParams.containsKey(key)) {
            return true;
        }
        if (null != mParamFromJsonObject && mParamFromJsonObject.containsKey(key)) {
            return true;
        }
        return false;
    }

    public void putParamFromUrl(Map<String, Object> paramFromUrlMap) {
        for (Map.Entry<String, Object> entry : paramFromUrlMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (key != null && value != null) {
                if (value instanceof Long) {
                    mParams.putLong(key, (Long)value);
                    continue;
                }

                if (value instanceof Integer) {
                    mParams.putInt(key, (Integer)value);
                    continue;
                }

                mParams.putString(key, value.toString());
            }
        }
    }


    public void putParams(Bundle params) {
        if (params == null) {
            return;
        }

        mParams.putAll(params);
    }

    public Bundle getParams() {
        return mParams;
    }
}
