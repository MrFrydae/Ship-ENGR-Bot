package edu.ship.engr.discordbot.utils;

import lombok.Getter;
import lombok.Setter;

public class OptionsManager {
    private static OptionsManager singleton;
    @Getter @Setter private boolean testMode = false;
    @Getter @Setter private boolean devMode = false;

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
}
