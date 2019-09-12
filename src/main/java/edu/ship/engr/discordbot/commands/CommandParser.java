package edu.ship.engr.discordbot.commands;

import com.google.common.collect.Lists;
import edu.ship.engr.discordbot.Config;
import edu.ship.engr.discordbot.utils.Patterns;
import edu.ship.engr.discordbot.utils.StringUtil;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class CommandParser {
    public CommandContainer parse(String rawMessage, MessageReceivedEvent event) {
        String beheaded = rawMessage.substring(Config.getCommandPrefix().length()).trim();
        String[] splitBeheaded = Patterns.SPACE.split(beheaded);
        List<String> split = Arrays.asList(splitBeheaded);
        String command = split.get(0);
        String[] rawArgs = new String[split.size() - 1];
        split.subList(1, split.size()).toArray(rawArgs);

        List<String> args = Lists.newArrayList();

        boolean inString = false;
        List<String> string = Lists.newArrayList();
        for (String arg : rawArgs) {
            if (arg.startsWith("\"") && arg.endsWith("\"")) {
                args.add(arg.replace("\"", ""));
            } else if (!arg.startsWith("\"") && !arg.endsWith("\"") && !inString) {
                args.add(arg);
            } else if (arg.startsWith("\"")) {
                string.add(arg.replace("\"", ""));
                inString = true;
            } else if (arg.endsWith("\"")) {
                string.add(arg.replace("\"", ""));
                args.add(StringUtil.join(string, " "));
                inString = false;
            } else if (inString) {
                string.add(arg);
            }
        }

        return new CommandContainer(rawMessage, beheaded, splitBeheaded, command, args.toArray(new String[0]), event);
    }

    protected static class CommandContainer {
        public final String rawMessage;
        public final String beheaded;
        public final String[] splitBeheaded;
        public final String command;
        public final String[] args;
        public final MessageReceivedEvent event;

        public CommandContainer(String rawMessage, String beheaded, String[] splitBeheaded, String command, String[] args, MessageReceivedEvent event) {
            this.rawMessage = rawMessage;
            this.beheaded = beheaded;
            this.splitBeheaded = splitBeheaded;
            this.command = command;
            this.args = args;
            this.event = event;
        }

        public String getRawMessage() {
            return rawMessage;
        }

        public String getBeheaded() {
            return beheaded;
        }

        public String[] getSplitBeheaded() {
            return splitBeheaded;
        }

        public String getCommand() {
            return command;
        }

        public String[] getArgs() {
            return args;
        }

        public MessageReceivedEvent getEvent() {
            return event;
        }
    }
}
