package com.devonfrydae.ship.engrbot.commands.chat;

import com.devonfrydae.ship.engrbot.commands.BotCommand;
import com.devonfrydae.ship.engrbot.commands.Command;
import com.devonfrydae.ship.engrbot.commands.CommandEvent;
import com.devonfrydae.ship.engrbot.commands.CommandType;
import com.devonfrydae.ship.engrbot.utils.GuildUtil;
import com.devonfrydae.ship.engrbot.utils.Util;

@BotCommand(
        name = "ping",
        description = "Pong!",
        type = CommandType.CHAT
)
public class PingCommand extends Command {

    @Override
    public void onCommand(CommandEvent event) {
        long ping = GuildUtil.getGuild().getJDA().getPing();
        Util.sendMsg(event.getTextChannel(), ":ping_pong: Pong! ``" + ping + "ms``");
    }
}
