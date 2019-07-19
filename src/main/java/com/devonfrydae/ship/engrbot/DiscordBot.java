package com.devonfrydae.ship.engrbot;

import com.devonfrydae.ship.engrbot.commands.CommandParser;
import com.devonfrydae.ship.engrbot.commands.Commands;
import com.devonfrydae.ship.engrbot.listeners.CommandListener;
import com.devonfrydae.ship.engrbot.listeners.GuildListener;
import com.devonfrydae.ship.engrbot.listeners.MessageListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;

public class DiscordBot {
    private static JDA jda;
    private static CommandParser commandParser = new CommandParser();

    public static void main(String[] args) throws LoginException {
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        builder.setToken(Config.getBotToken());
        builder.addEventListener(new CommandListener());
        builder.addEventListener(new MessageListener());
        builder.addEventListener(new GuildListener());
        jda = builder.build();

        Commands.registerCommands();
    }

    // <editor-fold desc="Getters">
    public static CommandParser getCommandParser() {
        return commandParser;
    }
    public static JDA getJDA() {
        return jda;
    }
    // </editor-fold>
}
