package com.lusen.cardola.framework.uikit.choicedialogxm.cmd.core;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lusen.cardola.R;
import com.lusen.cardola.framework.manager.AppManager;
import com.lusen.cardola.framework.uibase.base.BaseDialogFragmentDelegate;
import com.lusen.cardola.framework.uibase.base.BaseDialogFragmentSystem;
import com.lusen.cardola.framework.uikit.RemoteImageView;
import com.lusen.cardola.framework.uikit.choicedialogxm.ChoiceDialog;
import com.lusen.cardola.framework.uikit.choicedialogxm.MultiItem;
import com.lusen.cardola.framework.util.ContextUtil;
import com.lusen.cardola.framework.util.DisplayUtil;
import com.lusen.cardola.framework.util.ThreadUtil;
import com.lusen.cardola.framework.util.UiUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leo on 16/12/2.
 */

public class AccsDialogPerformer {

    public void execute(JSONObject data) {
        // 捕获所有异常,一旦存在异常,不进行弹窗处理,连贯性逻辑保障
        try {
            if (null != data) {
                NavParamParserResult param = new NavParamParserResult(data);
                // 解析dialog字段
                JSONObject jsonObjectDialog = param.getParamJsonObject("dialog", null);
                // 寻找TopActivity用于承载弹窗
                final Activity topActivity = AppManager.getInstance().getCurrentActivityIfExist();
                if (null != topActivity && null != jsonObjectDialog) {
                    // 弹窗控件
                    final ChoiceDialog choiceDialog = new ChoiceDialog();
                    // 弹窗业务参数
                    String model = param.getParamString("model", null);
                    long timeout = param.getParamLong("timeout", 0);
                    final String annourceId = param.getParamString("annourceId", null);
                    final String annourceType = param.getParamString("annourceType", null);
                    // 弹窗样式参数
                    NavParamParserResult navParamParserResultDialog = new NavParamParserResult(jsonObjectDialog);
                    final String bgColor = navParamParserResultDialog.getParamString("bgColor", null);
                    boolean closeWhenBack = navParamParserResultDialog.getParamBoolean("closeWhenBack", true);
                    // 弹窗区域参数
                    final AreaTitle areaTitle = generateAreaTitle(navParamParserResultDialog.getParamJsonObject("title", null));
                    final AreaMessage areaMessage = generateAreaMessage(navParamParserResultDialog.getParamJsonObject("message", null));
                    final AreaButton areaButton = generateAreaButton(navParamParserResultDialog.getParamJsonObject("button", null));
                    final AreaClose areaClose = generateAreaClose(navParamParserResultDialog.getParamJsonObject("close", null));

                    // Title区
                    buildAreaTitle(choiceDialog, areaTitle);
                    // Button区
                    buildAreaButton(choiceDialog, areaButton, annourceId, annourceType);
                    // Message区
                    buildAreaMessage(choiceDialog, areaMessage);

                    // 设置是否可取消(针对物理键及虚拟键返回)
                    choiceDialog.setCancelable(closeWhenBack);
                    // 设置ViewCreated之后Callback,主要修改视图属性
                    choiceDialog.setDialogLifeCycleCallback(new BaseDialogFragmentDelegate.DialogLifeCycleCallback() {
                        @Override
                        public void onViewCreated(BaseDialogFragmentSystem dialog, View view, Bundle bundle) {
                            // 埋点:页面进入
                            performTrackPageEnter(choiceDialog, annourceId, annourceType);
                            try {
                                // 弹窗背景颜色
                                setBackgroundColor(choiceDialog.mLayoutChoiceRoot, bgColor);
                                // Title(属性)
                                if (null != choiceDialog.mLayoutChoiceDialogTitle) {
                                    if (null != areaTitle) {
                                        // 设置自定义高度
                                        if (areaTitle.height > 0) {
                                            int height;
                                            // 屏高比例
                                            if (areaTitle.height <= 1.0f) {
                                                height = (int) (DisplayUtil.getDisplayHeight() * areaTitle.height);
                                            }
                                            // 固定高度
                                            else {
                                                height = DisplayUtil.dip2px(areaTitle.height);
                                            }
                                            if (height > 0) {
                                                ViewGroup.LayoutParams lp = choiceDialog.mLayoutChoiceDialogTitle.getLayoutParams();
                                                lp.height = height;
                                                choiceDialog.mLayoutChoiceDialogTitle.setLayoutParams(lp);
                                            }
                                        }
                                    }
                                }
                                if (null != areaButton) {
                                    // Button(button控件)
                                    List<WidgetButton> widgetButtons = areaButton.widgetButtons;
                                    if (null != widgetButtons && widgetButtons.size() > 0 && widgetButtons.size() <= 2) {
                                        if (widgetButtons.size() == 1) {
                                            setBackgroundColor(choiceDialog.mTvChoiceDialogPositiveButton, widgetButtons.get(0).bgColor);
                                            setTextColor(choiceDialog.mTvChoiceDialogPositiveButton, widgetButtons.get(0).txColor);
                                        } else if (widgetButtons.size() == 2) {
                                            setBackgroundColor(choiceDialog.mTvChoiceDialogNegativeButton, widgetButtons.get(0).bgColor);
                                            setTextColor(choiceDialog.mTvChoiceDialogNegativeButton, widgetButtons.get(0).txColor);
                                            setBackgroundColor(choiceDialog.mTvChoiceDialogPositiveButton, widgetButtons.get(1).bgColor);
                                            setTextColor(choiceDialog.mTvChoiceDialogPositiveButton, widgetButtons.get(1).txColor);
                                        }
                                    }
                                    // Button(divide控件)
                                    if (null != choiceDialog.mDivideChoiceDialogButton) {
                                        if (null != areaButton.widgetDivide) {
                                            choiceDialog.mDivideChoiceDialogButton.setVisibility(View.VISIBLE);
                                            buildAreaButtonDivide(choiceDialog, choiceDialog.mDivideChoiceDialogButton, areaButton.widgetDivide);
                                        } else {
                                            choiceDialog.mDivideChoiceDialogButton.setVisibility(View.GONE);
                                        }
                                    }
                                }
//                                // Close
//                                if (null != areaClose) {
//                                    choiceDialog.mLayoutChoiceDialogClose.setVisibility(View.VISIBLE);
//                                    int paddingTop = (areaClose.padding >= 0 ? DisplayUtil.dip2px(areaClose.padding) : choiceDialog.mLayoutChoiceDialogClose.getPaddingTop());
//                                    int paddingRight = (areaClose.padding >= 0 ? DisplayUtil.dip2px(areaClose.padding) : choiceDialog.mLayoutChoiceDialogClose.getPaddingRight());
//                                    choiceDialog.mLayoutChoiceDialogClose.setPadding(0, paddingTop, paddingRight, 0);
//                                    choiceDialog.mLayoutChoiceDialogClose.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            choiceDialog.hideSelf();
//                                        }
//                                    });
//                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    // 监听Dismiss
                    choiceDialog.setDialogOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            // 埋点:页面退出
                            performTrackPageLeave(choiceDialog);
                        }
                    });
                    // 显示弹窗
                    choiceDialog.showSelf(topActivity);
                    // 定时关闭弹窗
                    if (timeout > 0) {
                        ThreadUtil.MAIN_THREAD_HANDLER.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                choiceDialog.hideSelf();
                            }
                        }, timeout);
                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildAreaTitle(final ChoiceDialog choiceDialog, AreaTitle areaTitle) {
        if (null != areaTitle) {
            // Title区将会采用自定义布局实现
            View customTitleView = LayoutInflater.from(ContextUtil.getContext()).inflate(R.layout.xm_choice_dialog_title_style_1, null);
            final TextView titlePlain = UiUtil.findViewById(customTitleView, R.id.xm_choice_dialog_title, TextView.class);
            final View titleDivide = UiUtil.findViewById(customTitleView, R.id.xm_choice_dialog_title_divide, View.class);
            // 自定义布局解析失败,不显示Title区
            if (null == customTitleView || null == titlePlain || null == titleDivide) {
                choiceDialog.setDialogTitleVisibility(false);
            }
            // 自定义布局设置
            else {
                choiceDialog.setDialogTitleVisibility(true);
                choiceDialog.setCustomTitleView(customTitleView);
                choiceDialog.setFullCustomTitleView(true);
                // plain控件
                if (null != areaTitle.widgetPlain) {
                    titlePlain.setVisibility(View.VISIBLE);
                    buildAreaTitlePlain(choiceDialog, titlePlain, areaTitle.widgetPlain);
                } else {
                    titlePlain.setVisibility(View.GONE);
                }
                // divide控件
                if (null != areaTitle.widgetDivide) {
                    titleDivide.setVisibility(View.VISIBLE);
                    buildAreaTitleDivide(choiceDialog, titleDivide, areaTitle.widgetDivide);
                } else {
                    titleDivide.setVisibility(View.GONE);
                }
            }
        } else {
            choiceDialog.setDialogTitleVisibility(false);
            choiceDialog.setDialogTitleDividerVisibility(false);
        }
    }

    private void buildAreaTitlePlain(final ChoiceDialog choiceDialog, TextView titlePlain, final WidgetPlain widgetPlain) {
        titlePlain.setText(widgetPlain.text);
        setBackgroundColor(titlePlain, widgetPlain.bgColor);
        setTextColor(titlePlain, widgetPlain.txColor);
        titlePlain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performNavigate(widgetPlain.schemeUrl);
                if (widgetPlain.closeWhenClick) {
                    choiceDialog.hideSelf();
                }
            }
        });
        if (widgetPlain.canScroll) {
            // 可滚动
            titlePlain.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            titlePlain.setFocusable(true);
            titlePlain.setSelected(true);
        } else {
            titlePlain.setEllipsize(TextUtils.TruncateAt.END);
        }
    }

