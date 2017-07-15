package com.lusen.cardola.framework.uikit;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by leo on 2017/7/15.
 */

public class StateView extends FrameLayout {

    public StateView(@NonNull Context context) {
        super(context);
    }

    public StateView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StateView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
