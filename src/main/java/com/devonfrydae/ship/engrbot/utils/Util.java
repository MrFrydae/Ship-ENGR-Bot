package com.devonfrydae.ship.engrbot.utils;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Util {
    /**
     * Sends a message
     *
     * @param channel The {@link MessageChannel} to send the message to
     * @param message The message to send
     */
    public static void sendMsg(MessageChannel channel, String message) {
        channel.sendMessage(message).queue();
    }

    /**
     * Sends a private message
     *
     * @param user The {@link User} to send the message to
     * @param message The message to send
     */
    public static void sendPrivateMsg(User user, String message) {
        user.openPrivateChannel().queue(channel -> sendMsg(channel, message));
    }

    /**
     * Sends an embedded message
     *
     * @param channel The {@link MessageChannel} to send the embed to
     * @param embed The {@link MessageEmbed} to send
     */
    public static void sendMsg(MessageChannel channel, MessageEmbed embed) {
        channel.sendMessage(embed).queue();
    }

    /**
     * Sends am embedded private message
     *
     * @param user The {@link User} to send the embed to
     * @param embed The {@link MessageEmbed} to send
     */
    public static void sendPrivateMsg(User user, MessageEmbed embed) {
        user.openPrivateChannel().queue(channel -> sendMsg(channel, embed));
    }

    //<editor-fold desc="Joining" defaultstate="collapsed">
    public static String join(Object[] objects) {
        return StringUtils.join(objects);
    }
    public static String join(String[] strings) {
        return join(strings, " ");
    }
    public static String join(String[] strings, String separator) {
        return StringUtils.join(strings, separator);
    }
    public static String join(List<String> strings) {
        return join(strings, " ");
    }
    public static String join(List<String> strings, String separator) {
        return StringUtils.join(strings, separator);
    }
    public static String join(Set<String> strings) {
        return join(strings, " ");
    }
    public static String join(Set<String> strings, String separator) {
        return StringUtils.join(strings, separator);
    }
    //</editor-fold>

    /**
     * Capitalizes the first letter of every word in the string
     */
    public static String ucfirst(String line) {
        return Arrays
                .stream(Patterns.SPACE.split(line.toLowerCase()))
                .map(s -> Character.toUpperCase(s.charAt(0)) + s.substring(1) + ' ')
                .collect(Collectors.joining())
                .trim();
    }

}
