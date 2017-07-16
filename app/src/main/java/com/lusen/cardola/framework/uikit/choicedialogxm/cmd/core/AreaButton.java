package com.lusen.cardola.framework.uikit.choicedialogxm.cmd.core;

import com.lusen.cardola.framework.util.WebParamUtil;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by leo on 2017/7/15.
 */

public class AreaButton {

    private WebParamUtil.ParamBuilder mJson;

    public AreaButton() {
        mJson = new WebParamUtil.ParamBuilder();
    }

    public AreaButton button(WidgetButton... buttons) {
        if (null != buttons && buttons.length > 0) {
            JSONArray jsonArray = new JSONArray();
            for (WidgetButton button : buttons) {
                if (null != button) {
                    jsonArray.put(button.getJson());
                }
            }
            mJson.addParamJsonArray("button", jsonArray);
        }
        return this;
    }

    public AreaButton divide(WidgetDivide divide) {
        if (null != divide) {
            mJson.addParamJsonObject("divide", divide.getJson());
        }
        return this;
    }

    JSONObject getJson() {
        return mJson.buildParamJSONObject();
    }

}
