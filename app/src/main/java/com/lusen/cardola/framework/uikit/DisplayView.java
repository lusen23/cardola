package com.lusen.cardola.framework.uikit;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.bumptech.glide.Glide;

/**
 * Created by leo on 2017/7/15.
 */

public class DisplayView extends android.support.v7.widget.AppCompatImageView {

    public DisplayView(Context context) {
        super(context);
    }

    public DisplayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DisplayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void load(String url) {
        Glide.with(getContext()).load(url).into(this);
    }

}
