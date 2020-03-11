package edu.ship.engr.discordbot.containers.entrystates.identify;

import edu.ship.engr.discordbot.commands.user.IdentifyCommand;
import edu.ship.engr.discordbot.containers.Alumnus;
import edu.ship.engr.discordbot.utils.Util;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class AlumnusEnterMajorsState extends IdentifyEntryState {
    private Alumnus.AlumnusBuilder builder;
    private String majors;

    public AlumnusEnterMajorsState(Alumnus.AlumnusBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void promptUser(User user) {
        Util.sendPrivateMsg(user, "Please enter any majors that you have, separated by commas.",
                "If you have more than one, please take a break as you've done a lot of work :)");
    }

    @Override
    public void receiveMessage(PrivateMessageReceivedEvent event) {
        this.majors = event.getMessage().getContentRaw().replace(",", "||");

        execute(event.getAuthor());
    }

    @Override
    public void execute(User user) {
        IdentifyEntryState newState = new AlumnusEnterMinorsState(builder.majors(majors));

        IdentifyCommand.changeEntryState(user, newState);
    }
}
