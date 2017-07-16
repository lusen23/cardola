package com.lusen.cardola.framework.uikit.choicedialogxm;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lusen.cardola.R;
import com.lusen.cardola.framework.adapter.HolderViewAdapter;
import com.lusen.cardola.framework.adapter.HolderViewItem;
import com.lusen.cardola.framework.adapter.IAdapterData;
import com.lusen.cardola.framework.uibase.base.BaseDialogFragmentSystem;
import com.lusen.cardola.framework.util.ContextUtil;
import com.lusen.cardola.framework.util.KeyboardUtil;
import com.lusen.cardola.framework.util.UiUtil;

import java.util.List;

/**
 * Created by leo on 2014/12/2.<br><br>
 */
public class ChoiceDialog extends BaseDialogFragmentSystem {

    public View mRootView;

    public ViewGroup mLayoutChoiceRoot;

    public ViewGroup mLayoutChoiceDialogTitle;
    public TextView mTvChoiceDialogTitle;
    public View mDivideChoiceDialogTitle;

    public ViewGroup mLayoutChoiceDialogMessage;
    public TextView mTvChoiceDialogMessage;
    public CheckBox mCbChoiceDialogMessage;
    public EditText mEtvChoiceDialogMessage;
    public View mDivideChoiceDialogMessage;

    public View mViewBottom;
    public TextView mTvChoiceDialogPositiveButton;
    public TextView mTvChoiceDialogNegativeButton;
    public ListView mLvChoiceDialogMultiList;
    public TextView mTvChoiceDialogSubTitle;
    public View mDivideChoiceDialogButton;

    private String mDialogTitle;
    private boolean mDialogTitleVisible = true;
    private boolean mDialogTitleDividerVisible = true;
    private boolean mNeedDialogTitleAlignLeft = false;

    private String mDialogSubTitle;
    private boolean mDialogSubTitleVisible = false;
    private View.OnClickListener mDialogSubTitleClickListener;

    private ChoiceDialogMessageStyle mDialogMessageStyle;
    private String mDialogMessage;
    private boolean mDialogMessageChecked;
    private boolean mDialogMessageVisible = true;
    private boolean mDialogMessageDividerVisible = true;
    private boolean mDialogMessageGravityChanged = false;
    private int mDialogMessageGravity = Gravity.CENTER;

    private ChoiceDialogStyle mChoiceDialogStyle;
    private String mPositiveTitle;
    private String mNegativeTitle;
    private List<MultiItem> mMultiItemList;
    private HolderViewAdapter mAdapter;
    private boolean mDialogBottomVisible = true;
    private boolean mDialogButtonDividerVisible = false;

    public View mCustomTitleView;
    public View mCustomMessageView;
    public View mCustomView;

    private boolean mFullCustomTitleView;
    private boolean mFullCustomMessageView;
    private boolean mFullCustomView;

    private DialogStyleSingleCallback mDialogStyleSingleCallback;
    private DialogStyleCoupleCallback mDialogStyleCoupleCallback;
    private DialogStyleMultiCallback mDialogStyleMultiCallback;

    private Object mExtra;

    private Integer mLayoutChoiceDialogMessageHeight;
    private Integer mLayoutChoiceDialogMessageMaxHeight;
    private Integer mLayoutChoiceDialogMessageMinHeight;

