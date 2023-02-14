package edu.ship.engr.discordbot.utils;

import com.google.common.collect.Lists;
import edu.ship.engr.discordbot.Config;
import edu.ship.engr.discordbot.DiscordBot;
import edu.ship.engr.discordbot.containers.Course;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.ForumChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import net.dv8tion.jda.api.requests.restaction.FluentAuditableRestAction;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.CheckReturnValue;
import java.util.List;
import java.util.Objects;

public class GuildUtil {
    private static final List<String> MAJOR_ROLE_NAMES = Lists.newArrayList(
            "Software Engineering", "Civil Engineering",
            "Computer Engineering", "Electrical Engineering",
            "Mechanical Engineering", "Computer Science");

    public static List<String> getMajorRoleNames() {
        return MAJOR_ROLE_NAMES;
    }

    /**
     * Get the discord {@link Guild guild} object for the server.
     *
     * @return The guild matching the id in "config.json"
     */
    @NotNull
    public static Guild getGuild() {
        return Objects.requireNonNull(DiscordBot.getJDA().getGuildById(Config.getGuildId()));
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
     * Finds a {@link ForumChannel} by name.
     *
     * @param forumName the name of the channel
     * @return a {@link ForumChannel} if one is found, null otherwise
     */
    public static ForumChannel getForumChannel(String forumName) {
        return getForumChannel(forumName, true);
    }

    /**
     * Finds a {@link ForumChannel} by name.
     *
     * @param forumName the name of the channel
     * @param ignoreCase whether to ignore case
     * @return a {@link ForumChannel} if one is found, null otherwise
     */
    public static ForumChannel getForumChannel(String forumName, boolean ignoreCase) {
        try {
            return getGuild().getForumChannelsByName(forumName, ignoreCase).get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Nullable
    public static Role getRegisteredRole() {
        return GuildUtil.getRole("Registered");
    }

    @Nullable
    public static Role getStudentRole() {
        return GuildUtil.getRole("Students");
    }

    @Nullable
    public static Role getAlumniRole() {
        return getRole("Alumni");
    }

    /**
     * Gets the role for the Software Engineering major.
     *
     * @return The "Software Engineering" {@link Role}
     */
    public static Role getSoftwareEngineeringRole() {
        return getRole(MAJOR_ROLE_NAMES.get(0));
    }

    /**
     * Gets the role for the Civil Engineering major.
     *
     * @return The "Civil Engineering" {@link Role}
     */
    public static Role getCivilEngineeringRole() {
        return getRole(MAJOR_ROLE_NAMES.get(1));
    }

    /**
     * Gets the role for the Computer Engineering major.
     *
     * @return The "Computer Engineering" {@link Role}
     */
    public static Role getComputerEngineeringRole() {
        return getRole(MAJOR_ROLE_NAMES.get(2));
    }

    /**
     * Gets the role for the Electrical Engineering major.
     *
     * @return The "Electrical Engineering" {@link Role}
     */
    public static Role getElectricalEngineeringRole() {
        return getRole(MAJOR_ROLE_NAMES.get(3));
    }

    /**
     * Gets the role for the Mechanical Engineering major.
     *
     * @return The "Mechanical Engineering" {@link Role}
     */
    public static Role getMechanicalEngineeringRole() {
        return getRole(MAJOR_ROLE_NAMES.get(4));
    }

    /**
     * Gets the role for the Computer Science major.
     *
     * @return The "Computer Science" {@link Role}
     */
    public static Role getComputerScienceRole() {
        return getRole(MAJOR_ROLE_NAMES.get(5));
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
     * Adds a role to a member.
     *
     * @param member the member to add a role to
     * @param role the role to add
     * @return an {@link AuditableRestAction} that adds the role
     * @throws NullPointerException if either parameter is null
     */
    @NotNull
    @CheckReturnValue
    @Contract("null, _ -> fail; !null, null -> fail")
    public static AuditableRestAction<Void> addRoleToMember(@Nullable Member member, @Nullable Role role) throws NullPointerException {
        if (member == null) {
            throw new NullPointerException("Cannot add role to null user");
        }

        if (role == null) {
            throw new NullPointerException("Cannot add null role to user");
        }

        return getGuild().addRoleToMember(member.getUser(), role);
    }

    /**
     * Removes a role from a member.
     *
     * @param member the member to remove a role from
     * @param role the role to remove
     * @return an {@link AuditableRestAction} that removes the role
     * @throws NullPointerException if either parameter is null
     */
    @NotNull
    @CheckReturnValue
    @Contract("null, _ -> fail; !null, null -> fail")
    public static AuditableRestAction<Void> removeRoleFromMember(@Nullable Member member, @Nullable Role role) throws NullPointerException {
        if (member == null) {
            throw new NullPointerException("Cannot remove role from null user");
        }

        if (role == null) {
            throw new NullPointerException("Cannot remove null role from user");
        }

        return getGuild().removeRoleFromMember(member, role);
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

        try {
            return getGuild().retrieveMemberById(userId).complete();
        } catch (ErrorResponseException e) {
            return null;
        }
    }

    /**
     * Changes a member's nickname.
     *
     * @param member The member to modify
     * @param nickname The new nickname
     */
    @CheckReturnValue
    public static AuditableRestAction<Void> setNickname(Member member, String nickname) {
        if (member == null) {
            Log.error("Tried to set nickname of null member to %s", nickname);
            return null;
        }

        if (nickname == null) {
            Log.warn("Tried to change " + member.getUser().getName() + "'s nickname to null");
            return null;
        }

        try {
            return getGuild().modifyNickname(member, nickname);
        } catch (HierarchyException e) {
            Log.error("Tried to change owner's nickname to: " + nickname);
            return null;
        }
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

    @CheckReturnValue
    public static ChannelAction<Category> createCategoryAction(String name) {
        return getGuild().createCategory(name);
    }

    @CheckReturnValue
    public static ChannelAction<ForumChannel> createForumAction(String name, Category parent) {
        return getGuild().createForumChannel(name, parent);
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
        return Objects.requireNonNull(member.getVoiceState()).getChannel().asVoiceChannel();
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

    public static void shutdown() {
        DiscordBot.getJDA().shutdownNow();
    }

    /**
     * Creates a category and a channel inside.
     *
     * @param course the course category to create
     * @return a {@link RestAction} that creates a category
     */
    @NotNull
    @CheckReturnValue
    public static RestAction<Category> createCourseCategoryAction(@NotNull Course course) {
        return createCategoryAction(course.getCode()).onSuccess(category -> {
            category.upsertPermissionOverride(getPublicRole()).setDenied(Permission.VIEW_CHANNEL).queue(success -> {
                String channelName = course.getCode().replace("-", "") + "-general";
                category.createTextChannel(channelName).setTopic(course.getTitle()).queue();
            });
        });
    }
}
