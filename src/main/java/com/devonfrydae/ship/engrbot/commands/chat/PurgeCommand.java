package com.devonfrydae.ship.engrbot.commands.chat;

import com.devonfrydae.ship.engrbot.utils.NumUtil;
import com.devonfrydae.ship.engrbot.commands.BotCommand;
import com.devonfrydae.ship.engrbot.commands.Command;
import com.devonfrydae.ship.engrbot.commands.CommandType;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

@BotCommand(
        name = "purge",
        usage = "purge [amount]",
        description = "Bulk delete chat messages",
        type = CommandType.CHAT,
        permissions = Permission.MESSAGE_MANAGE
)
public class PurgeCommand extends Command {

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        try {
            MessageHistory history = new MessageHistory(event.getTextChannel());
            List<Message> messages = new ArrayList<>();

            if (NumUtil.parseInt(args[0]) <= 100) {
                event.getChannel().purgeMessages(event.getMessage());
                messages = history.retrievePast(NumUtil.parseInt(args[0])).complete();
            }

            event.getChannel().purgeMessages(messages);
        } catch (Exception ignored) {}
    }
}
