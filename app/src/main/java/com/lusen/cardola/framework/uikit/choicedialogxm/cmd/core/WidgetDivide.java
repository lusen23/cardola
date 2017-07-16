package com.lusen.cardola.framework.uikit.choicedialogxm.cmd.core;

import com.lusen.cardola.framework.util.WebParamUtil;

import org.json.JSONObject;

/**
 * Created by leo on 2017/7/15.
 */

public class WidgetDivide {

    private WebParamUtil.ParamBuilder mJson;

    public WidgetDivide() {
        mJson = new WebParamUtil.ParamBuilder();
    }

    public WidgetDivide bgColor(String bgColor) {
        mJson.addParamString("bgColor", bgColor);
        return this;
    }

    JSONObject getJson() {
        return mJson.buildParamJSONObject();
    }

}
