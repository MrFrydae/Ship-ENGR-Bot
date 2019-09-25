package edu.ship.engr.discordbot.commands.classes;

import edu.ship.engr.discordbot.commands.BotCommand;
import edu.ship.engr.discordbot.commands.Command;
import edu.ship.engr.discordbot.commands.CommandEvent;
import edu.ship.engr.discordbot.commands.CommandType;
import edu.ship.engr.discordbot.containers.Course;
import edu.ship.engr.discordbot.utils.CSVUtil;
import edu.ship.engr.discordbot.utils.Util;

@BotCommand(
        name = "classinfo",
        aliases = "courseinfo",
        usage = "[className]",
        description = "Shows all information about a class",
        type = CommandType.CLASSES
)
public class ClassInfoCommand extends Command {
    @Override
    public void onCommand(CommandEvent event) {
        Course course = CSVUtil.getSingleton().getCourse(event.getArg(0));

        Util.sendMsg(event.getTextChannel(), course.getInfoEmbed());
    }
}
