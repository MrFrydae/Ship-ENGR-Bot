package com.devonfrydae.ship.engrbot.listeners;

import com.devonfrydae.ship.engrbot.commands.user.IdentifyCommand;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        if (IdentifyCommand.isInEntryState(event.getAuthor())) {
            IdentifyCommand.handlePrivateMessage(event);
        }
    }
}