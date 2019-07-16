package com.devonfrydae.ship.engrbot.commands;

import lombok.Getter;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public abstract class Command {
    public abstract void onCommand(MessageReceivedEvent event, String[] args);

    @Getter public String aliases;
    @Getter public String usage;
    @Getter public String description;
    @Getter public CommandType type;
    @Getter public Permission[] permissions;
}
