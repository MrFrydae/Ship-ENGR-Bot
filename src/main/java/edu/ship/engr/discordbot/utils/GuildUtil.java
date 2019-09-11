package edu.ship.engr.discordbot.utils;

import edu.ship.engr.discordbot.Config;
import edu.ship.engr.discordbot.DiscordBot;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
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

public class GuildUtil {

    /**
     * @return The guild matching the id in "config.json"
     */
    private static Guild getGuild() {
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
        try {
            return getGuild().getRolesByName(roleName, ignoreCase).get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Find the category matching the provided string, ignoring capitalization
     *
     * @param categoryName The category to find, ignoring capitalization
     * @return The {@link Category} matching the provided name
     */
    public static Category getCategory(String categoryName) {
        return getCategory(categoryName, true);
    }

    /**
     * Finds the category matching the provided string
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
     * Finds the voice channel matching the provided name,
     * in the provided category, ignoring capitalization
     *
     * @param category The category to search in
     * @param channelName The channel to find, ignoring capitalization
     * @return The {@link VoiceChannel} matching the provided name
     */
    public static VoiceChannel getVoiceChannelFromCategory(Category category, String channelName) {
        return getVoiceChannelFromCategory(category, channelName, true);
    }

    /**
     * Finds the voice channel matching the provided name,
     * in the provided category
     *
     * @param category The category to search in
     * @param channelName The channel to find
     * @param ignoreCase Should we match against capital letters
     * @return The {@link VoiceChannel} matching the provided name
     */
    public static VoiceChannel getVoiceChannelFromCategory(Category category, String channelName, boolean ignoreCase) {
        return (VoiceChannel) getChannelFromCategory(category, ChannelType.VOICE, channelName, ignoreCase);
    }

    /**
     * Finds the text channel matching the provided name,
     * in the provided category, ignoring capitalization
     *
     * @param category The category to search in
     * @param channelName The channel to find, ignoring capitalization
     * @return The {@link TextChannel} matching the provided name
     */
    public static TextChannel getTextChannelFromCategory(Category category, String channelName) {
        return getTextChannelFromCategory(category, channelName, true);
    }

    /**
     * Finds the text channel matching the provided name,
     * in the provided category
     *
     * @param category The category to search in
     * @param channelName The channel to find
     * @param ignoreCase Should we match against capital letters
     * @return The {@link TextChannel} matching the provided name
     */
    public static TextChannel getTextChannelFromCategory(Category category, String channelName, boolean ignoreCase) {
        return (TextChannel) getChannelFromCategory(category, ChannelType.TEXT, channelName, ignoreCase);
    }

    /**
     * Finds the channel matching the provided name,
     * in the provided category
     *
     * @param category The category to search in
     * @param type Is this a Text or Voice Channel
     * @param channelName The channel to find
     * @param ignoreCase Should we match against capital letters
     * @return The {@link GuildChannel} matching the provided name
     */
    private static GuildChannel getChannelFromCategory(Category category, ChannelType type, String channelName, boolean ignoreCase) {
        GuildChannel channel = null;
        if (type == ChannelType.VOICE) {
            for (VoiceChannel voice : category.getVoiceChannels()) {
                if (StringUtil.equals(voice.getName(), channelName, ignoreCase)) {
                    channel = voice;
                }
            }
        } else if (type == ChannelType.TEXT) {
            for (TextChannel text : category.getTextChannels()) {
                if (StringUtil.equals(text.getName(), channelName, ignoreCase)) {
                    channel = text;
                }
            }
        }
        return channel;
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
     * @return The "@everyone" {@link Role}
     */
    public static Role getPublicRole() {
        return getGuild().getPublicRole();
    }

    /**
     * @return The "Professors" {@link Role}
     */
    public static Role getProfessorRole() {
        return getRole("Professors");
    }

    /**
     * Gets the {@link Member} matching the provided user object
     *
     * @param user The Discord User
     * @return The {@link Member} matching the provided user object
     */
    public static Member getMember(User user) {
        return getMember(user.getId());
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
     * Changes a member's nickname
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
     * Adds and removes roles from the {@link Member}
     *
     * @param member The Discord Member Object
     * @param toAdd A list of roles to add
     * @param toRemove A list of roles to remove
     */
    public static void modifyRoles(Member member, @Nullable List<Role> toAdd, @Nullable List<Role> toRemove) {
        try {
            getGuild().modifyMemberRoles(member, toAdd, toRemove).queue();
        } catch (IllegalArgumentException e) {
            Log.exception("Failed to modify member role. Member: " + member.getNickname(), e);
        }
        Log.info("Modified roles for " + member.getEffectiveName());
    }

    public static Role createRole(String name) {
        return getGuild().createRole().setName(name).complete();
    }

    public static Category createCategory(String name) {
        return getGuild().createCategory(name).complete();
    }

    // <editor-fold desc="Create Text Channels">
    public static TextChannel createTextChannel(String name) {
        return createTextChannel(name, "");
    }

    public static TextChannel createTextChannel(String name, String description) {
        return createTextChannel(name, description, null);
    }

    public static TextChannel createTextChannel(String name, Category parent) {
        return createTextChannel(name, "", parent);
    }

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
    // </editor-fold>

    // <editor-fold desc="Music Stuff">
    public static boolean isBotInVoiceChannel() {
        return getMemberVoiceChannel(getGuild().getSelfMember()) != null;
    }

    public static VoiceChannel getMemberVoiceChannel(Member member) {
        return member.getVoiceState().getChannel();
    }

    public static AudioManager getAudioManager() {
        return getGuild().getAudioManager();
    }
    // </editor-fold>

    public static Member getBotMember() {
        return getGuild().getSelfMember();
    }
}
