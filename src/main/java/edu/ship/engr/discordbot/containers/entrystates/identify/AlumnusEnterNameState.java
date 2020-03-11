package edu.ship.engr.discordbot.containers.entrystates.identify;

import edu.ship.engr.discordbot.commands.user.IdentifyCommand;
import edu.ship.engr.discordbot.containers.Alumnus;
import edu.ship.engr.discordbot.utils.Util;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class AlumnusEnterNameState extends IdentifyEntryState {
    private Alumnus.AlumnusBuilder builder;
    private String name;

    public AlumnusEnterNameState(Alumnus.AlumnusBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void promptUser(User user) {
        Util.sendPrivateMsg(user, "Please enter your name.");
    }

    @Override
    public void receiveMessage(PrivateMessageReceivedEvent event) {
        this.name = event.getMessage().getContentRaw();

        execute(event.getAuthor());
    }

    @Override
    public void execute(User user) {
        IdentifyEntryState newState = new AlumnusEnterGradYearState(builder.name(name));

        IdentifyCommand.changeEntryState(user, newState);
    }
}
