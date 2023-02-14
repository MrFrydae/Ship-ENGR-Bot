package edu.ship.engr.discordbot.tasks;

import edu.ship.engr.discordbot.utils.TimeUtil;

import java.util.Calendar;
import java.util.Objects;
import java.util.TimerTask;

public class EnrollEveryoneTask extends TimerTask {
    @Override
    public void run() {
        Calendar currentDate = Calendar.getInstance();
        if (Objects.equals(currentDate, TimeUtil.getEndOfSpringSemester())
                || Objects.equals(currentDate, TimeUtil.getEndOfFallSemester())) {
            //EnrollEveryoneCommand.enrollEveryone();
        }
    }
}
