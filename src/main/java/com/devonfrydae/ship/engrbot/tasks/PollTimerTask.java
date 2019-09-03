package com.devonfrydae.ship.engrbot.tasks;

import com.devonfrydae.ship.engrbot.commands.chat.PollCommand;

import java.util.TimerTask;

public class PollTimerTask extends TimerTask {
    @Override
    public void run() {
        PollCommand.checkPolls();
    }
}
