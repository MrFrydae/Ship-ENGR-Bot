package com.devonfrydae.ship.engrbot.commands.classes;

import com.devonfrydae.ship.engrbot.Config;
import com.devonfrydae.ship.engrbot.commands.BotCommand;
import com.devonfrydae.ship.engrbot.commands.Command;
import com.devonfrydae.ship.engrbot.commands.CommandEvent;
import com.devonfrydae.ship.engrbot.commands.CommandType;
import com.devonfrydae.ship.engrbot.containers.Course;
import com.devonfrydae.ship.engrbot.utils.CSVUtil;
import com.devonfrydae.ship.engrbot.utils.Util;
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
