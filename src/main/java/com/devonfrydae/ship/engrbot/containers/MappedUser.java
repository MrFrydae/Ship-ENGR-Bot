package com.devonfrydae.ship.engrbot.containers;

import com.devonfrydae.ship.engrbot.DiscordBot;
import net.dv8tion.jda.core.entities.User;

/**
 * Only used to link all types of {@link MappedUser} together
 */
public abstract class MappedUser {
    public User user;
    public String email;
    public String discordId;

    public MappedUser(String email, String discordId) {
        this.user = DiscordBot.getJDA().getUserById(discordId);
        this.email = email;
        this.discordId = discordId;
    }

    public String getEmail() {
        return email;
    }

    public String getDiscordId() {
        return discordId;
    }

    /**
     * Sends the {@link MappedUser}'s information to the provided user
     * Is implemented in child classes
     *
     * @param sendTo The user to send the info to
     */
    public abstract void sendUserInfo(User sendTo);
}
