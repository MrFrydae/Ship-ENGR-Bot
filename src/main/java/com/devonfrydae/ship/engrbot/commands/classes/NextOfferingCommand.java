package com.devonfrydae.ship.engrbot.commands.classes;

import com.devonfrydae.ship.engrbot.commands.BotCommand;
import com.devonfrydae.ship.engrbot.commands.Command;
import com.devonfrydae.ship.engrbot.commands.CommandType;
import com.devonfrydae.ship.engrbot.utils.CSVUtil;
import com.devonfrydae.ship.engrbot.utils.Util;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

@BotCommand(
        name = "nextoffering",
        aliases = "futureclasses|nextsemester",
        usage = "nextoffering [className]",
        description = "Shows when this class is offered next",
        type = CommandType.CLASSES
)
public class NextOfferingCommand extends Command {
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        String className = CSVUtil.formatClassName(args[0].toUpperCase());
        String nextOffering = CSVUtil.getNextOffering(className);

        if (nextOffering != null) {
            Util.sendMsg(event.getTextChannel(), "The next time " + className + " is offered is in " + nextOffering + ".");
        } else {
            Util.sendMsg(event.getTextChannel(), "Sorry, " + className + " is not being offered at the moment.");
        }
    }
}