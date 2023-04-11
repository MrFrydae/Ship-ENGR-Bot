package edu.ship.engr.discordbot.commands.core;

import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Parameter;

@Builder
public final class CommandParameter {
    @Getter @NotNull private final Parameter parameter;
    @Getter private final String name;
    private final boolean optional;
    @Getter @Nullable private final String defaultValue;
    @Getter private final String description;
    @Getter private final String completion;
    @Getter private final String autoCompletion;
    @Getter private final String condition;

    public boolean isRequired() {
        return !optional;
    }

    public boolean isAutoComplete() {
        return autoCompletion != null;
    }

    public boolean hasCompletion() {
        return completion != null || autoCompletion != null;
    }

    public boolean hasCondition() {
        return condition != null;
    }
}
