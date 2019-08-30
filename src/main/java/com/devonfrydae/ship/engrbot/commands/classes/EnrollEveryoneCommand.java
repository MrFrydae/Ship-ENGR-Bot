package com.devonfrydae.ship.engrbot.commands.classes;

import com.devonfrydae.ship.engrbot.commands.BotCommand;
import com.devonfrydae.ship.engrbot.commands.Command;
import com.devonfrydae.ship.engrbot.commands.CommandEvent;
import com.devonfrydae.ship.engrbot.commands.CommandType;
import com.devonfrydae.ship.engrbot.commands.user.IdentifyCommand;
import com.devonfrydae.ship.engrbot.utils.CSVUtil;
import com.devonfrydae.ship.engrbot.utils.GuildUtil;
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
