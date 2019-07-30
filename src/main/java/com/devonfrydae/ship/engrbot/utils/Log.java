package com.devonfrydae.ship.engrbot.utils;

import com.devonfrydae.ship.engrbot.DiscordBot;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

public class Log {
    private static Logger log = DiscordBot.getLogger();

    public static void debug(String message) {
        log.debug(message);
    }

    public static void info(String message) {
        log.info(message);
    }

    public static void warn(String message) {
        log.warn(message);
    }

    public static void error(String message) {
        log.error(message);
    }

    public static void exception(String msg) {
        exception(new Throwable(msg));
    }

    public static void exception(Throwable e) {
        exception(e.getMessage(), e);
    }

    public static void exception(String msg, Throwable e) {
        if (msg != null) {
            log.error(msg);
        }
        log.error(ExceptionUtils.getStackTrace(e));
    }
}