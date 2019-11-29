package edu.ship.engr.discordbot.commands.misc;

import edu.ship.engr.discordbot.commands.BotCommand;
import edu.ship.engr.discordbot.commands.Command;
import edu.ship.engr.discordbot.commands.CommandEvent;
import edu.ship.engr.discordbot.utils.csv.CSVHandler;

@BotCommand(name = "test")
public class TestCommand extends Command {
    @Override
    public void onCommand(CommandEvent event) {
        CSVHandler handler = new CSVHandler("offerings");

        for (String header : handler.getParser().getHeaders()) {
            System.out.println(header);
        }
    }
}
