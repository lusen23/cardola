package com.lusen.cardola.framework.uikit.choicedialogxm;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lusen.cardola.R;
import com.lusen.cardola.framework.adapter.HolderViewItem;
import com.lusen.cardola.framework.adapter.IAdapterData;
import com.lusen.cardola.framework.util.ContextUtil;
import com.lusen.cardola.framework.util.UiUtil;

/**
 * Created by leo on 2014/12/3.<br><br>
 */
public class MultiItemHolderView extends HolderViewItem {

    private static ColorStateList sDefaultItemTitleTextColor = ContextUtil.getContext().getResources().getColorStateList(R.color.xm_choice_dialog_item_title_text_color);
    private static int sDefaultItemTitleBgColor = R.drawable.xm_choice_dialog_item_title_bg_color;
    private static int sDefaultItemDivideBgColor = R.drawable.xm_choice_dialog_item_divide_bg_color;

    private TextView mTvChoiceDialogItemTitle;
    private View mViewChoiceDialogItemDivide;

    private Callback mCallback;

    public MultiItemHolderView(Context context) {
        super(context);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.xm_choice_dialog_item;
    }

    @Override
    public void bindView(View rootView) {
        this.setClickable(true);
        mTvChoiceDialogItemTitle = UiUtil.findViewById(rootView, R.id.xm_choice_dialog_item_title, TextView.class);
        mViewChoiceDialogItemDivide = UiUtil.findViewById(rootView, R.id.xm_choice_dialog_item_divide, View.class);
    }

    @Override
    public void bindData(final IAdapterData data, final int position) {
        if (null != data) {
            MultiItem multiItem = (MultiItem) data;
            // 文案
            mTvChoiceDialogItemTitle.setText(multiItem.getItemTitle());
            // 背景颜色
            if (!TextUtils.isEmpty(multiItem.mItemBgColor)) {
                try {
                    int color = Color.parseColor(multiItem.mItemBgColor);
                    mTvChoiceDialogItemTitle.setBackgroundColor(color);
                } catch (Exception e) {
                    // color解析无效
                    mTvChoiceDialogItemTitle.setBackgroundResource(sDefaultItemTitleBgColor);
                }
            } else {
                mTvChoiceDialogItemTitle.setBackgroundResource(sDefaultItemTitleBgColor);
            }
            // 文字颜色
            if (!TextUtils.isEmpty(multiItem.mItemTxColor)) {
                try {
                    int color = Color.parseColor(multiItem.mItemTxColor);
                    mTvChoiceDialogItemTitle.setTextColor(color);
                } catch (Exception e) {
                    // color解析无效
                    mTvChoiceDialogItemTitle.setTextColor(sDefaultItemTitleTextColor);
                }
            } else {
                mTvChoiceDialogItemTitle.setTextColor(sDefaultItemTitleTextColor);
            }
            // 分割线显隐及背景颜色
            if (multiItem.mShowDivide) {
                if (!TextUtils.isEmpty(multiItem.mItemDivideColor)) {
                    try {
                        int color = Color.parseColor(multiItem.mItemDivideColor);
                        mViewChoiceDialogItemDivide.setBackgroundColor(color);
                    } catch (Exception e) {
                        // color解析无效
                        mViewChoiceDialogItemDivide.setBackgroundResource(sDefaultItemDivideBgColor);
                    }
                } else {
                    mViewChoiceDialogItemDivide.setBackgroundResource(sDefaultItemDivideBgColor);
                }
                mViewChoiceDialogItemDivide.setVisibility(View.VISIBLE);
            } else {
                mViewChoiceDialogItemDivide.setVisibility(View.GONE);
            }
            // 点击监听
            this.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != mCallback) mCallback.onMultiItemClick(data, position);
                }
            });
        }
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public interface Callback {
        void onMultiItemClick(IAdapterData data, int position);
    }

}
