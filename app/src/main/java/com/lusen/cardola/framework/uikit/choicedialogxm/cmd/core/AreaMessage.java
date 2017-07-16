package com.lusen.cardola.framework.uikit.choicedialogxm.cmd.core;

import com.lusen.cardola.framework.util.WebParamUtil;

import org.json.JSONObject;

/**
 * Created by leo on 2017/7/15.
 */

public class AreaMessage {

    private WebParamUtil.ParamBuilder mJson;

    public AreaMessage() {
        mJson = new WebParamUtil.ParamBuilder();
    }

    public AreaMessage height(double height) {
        mJson.addParamDouble("height", height);
        return this;
    }

    public AreaMessage style(@PropertyConstants.AreaMessageStyle int style) {
        mJson.addParamInt("style", style);
        return this;
    }

    public AreaMessage plain(WidgetPlain plain) {
        if (null != plain) {
            mJson.addParamJsonObject("plain", plain.getJson());
        }
        return this;
    }

    public AreaMessage image(WidgetImage image) {
        if (null != image) {
            mJson.addParamJsonObject("image", image.getJson());
        }
        return this;
    }

    JSONObject getJson() {
        return mJson.buildParamJSONObject();
    }

}
