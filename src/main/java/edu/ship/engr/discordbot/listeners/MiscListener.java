package edu.ship.engr.discordbot.listeners;

import edu.ship.engr.discordbot.systems.Registration;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.session.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MiscListener extends ListenerAdapter {
    @Override
    public void onShutdown(@NotNull ShutdownEvent event) {
        System.exit(0);
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (event.getComponentId().startsWith("registration")) {
            Registration.processRequestButton(event);
        }
    }
}
