package com.devonfrydae.ship.engrbot;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

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

    public static String getBotToken() {
        return getString("bot.token");
    }
}
