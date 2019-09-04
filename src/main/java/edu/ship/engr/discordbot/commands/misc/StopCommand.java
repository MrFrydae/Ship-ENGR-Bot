package edu.ship.engr.discordbot.commands.misc;

import edu.ship.engr.discordbot.commands.BotCommand;
import edu.ship.engr.discordbot.commands.Command;
import edu.ship.engr.discordbot.commands.CommandEvent;
import net.dv8tion.jda.api.Permission;

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
