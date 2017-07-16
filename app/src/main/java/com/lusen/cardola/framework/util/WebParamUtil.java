package com.lusen.cardola.framework.util;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by leo on 16/4/7.
 * Web参数工具
 */
public class WebParamUtil {

    /**
     * 解析参数串至Json对象
     *
     * @param param 参数串
     * @return Json对象
     * @throws JSONException Json解析异常
     */
    public static JSONObject parseParamStrToJson(String param) throws JSONException {
        JSONObject result = null;
        if (TextUtils.isEmpty(param)) {
            result = new JSONObject();
        } else {
            try {
                result = new JSONObject(param);
            } catch (JSONException e) {
                throw e;
            }
        }
        return result;
    }

    /**
     * 解析参数串至Json对象(忽略JsonException)
     *
     * @param param 参数串
     * @return Json对象
     */
    public static JSONObject parseParamStrToJsonIgnoreException(String param) {
        JSONObject result = null;
        if (TextUtils.isEmpty(param)) {
            result = new JSONObject();
        } else {
            try {
                result = new JSONObject(param);
            } catch (JSONException e) {
            }
        }
        return result;
    }

    /**
     * 参数构建器
     * 主要用于参数构建
     */
    public static class ParamBuilder {

        private JSONObject mParamJSONObject = new JSONObject();

        public ParamBuilder() {

        }

        public ParamBuilder(JSONObject param) {
            initParam(param);
        }

        public ParamBuilder initParam(JSONObject param) {
            if (null != param) {
                mParamJSONObject = param;
            }
            return this;
        }

        public ParamBuilder addParamString(String key, String value) {
            if (null != key && null != value) {
                try {
                    mParamJSONObject.put(key, value);
                } catch (JSONException e) {
                }
            }
            return this;
        }

        public ParamBuilder addParamBoolean(String key, boolean value) {
            if (null != key) {
                try {
                    mParamJSONObject.put(key, value);
                } catch (JSONException e) {
                }
            }
            return this;
        }

        public ParamBuilder addParamDouble(String key, double value) {
            if (null != key) {
                try {
                    mParamJSONObject.put(key, value);
                } catch (JSONException e) {
                }
            }
            return this;
        }

        public ParamBuilder addParamInt(String key, int value) {
            if (null != key) {
                try {
                    mParamJSONObject.put(key, value);
                } catch (JSONException e) {
                }
            }
            return this;
        }

        public ParamBuilder addParamLong(String key, long value) {
            if (null != key) {
                try {
                    mParamJSONObject.put(key, value);
                } catch (JSONException e) {
                }
            }
            return this;
        }

        public ParamBuilder addParamJsonObject(String key, JSONObject value) {
            if (null != key && null != value) {
                try {
                    mParamJSONObject.put(key, value);
                } catch (JSONException e) {
                }
            }
            return this;
        }

        public ParamBuilder addParamJsonArray(String key, JSONArray value) {
            if (null != key && null != value) {
                try {
                    mParamJSONObject.put(key, value);
                } catch (JSONException e) {
                }
            }
            return this;
        }

        public String getParamString(String key, String defaultValue) {
            String result = defaultValue;
            try {
                if (null != key) {
                    result = mParamJSONObject.getString(key);
                }
            } catch (JSONException e) {
            }
            return result;
        }

        public boolean getParamBoolean(String key, boolean defaultValue) {
            boolean result = defaultValue;
            try {
                if (null != key) {
                    result = mParamJSONObject.getBoolean(key);
                }
            } catch (JSONException e) {
            }
            return result;
        }

        public double getParamDouble(String key, double defaultValue) {
            double result = defaultValue;
            try {
                if (null != key) {
                    result = mParamJSONObject.getDouble(key);
                }
            } catch (JSONException e) {
            }
            return result;
        }

        public int getParamInt(String key, int defaultValue) {
            int result = defaultValue;
            try {
                if (null != key) {
                    result = mParamJSONObject.getInt(key);
                }
            } catch (JSONException e) {
            }
            return result;
        }

        public long getParamLong(String key, long defaultValue) {
            long result = defaultValue;
            try {
                if (null != key) {
                    result = mParamJSONObject.getLong(key);
                }
            } catch (JSONException e) {
            }
            return result;
        }

        public JSONObject getParamJsonObject(String key, JSONObject defaultValue) {
            JSONObject result = defaultValue;
            try {
                if (null != key) {
                    result = mParamJSONObject.getJSONObject(key);
                }
            } catch (JSONException e) {
            }
            return result;
        }

        public JSONArray getParamJsonArray(String key, JSONArray defaultValue) {
            JSONArray result = defaultValue;
            try {
                if (null != key) {
                    result = mParamJSONObject.getJSONArray(key);
                }
            } catch (JSONException e) {
            }
            return result;
        }

        public JSONObject buildParamJSONObject() {
            return mParamJSONObject;
        }

        public String buildParamString() {
            return mParamJSONObject.toString();
        }

    }

}
