package edu.ship.engr.discordbot.listeners;

import edu.ship.engr.discordbot.Config;
import edu.ship.engr.discordbot.commands.Commands;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        if (event.getMessage().getContentRaw().startsWith(Config.getCommandPrefix())) {
            Commands.processCommand(event);
        }
    }
}
