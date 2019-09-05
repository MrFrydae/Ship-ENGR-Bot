package edu.ship.engr.discordbot.commands.classes;

import edu.ship.engr.discordbot.commands.BotCommand;
import edu.ship.engr.discordbot.commands.Command;
import edu.ship.engr.discordbot.commands.CommandEvent;
import edu.ship.engr.discordbot.commands.CommandType;
import edu.ship.engr.discordbot.commands.user.IdentifyCommand;
import edu.ship.engr.discordbot.utils.CSVUtil;
import edu.ship.engr.discordbot.utils.GuildUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

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

    public static void enrollEveryone() {
        CSVUtil.getMappedStudents().forEach(student -> {
            String discordId = student.getDiscordId();
            String email = student.getEmail();

            Member member = GuildUtil.getMember(discordId);

            IdentifyCommand.setupUser(member, email);
        });
    }
}
