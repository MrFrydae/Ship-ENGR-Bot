package edu.ship.engr.discordbot.containers.entrystates.identify;

import edu.ship.engr.discordbot.commands.user.IdentifyCommand;
import edu.ship.engr.discordbot.containers.Alumnus;
import edu.ship.engr.discordbot.containers.Student;
import edu.ship.engr.discordbot.gateways.DiscordGateway;
import edu.ship.engr.discordbot.gateways.StudentMapper;
import edu.ship.engr.discordbot.utils.Util;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class FinalEntryState extends IdentifyEntryState {
    private Alumnus.AlumnusBuilder builder;
    private DiscordGateway gateway = new DiscordGateway();

    public FinalEntryState(Alumnus.AlumnusBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void promptUser(User user) {
        execute(user);
    }

    @Override
    public void receiveMessage(PrivateMessageReceivedEvent event) {
        // Do nothing
    }

    @Override
    public void execute(User user) {
        gateway.storeDiscordId(builder.getDiscordId(), builder.getEmail());
        
        if (builder.getEmail().contains("alum")) {
            Alumnus alumnus = builder.build();
            alumnus.register();
        } else {
            Student student = new StudentMapper().getStudentByEmail(builder.getEmail());
            student.enrollStudent();
            student.setNickname();
        }

        IdentifyCommand.leaveEntryState(user);

        Util.sendPrivateMsg(user, "Thank you for registering with the School of Engineering Discord server.");
    }
}
