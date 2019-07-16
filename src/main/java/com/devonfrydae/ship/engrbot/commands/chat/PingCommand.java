package com.devonfrydae.ship.engrbot.commands.chat;

import com.devonfrydae.ship.engrbot.commands.BotCommand;
import com.devonfrydae.ship.engrbot.commands.Command;
import com.devonfrydae.ship.engrbot.commands.CommandType;
import com.devonfrydae.ship.engrbot.utils.Util;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

@BotCommand(
        name = "ping",
        usage = "ping",
        description = "Pong!",
        type = CommandType.CHAT
)
public class PingCommand extends Command {

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        long ping = event.getJDA().getPing();
        Util.sendMsg(event.getTextChannel(), ":ping_pong: Pong! ``" + ping + "ms``");
    }
}
