package com.devonfrydae.ship.engrbot.commands.user;

import com.devonfrydae.ship.engrbot.Config;
import com.devonfrydae.ship.engrbot.commands.BotCommand;
import com.devonfrydae.ship.engrbot.commands.Command;
import com.devonfrydae.ship.engrbot.commands.CommandEvent;
import com.devonfrydae.ship.engrbot.commands.CommandType;
import com.devonfrydae.ship.engrbot.containers.Professor;
import com.devonfrydae.ship.engrbot.utils.CSVUtil;
import com.devonfrydae.ship.engrbot.utils.StringUtil;
import com.devonfrydae.ship.engrbot.utils.Util;
import com.google.common.collect.Lists;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.List;

@BotCommand(
        name = "professor",
        usage = "[name/email]",
        description = "Returns information about a professor.",
        type = CommandType.USER
)
public class ProfessorCommand extends Command {

    @Override
    public void onCommand(CommandEvent event) {
        String professorName = event.getArg(0);
        List<Professor> profMatch = CSVUtil.getProfessorMatch(professorName);

        if (profMatch.isEmpty()) return;

        if (profMatch.size() > 1) {
            handleMultipleProfs(event, profMatch);
            return;
        }

        Professor professor = profMatch.get(0);

        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Config.getPrimaryEmbedColor());

        // Only add to builder if present
        if (professor.hasName()) builder.addField("Professor", professor.getName(), true);
        if (professor.hasAlmaMater()) builder.addField("Alma Mater", professor.getAlmaMater(), true);
        if (professor.hasEmail()) builder.addField("Email", professor.getEmail(), true);
        if (professor.hasOfficeNumber()) builder.addField("Office Number", professor.getOfficeNumber(), true);
        if (professor.hasPhone()) builder.addField("Phone Number", professor.getPhone(), true);
        if (professor.hasSpecialty()) builder.addField("Specialty", professor.getSpecialty(), true);
        if (professor.hasTitle()) builder.addField("Title", professor.getTitle(), true);
        if (professor.hasWebsite()) builder.addField("Website", professor.getWebsite(), true);
        if (professor.hasOfficeHours()) builder.addField("Office Hours", professor.getOfficeHours(), true);

        Util.sendMsg(event.getTextChannel(), builder.build());
    }

    private void handleMultipleProfs(CommandEvent event, List<Professor> profMatch) {
        List<String> profs = Lists.newArrayList();
        for (int i = 0; i < profMatch.size(); i++) {
            Professor professor = profMatch.get(i);
            String email = professor.getEmail();

            profs.add((i + 1) + ") " + email.toLowerCase());
        }

        Util.sendMsg(event.getTextChannel(), "Please try this command again with one of the following emails:");
        Util.sendMsg(event.getTextChannel(), StringUtil.join(profs, "\n"));
    }
}