    private void buildAreaTitleDivide(final ChoiceDialog choiceDialog, View titleDivide, final WidgetDivide widgetDivide) {
        setBackgroundColor(titleDivide, widgetDivide.bgColor);
    }

    private void buildAreaButton(final ChoiceDialog choiceDialog, AreaButton areaButton, final String annourceId, final String annourceType) {
        if (null != areaButton && null != areaButton.widgetButtons && areaButton.widgetButtons.size() > 0) {
            choiceDialog.setDialogBottomVisible(true);
            // 解析WidgetButton
            List<WidgetButton> widgetButtons = areaButton.widgetButtons;
            int buttonCount = widgetButtons.size();
            // 单Button
            if (buttonCount == 1) {
                final WidgetButton button = widgetButtons.get(0);
                choiceDialog.setDialogSingleStyleSetting(button.text, new ChoiceDialog.DialogStyleSingleCallback() {
                    @Override
                    public boolean onPositiveButtonClick() {
                        // 埋点:点击
                        performTrackClick(annourceId, annourceType, button.id);
                        // 跳转
                        performNavigate(button.schemeUrl);
                        return !button.closeWhenClick;
                    }
                });
            }
            // 双Button
            else if (buttonCount == 2) {
                final WidgetButton button0 = widgetButtons.get(0);
                final WidgetButton button1 = widgetButtons.get(1);
                choiceDialog.setDialogCoupleStyleSetting(button1.text, button0.text, new ChoiceDialog.DialogStyleCoupleCallback() {
                    @Override
                    public boolean onPositiveButtonClick() {
                        // 埋点:点击
                        performTrackClick(annourceId, annourceType, button1.id);
                        // 跳转
                        performNavigate(button1.schemeUrl);
                        return !button1.closeWhenClick;
                    }

                    @Override
                    public boolean onNegativeButtonClick() {
                        // 埋点:点击
                        performTrackClick(annourceId, annourceType, button0.id);
                        // 跳转
                        performNavigate(button0.schemeUrl);
                        return !button0.closeWhenClick;
                    }
                });
            }
            // 多Button
            else if (buttonCount > 2) {
                List<MultiItem> multiItemList = new ArrayList<>();
                for (int index = 0; index < buttonCount; index++) {
                    WidgetButton button = widgetButtons.get(index);
                    MultiItem multiItem = new MultiItem(button.text);
                    multiItem.setExtra(button);
                    multiItem.mItemTxColor = button.txColor;
                    multiItem.mItemBgColor = button.bgColor;
                    if (null != areaButton.widgetDivide) {
                        multiItem.mShowDivide = true;
                        multiItem.mItemDivideColor = areaButton.widgetDivide.bgColor;
                    } else {
                        multiItem.mShowDivide = false;
                        multiItem.mItemDivideColor = null;
                    }
                    multiItemList.add(multiItem);
                }
                choiceDialog.setDialogMultiStyleSetting(multiItemList, new ChoiceDialog.DialogStyleMultiCallback() {
                    @Override
                    public boolean onMutliItemClick(MultiItem multiItem, int position) {
                        Object extra = multiItem.getExtra();
                        if (null != extra && extra instanceof WidgetButton) {
                            WidgetButton button = (WidgetButton) extra;
                            // 埋点:点击
                            performTrackClick(annourceId, annourceType, button.id);
                            // 跳转
                            performNavigate(button.schemeUrl);
                            return !button.closeWhenClick;
                        }
                        return false;
                    }
                });
            }
        } else {
            choiceDialog.setDialogBottomVisible(false);
            choiceDialog.setDialogButtonDividerVisible(false);
        }
    }

