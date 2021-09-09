package edu.ship.engr.discordbot.tasks;

import edu.ship.engr.discordbot.commands.classes.GroupCommand;
import edu.ship.engr.discordbot.utils.GuildUtil;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class TimeoutGroupTask extends TimerTask {
    @Override
    public void run() {
        execute();
    }

    public static void execute() {
        List<TextChannel> groupChannels = GroupCommand.getGroupChannels();

        for (TextChannel channel : groupChannels) {
            Message message = channel.getHistory().retrievePast(1).completeAfter(1, TimeUnit.SECONDS).get(0);

            OffsetDateTime timeCreated = message.getTimeCreated();
            OffsetDateTime expireOn = OffsetDateTime.now().minusSeconds(GroupCommand.TIMEOUT_DELAY);

            if (timeCreated.isBefore(expireOn)) {
                GroupCommand.processExpiredChannel(channel);
            }
        }
    }
}
