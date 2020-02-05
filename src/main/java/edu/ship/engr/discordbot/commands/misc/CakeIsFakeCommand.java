package edu.ship.engr.discordbot.commands.misc;

import edu.ship.engr.discordbot.commands.BotCommand;
import edu.ship.engr.discordbot.commands.Command;
import edu.ship.engr.discordbot.commands.CommandEvent;
import edu.ship.engr.discordbot.commands.CommandType;
import edu.ship.engr.discordbot.utils.Util;

@BotCommand(
        name = "cake",
        description = "You can't really expect to eat your cake too...",
        type = CommandType.MISC
)
public class CakeIsFakeCommand extends Command {
    @Override
    public void onCommand(CommandEvent event) {
        Util.sendMsg(event.getTextChannel(), ":cake: The cake is a lie!");
    }
}
