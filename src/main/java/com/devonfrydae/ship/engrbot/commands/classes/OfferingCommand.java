package com.devonfrydae.ship.engrbot.commands.classes;

import com.devonfrydae.ship.engrbot.commands.BotCommand;
import com.devonfrydae.ship.engrbot.commands.Command;
import com.devonfrydae.ship.engrbot.commands.CommandEvent;
import com.devonfrydae.ship.engrbot.commands.CommandType;
import com.devonfrydae.ship.engrbot.utils.CSVUtil;
import com.devonfrydae.ship.engrbot.utils.Patterns;
import com.devonfrydae.ship.engrbot.utils.Util;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.List;

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
        String className = Util.formatClassName(event.getArg(0).toUpperCase());

        if (event.isBaseCommand("offerings")) {
            processAllOfferings(event, className);
        } else {
            processNextOffering(event, className);
        }
    }

    private void processAllOfferings(CommandEvent event, String className) {
        List<String> offerings = CSVUtil.getAllOfferings(className);
        String courseTitle = CSVUtil.getCourseTitle(className);

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Offerings for \"" + courseTitle + "\"");

        for (String offering : offerings) {
            String[] split = Patterns.COMMA.split(offering);
            String year = split[0];
            String spring = "Spring: " + split[1];
            String fall = "Fall: " + split[2];

            builder.addField(year, spring + "\n" + fall, true);
        }

        Util.sendMsg(event.getTextChannel(), builder.build());
    }

    private void processNextOffering(CommandEvent event, String className) {
        String nextOffering = CSVUtil.getNextOffering(className);

        if (nextOffering != null) {
            Util.sendMsg(event.getTextChannel(), "The next time " + className + " is offered is in " + nextOffering + ".");
        } else {
            Util.sendMsg(event.getTextChannel(), "Sorry, " + className + " is not being offered at the moment.");
        }
    }
}
