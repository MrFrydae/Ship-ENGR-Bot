package edu.ship.engr.discordbot.commands.classes;

import com.google.common.collect.Lists;
import edu.ship.engr.discordbot.commands.BotCommand;
import edu.ship.engr.discordbot.commands.Command;
import edu.ship.engr.discordbot.commands.CommandEvent;
import edu.ship.engr.discordbot.commands.CommandType;
import edu.ship.engr.discordbot.commands.user.IdentifyCommand;
import edu.ship.engr.discordbot.gateways.DiscordGateway;
import edu.ship.engr.discordbot.gateways.StudentMapper;
import edu.ship.engr.discordbot.utils.GuildUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@BotCommand(
        name = "enrolleveryone",
        type = CommandType.CLASSES,
        permissions = {Permission.MANAGE_ROLES, Permission.MANAGE_SERVER}
)
public class EnrollEveryoneCommand extends Command {
    @Override
    public void onCommand(CommandEvent event) {
        enrollEveryone();
    }

    /**
     * Enroll every registered user into their classes for this semester.
     */
    public static void enrollEveryone() {
        for (String discordId : new DiscordGateway().getAllIds()) {
            String email = new DiscordGateway().getEmailByDiscordId(discordId); // Get the email for this discord id

            if (new StudentMapper().getStudentByEmail(email) == null) { // Check if this student has SoE classes this semester
                removeCourseRoles(Objects.requireNonNull(GuildUtil.getMember(discordId))); // If they don't, remove their course roles
            }

            IdentifyCommand.setupUser(email); // Setup this student
        }
    }

    /**
     * Removes all course roles from the provided member.
     *
     * @param member The member to modify
     */
    private static void removeCourseRoles(Member member) {
        List<Role> toRemove = member.getRoles().stream()                // Look at each role the member has,
                .filter(role -> GuildUtil.isCourseRole(role.getName())) // and if it is a course role
                .collect(Collectors.toList());                          // add it to the list to be removed

        GuildUtil.modifyRoles(member, Lists.newArrayList(), toRemove);            // Send the roles to be removed
    }
}
