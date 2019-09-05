package edu.ship.engr.discordbot;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class Config {
    // <editor-fold desc="Getters">
    private static JSONObject get() {
        InputStream in = DiscordBot.class.getResourceAsStream("/config.json");
        Reader reader = new InputStreamReader(in);

        Object obj = null;
        try {
            obj = new JSONParser().parse(reader);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return (JSONObject) obj;
    }
    public static String getString(String key) {
        return (String) get().get(key);
    }
    public static Long getLong(String key) {
        return (Long) get().get(key);
    }
    public static Float getFloat(String key) {
        return (Float) get().get(key);
    }
    public static Double getDouble(String key) {
        return (Double) get().get(key);
    }
    // </editor-fold>

    /**
     * @return The bot token for launch
     */
    public static String getBotToken() {
        return getString("bot.token");
    }


    /**
     * @return The prefix for commands
     */
    public static String getCommandPrefix() {
        return getString("bot.command.prefix");
    }

    public static Color getPrimaryEmbedColor() {
        return new Color(22, 138, 233);
    }
    public static Color getErrorEmbedColor() {
        return new Color(255, 0, 0);
    }
    public static Color getSuccessEmbedColor() {
        return new Color(0, 255, 0);
    }
}
