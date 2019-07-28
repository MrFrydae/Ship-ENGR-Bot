package com.devonfrydae.ship.engrbot.commands.classes;

import com.devonfrydae.ship.engrbot.commands.BotCommand;
import com.devonfrydae.ship.engrbot.commands.Command;
import com.devonfrydae.ship.engrbot.commands.CommandEvent;
import com.devonfrydae.ship.engrbot.commands.CommandType;
import com.devonfrydae.ship.engrbot.utils.CSVUtil;
import com.devonfrydae.ship.engrbot.utils.GuildUtil;
import com.google.common.collect.Lists;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

import java.util.List;

@BotCommand(
        name = "enroll",
        usage = "[suid]",
        type = CommandType.CLASSES
)
public class EnrollCommand extends Command {
    @Override
    public void onCommand(CommandEvent event) {
        String email = event.getArg(0) + "@ship.edu";

        enrollMember(event.getMember(), email);
    }

    public static void enrollMember(Member member, String email) {
        List<Role> toAdd = Lists.newArrayList();
        toAdd.add(CSVUtil.getStudentCrew(email));
        toAdd.addAll(CSVUtil.getNewStudentClasses(email));

        List<Role> toRemove = CSVUtil.getOldStudentClasses(email);

        GuildUtil.modifyRoles(member, toAdd, toRemove);
    }
}
