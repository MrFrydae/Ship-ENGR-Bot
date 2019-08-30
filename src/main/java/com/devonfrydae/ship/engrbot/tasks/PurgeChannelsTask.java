package com.devonfrydae.ship.engrbot.tasks;

import com.devonfrydae.ship.engrbot.commands.classes.CreateCourseChannelsCommand;
import com.devonfrydae.ship.engrbot.commands.classes.PurgeCourseChannelsCommand;
import com.devonfrydae.ship.engrbot.commands.classes.SetupCoursesCommand;
import com.devonfrydae.ship.engrbot.utils.Log;
import com.devonfrydae.ship.engrbot.utils.TimeUtil;

import java.util.Calendar;
import java.util.Objects;
import java.util.TimerTask;

/**
 * This event will run every day to check if the semester is over.
 * If the semester is finished, purge all of the course channels.
 */
public class PurgeChannelsTask extends TimerTask {
    private Long timeToWait = TimeUtil.MINUTE.inMilli(5L);

    @Override
    public void run() {
        Calendar currentDate = Calendar.getInstance();
        if (Objects.equals(currentDate, TimeUtil.getEndOfSpringSemester())
                || Objects.equals(currentDate, TimeUtil.getEndOfFallSemester())) {
            try {
                purgeChannels();
            } catch (InterruptedException e) {
                Log.exception("Exception in PurgeChannelsTask: ", e);
            }
        }
    }

    private void purgeChannels() throws InterruptedException {
        SetupCoursesCommand.createCategories();
        Thread.sleep(timeToWait);
        PurgeCourseChannelsCommand.emptyCategories();
        Thread.sleep(timeToWait);
        CreateCourseChannelsCommand.createCurrentSemester();
    }
}
