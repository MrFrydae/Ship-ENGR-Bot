package com.devonfrydae.ship.engrbot.listeners;

import com.devonfrydae.ship.engrbot.Config;
import com.devonfrydae.ship.engrbot.commands.Commands;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        if (event.getMessage().getContentRaw().startsWith(Config.getCommandPrefix())) {
            Commands.processCommand(event);
        }
    }
}
