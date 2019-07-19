package com.devonfrydae.ship.engrbot.commands.misc;


import com.devonfrydae.ship.engrbot.Config;
import com.devonfrydae.ship.engrbot.commands.BotCommand;
import com.devonfrydae.ship.engrbot.commands.Command;
import com.devonfrydae.ship.engrbot.commands.CommandEvent;
import com.devonfrydae.ship.engrbot.commands.CommandType;
import com.devonfrydae.ship.engrbot.containers.Professor;
import com.devonfrydae.ship.engrbot.utils.CSVUtil;
import com.devonfrydae.ship.engrbot.utils.Util;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

@BotCommand(
        name = "professor",
        usage = "professor [professorname]",
        description = "Returns information about a professor.",
        type = CommandType.MISC
)

public class ProfessorCommand extends Command {

    @Override
    public void onCommand(CommandEvent event) {
        String professorName = event.getArg(0);
        Professor professor = CSVUtil.getProfessors(professorName);

        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Config.getPrimaryEmbedColor());
        builder.addField("Professor", professor.getProfessorName(), true);
        builder.addField("Alma Mater", professor.getAlmaMater(), true);
        builder.addField("Email", professor.getEmail(), true);
        builder.addField("Office Number", professor.getOfficeNumber(), true);
        builder.addField("Phone Number", professor.getPhone(), true);
        builder.addField("Specialty", professor.getSpecialty(), true);
        builder.addField("Title", professor.getTitle(), true);
        builder.addField("Website", professor.getWebsite(), true);
        Util.sendMsg(event.getTextChannel(), builder.build());

    }
}
