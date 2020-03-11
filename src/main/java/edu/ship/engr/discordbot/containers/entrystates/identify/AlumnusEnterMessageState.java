package edu.ship.engr.discordbot.containers.entrystates.identify;

import edu.ship.engr.discordbot.commands.user.IdentifyCommand;
import edu.ship.engr.discordbot.containers.Alumnus;
import edu.ship.engr.discordbot.utils.Util;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class AlumnusEnterMessageState extends IdentifyEntryState {
    private Alumnus.AlumnusBuilder builder;
    private String message;

    public AlumnusEnterMessageState(Alumnus.AlumnusBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void promptUser(User user) {
        Util.sendPrivateMsg(user, "Please enter a message to either inform or entertain the students.",
                "Once you enter a message, it will be saved. So type carefully.");
    }

    @Override
    public void receiveMessage(PrivateMessageReceivedEvent event) {
        // Replace commas because it will mess up the file
        this.message = event.getMessage().getContentRaw().replace(",", "||");

        execute(event.getAuthor());
    }

    @Override
    public void execute(User user) {
        IdentifyEntryState newState = new FinalEntryState(builder.message(message));

        IdentifyCommand.changeEntryState(user, newState);
    }
}
