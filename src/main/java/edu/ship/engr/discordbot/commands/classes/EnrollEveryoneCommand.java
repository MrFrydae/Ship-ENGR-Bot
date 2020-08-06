package edu.ship.engr.discordbot.commands.classes;

import com.google.common.collect.Lists;
import edu.ship.engr.discordbot.commands.BotCommand;
import edu.ship.engr.discordbot.commands.Command;
import edu.ship.engr.discordbot.commands.CommandEvent;
import edu.ship.engr.discordbot.commands.CommandType;
import edu.ship.engr.discordbot.commands.user.IdentifyCommand;
import edu.ship.engr.discordbot.gateways.CrewGateway;
import edu.ship.engr.discordbot.gateways.DiscordGateway;
import edu.ship.engr.discordbot.gateways.StudentMapper;
import edu.ship.engr.discordbot.utils.GuildUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.List;
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
                Member member = GuildUtil.getMember(discordId);

                // Get the student's crew role
                List<Role> toAdd = Lists.newArrayList();
                Role crewRole = new CrewGateway().getCrewRoleByEmail(email);
                if (crewRole != null) toAdd.add(crewRole);

                List<Role> toRemove = getOldCourseRoles(member); // Get old courses from last semester

                GuildUtil.modifyRoles(member, toAdd, toRemove); // Add crew role and remove old course roles
            } else {
                IdentifyCommand.setupUser(email); // Setup this student
            }
        }
    }

    /**
     * Removes all course roles from the provided member.
     *
     * @param member The member to modify
     */
    private static List<Role> getOldCourseRoles(Member member) {
        return member.getRoles().stream()                               // Look at each role the member has,
                .filter(role -> GuildUtil.isCourseRole(role.getName())) // and if it is a course role
                .collect(Collectors.toList());                          // add it to the list to be removed
    }
}
