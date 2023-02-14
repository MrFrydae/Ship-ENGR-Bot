package edu.ship.engr.discordbot.commands;

import edu.ship.engr.discordbot.commands.annotations.CommandAlias;
import edu.ship.engr.discordbot.commands.annotations.Subcommand;

@CommandAlias("simple")
class SimpleCommandClass {

    @Subcommand("simple")
    public void onSubCommand() {

    }
}
