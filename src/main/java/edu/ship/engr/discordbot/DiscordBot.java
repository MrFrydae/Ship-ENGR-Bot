package edu.ship.engr.discordbot;

import edu.ship.engr.discordbot.commands.Commands;
import edu.ship.engr.discordbot.commands.core.CommandManager;
import edu.ship.engr.discordbot.gateways.DiscordGateway;
import edu.ship.engr.discordbot.listeners.CommandListener;
import edu.ship.engr.discordbot.listeners.GuildListener;
import edu.ship.engr.discordbot.listeners.MessageListener;
import edu.ship.engr.discordbot.listeners.MiscListener;
import edu.ship.engr.discordbot.tasks.Tasks;
import edu.ship.engr.discordbot.utils.GuildUtil;
import edu.ship.engr.discordbot.utils.OptionsManager;
import edu.ship.engr.discordbot.utils.java.CaselessHashMap;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;
import java.util.List;
import java.util.Scanner;

public class DiscordBot {
    private static JDA jda;
    private static Logger logger;

    /**
     * This is my main method...
     * </p>
     * There are many like it but this one is mine.
     */
    @SneakyThrows(InterruptedException.class)
    public static void main(String[] args) {
        // Initialize logger
        logger = LoggerFactory.getLogger(DiscordBot.class);

        // This block is only used if running through an IDE.
        // This can be ignored otherwise
        if (System.getProperty("devMode") != null) {
            OptionsManager.getSingleton().setDevMode(true);
        }

        // Initialize bot
        setupJDA();

        CommandManager.setJDA(jda);
        CommandManager.setLogger(logger);

        jda.awaitReady();

        Commands.registerCommands();
        Tasks.initialize();

        // Scanner to kill bot from script
        startTerminalListener();
    }

    private static void setupJDA() {
        JDABuilder builder = JDABuilder.createDefault(Config.getBotToken());
        builder.enableIntents(EnumSet.allOf(GatewayIntent.class));
        builder.addEventListeners(new CommandListener());
        builder.addEventListeners(new MessageListener());
        builder.addEventListeners(new GuildListener());
        builder.addEventListeners(new MiscListener());
        jda = builder.build();
    }

    private static void startTerminalListener() {
        CaselessHashMap<Runnable> actions = new CaselessHashMap<>();

        actions.put("stop", GuildUtil::shutdown);
        actions.put("audit users", () -> {
            List<String> allIds = new DiscordGateway().getAllIds();

            for (String memberId : allIds) {
                Member member = GuildUtil.getMember(memberId);

                if (member == null) {
                    System.out.printf("ID: %s not found\n", memberId);
                }
            }
        });

        Scanner scanner = new Scanner(System.in);
        System.out.println("Type stop to exit");

        new Thread(() -> {
            while (true) {
                String line = scanner.nextLine();

                if (actions.containsKey(line)) {
                    actions.get(line).run();
                }
            }
        }).start();
    }

    // <editor-fold desc="Getters">
    public static JDA getJDA() {
        return jda;
    }

    public static Logger getLogger() {
        return logger;
    }
    // </editor-fold>
}
