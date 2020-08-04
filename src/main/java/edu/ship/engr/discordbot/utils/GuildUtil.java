package edu.ship.engr.discordbot.utils;

import edu.ship.engr.discordbot.Config;
import edu.ship.engr.discordbot.DiscordBot;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class GuildUtil {

    /**
     * Get the discord {@link Guild guild} object for the server.
     *
     * @return The guild matching the id in "config.json"
     */
    private static Guild getGuild() {
        return DiscordBot.getJDA().getGuildById(Config.getLong("bot.guild.id"));
    }

    /**
     * Finds the role matching the provided string, ignoring capitalization.
     *
     * @param roleName The role to find, ignoring capitalization
     * @return The {@link Role} matching the provided name
     */
    public static Role getRole(String roleName) {
        return getRole(roleName, true);
    }

    /**
     * Finds the role matching the provided string.
     *
     * @param roleName The role to find
     * @param ignoreCase Should we match against capital letters
     * @return The {@link Role} matching the provided name
     */
    public static Role getRole(String roleName, boolean ignoreCase) {
        try {
            return getGuild().getRolesByName(roleName, ignoreCase).get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Find the category matching the provided string, ignoring capitalization.
     *
     * @param categoryName The category to find, ignoring capitalization
     * @return The {@link Category} matching the provided name
     */
    public static Category getCategory(String categoryName) {
        return getCategory(categoryName, true);
    }

    /**
     * Finds the category matching the provided string.
     *
     * @param categoryName The category to find
     * @param ignoreCase Should we match against capital letters
     * @return The {@link Category} matching the provided name
     */
    public static Category getCategory(String categoryName, boolean ignoreCase) {
        try {
            return getGuild().getCategoriesByName(categoryName, ignoreCase).get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Get the role for the OutOfBounds crew.
     *
     * @return The "OutOfBounds" {@link Role}
     */
    public static Role getOutOfBounds() {
        return GuildUtil.getRole("OutOfBounds");
    }

    /**
     * Get the role for the NullPointer crew.
     *
     * @return The "NullPointer" {@link Role}
     */
    public static Role getNullPointer() {
        return GuildUtil.getRole("NullPointer");
    }

    /**
     * Gets the role for the OffByOne role.
     *
     * @return The "OffByOne" {@link Role}
     */
    public static Role getOffByOne() {
        return GuildUtil.getRole("OffByOne");
    }

    /**
     * Gets the role for the public role.
     *
     * @return The "@everyone" {@link Role}
     */
    public static Role getPublicRole() {
        return getGuild().getPublicRole();
    }

    /**
     * Gets the role for the Professors role.
     *
     * @return The "Professors" {@link Role}
     */
    public static Role getProfessorRole() {
        return getRole("Professors");
    }

    /**
     * Gets the {@link Member} matching the provided user object.
     *
     * @param user The Discord User
     * @return The {@link Member} matching the provided user object
     */
    public static Member getMember(User user) {
        if (OptionsManager.getSingleton().isTestMode()) {
            return null;
        }

        return getMember(user.getId());
    }

    /**
     * Gets the {@link Member} matching the provided user ID.
     *
     * @param userId The Discord Member's ID
     * @return The {@link Member} matching the provided user ID
     */
    public static Member getMember(String userId) {
        if (OptionsManager.getSingleton().isTestMode()) {
            return null;
        }

        return getGuild().getMemberById(userId);
    }

    /**
     * Changes a member's nickname.
     *
     * @param member The member to modify
     * @param nickname The new nickname
     */
    public static void setNickname(Member member, String nickname) {
        if (nickname == null) {
            Log.warn("Tried to change " + member.getUser().getName() + "'s nickname to null");
            return;
        }

        try {
            getGuild().modifyNickname(member, nickname).queue();
        } catch (HierarchyException e) {
            Log.error("Tried to change owner's nickname to: " + nickname);
            return;
        }

        Log.info("Changed " + member.getUser().getName() + "'s nickname to " + nickname);
    }

    /**
     * Adds and removes roles from the {@link Member member}.
     *
     * @param member The Discord Member Object
     * @param toAdd A list of roles to add
     * @param toRemove A list of roles to remove
     */
    public static void modifyRoles(Member member, @Nullable List<Role> toAdd, @Nullable List<Role> toRemove) {
        try {
            getGuild().modifyMemberRoles(member, toAdd, toRemove).queue();
        } catch (IllegalArgumentException | HierarchyException e) {
            Log.exception("Failed to modify member role. Member: " + member.getNickname(), e);
        }
        Log.info("Modified roles for " + member.getEffectiveName());
    }

    /**
     * Creates a role with the provided name.
     *
     * @param name The name of the role
     * @return The role that has been created
     */
    public static Role createRole(String name) {
        return getGuild().createRole().setName(name).complete();
    }

    public static boolean isCourseRole(String name) {
        return name.toUpperCase().matches("[A-Z]{3,4}-\\d\\d\\d");
    }

    /**
     * Creates a category with the provided name.
     *
     * @param name The name of the category
     * @return The category that has been created
     */
    public static Category createCategory(String name) {
        return getGuild().createCategory(name).complete();
    }

    public static ChannelAction<Category> createCategoryAction(String name) {
        return getGuild().createCategory(name);
    }

    /**
     * Create a text channel with the provided information.
     *
     * @param name the name for the channel.
     * @param description the description for the channel.
     * @param parent the parent for this channel, null if it is alone
     * @return the newly created {@link TextChannel channel}
     */
    public static TextChannel createTextChannel(String name, String description, Category parent) {
        ChannelAction<TextChannel> action = getGuild().createTextChannel(name);

        if (parent != null) {
            action = action.setParent(parent);
        }

        if (!description.isEmpty()) {
            action = action.setTopic(description);
        }

        return action.complete();
    }

    // <editor-fold desc="Music Stuff">
    public static boolean isBotInVoiceChannel() {
        return getMemberVoiceChannel(getGuild().getSelfMember()) != null;
    }

    public static VoiceChannel getMemberVoiceChannel(Member member) {
        return Objects.requireNonNull(member.getVoiceState()).getChannel();
    }

    public static AudioManager getAudioManager() {
        return getGuild().getAudioManager();
    }
    // </editor-fold>

    public static Member getBotMember() {
        return getGuild().getSelfMember();
    }

    /**
     * Finds a {@link TextChannel Text Channel} with the provided name.
     *
     * @param name what to search for
     * @return the TextChannel matching the provided name
     */
    public static TextChannel getTextChannel(String name) {
        return getGuild().getTextChannels()
                .stream().filter(textChannel -> textChannel.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }
}
