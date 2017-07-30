package com.lusen.cardola.framework.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by leo on 2017/7/30.
 */

public class DayTimeUtil {

    public static String timeStamp2YMD(long timeStamp) {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date(timeStamp));
    }

}
