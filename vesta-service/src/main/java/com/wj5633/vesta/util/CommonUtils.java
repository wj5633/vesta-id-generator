package com.wj5633.vesta.util;

import java.util.Arrays;
import java.util.List;

/**
 * Created at 2019/7/16 15:16.
 *
 * @author wangjie
 * @version 1.0.0
 */

public class CommonUtils {

    public static List<String> SWITCH_ON_EXP = Arrays.asList("ON", "TRUE", "on", "true");

    public static List<String> SWITCH_OFF_EXP = Arrays.asList("OFF", "FALSE", "off", "false");

    public static boolean isOn(String swtch) {
        return SWITCH_ON_EXP.contains(swtch);
    }

    public static boolean isPropKeyOn(String key) {

        String prop = System.getProperty(key);

        return SWITCH_ON_EXP.contains(prop);
    }

}
