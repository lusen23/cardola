package com.lusen.cardola.framework.uikit.choicedialogxm.cmd.core;

import android.support.annotation.StringRes;

import com.lusen.cardola.framework.util.ContextUtil;
import com.lusen.cardola.framework.util.WebParamUtil;

import org.json.JSONObject;

/**
 * Created by leo on 2017/7/15.
 */

public class WidgetButton {

    private WebParamUtil.ParamBuilder mJson;

    public WidgetButton() {
        mJson = new WebParamUtil.ParamBuilder();
    }

    public WidgetButton itemId(String itemId) {
        mJson.addParamString("itemId", itemId);
        return this;
    }

    public WidgetButton text(String text) {
        mJson.addParamString("text", text);
        return this;
    }

    public WidgetButton text(@StringRes int text) {
        mJson.addParamString("text", ContextUtil.getContext().getString(text));
        return this;
    }

    public WidgetButton schemeUrl(String schemeUrl) {
        mJson.addParamString("schemeUrl", schemeUrl);
        return this;
    }

    public WidgetButton closeWhenClick(boolean closeWhenClick) {
        mJson.addParamBoolean("closeWhenClick", closeWhenClick);
        return this;
    }

    public WidgetButton bgColor(String bgColor) {
        mJson.addParamString("bgColor", bgColor);
        return this;
    }

    public WidgetButton txColor(String txColor) {
        mJson.addParamString("txColor", txColor);
        return this;
    }

    JSONObject getJson() {
        return mJson.buildParamJSONObject();
    }

}
