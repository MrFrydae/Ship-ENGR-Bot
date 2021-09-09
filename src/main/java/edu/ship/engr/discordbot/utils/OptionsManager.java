package edu.ship.engr.discordbot.utils;

public class OptionsManager {
    private static OptionsManager singleton;
    private boolean testMode = false;
    private boolean devMode = false;

    /**
     * Returns the singleton object for this class.
     *
     * @return the singleton object
     */
    public static OptionsManager getSingleton() {
        if (singleton == null) {
            singleton = new OptionsManager();
        }

        return singleton;
    }

    public OptionsManager setTestMode(boolean testMode) {
        this.testMode = testMode;

        return this;
    }

    public boolean isTestMode() {
        return testMode;
    }

    public OptionsManager setDevMode(boolean devMode) {
        this.devMode = devMode;

        return this;
    }

    public boolean isDevMode() {
        return devMode;
    }
}
