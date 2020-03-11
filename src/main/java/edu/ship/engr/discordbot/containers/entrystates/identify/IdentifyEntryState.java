package edu.ship.engr.discordbot.containers.entrystates.identify;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public abstract class IdentifyEntryState {
    public abstract void promptUser(User user);

    public abstract void execute(User user);

    public abstract void receiveMessage(PrivateMessageReceivedEvent event);
}
