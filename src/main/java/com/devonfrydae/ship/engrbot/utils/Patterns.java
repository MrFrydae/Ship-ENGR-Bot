package com.devonfrydae.ship.engrbot.utils;

import org.intellij.lang.annotations.Language;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Patterns {
    public static final Pattern SPACE = Pattern.compile("\\s");
    public static final Pattern PIPE = Pattern.compile("\\|");
    public static final Pattern CLASS_NAME = Pattern.compile("([a-zA-Z]+)[-]?([0-9]+)");
    public static final Pattern VALID_EMAIL_PATTERN = Pattern.compile("(^[a-zA-Z0-9._%+-]+)@([a-zA-Z0-9.-]+)\\.[a-zA-Z]{2,4}$");
    public static final Pattern USER_MENTION = Pattern.compile("(?:<@!)?(\\d+)(?:>)?");

    /**
     * Gets a certain RegEx group from the provided string
     *
     * @param pattern The pattern to match
     * @param string The string to match against
     * @param groupId The RegEx group to get
     * @return The matched RegEx group
     */
    public static String getGroup(Pattern pattern, @Language("RegExp") String string, int groupId) {
        Matcher matcher = pattern.matcher(string);

        return matcher.matches() ? matcher.group(groupId) : null;
    }
}
