package com.devonfrydae.ship.engrbot.commands.misc;


import com.devonfrydae.ship.engrbot.commands.BotCommand;
import com.devonfrydae.ship.engrbot.commands.Command;
import com.devonfrydae.ship.engrbot.commands.CommandType;
import com.devonfrydae.ship.engrbot.containers.Professor;
import com.devonfrydae.ship.engrbot.utils.CSVUtil;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

@BotCommand(
        name = "professor",
        usage = "professor [professorname]",
        description = "Returns information about a professor.",
        type = CommandType.MISC
)

public class ProfessorCommand extends Command {

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        String professorName = args[0];
        Professor professor = CSVUtil.getProfessors(professorName);


    }
}
