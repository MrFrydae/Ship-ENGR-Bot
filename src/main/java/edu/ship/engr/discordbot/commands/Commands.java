package edu.ship.engr.discordbot.commands;

import edu.ship.engr.discordbot.commands.core.CommandCompletions;
import edu.ship.engr.discordbot.commands.core.CommandConditions;
import edu.ship.engr.discordbot.commands.core.CommandContexts;
import edu.ship.engr.discordbot.commands.core.CommandExecutionContext;
import edu.ship.engr.discordbot.commands.core.CommandManager;
import edu.ship.engr.discordbot.commands.core.IllegalCommandException;
import edu.ship.engr.discordbot.containers.Course;
import edu.ship.engr.discordbot.containers.Group;
import edu.ship.engr.discordbot.gateways.CourseGateway;
import edu.ship.engr.discordbot.systems.Groups;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.List;
import java.util.stream.Collectors;

public class Commands {

    /**
     * Register command related things.
     */
    public static void registerCommands() {
        new MiscCommands();
        new GroupCommand();
        new CourseCommands();
        new RegisterCommand();
        new MaintenanceCommand();
        new EnrollCommand();

        registerContexts();
        registerMappings();
        registerCompletions();

        CommandManager.upsertCommands();
    }

    private static void registerContexts() {
        CommandContexts<CommandExecutionContext> commandContexts = CommandManager.getCommandContexts();

        commandContexts.registerContext(Course.class, c -> {
            String courseCode = c.getMapping().getAsString();

            Course course = new CourseGateway().getCourse(courseCode);

            if (course == null) {
                throw new IllegalCommandException("Course: " + courseCode + " does not exist");
            }

            return course;
        });

        commandContexts.registerContext(Group.class, c -> {
            TextChannel channel = c.getMapping().getAsChannel().asTextChannel();

            if (!channel.getName().startsWith("group-")) {
                throw new IllegalCommandException("Invalid channel! Only channels prefixed with \"group-\" are accepted here.");
            }

            return Groups.getGroup(channel);
        });
    }

    private static void registerMappings() {
        CommandContexts<CommandExecutionContext> commandContexts = CommandManager.getCommandContexts();

        commandContexts.registerMapping(Course.class, OptionType.STRING);
        commandContexts.registerMapping(Group.class, OptionType.CHANNEL);
    }

    private static void registerCompletions() {
        CommandCompletions commandCompletions = CommandManager.getCommandCompletions();

        commandCompletions.registerAutoCompletion("courses", c -> {
            List<Course> allOfferedCourses = new CourseGateway().getAllOfferedCourses();

            return allOfferedCourses.stream()
                    .filter(e -> e.getCode().toLowerCase().startsWith(c.getCurrent().toLowerCase()))
                    .limit(25)
                    .map(e -> new Command.Choice(e.getCode(), e.getCode()))
                    .collect(Collectors.toList());
        });
    }

    private static void registerConditions() {
        CommandConditions commandConditions = CommandManager.getCommandConditions();

        commandConditions.addCondition(Group.class, "in", (c, exec, value) -> {
            if (value == null) {
                return;
            }

            if (!value.contains(exec.getEvent().getMember())) {
                throw new IllegalCommandException("You cannot run commands on this channel as you are not in it.");
            }
        });
    }
}
