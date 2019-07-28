package com.devonfrydae.ship.engrbot.utils;

public class StringUtil {
    public static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public static boolean isNotEmpty(String string) {
        return !isEmpty(string);
    }

    public static String getOrDefault(String string, String def) {
        return isEmpty(string) ? def : string;
    }
}
