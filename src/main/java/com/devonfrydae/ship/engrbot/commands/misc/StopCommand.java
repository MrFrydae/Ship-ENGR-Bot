package com.devonfrydae.ship.engrbot.commands.misc;

import com.devonfrydae.ship.engrbot.commands.BotCommand;
import com.devonfrydae.ship.engrbot.commands.Command;
import com.devonfrydae.ship.engrbot.commands.CommandEvent;
import net.dv8tion.jda.core.Permission;

@BotCommand(
        name = "stop",
        description = "Stops the bot",
        permissions = Permission.ADMINISTRATOR
)
public class StopCommand extends Command {
    @Override
    public void onCommand(CommandEvent event) {
        System.exit(0);
    }
}
