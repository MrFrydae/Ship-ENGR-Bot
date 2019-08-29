package com.devonfrydae.ship.engrbot.tasks;

import com.devonfrydae.ship.engrbot.commands.classes.EnrollEveryoneCommand;
import com.devonfrydae.ship.engrbot.utils.TimeUtil;

import java.util.Calendar;
import java.util.Objects;
import java.util.TimerTask;

public class EnrollEveryoneTask extends TimerTask {
    @Override
    public void run() {
        Calendar currentDate = Calendar.getInstance();
        if (Objects.equals(currentDate, TimeUtil.getEndOfSpringSemester())
                || Objects.equals(currentDate, TimeUtil.getEndOfFallSemester())) {
            EnrollEveryoneCommand.enrollEveryone();
        }
    }
}
