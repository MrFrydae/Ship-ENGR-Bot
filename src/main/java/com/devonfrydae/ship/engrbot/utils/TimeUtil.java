package com.devonfrydae.ship.engrbot.utils;

public enum TimeUtil {
    SECOND(1), MINUTE(60), HOUR(60 * 60), DAY(60 * 60 * 24), WEEK(60 * 60 * 24 * 7), MONTH(60 * 60 * 24 * 31), YEAR(60 * 60 * 24 * 31 * 12);

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
}
