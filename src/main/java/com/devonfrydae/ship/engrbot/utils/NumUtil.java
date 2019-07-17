package com.devonfrydae.ship.engrbot.utils;

public class NumUtil {
    public static Integer parseInt(String val) {
        return parseInt(val, null);
    }
    public static Integer parseInt(String val, Integer def) {
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static Long parseLong(String val) {
        return parseLong(val, null);
    }
    public static Long parseLong(String val, Long def) {
        try {
            return Long.parseLong(val);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static String max(String val1, String val2) {
        int max = Math.max(parseInt(val1), parseInt(val2));
        return Integer.toString(max);
    }
}
