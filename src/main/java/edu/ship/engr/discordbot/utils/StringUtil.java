package edu.ship.engr.discordbot.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;

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

    /**
     * Checks if the two strings are equal
     *
     * Wrapper method for String#equals/IgnoreCase()
     *
     * @param actual The actual string
     * @param expected The expected string
     * @param ignoreCase Should we match against capital letters
     * @return if the two strings are equal
     */
    public static boolean equals(String actual, String expected, boolean ignoreCase) {
        return ignoreCase ? actual.equalsIgnoreCase(expected) : actual.equals(expected);
    }

    //<editor-fold desc="Joining">
    public static String join(Object[] objects) {
        return StringUtils.join(objects);
    }

    public static String join(String[] strings) {
        return join(strings, " ");
    }

    public static String join(String[] strings, String separator) {
        return StringUtils.join(strings, separator);
    }

    public static String join(List<String> strings) {
        return join(strings, " ");
    }

    public static String join(List<String> strings, String separator) {
        return StringUtils.join(strings, separator);
    }

    public static String join(Set<String> strings) {
        return join(strings, " ");
    }

    public static String join(Set<String> strings, String separator) {
        return StringUtils.join(strings, separator);
    }
    //</editor-fold>
}
