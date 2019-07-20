package com.devonfrydae.ship.engrbot.utils;

import org.intellij.lang.annotations.Language;

import java.util.regex.Matcher;

public class Patterns {
    public static final Pattern SPACE = new Pattern("\\s");
    public static final Pattern PIPE = new Pattern("\\|");
    public static final Pattern CLASS_NAME = new Pattern("([a-zA-Z]+)[-]?([0-9]+)");
    public static final Pattern VALID_EMAIL_PATTERN = new Pattern("(^[a-zA-Z0-9._%+-]+)@([a-zA-Z0-9.-]+)\\.[a-zA-Z]{2,4}$");
    public static final Pattern USER_MENTION = new Pattern("(?:<@!)?(\\d+)(?:>)?");

    public static class Pattern {
        private java.util.regex.Pattern pattern;

        public Pattern(@Language("RegExp") String regex) {
            this.pattern = java.util.regex.Pattern.compile(regex);
        }

        public String getGroup(String string, int groupId) {
            Matcher matcher = matcher(string);

            return matcher.matches() ? matcher.group(groupId) : null;
        }

        public Matcher matcher(String string) {
            return pattern.matcher(string);
        }

        public boolean matches(String string) {
            return matcher(string).matches();
        }

        public String[] split(String string) {
            return pattern.split(string);
        }
    }
}
