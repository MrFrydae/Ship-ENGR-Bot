package edu.ship.engr.discordbot.systems;

import com.google.common.collect.Lists;
import edu.ship.engr.discordbot.utils.GuildUtil;
import edu.ship.engr.discordbot.utils.Log;
import edu.ship.engr.discordbot.utils.Util;
import edu.ship.engr.discordbot.utils.java.CaselessHashMap;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class ChatFilter {
    private static HashMap<String, Pattern> CHAT_FILTER = new CaselessHashMap<>();
    private static boolean disabled = false;

    /**
     * Refreshes the stored filter words from the file
     */
    public static void update() {
        CaselessHashMap<Pattern> idler = new CaselessHashMap<>();
        try {
            loadFromLocal(idler);
        } catch (IOException e) {
            Log.exception("Error loading chat filter. Not gonna use it this time");
            disabled = true;
        }

        CHAT_FILTER.clear();
        CHAT_FILTER.putAll(idler);
    }

    /**
     * Checks if the provided message violates the chat filter
     *
     * @param message The message to scan
     * @return true if the message violates the filter
     */
    public static boolean isBadMessage(String message) {
        if (disabled) return false;
        List<String> violations = violations(message);
        return violations.size() > 0;
    }

    /**
     * Checks how many violations are in the message
     *
     * @param msg The message to scan
     * @return the amount of bad words in the message
     */
    private static List<String> violations(String msg) {
        List<String> matches = Lists.newArrayList();
        CHAT_FILTER.forEach((word, pattern) -> {
            if (pattern.matcher(msg.toLowerCase()).matches()) {
                matches.add(word);
            }
        });
        return matches;
    }

    /**
     * Loads the local file into the provided map
     *
     * @param idler the map to store into
     */
    private static void loadFromLocal(CaselessHashMap<Pattern> idler) throws IOException {
        List<String> words = Lists.newArrayList();
        BufferedReader reader = Files.newBufferedReader(Paths.get("chat_filter.txt"));

        String line;
        while ((line = reader.readLine()) != null) {
            words.add(line);
        }

        processWords(words, idler);
    }

    /**
     * Adds the provided strings into the map
     *
     * @param words The list of words to add the the map
     * @param idler The map to add words to
     */
    private static void processWords(List<String> words, CaselessHashMap<Pattern> idler) {
        words.forEach(word -> {
            Pattern pattern = Pattern.compile(".*\\b" + word.toLowerCase() + "\\b.*");
            idler.put(word, pattern);
        });
    }

    /**
     * Tell people about the violation
     *
     * @param event The message event
     */
    public static void sendNag(MessageReceivedEvent event) {
        Message message = event.getMessage();
        TextChannel channel = event.getTextChannel();
        Member member = event.getMember();
        User author = event.getAuthor();

        Util.sendPrivateMsg(author, "Your previous message in #" + channel.getName() + " violated our chat filter",
                "Please refrain from using words like that in the future",
                "Your message: " + message.getContentRaw());
        logViolation(event);

        TextChannel filterViolations = GuildUtil.getTextChannel("chat-filter-violations");
        Util.sendMsg(filterViolations, member.getEffectiveName() + " sent a bad message in #" + channel.getName() + ".",
                "Their message: " + message.getContentRaw());
        message.delete().queue();
    }

    /**
     * Log the violation in bot.log
     *
     * @param event The Message event
     */
    private static void logViolation(MessageReceivedEvent event) {
        Log.info("[Chat Filter] " + event.getMember().getEffectiveName() + " sent a bad message in #" + event.getTextChannel().getName() + ".");
        Log.info("[Chat Filter] Their message: " + event.getMessage().getContentRaw());
    }
}
