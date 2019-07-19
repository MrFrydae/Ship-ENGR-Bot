package com.devonfrydae.ship.engrbot.utils;

import com.devonfrydae.ship.engrbot.Config;
import com.devonfrydae.ship.engrbot.DiscordBot;
import com.google.common.collect.Lists;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class GuildUtil {

    /**
     * @return The guild matching the id in "config.json"
     */
    public static Guild getGuild() {
        return DiscordBot.getJDA().getGuildById(Config.getLong("bot.guild.id"));
    }

    /**
     * Finds the role matching the provided string, ignoring capitalization
     *
     * @param roleName The role to find, ignoring capitalization
     * @return The {@link Role} matching the provided name
     */
    public static Role getRole(String roleName) {
        return getRole(roleName, true);
    }

    /**
     * Finds the role matching the provided string
     *
     * @param roleName The role to find
     * @param ignoreCase Should we match against capital letters
     * @return The {@link Role} matching the provided name
     */
    public static Role getRole(String roleName, boolean ignoreCase) {
        return getGuild().getRolesByName(roleName, ignoreCase).get(0);
    }

    /**
     * Adds a list of roles to the member
     *
     * @param member The Discord Member object
     * @param toAdd A list of roles to add
     */
    public static void addRolesToMember(Member member, Role... toAdd) {
        addRolesToMember(member, Arrays.asList(toAdd));
    }

    /**
     * Adds a list of roles to the member
     *
     * @param member The Discord Member object
     * @param toAdd A list of roles to add
     */
    public static void addRolesToMember(Member member, List<Role> toAdd) {
        modifyRoles(member, toAdd, null);
    }

    /**
     * Removes a list of roles from the member
     *
     * @param member The Discord Member object
     * @param toRemove A list of roles to remove
     */
    public static void removeRolesFromMember(Member member, Role... toRemove) {
        removeRolesFromMember(member, Arrays.asList(toRemove));
    }

    /**
     * Removes a list of roles from the member
     *
     * @param member The Discord Member object
     * @param toRemove A list of roles to remove
     */
    public static void removeRolesFromMember(Member member, List<Role> toRemove) {
        modifyRoles(member, null, toRemove);
    }

    /**
     * @return The "OutOfBounds" {@link Role}
     */
    public static Role getOutOfBounds() {
        return GuildUtil.getRole("OutOfBounds");
    }

    /**
     * @return The "NullPointer" {@link Role}
     */
    public static Role getNullPointer() {
        return GuildUtil.getRole("NullPointer");
    }

    /**
     * @return The "OffByOne" {@link Role}
     */
    public static Role getOffByOne() {
        return GuildUtil.getRole("OffByOne");
    }

    /**
     * Gets the {@link Role} matching the provided string
     * @param crewName The crew name
     * @return The matching {@link Role}
     */
    public static Role getCrew(String crewName) {
        switch (crewName.toLowerCase()) {
            case "outofbounds": return getOutOfBounds();
            case "nullpointer": return getNullPointer();
            case "offbyone": return getOffByOne();
        }
        return null;
    }

    /**
     * Gets the {@link Member} matching the provided user ID
     *
     * @param userId The Discord Member's ID
     * @return The {@link Member} matching the provided user ID
     */
    public static Member getMember(String userId) {
        return getGuild().getMemberById(userId);
    }

    /**
     * Adds and removes roles from the {@link Member}
     *
     * @param member The Discord Member Object
     * @param toAdd A list of roles to add
     * @param toRemove A list of roles to remove
     */
    public static void modifyRoles(Member member, @Nullable List<Role> toAdd, @Nullable List<Role> toRemove) {
        if (toAdd == null) toAdd = Lists.newArrayList();
        if (toRemove == null) toRemove = Lists.newArrayList();

        getGuild().getController().modifyMemberRoles(member, toAdd, toRemove).queue();
    }
}
