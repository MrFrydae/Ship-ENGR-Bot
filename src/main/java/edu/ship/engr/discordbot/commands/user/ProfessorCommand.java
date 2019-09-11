package edu.ship.engr.discordbot.commands.user;

import com.google.common.collect.Lists;
import edu.ship.engr.discordbot.commands.BotCommand;
import edu.ship.engr.discordbot.commands.Command;
import edu.ship.engr.discordbot.commands.CommandEvent;
import edu.ship.engr.discordbot.commands.CommandType;
import edu.ship.engr.discordbot.containers.Professor;
import edu.ship.engr.discordbot.utils.CSVUtil;
import edu.ship.engr.discordbot.utils.StringUtil;
import edu.ship.engr.discordbot.utils.Util;

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

        Util.sendMsg(event.getTextChannel(), professor.getInfoEmbed());
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
