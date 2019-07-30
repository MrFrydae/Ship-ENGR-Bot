package com.devonfrydae.ship.engrbot.commands.chat;

import com.devonfrydae.ship.engrbot.commands.BotCommand;
import com.devonfrydae.ship.engrbot.commands.Command;
import com.devonfrydae.ship.engrbot.commands.CommandEvent;
import com.devonfrydae.ship.engrbot.commands.CommandType;
import com.devonfrydae.ship.engrbot.utils.Log;
import com.devonfrydae.ship.engrbot.utils.NumUtil;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;

import java.util.ArrayList;
import java.util.List;

@BotCommand(
        name = "purge",
        aliases = "clear",
        usage = "[amount]",
        description = "Bulk delete chat messages",
        type = CommandType.CHAT,
        permissions = Permission.MESSAGE_MANAGE
)
public class PurgeCommand extends Command {

    @Override
    public void onCommand(CommandEvent event) {
        try {
            MessageHistory history = new MessageHistory(event.getTextChannel());
            List<Message> messages = new ArrayList<>();
            int amountOfMessages = NumUtil.parseInt(event.getArg(0));

            if (amountOfMessages <= 100) {
                event.getTextChannel().purgeMessages(event.getMessage());
                messages = history.retrievePast(NumUtil.parseInt(event.getArg(0))).complete();
            }

            event.getTextChannel().purgeMessages(messages);
            Log.info("Purged " + amountOfMessages + " messages in #" + event.getTextChannel().getName());
        } catch (Exception ignored) {}
    }
}
