package com.devonfrydae.ship.engrbot.commands;

import com.devonfrydae.ship.engrbot.utils.Patterns;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public abstract class Command {
    public abstract void onCommand(MessageReceivedEvent event, String[] args);

    public String aliases;
    public String usage;
    public String description;
    public CommandType type;
    public Permission[] permissions;

    public List<String> getAliases() {
       return Arrays.asList(Patterns.PIPE.split(this.aliases));
    }

    public String getUsage() {
        return this.usage;
    }

    public String getDescription() {
        return this.description;
    }

    public CommandType getType() {
        return this.type;
    }

    public Permission[] getPermissions() {
        return this.permissions;
    }
}