    private void buildAreaButtonDivide(final ChoiceDialog choiceDialog, View buttonDivide, final WidgetDivide widgetDivide) {
        setBackgroundColor(buttonDivide, widgetDivide.bgColor);
    }

    private void buildAreaMessage(final ChoiceDialog choiceDialog, final AreaMessage areaMessage) {
        // message区不带分割线
        choiceDialog.setDialogMessageDividerVisibility(false);
        if (null != areaMessage) {
            // Message区将会采用自定义布局实现
            View customMessageView = LayoutInflater.from(ContextUtil.getContext()).inflate(R.layout.xm_choice_dialog_message_style_1, null);
            RemoteImageView messageImage = UiUtil.findViewById(customMessageView, R.id.xm_choice_dialog_message_image, RemoteImageView.class);
            final TextView messagePlain = UiUtil.findViewById(customMessageView, R.id.xm_choice_dialog_message_tv, TextView.class);
            // 自定义布局解析失败,不显示Message区
            if (null == customMessageView || null == messageImage || null == messagePlain) {
                choiceDialog.setDialogMessageVisibility(false);
            }
            // 自定义布局设置
            else {
                choiceDialog.setDialogMessageVisibility(true);
                choiceDialog.setCustomMessageView(customMessageView);
                choiceDialog.setFullCustomMessageView(true);
                // 设置自定义高度
                if (areaMessage.height > 0) {
                    int height;
                    // 屏高比例
                    if (areaMessage.height <= 1.0f) {
                        height = (int) (DisplayUtil.getDisplayHeight() * areaMessage.height);
                    }
                    // 固定高度
                    else {
                        height = DisplayUtil.dip2px(areaMessage.height);
                    }
                    if (height > 0) {
                        choiceDialog.setLayoutChoiceDialogMessageHeight(height);
                    }
                }
                // 布局样式
                int style = areaMessage.style;
                // 样式:仅文案
                if (style == AreaMessage.DIALOG_MESSAGE_STYLE_ONLY_PLAIN) {
                    messageImage.setVisibility(View.GONE);
                    if (null != areaMessage.widgetPlain) {
                        messagePlain.setVisibility(View.VISIBLE);
                        buildAreaMessagePlain(choiceDialog, messagePlain, areaMessage.widgetPlain);
                    } else {
                        messagePlain.setVisibility(View.GONE);
                    }
                }
                // 样式:仅图片
                else if (style == AreaMessage.DIALOG_MESSAGE_STYLE_ONLY_IMAGE) {
                    messagePlain.setVisibility(View.GONE);
                    if (null != areaMessage.widgetImage) {
                        messageImage.setVisibility(View.VISIBLE);
                        buildAreaMessageImage(choiceDialog, messageImage, areaMessage.widgetImage);
                    } else {
                        messageImage.setVisibility(View.GONE);
                    }
                }
                // 样式:图片上、文案下
                else if (style == AreaMessage.DIALOG_MESSAGE_STYLE_IMAGE_UPON_PLAIN) {
                    float weightTotal = 1.0f;
                    if (null != areaMessage.widgetImage) {
                        messageImage.setVisibility(View.VISIBLE);
                        buildAreaMessageImage(choiceDialog, messageImage, areaMessage.widgetImage);
                        // 计算image控件weight
                        float weightImage = 0.5f;
                        if (areaMessage.widgetImage.height > 0 && areaMessage.widgetImage.height <= 1) {
                            weightImage = areaMessage.widgetImage.height;
                        }
                        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) messageImage.getLayoutParams();
                        lp.weight = weightImage;
                        messageImage.setLayoutParams(lp);
                        // 计算剩余weight
                        weightTotal -= weightImage;
                    } else {
                        messageImage.setVisibility(View.GONE);
                    }
                    if (null != areaMessage.widgetPlain) {
                        messagePlain.setVisibility(View.VISIBLE);
                        buildAreaMessagePlain(choiceDialog, messagePlain, areaMessage.widgetPlain);
                        // 计算plain控件weight
                        if (weightTotal <= 0f) {
                            messagePlain.setVisibility(View.GONE);
                        } else {
                            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) messagePlain.getLayoutParams();
                            lp.weight = weightTotal;
                            messagePlain.setLayoutParams(lp);
                        }
                    } else {
                        messagePlain.setVisibility(View.GONE);
                    }
                }
            }
        } else {
            choiceDialog.setDialogMessageVisibility(false);
        }
    }

    private void buildAreaMessagePlain(final ChoiceDialog choiceDialog, TextView messagePlain, final WidgetPlain widgetPlain) {
        messagePlain.setText(widgetPlain.text);
        setBackgroundColor(messagePlain, widgetPlain.bgColor);
        setTextColor(messagePlain, widgetPlain.txColor);
        messagePlain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performNavigate(widgetPlain.schemeUrl);
                if (widgetPlain.closeWhenClick) {
                    choiceDialog.hideSelf();
                }
            }
        });
        messagePlain.setMovementMethod(ScrollingMovementMethod.getInstance());
        // 设置padding
        int paddingLeft = (widgetPlain.paddingLeft >= 0 ? widgetPlain.paddingLeft : messagePlain.getPaddingLeft());
        int paddingRight = (widgetPlain.paddingRight >= 0 ? widgetPlain.paddingRight : messagePlain.getPaddingRight());
        int paddingTop = (widgetPlain.paddingTop >= 0 ? widgetPlain.paddingTop : messagePlain.getPaddingTop());
        int paddingBottom = (widgetPlain.paddingBottom >= 0 ? widgetPlain.paddingBottom : messagePlain.getPaddingBottom());
        messagePlain.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    private void buildAreaMessageImage(final ChoiceDialog choiceDialog, RemoteImageView messageImage, final WidgetImage widgetImage) {
        messageImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        messageImage.load(widgetImage.url);
        messageImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performNavigate(widgetImage.schemeUrl);
                if (widgetImage.closeWhenClick) {
                    choiceDialog.hideSelf();
                }
            }
        });
    }

    private AreaTitle generateAreaTitle(JSONObject jsonObjectArea) {
        AreaTitle area = null;
        if (null != jsonObjectArea) {
            area = new AreaTitle();
            NavParamParserResult navParamParserResult = new NavParamParserResult(jsonObjectArea);
            float height = navParamParserResult.getParamFloat("height", 0f);
            int style = navParamParserResult.getParamInt("style", AreaTitle.DIALOG_TITLE_STYLE_ONLY_PLAIN);
            WidgetPlain widgetPlain = generateWidgetPlain(navParamParserResult.getParamJsonObject("plain", null), WidgetPlain.SOURCE_TITLE);
            WidgetDivide widgetDivide = generateWidgetDivide(navParamParserResult.getParamJsonObject("divide", null));
            // style默认值策略
            if (style != AreaTitle.DIALOG_TITLE_STYLE_ONLY_PLAIN) {
                style = AreaTitle.DIALOG_TITLE_STYLE_ONLY_PLAIN;
            }
            area.height = height;
            area.style = style;
            area.widgetPlain = widgetPlain;
            area.widgetDivide = widgetDivide;
        }
        return area;
    }

    private AreaMessage generateAreaMessage(JSONObject jsonObjectArea) {
        AreaMessage area = null;
        if (null != jsonObjectArea) {
            area = new AreaMessage();
            NavParamParserResult navParamParserResult = new NavParamParserResult(jsonObjectArea);
            float height = navParamParserResult.getParamFloat("height", 0f);
            int style = navParamParserResult.getParamInt("style", AreaMessage.DIALOG_MESSAGE_STYLE_ONLY_PLAIN);
            WidgetPlain widgetPlain = generateWidgetPlain(navParamParserResult.getParamJsonObject("plain", null), WidgetPlain.SOURCE_MESSAGE);
            WidgetImage widgetImage = generateWidgetImage(navParamParserResult.getParamJsonObject("image", null));
            // style默认值策略
            if (style != AreaMessage.DIALOG_MESSAGE_STYLE_ONLY_PLAIN
                    && style != AreaMessage.DIALOG_MESSAGE_STYLE_ONLY_IMAGE
                    && style != AreaMessage.DIALOG_MESSAGE_STYLE_IMAGE_UPON_PLAIN) {
                style = AreaMessage.DIALOG_MESSAGE_STYLE_ONLY_PLAIN;
            }
            area.height = height;
            area.style = style;
            area.widgetPlain = widgetPlain;
            area.widgetImage = widgetImage;
        }
        return area;
    }

    private AreaButton generateAreaButton(JSONObject jsonObjectArea) {
        AreaButton area = null;
        if (null != jsonObjectArea) {
            area = new AreaButton();
            NavParamParserResult navParamParserResult = new NavParamParserResult(jsonObjectArea);
            List<WidgetButton> widgetButtons = generateWidgetButtonList(navParamParserResult.getParamJsonArray("button", null));
            WidgetDivide widgetDivide = generateWidgetDivide(navParamParserResult.getParamJsonObject("divide", null));
            area.widgetButtons = widgetButtons;
            area.widgetDivide = widgetDivide;
        }
        return area;
    }

    private AreaClose generateAreaClose(JSONObject jsonObjectArea) {
        AreaClose area = null;
        if (null != jsonObjectArea) {
            area = new AreaClose();
            NavParamParserResult navParamParserResult = new NavParamParserResult(jsonObjectArea);
            int padding = navParamParserResult.getParamInt("padding", AreaClose.DEFAULT_PADDING);
            area.padding = padding;
        }
        return area;
    }

    private List<WidgetButton> generateWidgetButtonList(JSONArray jsonArrayButton) {
        List<WidgetButton> widgetButtonList = null;
        if (null != jsonArrayButton && jsonArrayButton.size() > 0) {
            widgetButtonList = new ArrayList<>();
            for (int index = 0; index < jsonArrayButton.size(); index++) {
                JSONObject jsonObjectButton = jsonArrayButton.getJSONObject(index);
                NavParamParserResult navParamParserResultButton = new NavParamParserResult(jsonObjectButton);
                String id = navParamParserResultButton.getParamString("itemId", null);
                String text = navParamParserResultButton.getParamString("text", null);
                String schemeUrl = navParamParserResultButton.getParamString("schemeUrl", null);
                String bgColor = navParamParserResultButton.getParamString("bgColor", null);
                String txColor = navParamParserResultButton.getParamString("txColor", null);
                boolean closeWhenClick = navParamParserResultButton.getParamBoolean("closeWhenClick", true);
                if (null == text) {
                    text = "";
                }
                WidgetButton widgetButton = new WidgetButton();
                widgetButton.id = id;
                widgetButton.text = text;
                widgetButton.schemeUrl = schemeUrl;
                widgetButton.closeWhenClick = closeWhenClick;
                widgetButton.bgColor = bgColor;
                widgetButton.txColor = txColor;
                widgetButtonList.add(widgetButton);
            }
        }
        return widgetButtonList;
    }

    private WidgetPlain generateWidgetPlain(JSONObject jsonObjectPlain, int source) {
        WidgetPlain widgetPlain = null;
        if (null != jsonObjectPlain) {
            NavParamParserResult navParamParserResult = new NavParamParserResult(jsonObjectPlain);
            float height = navParamParserResult.getParamFloat("height", 0f);
            String text = navParamParserResult.getParamString("text", null);
            String schemeUrl = navParamParserResult.getParamString("schemeUrl", null);
            boolean closeWhenClick = navParamParserResult.getParamBoolean("closeWhenClick", false);
            String bgColor = navParamParserResult.getParamString("bgColor", null);
            String txColor = navParamParserResult.getParamString("txColor", null);
            int paddingLeft = navParamParserResult.getParamInt("paddingLeft", WidgetPlain.DEFAULT_PADDING);
            int paddingRight = navParamParserResult.getParamInt("paddingRight", WidgetPlain.DEFAULT_PADDING);
            int paddingTop = navParamParserResult.getParamInt("paddingTop", WidgetPlain.DEFAULT_PADDING);
            int paddingBottom = navParamParserResult.getParamInt("paddingBottom", WidgetPlain.DEFAULT_PADDING);
            // 是否可滚动
            boolean defaultCanScroll = false;
            if (source == WidgetPlain.SOURCE_TITLE) {
                defaultCanScroll = true;
            } else if (source == WidgetPlain.SOURCE_MESSAGE) {
                defaultCanScroll = false;
            }
            boolean canScroll = navParamParserResult.getParamBoolean("canScroll", defaultCanScroll);
            if (null == text) {
                text = "";
            }
            widgetPlain = new WidgetPlain();
            widgetPlain.height = height;
            widgetPlain.text = text;
            widgetPlain.schemeUrl = schemeUrl;
            widgetPlain.closeWhenClick = closeWhenClick;
            widgetPlain.bgColor = bgColor;
            widgetPlain.txColor = txColor;
            widgetPlain.canScroll = canScroll;
            widgetPlain.paddingLeft = paddingLeft;
            widgetPlain.paddingRight = paddingRight;
            widgetPlain.paddingTop = paddingTop;
            widgetPlain.paddingBottom = paddingBottom;
        }
        return widgetPlain;
    }

    private WidgetImage generateWidgetImage(JSONObject jsonObjectImage) {
        WidgetImage widgetImage = null;
        if (null != jsonObjectImage) {
            NavParamParserResult navParamParserResult = new NavParamParserResult(jsonObjectImage);
            float height = navParamParserResult.getParamFloat("height", 0f);
            String url = navParamParserResult.getParamString("url", null);
            String schemeUrl = navParamParserResult.getParamString("schemeUrl", null);
            boolean closeWhenClick = navParamParserResult.getParamBoolean("closeWhenClick", false);
            int type = navParamParserResult.getParamInt("type", WidgetImage.WIDGET_IMAGE_TYPE_CENTER_CROP);
            widgetImage = new WidgetImage();
            widgetImage.height = height;
            widgetImage.url = url;
            widgetImage.schemeUrl = schemeUrl;
            widgetImage.closeWhenClick = closeWhenClick;
            widgetImage.type = type;
        }
        return widgetImage;
    }

    private WidgetDivide generateWidgetDivide(JSONObject jsonObjectDivide) {
        WidgetDivide widgetDivide = null;
        if (null != jsonObjectDivide) {
            NavParamParserResult navParamParserResult = new NavParamParserResult(jsonObjectDivide);
            String bgColor = navParamParserResult.getParamString("bgColor", null);
            widgetDivide = new WidgetDivide();
            widgetDivide.bgColor = bgColor;
        }
        return widgetDivide;
    }

    private void performTrackPageEnter(ChoiceDialog dialog, String annourceId, String annourceType) {
//        Map<String, String> properties = new LinkedHashMap<>();
//        properties.put("annourceId", null != annourceId ? annourceId : "");
//        properties.put("annourceType", null != annourceType ? annourceType : "");
//        Track.enterPage(dialog, PageName.ACCSDIALOG, NodeB.ACCSDIALOG, properties);
    }

    private void performTrackPageLeave(ChoiceDialog dialog) {
//        Track.leavePage(dialog);
    }

    private void performTrackClick(String annourceId, String annourceType, String widgetId) {
//        Properties properties = new Properties();
//        properties.put("annourceId", null != annourceId ? annourceId : "");
//        properties.put("annourceType", null != annourceType ? annourceType : "");
//        properties.put("itemId", null != widgetId ? widgetId : "");
//        Track.commitClick(SpmDict.ACCSDIALOG_DIALOG_ITEM, properties);
    }

    private void performNavigate(String url) {
//        if (!TextUtils.isEmpty(url)) {
//            Nav.fromUrl(url).nav();
//        }
    }

    private boolean setBackgroundColor(View view, String colorStr) {
        boolean result = false;
        if (null != view) {
            Integer color = convertColor(colorStr);
            if (null != color) {
                view.setBackgroundColor(color);
                result = true;
            }
        }
        return result;
    }

    private boolean setTextColor(TextView view, String colorStr) {
        boolean result = false;
        if (null != view) {
            Integer color = convertColor(colorStr);
            if (null != color) {
                view.setTextColor(color);
                result = true;
            }
        }
        return result;
    }

    private Integer convertColor(String colorStr) {
        Integer color = null;
        if (!TextUtils.isEmpty(colorStr)) {
            try {
                color = Color.parseColor(colorStr);
            } catch (Exception e) {
                // color解析无效
            }
        }
        return color;
    }

    public static class AreaTitle {
        public static final int DIALOG_TITLE_STYLE_ONLY_PLAIN = 0;
        public float height;
        public int style;
        public WidgetPlain widgetPlain;
        public WidgetDivide widgetDivide;
    }

    public static class AreaMessage {
        public static final int DIALOG_MESSAGE_STYLE_ONLY_PLAIN = 0;
        public static final int DIALOG_MESSAGE_STYLE_ONLY_IMAGE = 1;
        public static final int DIALOG_MESSAGE_STYLE_IMAGE_UPON_PLAIN = 2;
        public float height;
        public int style;
        public WidgetPlain widgetPlain;
        public WidgetImage widgetImage;
    }

    public static class AreaButton {
        public List<WidgetButton> widgetButtons;
        public WidgetDivide widgetDivide;
    }

    public static class AreaClose {
        public static final int DEFAULT_PADDING = -1;
        public int padding;
    }

    public static class WidgetButton {
        public String id;
        public String text;
        public String schemeUrl;
        public boolean closeWhenClick;
        public String bgColor;
        public String txColor;
    }

    public static class WidgetPlain {
        public static final int DEFAULT_PADDING = -1;
        public static final int SOURCE_TITLE = 0;
        public static final int SOURCE_MESSAGE = 1;
        public float height;
        public String text;
        public String schemeUrl;
        public boolean closeWhenClick;
        public String bgColor;
        public String txColor;
        public boolean canScroll;
        public int paddingLeft;
        public int paddingRight;
        public int paddingTop;
        public int paddingBottom;
    }

    public static class WidgetImage {
        public static final int WIDGET_IMAGE_TYPE_CENTER_CROP = 0;
        public float height;
        public String url;
        public String schemeUrl;
        public boolean closeWhenClick;
        public int type;
    }

    public static class WidgetDivide {
        public String bgColor;
    }

}
