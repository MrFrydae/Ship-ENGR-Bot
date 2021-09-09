package edu.ship.engr.discordbot.utils;

import java.util.Calendar;

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
}
