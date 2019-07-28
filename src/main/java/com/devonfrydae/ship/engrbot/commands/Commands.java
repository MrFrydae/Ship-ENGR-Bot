package com.devonfrydae.ship.engrbot.commands;

import com.devonfrydae.ship.engrbot.Config;
import com.devonfrydae.ship.engrbot.DiscordBot;
import com.devonfrydae.ship.engrbot.utils.Patterns;
import com.devonfrydae.ship.engrbot.utils.Util;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.reflections.Reflections;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Commands {
    public static HashMap<String, Command> commands = new HashMap<>();

    /**
     * Finds and registers all commands in the commands package
     */
    public static void registerCommands() {
        Reflections commandClasses = new Reflections("com.devonfrydae.ship.engrbot.commands");
        Set<Class<? extends Command>> cmds = commandClasses.getSubTypesOf(Command.class);

        for (Class<? extends Command> cmd : cmds) {
            Command command = null;

            BotCommand annotation = cmd.getAnnotation(BotCommand.class);
            String names = annotation.name();

            for (String name : Patterns.PIPE.split(names)) {
                try {
                    command = cmd.getConstructor().newInstance();
                    command.aliases = annotation.aliases();
                    command.usage = annotation.usage();
                    command.description = annotation.description().replace("|", "\n");
                    command.type = annotation.type();
                    command.permissions = annotation.permissions();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (command != null) {
                    commands.put(name, command);
                }
            }
        }
    }

    /**
     * Checks if the provided alias belongs to a command
     *
     * @param alias The command
     * @return true if the alias belongs to a command
     */
    public static boolean isCommandAlias(String alias) {
        return commands
                .entrySet()
                .stream()
                .anyMatch(entry -> entry.getKey().equalsIgnoreCase(alias)
                        || (!entry.getValue().getAliases().isEmpty() && entry.getValue().getAliases().contains(alias)));
    }

    /**
     * Gets the command that the provided alias belongs to
     *
     * @param alias The command
     * @return The {@link Command} that the alias belongs to
     */
    public static Command getCommandByAlias(String alias) {
        return commands
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().equalsIgnoreCase(alias)
                        || entry.getValue().getAliases().contains(alias))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(null);
    }

    /**
     * Finds and executes the command
     *
     * @param event The {@link MessageReceivedEvent}
     */
    public static void processCommand(MessageReceivedEvent event) {
        CommandParser.CommandContainer cmd = DiscordBot.getCommandParser().parse(event.getMessage().getContentRaw(), event);

        if (isCommandAlias(cmd.command.toLowerCase())) {
            Command command = getCommandByAlias(cmd.command.toLowerCase());
            CommandEvent cEvent = new CommandEvent(cmd);
            boolean hasPerms = checkPerms(cEvent, command);

            if (hasPerms) {
                command.onCommand(cEvent);
            }

            List<TextChannel> channels = cmd.event.getGuild().getTextChannelsByName("cmdlog", true);
            if (channels.size() > 0) {
                User author = cmd.event.getAuthor();
                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(hasPerms ? Config.getSuccessEmbedColor() : Config.getErrorEmbedColor())
                        .setAuthor(author.getName() + "#" + author.getDiscriminator(), "https://web.engr.ship.edu", author.getAvatarUrl())
                        .addField("Command", "**``" + cmd.beheaded + "``**", true)
                        .addField("Channel Name", "**``#" + cmd.event.getChannel().getName() + "``**", true)
                        .addField("Has Permission", hasPerms ? ":white_check_mark:" : ":negative_squared_cross_mark:", true)
                        .setTimestamp(Instant.now());
                Util.sendMsg(channels.get(0), builder.build());
            }
        }
    }

    /**
     * Checks to see if the command author has permission to use the command
     *
     * @param event The {@link CommandEvent}
     * @param command The {@link Command}
     * @return true if the author has the correct permission
     */
    public static boolean checkPerms(CommandEvent event, Command command) {
        TextChannel channel = event.getTextChannel();
        Member member = event.getMember();
        return member.hasPermission(command.getPermissions()) || member.hasPermission(channel, command.getPermissions());
    }

}
