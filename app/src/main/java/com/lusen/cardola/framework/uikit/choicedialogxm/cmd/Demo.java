package com.lusen.cardola.framework.uikit.choicedialogxm.cmd;

import com.lusen.cardola.framework.uikit.choicedialogxm.cmd.core.AccsDialog;
import com.lusen.cardola.framework.uikit.choicedialogxm.cmd.core.PropertyConstants;
import com.lusen.cardola.framework.uikit.choicedialogxm.cmd.core.WidgetButton;
import com.lusen.cardola.framework.uikit.choicedialogxm.cmd.core.WidgetImage;
import com.lusen.cardola.framework.uikit.choicedialogxm.cmd.core.WidgetPlain;

/**
 * Created by leo on 2017/7/15.
 */

public class Demo {

    public void demo() {
        AccsDialog dialog = new AccsDialog();
        dialog.closeWhenBack(false).timeout(60 * 1000);
        dialog.buildAreaMessage()
                .height(0.4f)
                .style(PropertyConstants.AreaMessageStyle.IMAGE_PLAIN_FROM_TOP_TO_BOTTOM)
                .plain(new WidgetPlain().height(0.6f).text("欢迎来到虾米音乐").schemeUrl("111111").closeWhenClick(false))
                .image(new WidgetImage().height(0.4f).url("http://pic61.nipic.com/file/20150309/615823_101434713000_2.jpg").schemeUrl("222222").closeWhenClick(false));
        dialog.buildAreaButton()
                .button(new WidgetButton().itemId("button_id_0").text("查看VIP信息").schemeUrl("xiami://album/101").closeWhenClick(true).bgColor("#EF4136").txColor("#FFFFFF"),
                        new WidgetButton().itemId("button_id_1").text("取消").schemeUrl("xiami://album/101").closeWhenClick(true).bgColor("#777777").txColor("#543545"));
        dialog.show();
    }

}
