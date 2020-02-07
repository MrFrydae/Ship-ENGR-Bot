package edu.ship.engr.discordbot.tasks;

import edu.ship.engr.discordbot.utils.TimeUtil;

import java.util.Timer;

public class Tasks {
    /**
     * Initialize tasks.
     */
    public static void initialize() {
        Timer timer = new Timer();
        timer.schedule(new PurgeChannelsTask(), 0, TimeUtil.DAY.inMilli(1));
        timer.schedule(new EnrollEveryoneTask(), 0, TimeUtil.DAY.inMilli(1));
        timer.schedule(new UpdateChatFilterTask(), 0, TimeUtil.HOUR.inMilli(1));
    }
}
