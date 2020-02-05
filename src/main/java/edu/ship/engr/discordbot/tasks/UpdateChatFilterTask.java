package edu.ship.engr.discordbot.tasks;

import edu.ship.engr.discordbot.systems.ChatFilter;

import java.util.TimerTask;

public class UpdateChatFilterTask extends TimerTask {
    @Override
    public void run() {
        ChatFilter.update();
    }
}
