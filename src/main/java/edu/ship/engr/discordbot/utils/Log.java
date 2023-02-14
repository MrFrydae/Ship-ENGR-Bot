package edu.ship.engr.discordbot.utils;

import com.google.errorprone.annotations.FormatMethod;
import com.google.errorprone.annotations.FormatString;
import edu.ship.engr.discordbot.DiscordBot;
import lombok.Cleanup;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Objects;

public class Log {
    private static final Logger log = DiscordBot.getLogger();

    public static void debug(String message) {
        log.debug(message);
    }

    public static void info(String message) {
        log.info(message);
    }

    @FormatMethod
    public static void info(@FormatString String format, @Nullable Object... args) {
        log.info(String.format(format, args));
    }

    public static void warn(String message) {
        log.warn(message);
    }

    public static void error(String message) {
        log.error(message);
    }

    @FormatMethod
    public static void error(@FormatString String format, @Nullable Object... args) {
        log.error(String.format(format, args));
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

    /**
     * Finds the top-level directory by name.
     *
     * @param dirName the name of the directory
     * @return a File location
     */
    public static File getTopLevelDirectory(String dirName) {
        File topDir;
        if (OptionsManager.getSingleton().isDevMode()) {
            topDir = new File("stage", dirName);
        } else {
            topDir = new File(dirName);
        }

        if (!topDir.exists()) {
            topDir.mkdir();
        }

        return topDir;
    }

    /**
     * Logs the message to a file.
     *
     * @param event the message event
     */
    public static void logMessage(MessageReceivedEvent event) {
        String categoryName = Objects.requireNonNull(event.getMessage().getCategory()).getName();

        File messages = getTopLevelDirectory("Messages");

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

        logMessage(messageFolder, event, event.getChannel().getName());
    }

    @SneakyThrows(IOException.class)
    private static void logMessage(File parentDir, MessageReceivedEvent event, String fileName) {
        String memberName = Objects.requireNonNull(event.getMember()).getEffectiveName();
        String messageText = event.getMessage().getContentRaw();

        File logFile = new File(parentDir, fileName + ".log");
        try {
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
        } catch (IOException e) {
            Log.exception("Error creating log file: " + fileName + ".log", e);
        }

        String messageTime = event.getMessage().getTimeCreated().withOffsetSameInstant(ZoneOffset.ofHours(-5))
                .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.MEDIUM));

        String message = String.format("%s - %s - %s", messageTime, memberName, messageText);

        @Cleanup FileWriter fileWriter = new FileWriter(logFile, true);
        @Cleanup BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        @Cleanup PrintWriter printWriter = new PrintWriter(bufferedWriter);

        printWriter.println(message);
    }
}
