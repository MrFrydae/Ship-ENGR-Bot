package edu.ship.engr.discordbot.commands;

import net.dv8tion.jda.api.Permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BotCommand {
    /**
     * The original command name.
     */
    String name();

    /**
     * Any aliases for this command.
     */
    String aliases() default "";

    /**
     * A description saying what this command does.
     */
    String description() default "";

    /**
     * A message containing the proper syntax for this command.
     */
    String usage() default "";

    /**
     * Which {@link CommandType type} of command this is.
     */
    CommandType type() default CommandType.MISC;

    /**
     * What {@link Permission permissions} this command requires.
     */
    Permission[] permissions() default Permission.MESSAGE_WRITE;
}
