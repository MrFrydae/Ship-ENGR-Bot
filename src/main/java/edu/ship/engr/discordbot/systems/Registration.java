package edu.ship.engr.discordbot.systems;

import com.google.common.collect.Lists;
import edu.ship.engr.discordbot.containers.Course;
import edu.ship.engr.discordbot.containers.Student;
import edu.ship.engr.discordbot.gateways.DiscordGateway;
import edu.ship.engr.discordbot.gateways.StudentMapper;
import edu.ship.engr.discordbot.utils.GuildUtil;
import edu.ship.engr.discordbot.utils.Log;
import edu.ship.engr.discordbot.utils.Util;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.requests.restaction.PermissionOverrideAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.CheckReturnValue;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Registration {
    //                                                                          type key  action
    private static final Pattern BUTTON_PATTERN = Pattern.compile("registration-(.+)-(.+)-(.+)");

    /**
     * Registers a student.
     *
     * @param hook the {@link InteractionHook} from deferring the slash command
     * @param member the member who sent the slash command
     * @param email the student's email
     */
    public static void registerStudent(InteractionHook hook, Member member, String email) {
        boolean discordStored = new DiscordGateway().isDiscordStored(member.getId(), email);
        Student student = new StudentMapper().getStudentByEmail(email);

        if (student == null) {
            hook.editOriginal("Sorry... We couldn't find your email in our systems. Asking for help now :)").queue(success -> {
                Objects.requireNonNull(getRequestsChannel()).sendMessage(String.format("%s needs help registering as a student: Email is %s", member.getAsMention(), email)).queue();
            });
        } else {
            if (discordStored) {
                hook.editOriginal("You have already been registered. Have a nice day").queue();

                enrollStudent(student).queue();
            } else {
                new DiscordGateway().storeDiscordId(member.getId(), email);

                enrollStudent(student).queue();

                hook.editOriginal("You have now been registered").queue();
            }
        }
    }

    /**
     * Enrolls a student into their course channels.
     *
     * @param student the student to enroll
     * @return a {@link RestAction} which will enroll the student
     */
    @CheckReturnValue
    @NotNull
    public static RestAction<List<Object>> enrollStudent(Student student) {
        List<RestAction<?>> actions = Lists.newArrayList();

        Member member = student.getMember();

        actions.add(student.setNickname());

        for (Course course : student.getCourses()) {
            Category category = GuildUtil.getCategory(course.getCode());

            if (category == null) {
                Log.error("Category for %s does not exist... Creating one now", course.getCode());

                actions.add(GuildUtil.createCourseCategoryAction(course).onSuccess(c -> {
                    getSetPrivilegesAction(c, member).queue();
                }));
            } else {
                actions.add(getSetPrivilegesAction(category, member));
            }
        }

        // TODO: Remove this eventually
        for (Role role : member.getRoles()) {
            if (role != null && GuildUtil.isCourseRole(role.getName())) {
                actions.add(GuildUtil.removeRoleFromMember(member, role));
            }
        }

        actions.add(GuildUtil.addRoleToMember(member, GuildUtil.getStudentRole()));
        actions.add(GuildUtil.addRoleToMember(member, GuildUtil.getRegisteredRole()));

        return RestAction.allOf(actions).onSuccess(c -> Log.info("Enrolled student: " + student.getName()));
    }

    /**
     * Process a registration request for alumni.
     *
     * @param hook {@link InteractionHook} from deferring slash command
     * @param event the {@link SlashCommandInteractionEvent} itself
     * @param name the name of the user
     */
    public static void registerAlumnus(InteractionHook hook, SlashCommandInteractionEvent event, String name) {
        registerRequest(hook, event, "alumnus", name);
    }

    /**
     * Process a registration request for professors.
     *
     * @param hook {@link InteractionHook} from deferring slash command
     * @param event the {@link SlashCommandInteractionEvent} itself
     * @param name the name of the user
     */
    public static void registerProfessor(InteractionHook hook, SlashCommandInteractionEvent event, String name) {
        registerRequest(hook, event, "professor", name);
    }

    /**
     * Process a registration request.
     *
     * @param hook {@link InteractionHook} from deferring slash command
     * @param event the {@link SlashCommandInteractionEvent} itself
     * @param key alumnus or professor
     * @param name the name of the user
     */
    public static void registerRequest(InteractionHook hook, SlashCommandInteractionEvent event, String key, String name) {
        TextChannel requestsChannel = getRequestsChannel();

        Member member = event.getMember();

        if (member == null) {
            return;
        }

        if (requestsChannel != null) {
            EmbedBuilder builder = new EmbedBuilder();
            builder.addField("Username", member.getUser().getName() + "#" + member.getUser().getDiscriminator(), true);
            builder.addField("Display Name", member.getAsMention(), true);
            builder.addField("Name", Util.ucfirst(name), true);

            requestsChannel.sendMessage("Registration Request for: " + Util.ucfirst(key))
                    .addEmbeds(builder.build())
                    .addActionRow(
                            Button.success("registration-" + key + "-accept-" + member.getId(), "Accept"),
                            Button.primary("registration-" + key + "-ban-" + member.getId(), "Ban User"),
                            Button.primary("registration-" + key + "-kick-" + member.getId(), "Kick User"),
                            Button.danger("registration-" + key + "-deny-" + member.getId(), "Deny")
                    ).queue();
        }
    }

    /**
     * Process a button event.
     *
     * @param event button event to process
     */
    public static void processRequestButton(ButtonInteractionEvent event) {
        String buttonId = event.getComponentId();
        Matcher matcher = BUTTON_PATTERN.matcher(buttonId);

        if (!matcher.matches()) {
            return;
        }

        String action = matcher.group(2);
        String userId = matcher.group(3);

        if (matcher.group(1).equalsIgnoreCase("alumnus")) {
            processRequestButton(event, action, userId, GuildUtil.getAlumniRole());
        } else if (matcher.group(1).equalsIgnoreCase("professor")) {
            processRequestButton(event, action, userId, GuildUtil.getProfessorRole());
        }
    }

    @SneakyThrows(NullPointerException.class)
    private static void processRequestButton(ButtonInteractionEvent event, String action, String userId, Role requestedRole) {
        Member member = GuildUtil.getMember(userId);

        if (member == null) {
            return;
        }

        if (action.equalsIgnoreCase("accept")) {
            GuildUtil.addRoleToMember(member, requestedRole).queue();
            GuildUtil.addRoleToMember(member, GuildUtil.getRegisteredRole()).queue();
        } else if (action.equalsIgnoreCase("kick")) {
            GuildUtil.getGuild().kick(member).queue();
        } else if (action.equalsIgnoreCase("ban")) {
            sendBanConfirm(event.getChannel().asTextChannel(), member);
        }

        event.getMessage().delete().queue();
    }

    private static void sendBanConfirm(TextChannel channel, Member member) {
        // TODO: Make this do something
    }

    @Nullable
    private static TextChannel getRequestsChannel() {
        return GuildUtil.getTextChannel("registration-requests");
    }

    @CheckReturnValue
    @NotNull
    public static PermissionOverrideAction getSetPrivilegesAction(Category category, Member member) {
        return category.upsertPermissionOverride(member).setAllowed(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND, Permission.USE_APPLICATION_COMMANDS);
    }
}
