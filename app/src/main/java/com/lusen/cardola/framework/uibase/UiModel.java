package com.lusen.cardola.framework.uibase;

/**
 * Created by leo on 16/8/4.
 * UiModel模型,用于指定Fragment或Activity的UI模型组合
 * 目前支持(侧滑关闭、顶部栏、沉浸式)
 */
public class UiModel {

    public static final String PARAM_UI_MODEL = "param_ui_model";

    public static final int UI_MODEL_NONE = 0;
    public static final int UI_MODEL_SLIDE_CLOSE = 1;
    public static final int UI_MODEL_ACTION_BAR = UI_MODEL_SLIDE_CLOSE << 1;
    public static final int UI_MODEL_IMMERSIVE = UI_MODEL_SLIDE_CLOSE << 2;

    public static boolean isUiModelSlideCloseSupported(int uiModel) {
        return (uiModel & UI_MODEL_SLIDE_CLOSE) != 0;
    }

    public static boolean isUiModelActionBarSupported(int uiModel) {
        return (uiModel & UI_MODEL_ACTION_BAR) != 0;
    }

    public static boolean isUiModelImmersiveSupported(int uiModel) {
        return (uiModel & UI_MODEL_IMMERSIVE) != 0;
    }

}
