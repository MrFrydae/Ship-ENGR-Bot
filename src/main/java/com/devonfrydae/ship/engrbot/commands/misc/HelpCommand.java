package com.devonfrydae.ship.engrbot.commands.misc;

import com.devonfrydae.ship.engrbot.Config;
import com.devonfrydae.ship.engrbot.commands.BotCommand;
import com.devonfrydae.ship.engrbot.commands.Command;
import com.devonfrydae.ship.engrbot.commands.CommandType;
import com.devonfrydae.ship.engrbot.commands.Commands;
import com.devonfrydae.ship.engrbot.utils.Util;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@BotCommand(
        name = "help",
        aliases = "man",
        usage = "help <command>",
        description = "May I help you?",
        type = CommandType.MISC
)
public class HelpCommand extends Command {

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        EmbedBuilder builder = new EmbedBuilder();
        User bot = event.getGuild().getSelfMember().getUser();

        if (args.length > 0) {
            if (Commands.commands.containsKey(args[0])) {
                Command command = Commands.commands.get(args[0]);
                if (command.getUsage() != null && command.getDescription() != null) {
                    builder.setColor(Config.getPrimaryEmbedColor())
                            .setAuthor(bot.getName(), null, bot.getAvatarUrl())
                            .addField(Config.getCommandPrefix() + args[0], command.getDescription(), false)
                            .addField("Usage", "``" + Config.getCommandPrefix() + command.getUsage() + "``", false);
                } else {
                    builder.setColor(Config.getErrorEmbedColor()).setDescription(":warning: There is currently no information for this command");
                }
            } else {
                builder.setColor(Config.getErrorEmbedColor()).setDescription(":warning: The command list does not contain information for the command ``" + args[0] + "`` !");
            }
        } else {
            builder.setColor(Config.getPrimaryEmbedColor())
                    .setAuthor(bot.getName(), null, bot.getAvatarUrl())
                    .setDescription("Do ``" + Config.getCommandPrefix() + "help <command>`` for extended information on a command.");

            Arrays.stream(CommandType.values()).forEach(type -> {
                List<String> cmds = Commands.commands.entrySet()
                        .stream()
                        .filter(entry -> entry.getValue().getType().equals(type)
                                && Commands.checkPerms(event, entry.getValue()))
                        .map(entry -> "``" + Config.getCommandPrefix() + entry.getKey() + "``")
                        .collect(Collectors.toList());

                builder.addField(type.name().toUpperCase(), Util.join(cmds, "\n"), true);
            });
        }

        Util.sendPrivateMsg(event.getAuthor(), builder.build());
    }
}