    public static ChoiceDialog getInstance() {
        ChoiceDialog choiceDialog = new ChoiceDialog();
        return choiceDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHideWhenStop(false);
        setCancelable(isCancelable());
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.choiceDialogXM);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.xm_choice_dialog_layout, null, false);
        mLayoutChoiceRoot = UiUtil.findViewById(mRootView, R.id.xm_choice_dialog_root_layout, ViewGroup.class);
        mLayoutChoiceDialogTitle = UiUtil.findViewById(mRootView, R.id.xm_choice_dialog_title_layout, ViewGroup.class);
        mTvChoiceDialogTitle = UiUtil.findViewById(mRootView, R.id.xm_choice_dialog_title, TextView.class);
        mTvChoiceDialogSubTitle = UiUtil.findViewById(mRootView, R.id.xm_choice_dialog_sub_title, TextView.class);
        mDivideChoiceDialogTitle = UiUtil.findViewById(mRootView, R.id.xm_choice_dialog_title_divide, View.class);
        mLayoutChoiceDialogMessage = UiUtil.findViewById(mRootView, R.id.xm_choice_dialog_message_layout, ViewGroup.class);
        mTvChoiceDialogMessage = UiUtil.findViewById(mRootView, R.id.xm_choice_dialog_message_tv, TextView.class);
        mCbChoiceDialogMessage = UiUtil.findViewById(mRootView, R.id.xm_choice_dialog_message_cb, CheckBox.class);
        mEtvChoiceDialogMessage = UiUtil.findViewById(mRootView, R.id.xm_choice_dialog_message_etv, EditText.class);
        mDivideChoiceDialogMessage = UiUtil.findViewById(mRootView, R.id.xm_choice_dialog_message_divide, View.class);
        mDivideChoiceDialogButton = UiUtil.findViewById(mRootView, R.id.xm_choice_dialog_button_divide, View.class);
        mTvChoiceDialogMessage.setMovementMethod(ScrollingMovementMethod.getInstance());
        mTvChoiceDialogMessage.setScrollbarFadingEnabled(false);
        // 如果存在CustomView
        if (null != mCustomView) {
            try {
                ViewGroup parentView = (ViewGroup) mCustomView.getParent();
                if (null != parentView) {
                    parentView.removeView(mCustomView);
                }
                mLayoutChoiceRoot.removeAllViews();
                if (mFullCustomView) {
                    mLayoutChoiceRoot.addView(mCustomView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                } else {
                    mLayoutChoiceRoot.addView(mCustomView);
                }
            } catch (Exception e) {
            }
            return mRootView;
        }
        // 设置Dialog标题
        if (mDialogTitleVisible) {
            // 如果存在CustomTitleView
            if (null != mCustomTitleView) {
                try {
                    ViewGroup parentView = (ViewGroup) mCustomTitleView.getParent();
                    if (null != parentView) {
                        parentView.removeView(mCustomTitleView);
                    }
                    mLayoutChoiceDialogTitle.removeAllViews();
                    if (mFullCustomTitleView) {
                        mLayoutChoiceDialogTitle.addView(mCustomTitleView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    } else {
                        mLayoutChoiceDialogTitle.addView(mCustomTitleView);
                    }
                } catch (Exception e) {
                }
            } else {
                mLayoutChoiceDialogTitle.setVisibility(View.VISIBLE);
                // 设置DialogTitle居左
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mTvChoiceDialogTitle.getLayoutParams();
                if (mNeedDialogTitleAlignLeft) {
                    lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                } else {
                    lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
                }
                mTvChoiceDialogTitle.setLayoutParams(lp);
                mTvChoiceDialogTitle.setText(null == mDialogTitle ? "" : mDialogTitle);
                if (mDialogTitleDividerVisible) {
                    mDivideChoiceDialogTitle.setVisibility(View.VISIBLE);
                } else {
                    mDivideChoiceDialogTitle.setVisibility(View.INVISIBLE);
                }
            }
        } else {
            mLayoutChoiceDialogTitle.setVisibility(View.GONE);
        }
        // 设置Dialog内容
        if (mDialogMessageVisible) {
            // Message区域参数定制
            if (null != mLayoutChoiceDialogMessageHeight) {
                ViewGroup.LayoutParams lp = mLayoutChoiceDialogMessage.getLayoutParams();
                lp.height = mLayoutChoiceDialogMessageHeight;
                mLayoutChoiceDialogMessage.setLayoutParams(lp);
            }
            if (null != mLayoutChoiceDialogMessageMinHeight || null != mLayoutChoiceDialogMessageMaxHeight) {
                mLayoutChoiceDialogMessage.post(new Runnable() {
                    @Override
                    public void run() {
                        int height = mLayoutChoiceDialogMessage.getHeight();
                        ViewGroup.LayoutParams lp = mLayoutChoiceDialogMessage.getLayoutParams();
                        if (null != mLayoutChoiceDialogMessageMinHeight && height < mLayoutChoiceDialogMessageMinHeight) {
                            lp.height = mLayoutChoiceDialogMessageMinHeight;
                        }
                        if (null != mLayoutChoiceDialogMessageMaxHeight && height > mLayoutChoiceDialogMessageMaxHeight) {
                            lp.height = mLayoutChoiceDialogMessageMaxHeight;
                        }
                        mLayoutChoiceDialogMessage.setLayoutParams(lp);
                    }
                });
            }
            // 如果存在CustomMessageView
            if (null != mCustomMessageView) {
                try {
                    ViewGroup parentView = (ViewGroup) mCustomMessageView.getParent();
                    if (null != parentView) {
                        parentView.removeView(mCustomMessageView);
                    }
                    mLayoutChoiceDialogMessage.removeAllViews();
                    if (mFullCustomMessageView) {
                        mLayoutChoiceDialogMessage.addView(mCustomMessageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    } else {
                        mLayoutChoiceDialogMessage.addView(mCustomMessageView);
                    }
                } catch (Exception e) {
                }
            } else {
                mLayoutChoiceDialogMessage.setVisibility(View.VISIBLE);
                // 判断Dialog内容样式
                if (null != mDialogMessageStyle) {
                    switch (mDialogMessageStyle) {
                        case SINGLE_TEXT:
                            mTvChoiceDialogMessage.setVisibility(View.VISIBLE);
                            mCbChoiceDialogMessage.setVisibility(View.GONE);
                            mEtvChoiceDialogMessage.setVisibility(View.GONE);
                            mTvChoiceDialogMessage.setText(null == mDialogMessage ? "" : mDialogMessage);
                            if (mDialogMessageGravityChanged) {
                                mTvChoiceDialogMessage.setGravity(mDialogMessageGravity);
                            }
                            if (mDialogSubTitleVisible) {
                                mTvChoiceDialogSubTitle.setVisibility(View.VISIBLE);
                                if (!TextUtils.isEmpty(mDialogSubTitle)) {
                                    mTvChoiceDialogSubTitle.setText(mDialogSubTitle);
                                }
                                if (null != mDialogSubTitleClickListener) {
                                    mTvChoiceDialogSubTitle.setOnClickListener(mDialogSubTitleClickListener);
                                }
                            }
                            break;
                        case SINGLE_EDIT:
                            mTvChoiceDialogMessage.setVisibility(View.GONE);
                            mCbChoiceDialogMessage.setVisibility(View.GONE);
                            mEtvChoiceDialogMessage.setVisibility(View.VISIBLE);
                            mEtvChoiceDialogMessage.setSelectAllOnFocus(true);
                            mEtvChoiceDialogMessage.setText(null == mDialogMessage ? "" : mDialogMessage);
                            if (mDialogSubTitleVisible) {
                                mTvChoiceDialogSubTitle.setVisibility(View.VISIBLE);
                                if (!TextUtils.isEmpty(mDialogSubTitle)) {
                                    mTvChoiceDialogSubTitle.setText(mDialogSubTitle);
                                }
                                if (null != mDialogSubTitleClickListener) {
                                    mTvChoiceDialogSubTitle.setOnClickListener(mDialogSubTitleClickListener);
                                }
                            }
                            KeyboardUtil.showKeybBoard(ContextUtil.getContext(), mEtvChoiceDialogMessage);
                            break;
                        case CHECK_TEXT:
                            mTvChoiceDialogMessage.setVisibility(View.VISIBLE);
                            mCbChoiceDialogMessage.setVisibility(View.VISIBLE);
                            mEtvChoiceDialogMessage.setVisibility(View.GONE);
                            mTvChoiceDialogMessage.setText(null == mDialogMessage ? "" : mDialogMessage);
                            if (mDialogMessageGravityChanged) {
                                mTvChoiceDialogMessage.setGravity(mDialogMessageGravity);
                            }
                            mCbChoiceDialogMessage.setChecked(mDialogMessageChecked);
                            // Message文案增加点击监听,同步CheckBox选中状态变化
                            mTvChoiceDialogMessage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mCbChoiceDialogMessage.toggle();
                                }
                            });
                            if (mDialogSubTitleVisible) {
                                mTvChoiceDialogSubTitle.setVisibility(View.VISIBLE);
                                if (!TextUtils.isEmpty(mDialogSubTitle)) {
                                    mTvChoiceDialogSubTitle.setText(mDialogSubTitle);
                                }
                                if (null != mDialogSubTitleClickListener) {
                                    mTvChoiceDialogSubTitle.setOnClickListener(mDialogSubTitleClickListener);
                                }
                            }
                            break;
                        default:
                            break;
                    }
                }
                if (mDialogMessageDividerVisible) {
                    mDivideChoiceDialogMessage.setVisibility(View.VISIBLE);
                } else {
                    mDivideChoiceDialogMessage.setVisibility(View.INVISIBLE);
                }
            }
        } else {
            mLayoutChoiceDialogMessage.setVisibility(View.GONE);
        }
        // 设置Dialog底部操作区
        if (mDialogBottomVisible) {
            // 是否显示Button区域分割线
            if (mDialogButtonDividerVisible) {
                mDivideChoiceDialogButton.setVisibility(View.VISIBLE);
            } else {
                mDivideChoiceDialogButton.setVisibility(View.GONE);
            }
            if (null != mChoiceDialogStyle) {
                switch (mChoiceDialogStyle) {
                    case SINGLE:
                        mViewBottom = UiUtil.findViewById(mRootView, R.id.xm_choice_dialog_style_single, ViewStub.class).inflate();
                        mTvChoiceDialogPositiveButton = UiUtil.findViewById(mViewBottom, R.id.xm_choice_dialog_button_positive, TextView.class);
                        break;
                    case COUPLE:
                        mViewBottom = UiUtil.findViewById(mRootView, R.id.xm_choice_dialog_style_couple, ViewStub.class).inflate();
                        mTvChoiceDialogPositiveButton = UiUtil.findViewById(mViewBottom, R.id.xm_choice_dialog_button_positive, TextView.class);
                        mTvChoiceDialogNegativeButton = UiUtil.findViewById(mViewBottom, R.id.xm_choice_dialog_button_negative, TextView.class);
                        break;
                    case MULTI:
                        mViewBottom = UiUtil.findViewById(mRootView, R.id.xm_choice_dialog_style_multi, ViewStub.class).inflate();
                        mLvChoiceDialogMultiList = UiUtil.findViewById(mRootView, R.id.xm_choice_dialog_list, ListView.class);
                        break;
                }
            }
            // 设置Positive
            if (null != mTvChoiceDialogPositiveButton) {
                if (null == mPositiveTitle) {
                    mTvChoiceDialogPositiveButton.setText(R.string.xm_choice_dialog_sure);
                } else {
                    mTvChoiceDialogPositiveButton.setText(mPositiveTitle);
                }
                // 设置监听
                mTvChoiceDialogPositiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mChoiceDialogStyle == ChoiceDialogStyle.SINGLE) {
                            if (null != mDialogStyleSingleCallback) {
                                boolean result = mDialogStyleSingleCallback.onPositiveButtonClick();
                                if (!result) {
                                    hideSelf();
                                }
                            } else {
                                hideSelf();
                            }
                        } else if (mChoiceDialogStyle == ChoiceDialogStyle.COUPLE) {
                            if (null != mDialogStyleCoupleCallback) {
                                boolean result = mDialogStyleCoupleCallback.onPositiveButtonClick();
                                if (!result){
                                    hideSelf();
                                }
                            } else {
                                hideSelf();
                            }
                        }
                    }
                });
            }
            // 设置Negative
            if (null != mTvChoiceDialogNegativeButton) {
                if (null == mNegativeTitle) {
                    mTvChoiceDialogNegativeButton.setText(R.string.xm_choice_dialog_cancel);
                } else {
                    mTvChoiceDialogNegativeButton.setText(mNegativeTitle);
                }
                // 设置监听
                mTvChoiceDialogNegativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mChoiceDialogStyle == ChoiceDialogStyle.COUPLE) {
                            if (null != mDialogStyleCoupleCallback) {
                                boolean result = mDialogStyleCoupleCallback.onNegativeButtonClick();
                                if (!result) {
                                    hideSelf();
                                }
                            } else {
                                hideSelf();
                            }
                        }
                    }
                });
            }
            // 设置Multi List
            if (null != mLvChoiceDialogMultiList) {
                mAdapter = new HolderViewAdapter(ContextUtil.getContext(), mMultiItemList, MultiItemHolderView.class);
                mAdapter.setHolderViewCallback(new HolderViewAdapter.HolderViewCallback() {
                    @Override
                    public void onHolderViewInvalidate(HolderViewItem holderView, int position) {
                        if (null != holderView) {
                            MultiItemHolderView multiItemHolderView = (MultiItemHolderView) holderView;
                            multiItemHolderView.setCallback(new MultiItemHolderView.Callback() {
                                @Override
                                public void onMultiItemClick(IAdapterData data, int position) {
                                    if (null != mDialogStyleMultiCallback) {
                                        boolean result = mDialogStyleMultiCallback.onMutliItemClick((MultiItem) data, position);
                                        if (!result) {
                                            hideSelf();
                                        }
                                    }
                                }
                            });
                        }
                    }
                });
                mLvChoiceDialogMultiList.setAdapter(mAdapter);
            }
        }
        return mRootView;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        return dialog;
    }

    /**
     * 设置Dialog标题
     *
     * @param title
     */
    public void setDialogTitle(String title) {
        this.mDialogTitle = title;
    }

    /**
     * 设置Dialog标题可见性
     *
     * @param visible
     */
    public void setDialogTitleVisibility(boolean visible) {
        this.mDialogTitleVisible = visible;
    }

    /**
     * 设置Dialog标题分割线可见性
     *
     * @param visible
     */
    public void setDialogTitleDividerVisibility(boolean visible) {
        this.mDialogTitleDividerVisible = visible;
    }

    /**
     * 是否需要主标题居左
     *
     * @param needDialogTitleAlignLeft
     */
    public void setNeedDialogTitleAlignLeft(boolean needDialogTitleAlignLeft) {
        this.mNeedDialogTitleAlignLeft = needDialogTitleAlignLeft;
    }

    /**
     * 设置Dialog内容
     *
     * @param message
     */
    public void setDialogMessage(String message) {
        setDialogMessage(ChoiceDialogMessageStyle.SINGLE_TEXT, message, false);
    }

    /**
     * 设置Dialog内容
     *
     * @param messageStyle 内容样式(Null则默认为SING_TEXT样式)
     * @param message      内容原信息
     * @param checked      内容选中态
     */
    public void setDialogMessage(ChoiceDialogMessageStyle messageStyle, String message, boolean checked) {
        this.mDialogMessageStyle = messageStyle;
        this.mDialogMessage = message;
        this.mDialogMessageChecked = checked;
    }

    /**
     * 获取Dialog内容选中态
     *
     * @return
     */
    public boolean getDialogMessageChecked() {
        if (null != mCbChoiceDialogMessage)
            return mCbChoiceDialogMessage.isChecked();
        return false;
    }

    /**
     * 获取Dialog内容
     *
     * @return
     */
    public String getDialogMessage() {
        String message = null;
        if (null != mDialogMessageStyle) {
            switch (mDialogMessageStyle) {
                case SINGLE_TEXT:
                    if (null != mTvChoiceDialogMessage) {
                        message = mTvChoiceDialogMessage.getText().toString();
                    }
                    break;
                case SINGLE_EDIT:
                    if (null != mEtvChoiceDialogMessage) {
                        message = mEtvChoiceDialogMessage.getText().toString();
                    }
                    break;
                case CHECK_TEXT:
                    if (null != mTvChoiceDialogMessage) {
                        message = mTvChoiceDialogMessage.getText().toString();
                    }
                    break;
                default:
                    if (null != mTvChoiceDialogMessage) {
                        message = mTvChoiceDialogMessage.getText().toString();
                    }
                    break;
            }
        }
        return message;
    }

    /**
     * 设置Dialog内容可见性
     *
     * @param visible
     */
    public void setDialogMessageVisibility(boolean visible) {
        this.mDialogMessageVisible = visible;
    }

    /**
     * 设置Dialog内容分割线可见性
     *
     * @param visible
     */
    public void setDialogMessageDividerVisibility(boolean visible) {
        this.mDialogMessageDividerVisible = visible;
    }

    /**
     * 设置Dialog Single样式和设置
     *
     * @param positiveTitle (Null则采取默认)
     * @param callback
     */
    public void setDialogSingleStyleSetting(String positiveTitle, DialogStyleSingleCallback callback) {
        this.mChoiceDialogStyle = ChoiceDialogStyle.SINGLE;
        this.mPositiveTitle = positiveTitle;
        this.mDialogStyleSingleCallback = callback;
    }

    /**
     * 设置Dialog Couple样式和设置
     *
     * @param positiveTitle (Null则采取默认)
     * @param negativeTitle (Null则采取默认)
     * @param callback
     */
    public void setDialogCoupleStyleSetting(String positiveTitle, String negativeTitle, DialogStyleCoupleCallback callback) {
        this.mChoiceDialogStyle = ChoiceDialogStyle.COUPLE;
        this.mPositiveTitle = positiveTitle;
        this.mNegativeTitle = negativeTitle;
        this.mDialogStyleCoupleCallback = callback;
    }

    /**
     * 设置Dialog Multi样式和设置
     *
     * @param multiItemList (Null则采取默认)
     * @param callback
     */
    public void setDialogMultiStyleSetting(List<MultiItem> multiItemList, DialogStyleMultiCallback callback) {
        this.mChoiceDialogStyle = ChoiceDialogStyle.MULTI;
        this.mMultiItemList = multiItemList;
        this.mDialogStyleMultiCallback = callback;
    }

    /**
     * 设置SubTitle
     *
     * @param subTitle
     * @param clickListener
     */
    public void setSubTitileVisible(String subTitle, View.OnClickListener clickListener) {
        mDialogSubTitleVisible = true;
        mDialogSubTitle = subTitle;
        mDialogSubTitleClickListener = clickListener;
    }

    /**
     * 设置Dialog Bottom可见性
     *
     * @param dialogBottomVisible
     */
    public void setDialogBottomVisible(boolean dialogBottomVisible) {
        mDialogBottomVisible = dialogBottomVisible;
    }

    public void setCustomTitleView(View customTitleView) {
        mCustomTitleView = customTitleView;
    }

    public void setCustomMessageView(View customMessageView) {
        mCustomMessageView = customMessageView;
    }

    public void setCustomView(View customView) {
        mCustomView = customView;
    }

    public void setDialogMessageGravity(int gravity) {
        mDialogMessageGravityChanged = true;
        mDialogMessageGravity = gravity;
    }

    public void setExtra(Object extra) {
        this.mExtra = extra;
    }

    public Object getExtra() {
        return mExtra;
    }

    public void setLayoutChoiceDialogMessageHeight(int layoutChoiceDialogMessageHeight) {
        mLayoutChoiceDialogMessageHeight = layoutChoiceDialogMessageHeight;
    }

    public void setLayoutChoiceDialogMessageMaxHeight(int layoutChoiceDialogMessageMaxHeight) {
        mLayoutChoiceDialogMessageMaxHeight = layoutChoiceDialogMessageMaxHeight;
    }

    public void setLayoutChoiceDialogMessageMinHeight(int layoutChoiceDialogMessageMinHeight) {
        mLayoutChoiceDialogMessageMinHeight = layoutChoiceDialogMessageMinHeight;
    }

    public void setDialogButtonDividerVisible(boolean dialogButtonDividerVisible) {
        mDialogButtonDividerVisible = dialogButtonDividerVisible;
    }

    public void setFullCustomMessageView(boolean fullCustomMessageView) {
        mFullCustomMessageView = fullCustomMessageView;
    }

    public void setFullCustomView(boolean fullCustomView) {
        mFullCustomView = fullCustomView;
    }

    public void setFullCustomTitleView(boolean fullCustomTitleView) {
        mFullCustomTitleView = fullCustomTitleView;
    }

    public interface DialogStyleSingleCallback {
        /**
         * 当点击Positive控件(返回值True:维持ChoiceDialog,返回值False:关闭ChoiceDialog)
         *
         * @return
         */
        boolean onPositiveButtonClick();
    }

    public interface DialogStyleCoupleCallback {
        /**
         * 当点击Positive控件(返回值True:维持ChoiceDialog,返回值False:关闭ChoiceDialog)
         *
         * @return
         */
        boolean onPositiveButtonClick();

        /**
         * 当点击Negative控件(返回值True:维持ChoiceDialog,返回值False:关闭ChoiceDialog)
         *
         * @return
         */
        boolean onNegativeButtonClick();
    }

    public interface DialogStyleMultiCallback {
        /**
         * 当点击MultiItem(返回值True:维持ChoiceDialog,返回值False:关闭ChoiceDialog)
         *
         * @return
         */
        boolean onMutliItemClick(MultiItem multiItem, int position);
    }

}
