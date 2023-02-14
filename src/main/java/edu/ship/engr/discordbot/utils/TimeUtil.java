package edu.ship.engr.discordbot.utils;

import org.jetbrains.annotations.Contract;

import java.util.Calendar;

/**
 * Utility for time parsing.
 */
public enum TimeUtil {
    SECOND(1), MINUTE(60), HOUR(MINUTE.seconds * 60), DAY(HOUR.seconds * 24),
    WEEK(DAY.seconds * 7), MONTH(DAY.seconds * 31), YEAR(DAY.seconds * 365);

    int seconds;

    TimeUtil(int seconds) {
        this.seconds = seconds;
    }

    public int inSeconds(int mod) {
        return seconds * mod;
    }

    public long inSeconds(long mod) {
        return seconds * mod;
    }

    public int inMilli(int mod) {
        return seconds * mod * 1000;
    }

    public long inMilli(long mod) {
        return seconds * mod * 1000;
    }

    public static Integer getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static Calendar getCurrentDate() {
        return Calendar.getInstance();
    }

    /**
     * Get the date for the end of spring semester.
     *
     * @return the date for the end of spring semester.
     */
    public static Calendar getEndOfSpringSemester() {
        Calendar endOfSpring = Calendar.getInstance();
        endOfSpring.set(getCurrentYear(), Calendar.JUNE, 30);
        return endOfSpring;
    }

    /**
     * Get the date for the end of fall semester.
     *
     * @return the date for the end of fall semester.
     */
    public static Calendar getEndOfFallSemester() {
        Calendar endOfFall = Calendar.getInstance();
        endOfFall.set(getCurrentYear(), Calendar.DECEMBER, 30);
        return endOfFall;
    }

    /**
     * Get the code for the current semester.
     *
     * @return the code for the current semester.
     */
    public static String getCurrentSemesterCode() {
        int month = getCurrentDate().get(Calendar.MONTH);
        int year = getCurrentDate().get(Calendar.YEAR);

        String mon;

        if (month < 6) {
            mon = "20";
        } else {
            mon = "60";
        }

        return year + mon;
    }

    /**
     * Creates a pretty string representing a duration in seconds.
     *
     * @param seconds number of seconds
     * @return a pretty string representing the duration
     */
    @Contract("null -> !null")
    public static String formatTime(Integer seconds) {
        return formatTime(seconds, 1);
    }

    /**
     * Creates a pretty string representing a duration in seconds.
     *
     * @param seconds number of seconds
     * @param depth number of recursion calls that can be made
     * @return a pretty string representing the duration
     */
    @Contract("null, _ -> !null")
    public static String formatTime(Integer seconds, int depth) {
        if (seconds == null || seconds < 5) {
            return "moments";
        }

        if (seconds < 60) {
            return seconds + " seconds";
        }

        if (seconds < 3600) {
            return formatTimeBlock("minute", 60, seconds, depth);
        }

        if (seconds < 86400) {
            return formatTimeBlock("hour", 3600, seconds, depth);
        }

        if (seconds < 31536000) {
            return formatTimeBlock("day", 86400, seconds, depth);
        }

        return formatTimeBlock("year", 31536000, seconds, depth);
    }

    private static String formatTimeBlock(String displayLabel, int interval, int seconds, int depth) {
        int count = seconds / interval;

        String display;
        if (count > 1) {
            display = String.format("%d %ss", count, displayLabel);
        } else {
            display = String.format("1 %s", displayLabel);
        }

        int remaining = seconds % interval;
        if (depth > 0 && remaining >= 5) {
            return String.format("%s, %s", display, formatTime(remaining, --depth));
        }

        return display;
    }
}
