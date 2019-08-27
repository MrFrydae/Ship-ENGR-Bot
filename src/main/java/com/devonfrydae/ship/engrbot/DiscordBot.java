package com.devonfrydae.ship.engrbot;

import com.devonfrydae.ship.engrbot.commands.CommandParser;
import com.devonfrydae.ship.engrbot.commands.Commands;
import com.devonfrydae.ship.engrbot.listeners.CommandListener;
import com.devonfrydae.ship.engrbot.listeners.GuildListener;
import com.devonfrydae.ship.engrbot.listeners.MessageListener;
import com.devonfrydae.ship.engrbot.tasks.Tasks;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.security.auth.login.LoginException;
import java.net.URL;
import java.util.Scanner;

public class DiscordBot {
    private static JDA jda;
    private static Logger logger;
    private static CommandParser commandParser = new CommandParser();

    public static void main(String[] args) throws LoginException {
        // Initialize logger
        logger = Logger.getLogger(DiscordBot.class);
        URL propFile = DiscordBot.class.getResource("/log4j.properties");
        PropertyConfigurator.configure(propFile);

        // Initialize bot
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        builder.setToken(Config.getBotToken());
        builder.addEventListener(new CommandListener());
        builder.addEventListener(new MessageListener());
        builder.addEventListener(new GuildListener());
        jda = builder.build();

        Commands.registerCommands();
        Tasks.initialize();

        // Scanner to kill bot from script
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type stop to exit");
        if (scanner.nextLine().equalsIgnoreCase("stop")) {
            System.exit(0);
        }
    }

    // <editor-fold desc="Getters">
    public static CommandParser getCommandParser() {
        return commandParser;
    }
    public static JDA getJDA() {
        return jda;
    }

    public static Logger getLogger() {
        return logger;
    }
    // </editor-fold>
}
