package edu.ship.engr.discordbot.commands.misc;

import edu.ship.engr.discordbot.Config;
import edu.ship.engr.discordbot.commands.BotCommand;
import edu.ship.engr.discordbot.commands.Command;
import edu.ship.engr.discordbot.commands.CommandEvent;
import edu.ship.engr.discordbot.commands.CommandType;
import edu.ship.engr.discordbot.commands.Commands;
import edu.ship.engr.discordbot.utils.GuildUtil;
import edu.ship.engr.discordbot.utils.StringUtil;
import edu.ship.engr.discordbot.utils.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@BotCommand(
        name = "help",
        aliases = "man",
        usage = "<command>",
        description = "May I help you?",
        type = CommandType.MISC
)
public class HelpCommand extends Command {

    @Override
    public void onCommand(CommandEvent event) {
        EmbedBuilder builder = new EmbedBuilder();
        User bot = GuildUtil.getGuild().getSelfMember().getUser();

        if (event.hasArgs()) {
            if (Commands.isCommandAlias(event.getArg(0))) {
                Command command = Commands.getCommandByAlias(event.getArg(0));
                if (command.getUsage() != null && command.getDescription() != null) {
                    builder.setColor(Config.getPrimaryEmbedColor())
                            .setAuthor(bot.getName(), "https://web.engr.ship.edu", bot.getAvatarUrl())
                            .addField(Config.getCommandPrefix() + event.getArg(0), command.getDescription(), false)
                            .addField("Usage", "``" + Config.getCommandPrefix() + event.getArg(0) + " " + command.getUsage() + "``", false);
                } else {
                    builder.setColor(Config.getErrorEmbedColor()).setDescription(":warning: There is currently no information for this command");
                }
            } else {
                builder.setColor(Config.getErrorEmbedColor()).setDescription(":warning: The command list does not contain information for the command ``" + event.getArg(0) + "`` !");
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

                if (!cmds.isEmpty()) {
                    builder.addField(type.name().toUpperCase(), StringUtil.join(cmds, "\n"), true);
                }
            });
        }

        Util.sendPrivateMsg(event.getAuthor(), builder.build());
    }
}
