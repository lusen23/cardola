package com.lusen.cardola.framework.uikit;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.bumptech.glide.Glide;

/**
 * Created by leo on 2017/7/15.
 */

public class RemoteImageView extends android.support.v7.widget.AppCompatImageView {

    public RemoteImageView(Context context) {
        super(context);
    }

    public RemoteImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RemoteImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void load(String url) {
        Glide.with(getContext()).load(url).into(this);
    }

}
