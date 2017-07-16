package com.lusen.cardola.framework.uikit.choicedialogxm.cmd.core;

import android.support.annotation.StringRes;

import com.lusen.cardola.framework.util.ContextUtil;
import com.lusen.cardola.framework.util.WebParamUtil;

import org.json.JSONObject;

/**
 * Created by leo on 2017/7/15.
 */

public class WidgetPlain {

    private WebParamUtil.ParamBuilder mJson;

    public WidgetPlain() {
        mJson = new WebParamUtil.ParamBuilder();
    }

    public WidgetPlain height(double height) {
        mJson.addParamDouble("height", height);
        return this;
    }

    public WidgetPlain text(String text) {
        mJson.addParamString("text", text);
        return this;
    }

    public WidgetPlain text(@StringRes int text) {
        mJson.addParamString("text", ContextUtil.getContext().getString(text));
        return this;
    }

    public WidgetPlain schemeUrl(String schemeUrl) {
        mJson.addParamString("schemeUrl", schemeUrl);
        return this;
    }

    public WidgetPlain closeWhenClick(boolean closeWhenClick) {
        mJson.addParamBoolean("closeWhenClick", closeWhenClick);
        return this;
    }

    public WidgetPlain bgColor(String bgColor) {
        mJson.addParamString("bgColor", bgColor);
        return this;
    }

    public WidgetPlain txColor(String txColor) {
        mJson.addParamString("txColor", txColor);
        return this;
    }

    public WidgetPlain canScroll(boolean canScroll) {
        mJson.addParamBoolean("canScroll", canScroll);
        return this;
    }

    public WidgetPlain paddingLeft(int paddingLeft) {
        mJson.addParamInt("paddingLeft", paddingLeft);
        return this;
    }

    public WidgetPlain paddingRight(int paddingRight) {
        mJson.addParamInt("paddingRight", paddingRight);
        return this;
    }

    public WidgetPlain paddingTop(int paddingTop) {
        mJson.addParamInt("paddingTop", paddingTop);
        return this;
    }

    public WidgetPlain paddingBottom(int paddingBottom) {
        mJson.addParamInt("paddingBottom", paddingBottom);
        return this;
    }

    JSONObject getJson() {
        return mJson.buildParamJSONObject();
    }

}
