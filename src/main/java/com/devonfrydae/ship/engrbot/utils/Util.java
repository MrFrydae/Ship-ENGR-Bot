package com.devonfrydae.ship.engrbot.utils;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.util.Arrays;
import java.util.Calendar;
import java.util.stream.Collectors;

public class Util {
    /**
     * Sends a message
     *
     * @param channel The {@link MessageChannel} to send the message to
     * @param lines The message to send
     */
    public static Message sendMsg(MessageChannel channel, String... lines) {
        return channel.sendMessage(StringUtil.join(lines, "\n")).complete();
    }

    /**
     * Sends a private message
     *
     * @param user The {@link User} to send the message to
     * @param lines The message to send
     */
    public static void sendPrivateMsg(User user, String... lines) {
        user.openPrivateChannel().queue(channel -> sendMsg(channel, StringUtil.join(lines, "\n")));
    }

    /**
     * Sends an embedded message
     *
     * @param channel The {@link MessageChannel} to send the embed to
     * @param embed The {@link MessageEmbed} to send
     */
    public static Message sendMsg(MessageChannel channel, MessageEmbed embed) {
        return channel.sendMessage(embed).complete();
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
        Patterns.Pattern pattern = Patterns.CLASS_NAME;
        if (pattern.matches(className)) {
            return pattern.getGroup(className, 1).toUpperCase() + "-" + pattern.getGroup(className, 2).toUpperCase();
        }
        return null;
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

    public static Color decimalToColor(String dec) {
        int decimal = NumUtil.parseInt(dec);
        int red = (decimal >> 16) & 0xff;
        int green = (decimal >> 8) & 0xff;
        int blue = decimal & 0xff;
        return new Color(red, green, blue);
    }

    public static Color rgbToColor(String rgb) {
        Patterns.Pattern pattern = Patterns.RGB_COLOR;
        int red = NumUtil.parseInt(pattern.getGroup(rgb, 1));
        int green = NumUtil.parseInt(pattern.getGroup(rgb, 2));
        int blue = NumUtil.parseInt(pattern.getGroup(rgb, 3));
        return new Color(red, green, blue);
    }

    public static Color hexToColor(String hex) {
        hex = hex.replace("#", "");
        switch (hex.length()) {
            case 6:
                return new Color(
                        Integer.valueOf(hex.substring(0, 2), 16),
                        Integer.valueOf(hex.substring(2, 4), 16),
                        Integer.valueOf(hex.substring(4, 6), 16));
        }
        return null;
    }

    public static Color processColor(String color) {
        if (Patterns.HEX_COLOR.matches(color)) {
            return Color.decode(color.replace("#", ""));
        } else if (Patterns.RGB_COLOR.matches(color)) {
            return rgbToColor(color);
        } else {
            return decimalToColor(color);
        }
    }
}
