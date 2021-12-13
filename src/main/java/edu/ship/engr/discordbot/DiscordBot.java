package edu.ship.engr.discordbot;

import edu.ship.engr.discordbot.commands.CommandParser;
import edu.ship.engr.discordbot.commands.Commands;
import edu.ship.engr.discordbot.listeners.CommandListener;
import edu.ship.engr.discordbot.listeners.GuildListener;
import edu.ship.engr.discordbot.listeners.MessageListener;
import edu.ship.engr.discordbot.tasks.Tasks;
import edu.ship.engr.discordbot.utils.OptionsManager;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.security.auth.login.LoginException;
import java.net.URL;
import java.util.Scanner;

public class DiscordBot {
    private static JDA jda;
    private static Logger logger;
    private static CommandParser commandParser = new CommandParser();

    /**
     * This is my main method...
     * </p>
     * There are many like it but this one is mine.
     */
    public static void main(String[] args) throws LoginException {
        // Initialize logger
        logger = LogManager.getLogger(DiscordBot.class);

        // Initialize bot
        JDABuilder builder = JDABuilder.createDefault(Config.getBotToken());
        builder.addEventListeners(new CommandListener());
        builder.addEventListeners(new MessageListener());
        builder.addEventListeners(new GuildListener());
        jda = builder.build();

        if (System.getProperty("devMode").equals("true")) {
            OptionsManager.getSingleton().setDevMode(true);
        }

        Commands.registerCommands();
        Tasks.initialize();

        // Scanner to kill bot from script
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type stop to exit");
        new Thread(() -> {
            while (true) {
                if (scanner.nextLine().equalsIgnoreCase("stop")) {
                    System.exit(0);
                }
            }
        }).start();
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
