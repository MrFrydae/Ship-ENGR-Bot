package edu.ship.engr.discordbot.containers;

import net.dv8tion.jda.api.entities.User;

/**
 * Only used to link all types of {@link MappedUser} together
 */
public interface MappedUser {

    /**
     * Gets the {@link MappedUser user}'s full name
     *
     * @return The user's name
     */
    String getName();

    /**
     * Gets the {@link MappedUser user}'s Discord ID
     *
     * @return The user's Discord ID
     */
    String getDiscordId();

    /**
     * Gets the {@link MappedUser user}'s Shippensburg University Email
     *
     * @return The user's Shippensburg University Email
     */
    String getEmail();

    /**
     * Sends the {@link MappedUser}'s information to the provided user
     * Is implemented in child classes
     *
     * @param sendTo The user to send the info to
     */
    void sendUserInfo(User sendTo);
}
