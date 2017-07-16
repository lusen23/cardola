package com.lusen.cardola.framework.uikit.choicedialogxm.cmd.core;

import com.lusen.cardola.framework.util.WebParamUtil;

import org.json.JSONObject;

/**
 * Created by leo on 2017/7/15.
 */

public class AreaClose {

    private WebParamUtil.ParamBuilder mJson;

    public AreaClose() {
        mJson = new WebParamUtil.ParamBuilder();
    }

    public AreaClose padding(int padding) {
        mJson.addParamInt("padding", padding);
        return this;
    }

    JSONObject getJson() {
        return mJson.buildParamJSONObject();
    }

}
