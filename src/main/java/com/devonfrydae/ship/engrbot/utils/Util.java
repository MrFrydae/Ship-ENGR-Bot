package com.devonfrydae.ship.engrbot.utils;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;

public class Util {
    public static void sendMsg(MessageChannel channel, String message) {
        channel.sendMessage(message).queue();
    }
    public static void sendPrivateMsg(User user, String message) {
        user.openPrivateChannel().queue(channel -> sendMsg(channel, message));
    }
    public static void sendMsg(MessageChannel channel, MessageEmbed embed) {
        channel.sendMessage(embed).queue();
    }
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
}
