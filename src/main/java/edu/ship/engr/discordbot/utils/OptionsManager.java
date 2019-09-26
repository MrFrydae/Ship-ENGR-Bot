package edu.ship.engr.discordbot.utils;

public class OptionsManager {
    private static OptionsManager singleton;
    private final boolean testMode;

    private OptionsManager(boolean testMode) {
        this.testMode = testMode;
    }

    public static OptionsManager getSingleton(boolean testMode) {
        if (singleton == null) {
            singleton = new OptionsManager(testMode);
        } else {
            if (testMode != singleton.isTestMode()) {
                throw new IllegalArgumentException("Can't change test mode once OptionsManager exists");
            }
        }
        return singleton;
    }

    public static OptionsManager getSingleton() {
    	if (singleton != null)
    	{
    		return singleton;
    	}
        return getSingleton(false);
    }

    public boolean isTestMode() {
        return testMode;
    }
}
