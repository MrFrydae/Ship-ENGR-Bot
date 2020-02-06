package edu.ship.engr.discordbot.commands.classes;

import edu.ship.engr.discordbot.commands.BotCommand;
import edu.ship.engr.discordbot.commands.Command;
import edu.ship.engr.discordbot.commands.CommandEvent;
import edu.ship.engr.discordbot.commands.CommandType;
import edu.ship.engr.discordbot.commands.user.IdentifyCommand;
import edu.ship.engr.discordbot.gateways.StudentMapper;
import net.dv8tion.jda.api.Permission;

@BotCommand(
        name = "enrolleveryone",
        type = CommandType.CLASSES,
        permissions = {Permission.MANAGE_ROLES, Permission.MANAGE_SERVER}
)
public class EnrollEveryoneCommand extends Command {
    @Override
    public void onCommand(CommandEvent event) {
        enrollEveryone();
    }

    /**
     * Enroll every registered user into their classes for this semester.
     */
    public static void enrollEveryone() {
        (new StudentMapper()).getAllStudentsWithDiscordIDs().forEach(student -> {
            String email = student.getEmail();

            IdentifyCommand.setupUser(email);
        });
    }
}
