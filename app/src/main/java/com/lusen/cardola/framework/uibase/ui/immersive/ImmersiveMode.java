package com.lusen.cardola.framework.uibase.ui.immersive;

import android.view.View;

import com.lusen.cardola.R;

/**
 * Created by leo on 16/10/13.
 */
public class ImmersiveMode {

    public boolean mImmersiveStatusBar;
    public boolean mImmersiveNavBar;
    public boolean mImmersiveFeatureForV21;

    public static void updateViewImmersiveNoPadding(View view, Boolean immersiveNoPadding) {
        if (null != view && null != immersiveNoPadding) {
            view.setTag(R.id.uibase_immersive_no_padding, immersiveNoPadding);
        }
    }

    public static void updateViewImmersiveStatusBar(View view, Boolean immersiveStatusBar) {
        if (null != view && null != immersiveStatusBar) {
            view.setTag(R.id.uibase_immersive_status_bar, immersiveStatusBar);
        }
    }

    public static void updateViewImmersiveNavBar(View view, Boolean immersiveNavBar) {
        if (null != view && null != immersiveNavBar) {
            view.setTag(R.id.uibase_immersive_nav_bar, immersiveNavBar);
        }
    }

    public static boolean isViewImmersiveNoPadding(View view) {
        if (null != view) {
            Object tag = view.getTag(R.id.uibase_immersive_no_padding);
            if (null != tag && tag instanceof Boolean && ((Boolean) tag).booleanValue()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isViewImmersiveStatusBar(View view) {
        if (null != view) {
            Object tag = view.getTag(R.id.uibase_immersive_status_bar);
            if (null != tag && tag instanceof Boolean && ((Boolean) tag).booleanValue()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isViewImmersiveNavBar(View view) {
        if (null != view) {
            Object tag = view.getTag(R.id.uibase_immersive_nav_bar);
            if (null != tag && tag instanceof Boolean && ((Boolean) tag).booleanValue()) {
                return true;
            }
        }
        return false;
    }

}
