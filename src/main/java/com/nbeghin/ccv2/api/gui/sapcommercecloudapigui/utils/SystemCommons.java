package com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.utils;

import org.apache.commons.lang3.SystemUtils;

public class SystemCommons {

    public static OS getOS() {
        if (SystemUtils.IS_OS_MAC) {
            return OS.MAC;
        }
        return OS.WINDOWS;
    }

    public static boolean isWinOS() {
        return SystemUtils.IS_OS_WINDOWS;
    }

    public static boolean isMac() {
        return SystemUtils.IS_OS_MAC;
    }

    public enum OS {
        WINDOWS, MAC, LINUX
    }

}
