package edu.ship.engr.discordbot.commands.classes;

import edu.ship.engr.discordbot.commands.BotCommand;
import edu.ship.engr.discordbot.commands.Command;
import edu.ship.engr.discordbot.commands.CommandEvent;
import edu.ship.engr.discordbot.commands.CommandType;
import edu.ship.engr.discordbot.containers.Course;
import edu.ship.engr.discordbot.gateways.CourseGateway;
import edu.ship.engr.discordbot.utils.Util;

@BotCommand(
        name = "classinfo",
        aliases = "courseinfo",
        usage = "[className]",
        description = "Shows all information about a class",
        type = CommandType.CLASSES
)
public class ClassInfoCommand extends Command {
    private CourseGateway courseGateway;

    public ClassInfoCommand()
    {
        courseGateway = new CourseGateway();
    }

    @Override
    public void onCommand(CommandEvent event) {
        Course course = courseGateway.getCourse(event.getArg(0));

        Util.sendMsg(event.getTextChannel(), course.getInfoEmbed());
    }
}
