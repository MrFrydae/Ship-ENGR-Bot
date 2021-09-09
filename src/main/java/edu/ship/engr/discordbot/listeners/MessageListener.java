package edu.ship.engr.discordbot.listeners;

import edu.ship.engr.discordbot.commands.user.IdentifyCommand;
import edu.ship.engr.discordbot.systems.ChatFilter;
import edu.ship.engr.discordbot.utils.Log;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onPrivateMessageReceived(@Nonnull PrivateMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        if (IdentifyCommand.isInEntryState(event.getAuthor())) {
            IdentifyCommand.handlePrivateMessage(event);
        }
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        String message = event.getMessage().getContentRaw();

        if (ChatFilter.isBadMessage(message)) { // TODO: Marked for removal
            ChatFilter.sendNag(event);
        }

        if (event.getMessage().getCategory().getName().equals("Groups")) {
            Log.logGroupMessage(event);
        } else {
            Log.logMessage(event);
        }
    }
}
