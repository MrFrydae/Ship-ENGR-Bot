package com.devonfrydae.ship.engrbot.commands;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandEvent {
    private Message message;
    private String[] args;
    private Member member;
    private User author;
    private TextChannel textChannel;
    private MessageReceivedEvent event;
    private String command;

    public CommandEvent(CommandParser.CommandContainer cmd) {
        this.event = cmd.getEvent();
        this.member = event.getMember();
        this.author = event.getAuthor();
        this.args = cmd.getArgs();
        this.message = event.getMessage();
        this.textChannel = event.getTextChannel();
        this.command = cmd.getCommand();
    }

    public String getCommand() {
        return command;
    }

    public boolean isBaseCommand(String command) {
        return this.command.equalsIgnoreCase(command);
    }

    public Message getMessage() {
        return message;
    }

    public String[] getArgs() {
        return args;
    }

    /**
     * Get the provided command argument
     */
    public String getArg(int arg) {
        return args[arg];
    }

    /**
     * Checks if the event has at least one argument
     */
    public boolean hasArgs() {
        return args.length > 0;
    }

    public Member getMember() {
        return member;
    }

    public User getAuthor() {
        return author;
    }

    public TextChannel getTextChannel() {
        return textChannel;
    }

    public MessageReceivedEvent getEvent() {
        return event;
    }
}
