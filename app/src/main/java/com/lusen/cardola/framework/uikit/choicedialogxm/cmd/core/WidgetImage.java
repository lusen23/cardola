package com.lusen.cardola.framework.uikit.choicedialogxm.cmd.core;

import com.lusen.cardola.framework.util.WebParamUtil;

import org.json.JSONObject;

/**
 * Created by leo on 2017/7/15.
 */

public class WidgetImage {

    private WebParamUtil.ParamBuilder mJson;

    public WidgetImage() {
        mJson = new WebParamUtil.ParamBuilder();
    }

    public WidgetImage height(double height) {
        mJson.addParamDouble("height", height);
        return this;
    }

    public WidgetImage url(String url) {
        mJson.addParamString("url", url);
        return this;
    }

    public WidgetImage schemeUrl(String schemeUrl) {
        mJson.addParamString("schemeUrl", schemeUrl);
        return this;
    }

    public WidgetImage closeWhenClick(boolean closeWhenClick) {
        mJson.addParamBoolean("closeWhenClick", closeWhenClick);
        return this;
    }

    public WidgetImage type(@PropertyConstants.WidgetImageType int type) {
        mJson.addParamInt("type", type);
        return this;
    }

    JSONObject getJson() {
        return mJson.buildParamJSONObject();
    }

}
