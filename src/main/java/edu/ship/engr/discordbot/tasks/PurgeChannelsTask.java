package edu.ship.engr.discordbot.tasks;

import edu.ship.engr.discordbot.utils.TimeUtil;

import java.util.Calendar;
import java.util.Objects;
import java.util.TimerTask;

/**
 * This event will run every day to check if the semester is over.
 * If the semester is finished, purge all of the course channels.
 */
public class PurgeChannelsTask extends TimerTask {
    @Override
    public void run() {
        Calendar currentDate = Calendar.getInstance();
        if (Objects.equals(currentDate, TimeUtil.getEndOfSpringSemester())
                || Objects.equals(currentDate, TimeUtil.getEndOfFallSemester())) {
            //PurgeCourseChannelsCommand.purgeChannels();
        }
    }
}
