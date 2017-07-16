package com.lusen.cardola.framework.uikit.choicedialogxm.cmd.core;

import com.lusen.cardola.framework.util.WebParamUtil;

import org.json.JSONObject;

/**
 * Created by leo on 2017/7/15.
 */

public class AreaTitle {

    private WebParamUtil.ParamBuilder mJson;

    public AreaTitle() {
        mJson = new WebParamUtil.ParamBuilder();
    }

    public AreaTitle height(double height) {
        mJson.addParamDouble("height", height);
        return this;
    }

    public AreaTitle style(@PropertyConstants.AreaTitleStyle int style) {
        mJson.addParamInt("style", style);
        return this;
    }

    public AreaTitle plain(WidgetPlain plain) {
        if (null != plain) {
            mJson.addParamJsonObject("plain", plain.getJson());
        }
        return this;
    }

    public AreaTitle divide(WidgetDivide divide) {
        if (null != divide) {
            mJson.addParamJsonObject("divide", divide.getJson());
        }
        return this;
    }

    JSONObject getJson() {
        return mJson.buildParamJSONObject();
    }

}
