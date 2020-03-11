package edu.ship.engr.discordbot.containers.entrystates.identify;

import edu.ship.engr.discordbot.commands.user.IdentifyCommand;
import edu.ship.engr.discordbot.containers.Alumnus;
import edu.ship.engr.discordbot.utils.Util;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class AlumnusEnterGradYearState extends IdentifyEntryState {
    private Alumnus.AlumnusBuilder builder;
    private String gradYear;

    public AlumnusEnterGradYearState(Alumnus.AlumnusBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void promptUser(User user) {
        Util.sendPrivateMsg(user, "Please enter the year you graduated, prefixed by the semester if you would like to specify that too.",
                "Example: [Spring/Fall] 2017");
    }

    @Override
    public void receiveMessage(PrivateMessageReceivedEvent event) {
        this.gradYear = event.getMessage().getContentRaw();

        execute(event.getAuthor());
    }

    @Override
    public void execute(User user) {
        IdentifyEntryState newState = new AlumnusEnterMajorsState(builder.gradYear(gradYear));

        IdentifyCommand.changeEntryState(user, newState);
    }
}
