package com.devonfrydae.ship.engrbot.commands;

import com.devonfrydae.ship.engrbot.Config;
import com.devonfrydae.ship.engrbot.utils.Patterns;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class CommandParser {
    public CommandContainer parse(String rawMessage, MessageReceivedEvent event) {
        String beheaded = rawMessage.substring(Config.getCommandPrefix().length(), rawMessage.length());
        String[] splitBeheaded = Patterns.SPACE.split(beheaded);
        List<String> split = Arrays.asList(splitBeheaded);
        String command = split.get(0);
        String[] args = new String[split.size() - 1];
        split.subList(1, split.size()).toArray(args);

        return new CommandContainer(rawMessage, beheaded, splitBeheaded, command, args, event);
    }

    @AllArgsConstructor
    public class CommandContainer {
        public final String rawMessage;
        public final String beheaded;
        public final String[] splitBeheaded;
        public final String command;
        public final String[] args;
        public final MessageReceivedEvent event;
    }
}
