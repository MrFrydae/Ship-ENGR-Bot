package edu.ship.engr.discordbot.containers.entrystates.identify;

import edu.ship.engr.discordbot.commands.user.IdentifyCommand;
import edu.ship.engr.discordbot.containers.Alumnus;
import edu.ship.engr.discordbot.utils.Patterns;
import edu.ship.engr.discordbot.utils.Util;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class EnterEmailState extends IdentifyEntryState {
    private Alumnus.AlumnusBuilder builder;
    private String email;

    public EnterEmailState(Alumnus.AlumnusBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void promptUser(User user) {
        Util.sendPrivateMsg(user, "Please enter a valid Shippensburg University email.");
    }

    @Override
    public void receiveMessage(PrivateMessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();

        builder.discordId(event.getAuthor().getId());

        if (message.equalsIgnoreCase("skip")) {
            IdentifyCommand.leaveEntryState(event.getAuthor());

            return;
        } else if (!Patterns.VALID_SHIP_EMAIL_PATTERN.matches(message)) {
            promptUser(event.getAuthor());

            return; // Will continue asking for a new email until a valid email is entered
        } else {
            this.email = message.toLowerCase();
        }

        execute(event.getAuthor());
    }

    @Override
    public void execute(User user) {
        IdentifyEntryState newState;

        if (email.contains("alum")) {
            newState = new AlumnusEnterNameState(builder.email(email));
        } else {
            newState = new FinalEntryState(builder.email(email));
        }

        IdentifyCommand.changeEntryState(user, newState);
    }
}
