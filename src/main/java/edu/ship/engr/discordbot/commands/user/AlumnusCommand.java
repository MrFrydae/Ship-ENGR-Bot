package edu.ship.engr.discordbot.commands.user;

import com.google.common.collect.Lists;
import edu.ship.engr.discordbot.commands.BotCommand;
import edu.ship.engr.discordbot.commands.Command;
import edu.ship.engr.discordbot.commands.CommandEvent;
import edu.ship.engr.discordbot.commands.CommandType;
import edu.ship.engr.discordbot.containers.Alumnus;
import edu.ship.engr.discordbot.gateways.AlumnusGateway;
import edu.ship.engr.discordbot.utils.StringUtil;
import edu.ship.engr.discordbot.utils.Util;

import java.util.List;

@BotCommand(
        name = "alumnus",
        aliases = "alumni",
        description = "Get information about an alumnus",
        usage = "[name/email]",
        type = CommandType.USER
)
public class AlumnusCommand extends Command {
    @Override
    public void onCommand(CommandEvent event) {
        String alumnusName = event.getArg(0);
        AlumnusGateway gateway = new AlumnusGateway();
        List<Alumnus> alumniMatch = gateway.getAlumnusByNameOrEmail(alumnusName);

        if (alumniMatch.isEmpty()) {
            return;
        }

        if (alumniMatch.size() > 1) {
            handleMultipleProfs(event, alumniMatch);
            return;
        }

        Alumnus alumnus = alumniMatch.get(0);

        Util.sendMsg(event.getTextChannel(), alumnus.getInfoEmbed());
    }

    private void handleMultipleProfs(CommandEvent event, List<Alumnus> alumnusMatch) {
        List<String> alumni = Lists.newArrayList();
        for (int i = 0; i < alumnusMatch.size(); i++) {
            Alumnus alumnus = alumnusMatch.get(i);
            String email = alumnus.getEmail();

            alumni.add((i + 1) + ") " + email.toLowerCase());
        }

        Util.sendMsg(event.getTextChannel(), "Please try this command again with one of the following emails:");
        Util.sendMsg(event.getTextChannel(), StringUtil.join(alumni, "\n"));
    }
}
