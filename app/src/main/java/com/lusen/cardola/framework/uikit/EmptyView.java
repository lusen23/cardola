package com.lusen.cardola.framework.uikit;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lusen.cardola.R;
import com.lusen.cardola.framework.util.UiUtil;

/**
 * Created by leo on 2017/7/30.
 */

public class EmptyView extends FrameLayout {

    private TextView mEmptyTitleView;

    public EmptyView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public EmptyView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EmptyView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.empty_layout, this);
        mEmptyTitleView = UiUtil.findViewById(this, R.id.empty_title, TextView.class);
    }

    public void setEmptyTitle(String title) {
        if (null != title) {
            mEmptyTitleView.setText(title);
        }
    }

}
