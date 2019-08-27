package com.devonfrydae.ship.engrbot.tasks;

import com.devonfrydae.ship.engrbot.utils.TimeUtil;

import java.util.Timer;

public class Tasks {
    public static void initialize() {
        Timer timer = new Timer();
        PurgeChannelsTask purgeChannelsTask = new PurgeChannelsTask();
        timer.schedule(purgeChannelsTask, 0, TimeUtil.DAY.inMilli(1));
    }
}
