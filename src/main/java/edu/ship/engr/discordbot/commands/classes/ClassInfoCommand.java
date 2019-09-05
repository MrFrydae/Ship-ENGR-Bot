package edu.ship.engr.discordbot.commands.classes;

import edu.ship.engr.discordbot.Config;
import edu.ship.engr.discordbot.commands.BotCommand;
import edu.ship.engr.discordbot.commands.Command;
import edu.ship.engr.discordbot.commands.CommandEvent;
import edu.ship.engr.discordbot.commands.CommandType;
import edu.ship.engr.discordbot.containers.Course;
import edu.ship.engr.discordbot.utils.CSVUtil;
import edu.ship.engr.discordbot.utils.Util;
import net.dv8tion.jda.api.EmbedBuilder;

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
        String courseName = Util.formatClassName(event.getArg(0));
        Course course = CSVUtil.getCourse(courseName);

        String nextOffered = CSVUtil.getNextOffering(course.getCode());

        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Config.getPrimaryEmbedColor());
        builder.addField("Class Code", course.getCode(), true);
        builder.addField("Class Frequency", course.getFrequency().toString(), true);
        builder.addField("Class Name", course.getTitle(), true);
        if (nextOffered != null) builder.addField("Next Offering", nextOffered, true);

        Util.sendMsg(event.getTextChannel(), builder.build());
    }
}
