package com.devonfrydae.ship.engrbot.utils;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
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

    /**
     * Converts the date to a Semester Code
     * EX: "November 2019" -> "201960"
     * EX: "February 2021" -> "202120"
     *
     * @param date The date to convert
     * @return The formatted Semester Code
     */
    public static String getSemesterCode(Calendar date) {
        int month = date.get(Calendar.MONTH);
        int year = date.get(Calendar.YEAR);

        String mon;

        if (month < 6) mon = "20";
        else mon = "60";

        return year + mon;
    }

    /**
     * Searches all roles for the matching class role
     *
     * @param className The class code to search for
     * @return The role belonging to the provided course
     */
    public static Role getClass(String className) {
        className = formatClassName(className);
        return GuildUtil.getRole(className);
    }

    /**
     * Gets the proper class code with a dash
     *
     * @param className The raw class code
     * @return The class code with a dash
     */
    public static String formatClassName(String className) {
        Matcher matcher = Patterns.CLASS_NAME.matcher(className);

        String formatted = "";
        while (matcher.find()) {
            formatted = matcher.group(1).toUpperCase() + "-" + matcher.group(2);
        }
        return formatted;
    }

    /**
     * Formats the given code to a prettier format
     * EX: "201960" -> "Fall 2019"
     * EX: "202120" -> "Spring 2021"
     *
     * @param semesterCode Semester code from file
     * @return Semester code in readable format
     */
    public static String formatSemesterCode(String semesterCode) {
        String year = semesterCode.substring(0, 4);
        String semester = semesterCode.substring(4);

        semester = (semester.equals("20")) ? "Spring" : "Fall";

        return semester + " " + year;
    }

    /**
     * Gets the current time
     *
     * @return The current system time in a pretty format
     */
    public static String getCurrentSystemTime() {
        DateFormat format = new SimpleDateFormat("[dd.MM.yyyy - HH:mm:ss]");
        Date date = new Date();
        return format.format(date);
    }
}
