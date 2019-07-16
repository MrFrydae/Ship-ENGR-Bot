package com.devonfrydae.ship.engrbot.commands.classes;

import com.devonfrydae.ship.engrbot.commands.BotCommand;
import com.devonfrydae.ship.engrbot.commands.Command;
import com.devonfrydae.ship.engrbot.commands.CommandType;
import com.devonfrydae.ship.engrbot.utils.CSVUtil;
import com.devonfrydae.ship.engrbot.utils.GuildUtil;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

@BotCommand(
        name = "enroll",
        usage = "enroll [suid]",
        type = CommandType.CLASSES
)
public class EnrollCommand extends Command {
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        String email = args[0] + "@ship.edu";

        GuildUtil.addRolesToMember(event.getMember(), CSVUtil.getStudentCrew(email));
        GuildUtil.addRolesToMember(event.getMember(), CSVUtil.getNewStudentClasses(email));

        CSVUtil.storeDiscordId(event.getMember(), email);
    }
}
