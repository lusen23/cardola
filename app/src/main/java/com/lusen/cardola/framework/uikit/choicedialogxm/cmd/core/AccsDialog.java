package com.lusen.cardola.framework.uikit.choicedialogxm.cmd.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lusen.cardola.framework.util.LogUtil;
import com.lusen.cardola.framework.util.WebParamUtil;

/**
 * Created by leo on 2017/7/14.
 */

public class AccsDialog {

    private WebParamUtil.ParamBuilder mDataJson;
    private WebParamUtil.ParamBuilder mDialogJson;
    private AreaTitle mAreaTitle;
    private AreaMessage mAreaMessage;
    private AreaButton mAreaButton;
    private AreaClose mAreaClose;

    public AccsDialog() {
        mDataJson = new WebParamUtil.ParamBuilder();
        mDialogJson = new WebParamUtil.ParamBuilder();
    }

    public AccsDialog model(String model) {
        mDataJson.addParamString("model", model);
        return this;
    }

    public AccsDialog timeout(long timeout) {
        mDataJson.addParamLong("timeout", timeout);
        return this;
    }

    public AccsDialog annourceId(String annourceId) {
        mDataJson.addParamString("annourceId", annourceId);
        return this;
    }

    public AccsDialog annourceType(String annourceType) {
        mDataJson.addParamString("annourceType", annourceType);
        return this;
    }

    public AccsDialog bgColor(String bgColor) {
        mDialogJson.addParamString("bgColor", bgColor);
        return this;
    }

    public AccsDialog closeWhenBack(boolean closeWhenBack) {
        mDialogJson.addParamBoolean("closeWhenBack", closeWhenBack);
        return this;
    }

    public AreaTitle buildAreaTitle() {
        return buildAreaTitle(true);
    }

    public AreaMessage buildAreaMessage() {
        return buildAreaMessage(true);
    }

    public AreaButton buildAreaButton() {
        return buildAreaButton(true);
    }

    public AreaClose buildAreaClose() {
        return buildAreaClose(true);
    }

    public AreaTitle buildAreaTitle(boolean reset) {
        if (null != mAreaTitle && reset) {
            mAreaTitle = null;
        }
        if (null == mAreaTitle) {
            mAreaTitle = new AreaTitle();
        }
        mDialogJson.addParamJsonObject("title", mAreaTitle.getJson());
        return mAreaTitle;
    }

    public AreaMessage buildAreaMessage(boolean reset) {
        if (null != mAreaMessage && reset) {
            mAreaMessage = null;
        }
        if (null == mAreaMessage) {
            mAreaMessage = new AreaMessage();
        }
        mDialogJson.addParamJsonObject("message", mAreaMessage.getJson());
        return mAreaMessage;
    }

    public AreaButton buildAreaButton(boolean reset) {
        if (null != mAreaButton && reset) {
            mAreaButton = null;
        }
        if (null == mAreaButton) {
            mAreaButton = new AreaButton();
        }
        mDialogJson.addParamJsonObject("button", mAreaButton.getJson());
        return mAreaButton;
    }

    public AreaClose buildAreaClose(boolean reset) {
        if (null != mAreaClose && reset) {
            mAreaClose = null;
        }
        if (null == mAreaClose) {
            mAreaClose = new AreaClose();
        }
        mDialogJson.addParamJsonObject("close", mAreaClose.getJson());
        return mAreaClose;
    }

    public void show() {
        mDataJson.addParamJsonObject("dialog", mDialogJson.buildParamJSONObject());
        String data = mDataJson.buildParamString();
        LogUtil.log("AccsDialog show (data) = " + data);
        JSONObject dataJson = JSON.parseObject(data);
        new AccsDialogPerformer().execute(dataJson);
    }

}
