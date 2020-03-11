package edu.ship.engr.discordbot.containers.entrystates.identify;

import edu.ship.engr.discordbot.commands.user.IdentifyCommand;
import edu.ship.engr.discordbot.containers.Alumnus;
import edu.ship.engr.discordbot.utils.Util;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class AlumnusEnterMinorsState extends IdentifyEntryState {
    private Alumnus.AlumnusBuilder builder;
    private String minors;

    public AlumnusEnterMinorsState(Alumnus.AlumnusBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void promptUser(User user) {
        Util.sendPrivateMsg(user, "Please enter any minors that you have.",
                "Type ``skip`` if you don't have any minors.. We'll understand :)");
    }

    @Override
    public void receiveMessage(PrivateMessageReceivedEvent event) {
        this.minors = event.getMessage().getContentRaw().replace(",", "||");

        execute(event.getAuthor());
    }

    @Override
    public void execute(User user) {
        IdentifyEntryState newState = new AlumnusEnterMessageState(builder.minors(minors));

        IdentifyCommand.changeEntryState(user, newState);
    }
}
