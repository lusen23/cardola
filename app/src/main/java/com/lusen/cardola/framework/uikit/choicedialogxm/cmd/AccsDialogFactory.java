package com.lusen.cardola.framework.uikit.choicedialogxm.cmd;

import com.lusen.cardola.framework.uikit.choicedialogxm.cmd.core.AccsDialog;
import com.lusen.cardola.framework.uikit.choicedialogxm.cmd.core.PropertyConstants;
import com.lusen.cardola.framework.uikit.choicedialogxm.cmd.core.WidgetButton;
import com.lusen.cardola.framework.uikit.choicedialogxm.cmd.core.WidgetPlain;

/**
 * Created by leo on 2017/7/15.
 */

public class AccsDialogFactory {

    public static void showMessageDialog(String message) {
        AccsDialog dialog = new AccsDialog();
        dialog.buildAreaMessage()
                .height(0.4f)
                .style(PropertyConstants.AreaMessageStyle.PLAIN)
                .plain(new WidgetPlain().height(1.0f).text(message));
        dialog.buildAreaButton()
                .button(new WidgetButton().text("确定").closeWhenClick(true).bgColor("#EF4136").txColor("#FFFFFF"));
        dialog.show();
    }

}
