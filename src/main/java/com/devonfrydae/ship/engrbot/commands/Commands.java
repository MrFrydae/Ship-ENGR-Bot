package com.devonfrydae.ship.engrbot.commands;

import com.devonfrydae.ship.engrbot.Config;
import com.devonfrydae.ship.engrbot.DiscordBot;
import com.devonfrydae.ship.engrbot.utils.Util;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.reflections.Reflections;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Commands {
    public static HashMap<String, Command> commands = new HashMap<>();

    public static void registerCommands() {
        Reflections commandClasses = new Reflections("com.devonfrydae.ship.engrbot.commands");
        Set<Class<? extends Command>> cmds = commandClasses.getSubTypesOf(Command.class);

        for (Class<? extends Command> cmd : cmds) {
            Command command = null;

            BotCommand annotation = cmd.getAnnotation(BotCommand.class);
            String name = annotation.name();

            try {
                command = cmd.getConstructor().newInstance();
                command.aliases = annotation.aliases();
                command.usage = annotation.usage();
                command.description = annotation.description();
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

    private static boolean isCommandAlias(String alias) {
        return commands
                .entrySet()
                .stream()
                .anyMatch(entry -> entry.getKey().equalsIgnoreCase(alias)
                        || (!entry.getValue().getAliases().isEmpty() && entry.getValue().getAliases().contains(alias)));
    }

    private static Command getCommandByAlias(String alias) {
        return commands
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().equalsIgnoreCase(alias)
                        || entry.getValue().getAliases().contains(alias))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(null);
    }

    public static void processCommand(MessageReceivedEvent event) {
        CommandParser.CommandContainer cmd = DiscordBot.getCommandParser().parse(event.getMessage().getContentRaw(), event);

        if (isCommandAlias(cmd.command.toLowerCase())) {
            Command command = getCommandByAlias(cmd.command.toLowerCase());
            boolean hasPerms = checkPerms(cmd.event, command);

            if (hasPerms) {
                command.onCommand(cmd.event, cmd.args);
            }

            List<TextChannel> channels = cmd.event.getGuild().getTextChannelsByName("cmdlog", true);
            if (channels.size() > 0) {
                User author = cmd.event.getAuthor();
                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(hasPerms ? Config.getSuccessEmbedColor() : Config.getErrorEmbedColor())
                        .setAuthor(author.getName() + "#" + author.getDiscriminator(), null, author.getAvatarUrl())
                        .addField("Command", "**``" + cmd.beheaded + "``**", true)
                        .addField("Channel Name", "**``#" + cmd.event.getChannel().getName() + "``**", true)
                        .addField("Time", "**``" + getCurrentSystemTime() + "``**", true)
                        .addField("Has Permission", hasPerms ? ":white_check_mark:" : ":negative_squared_cross_mark:", true);
                Util.sendMsg(channels.get(0), builder.build());
            }
        }
    }

    public static boolean checkPerms(MessageReceivedEvent event, Command command) {
        TextChannel channel = event.getTextChannel();
        Member member = event.getMember();
        return member.hasPermission(command.getPermissions()) || member.hasPermission(channel, command.getPermissions());
    }

    private static String getCurrentSystemTime() {
        DateFormat format = new SimpleDateFormat("[dd.MM.yyyy - HH:mm:ss]");
        Date date = new Date();
        return format.format(date);
    }
}
