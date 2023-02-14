package edu.ship.engr.discordbot.listeners;

import edu.ship.engr.discordbot.utils.Log;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        String message = event.getMessage().getContentRaw();

        if (event.getMessage().getChannelType().isThread()) {
            // Don't do anything yet
        } else {
            Log.logMessage(event);
        }
    }
}
