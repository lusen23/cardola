package com.lusen.cardola.framework.uikit.choicedialogxm.cmd.core;

import android.support.annotation.IntDef;

/**
 * Created by leo on 2017/7/15.
 */

public class PropertyConstants {

    @IntDef({AreaTitleStyle.PLAIN})
    public @interface AreaTitleStyle {
        int PLAIN = 0;
    }

    @IntDef({AreaMessageStyle.PLAIN, AreaMessageStyle.IMAGE, AreaMessageStyle.IMAGE_PLAIN_FROM_TOP_TO_BOTTOM})
    public @interface AreaMessageStyle {
        int PLAIN = 0;
        int IMAGE = 1;
        int IMAGE_PLAIN_FROM_TOP_TO_BOTTOM = 2;
    }

    @IntDef({WidgetImageType.CENTER_CROP})
    public @interface WidgetImageType {
        int CENTER_CROP = 0;
    }

}
