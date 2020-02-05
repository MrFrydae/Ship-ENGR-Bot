package edu.ship.engr.discordbot.commands.chat;

import edu.ship.engr.discordbot.commands.BotCommand;
import edu.ship.engr.discordbot.commands.Command;
import edu.ship.engr.discordbot.commands.CommandEvent;
import edu.ship.engr.discordbot.commands.CommandType;
import edu.ship.engr.discordbot.utils.Util;

@BotCommand(
        name = "ping",
        description = "Pong!",
        type = CommandType.CHAT
)
public class PingCommand extends Command {

    @Override
    public void onCommand(CommandEvent event) {
        Util.sendMsg(event.getTextChannel(), ":ping_pong: Pong!");
    }
}
