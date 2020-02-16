package edu.ship.engr.discordbot.utils;

import edu.ship.engr.discordbot.DiscordBot;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Objects;

public class Log {
    private static Logger log = DiscordBot.getLogger();

    public static void debug(String message) {
        log.debug(message);
    }

    public static void info(String message) {
        log.info(message);
    }

    public static void warn(String message) {
        log.warn(message);
    }

    public static void error(String message) {
        log.error(message);
    }

    public static void exception(String msg) {
        exception(new Throwable(msg));
    }

    public static void exception(Throwable e) {
        exception(e.getMessage(), e);
    }

    /**
     * Throw an exception with a custom message.
     *
     * @param msg the custom message
     * @param e the exception
     */
    public static void exception(String msg, Throwable e) {
        if (msg != null) {
            log.error(msg);
        }
        log.error(ExceptionUtils.getStackTrace(e));
    }

    public static void logMessage(GuildMessageReceivedEvent event) {
        String memberName = Objects.requireNonNull(event.getMember()).getEffectiveName();
        String messageText = event.getMessage().getContentRaw();
        String channelName = event.getChannel().getName();
        String categoryName = Objects.requireNonNull(event.getMessage().getCategory()).getName();

        File messages = new File("Messages");
        if (!messages.exists()) {
            messages.mkdir();
        }

        File messageFolder = new File(messages, categoryName);
        if (!messageFolder.exists()) {
            messageFolder.mkdir();
        }

        if (Patterns.CLASS_NAME.matches(categoryName)) {
            messageFolder = new File(messageFolder, Util.formatSemesterCode(TimeUtil.getCurrentSemesterCode()));

            if (!messageFolder.exists()) {
                messageFolder.mkdir();
            }
        }

        File logFile = new File(messageFolder, channelName + ".log");
        try {
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
        } catch (IOException e) {
            Log.exception("Error creating log file: " + channelName + ".log", e);
        }

        String messageTime = event.getMessage().getTimeCreated().withOffsetSameInstant(ZoneOffset.ofHours(-5))
                .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.MEDIUM));

        String message = String.format("%s - %s - %s", messageTime, memberName, messageText);

        try (
            FileWriter fileWriter = new FileWriter(logFile, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bufferedWriter)) {

            printWriter.println(message);
        } catch (IOException e) {
            Log.exception("Error logging message: " + message, e);
        }
    }
}