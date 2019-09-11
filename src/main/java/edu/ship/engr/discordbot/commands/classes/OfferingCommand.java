package edu.ship.engr.discordbot.commands.classes;

import edu.ship.engr.discordbot.commands.BotCommand;
import edu.ship.engr.discordbot.commands.Command;
import edu.ship.engr.discordbot.commands.CommandEvent;
import edu.ship.engr.discordbot.commands.CommandType;
import edu.ship.engr.discordbot.containers.Course;
import edu.ship.engr.discordbot.utils.CSVUtil;
import edu.ship.engr.discordbot.utils.Util;

@BotCommand(
        name = "nextoffering|offerings",
        aliases = "futureclasses|nextsemester",
        usage = "[className]",
        description = "Shows when this class is offered",
        type = CommandType.CLASSES
)
public class OfferingCommand extends Command {
    @Override
    public void onCommand(CommandEvent event) {
        Course course = CSVUtil.getCourse(event.getArg(0));

        if (event.isBaseCommand("offerings")) {
            Util.sendMsg(event.getTextChannel(), course.getOfferingsEmbed());
        } else {
            Util.sendMsg(event.getTextChannel(), course.getNextOfferingString());
        }
    }
}
