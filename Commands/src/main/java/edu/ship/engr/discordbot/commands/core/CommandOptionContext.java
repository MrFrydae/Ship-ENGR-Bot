package edu.ship.engr.discordbot.commands.core;

import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.Map;

public class CommandOptionContext {
    @Getter private final String key;
    @Getter private final String config;
    @Getter private final Map<String, String> configs;

    CommandOptionContext(String input) {
        this.configs = Maps.newHashMap();

        String[] inputSplit = input.split("\\|");
        this.key = inputSplit[0];

        if (inputSplit.length > 1) {
            String config = inputSplit[1];

            if (config != null) {
                String[] configs = config.split(",");
                for (String s : configs) {
                    String[] split = s.split("=");

                    this.configs.put(split[0], split.length > 1 ? split[1] : null);
                }

                this.config = configs[0];
            } else {
                this.config = null;
            }
        } else {
            this.config = null;
        }
    }

    public String getConfig(String key) {
        return configs.getOrDefault(key.toLowerCase(), null);
    }

    public String getConfig(String key, String def) {
        return configs.getOrDefault(key.toLowerCase(), def);
    }

    public Integer getConfig(String key, Integer def) {
        return CommandUtils.parseInt(configs.get(key), def);
    }

    public boolean hasConfig(String key) {
        return configs.containsKey(key.toLowerCase());
    }
}
