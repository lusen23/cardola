package com.lusen.cardola.framework.uikit;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.lusen.cardola.R;
import com.lusen.cardola.framework.util.UiUtil;

/**
 * Created by leo on 2017/7/30.
 */

public class CommonItemView extends FrameLayout {

    public RemoteTextView mItemNameView;
    public RemoteTextView mItemSubNameView;
    public RemoteImageView mItemArrowView;
    public View mItemLineTop;
    public View mItemLineBottom;

    private CharSequence attrName;
    private CharSequence attrSubName;

    public CommonItemView(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }

    public CommonItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CommonItemView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        // 解析自定义属性
        if (null != attrs) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CommonItemView, defStyle, 0);
            if (a != null) {
                for (int i = a.getIndexCount() - 1; i >= 0; i--) {
                    int attr = a.getIndex(i);
                    if (attr == R.styleable.CommonItemView_name) {
                        attrName = a.getText(attr);
                    } else if (attr == R.styleable.CommonItemView_subname) {
                        attrSubName = a.getText(attr);
                    }
                }
                a.recycle();
            }
        }
        // 构造视图
        View view = inflate(context, R.layout.common_item_view, null);
        mItemNameView = UiUtil.findViewById(view, R.id.item_name, RemoteTextView.class);
        mItemSubNameView = UiUtil.findViewById(view, R.id.item_subname, RemoteTextView.class);
        mItemArrowView = UiUtil.findViewById(view, R.id.item_arrow, RemoteImageView.class);
        mItemLineTop = UiUtil.findViewById(view, R.id.item_line_top, View.class);
        mItemLineBottom = UiUtil.findViewById(view, R.id.item_line_bottom, View.class);
        addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置相关属性
        if (null != attrName) {
            setName(attrName + "");
        }
        if (null != attrSubName) {
            setSubName(attrSubName + "");
        }
    }

    public void setName(String name) {
        mItemNameView.setText(name);
    }

    public void setSubName(String name) {
        mItemSubNameView.setText(name);
    }

    public void setItemLineTopVisibility(int visibility) {
        mItemLineTop.setVisibility(visibility);
    }

    public void setItemLineBottomVisibility(int visibility) {
        mItemLineBottom.setVisibility(visibility);
    }

}
