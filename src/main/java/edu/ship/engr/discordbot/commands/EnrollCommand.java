package edu.ship.engr.discordbot.commands;

import edu.ship.engr.discordbot.commands.annotations.AutoCompletion;
import edu.ship.engr.discordbot.commands.annotations.CommandAlias;
import edu.ship.engr.discordbot.commands.annotations.CommandPermission;
import edu.ship.engr.discordbot.commands.annotations.Description;
import edu.ship.engr.discordbot.commands.annotations.Name;
import edu.ship.engr.discordbot.commands.annotations.Optional;
import edu.ship.engr.discordbot.commands.annotations.Subcommand;
import edu.ship.engr.discordbot.commands.core.BaseCommand;
import edu.ship.engr.discordbot.containers.Course;
import edu.ship.engr.discordbot.gateways.StudentMapper;
import edu.ship.engr.discordbot.systems.Registration;
import edu.ship.engr.discordbot.utils.GuildUtil;
import edu.ship.engr.discordbot.utils.Log;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.interactions.InteractionHook;

@CommandAlias("enroll")
@Description("Command to enroll students")
@CommandPermission({Permission.MANAGE_SERVER, Permission.MANAGE_ROLES, Permission.MANAGE_CHANNEL})
public class EnrollCommand extends BaseCommand {

    @CommandAlias("enrolleveryone")
    @Subcommand("everyone")
    @Description("Enroll all students into course channels")
    public void onEnrollEveryone(@Name("all") @Description("Whether to enroll students who already have the Student role") @Optional Boolean all) {
        InteractionHook hook = getEvent().getInteraction().deferReply(true).complete();

        new StudentMapper().getAllMappedStudents().forEach(student -> {
            if (student.getMember() == null) {
                return;
            }

            if ((all == null || !all) && student.getMember().getRoles().contains(GuildUtil.getStudentRole())) {
                Log.info("Skipping enrollment for: " + student.getEmail());
                return;
            }

            Registration.enrollStudent(student).queue();
        });

        hook.editOriginal("All Actions Queued").queue();
    }

    @CommandAlias("enrollstudent")
    @Subcommand("student")
    @Description("Enroll individual student into course channels")
    public void onEnrollStudent(@Name("student") @Description("The student you wish to enroll") Member member,
                                @Name("course") @Description("The course you wish to apply") @AutoCompletion("courses") Course course) {
        InteractionHook hook = getEvent().getInteraction().deferReply(true).complete();

        Category category = GuildUtil.getCategory(course.getCode());

        if (category == null) {
            Log.error("Category for %s does not exist... Creating one now", course.getCode());

            GuildUtil.createCourseCategoryAction(course).queue(c -> {
                Registration.getSetPrivilegesAction(c, member).queue(success -> {
                    hook.editOriginal("Student enrolled in " + course.getCode()).queue();
                });
            });
        } else {
            Registration.getSetPrivilegesAction(category, member).queue(success -> {
                hook.editOriginal("Student enrolled in " + course.getCode()).queue();
            });
        }
    }
}
