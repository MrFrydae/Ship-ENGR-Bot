package com.devonfrydae.ship.engrbot.utils;

import java.util.regex.Pattern;

public class Patterns {
    public static final Pattern SPACE = Pattern.compile("\\s");
    public static final Pattern PIPE = Pattern.compile("\\|");
    public static final Pattern CLASS_NAME = Pattern.compile("([a-zA-Z]+)[-]?([0-9]+)");
}
