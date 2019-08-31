package com.devonfrydae.ship.engrbot.commands.chat;

import co.aikar.idb.DB;
import co.aikar.idb.DbRow;
import com.devonfrydae.ship.engrbot.commands.BotCommand;
import com.devonfrydae.ship.engrbot.commands.Command;
import com.devonfrydae.ship.engrbot.commands.CommandEvent;
import com.devonfrydae.ship.engrbot.commands.CommandType;
import com.devonfrydae.ship.engrbot.containers.Poll;
import com.devonfrydae.ship.engrbot.utils.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.sql.SQLException;
import java.util.List;

@BotCommand(
        name = "poll",
        aliases = "survey",
        type = CommandType.CHAT
)
public class PollCommand extends Command {
    @Override
    public void onCommand(CommandEvent event) {
        if (event.hasArgs()) {
            switch (event.getArg(0)) {
                case "create":
                    createPoll(event);
                    break;
                case "edit":
                    editPoll(event);
                    break;
                case "results":
                    getResults(event);
                    break;
                case "end":
                    endPoll(event);
                    break;
            }
        }
    }

    private void createPoll(CommandEvent event) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Title")
                .setDescription("Description");
        Message message = Util.sendMsg(event.getTextChannel(), builder.build());
        DB.executeUpdateAsync("INSERT INTO polls (channel_id, message_id) VALUES (?, ?)", event.getTextChannel().getId(), message.getId());
        Poll poll = new Poll(event.getTextChannel().getId(), message.getId());
        poll.setFooter("This message's ID is: " + message.getId()).update();
    }

    private void editPoll(CommandEvent event) {
        String messageId = event.getArg(1);
        String channelId = Poll.getPollChannelFromMessage(messageId);
        Poll poll = new Poll(channelId, messageId);

        switch (event.getArg(2)) {
            case "desc":
            case "description":
                String description = event.getArg(3);
                poll.setDescription(description).update();
                break;
            case "title":
                String title = event.getArg(3);
                poll.setTitle(title).update();
                break;
            case "color":
                String color = event.getArg(3);
                poll.setColor(Util.processColor(color)).update();
                break;
            case "reaction":
                String reactions = event.getArg(3);
                poll.setReactions(reactions).update();
                break;
        }
    }

    private void endPoll(CommandEvent event) {
        String messageId = event.getArg(1);

        Util.sendMsg(event.getTextChannel(), "Ended poll: " + messageId);
    }

    private void getResults(CommandEvent event) {
        Util.sendMsg(event.getTextChannel(), "Here are the results:");

    }

    public static void checkPolls() {
        try {
            List<DbRow> expiredPolls = DB.getResults("SELECT * FROM polls WHERE valid_until <= UNIX_TIMESTAMP()");
            for (DbRow row : expiredPolls) {
                String channelId = row.getString("channel_id");
                String messageId = row.getString("message_id");
                Poll poll = new Poll(channelId, messageId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
