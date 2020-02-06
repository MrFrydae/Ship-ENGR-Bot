package edu.ship.engr.discordbot.listeners;

import edu.ship.engr.discordbot.commands.user.IdentifyCommand;
import edu.ship.engr.discordbot.systems.ChatFilter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        if (IdentifyCommand.isInEntryState(event.getAuthor())) {
            IdentifyCommand.handlePrivateMessage(event);
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        String message = event.getMessage().getContentRaw();

        if (ChatFilter.isBadMessage(message)) {
            ChatFilter.sendNag(event);
        }
    }
}
