package com.devonfrydae.ship.engrbot.commands;

import net.dv8tion.jda.api.Permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BotCommand {
    String name();

    String aliases() default "";

    String description() default "";

    String usage() default "";

    CommandType type() default CommandType.MISC;

    Permission[] permissions() default Permission.MESSAGE_WRITE;
}
