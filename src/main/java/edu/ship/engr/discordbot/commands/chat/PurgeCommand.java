package edu.ship.engr.discordbot.commands.chat;

import edu.ship.engr.discordbot.commands.BotCommand;
import edu.ship.engr.discordbot.commands.Command;
import edu.ship.engr.discordbot.commands.CommandEvent;
import edu.ship.engr.discordbot.commands.CommandType;
import edu.ship.engr.discordbot.utils.Log;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;

import java.util.ArrayList;
import java.util.List;

/**
 * The implementation of the purge command that deletes the meesages in a channel
 *
 */
@BotCommand(
        name = "purge",
        aliases = "clear",
        usage = "[amount]",
        description = "Bulk delete chat messages",
        type = CommandType.CHAT,
        permissions = Permission.MESSAGE_MANAGE
)
public class PurgeCommand extends Command {

    /**
     * @see edu.ship.engr.discordbot.commands.Command#onCommand(edu.ship.engr.discordbot.commands.CommandEvent)
     */
    @Override
    public void onCommand(CommandEvent event) {
        try {
            MessageHistory history = new MessageHistory(event.getTextChannel());
            List<Message> messages = new ArrayList<>();
            int amountOfMessages = Integer.parseInt(event.getArg(0));

            if (amountOfMessages <= 100) {
                event.getTextChannel().purgeMessages(event.getMessage());
                messages = history.retrievePast(Integer.parseInt(event.getArg(0))).complete();
            }

            event.getTextChannel().purgeMessages(messages);
            Log.info("Purged " + amountOfMessages + " messages in #" + event.getTextChannel().getName());
        } catch (Exception ignored) {}
    }
}
