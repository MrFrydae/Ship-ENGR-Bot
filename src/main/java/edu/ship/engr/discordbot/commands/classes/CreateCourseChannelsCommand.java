package edu.ship.engr.discordbot.commands.classes;

import edu.ship.engr.discordbot.commands.BotCommand;
import edu.ship.engr.discordbot.commands.Command;
import edu.ship.engr.discordbot.commands.CommandEvent;
import edu.ship.engr.discordbot.commands.CommandType;
import edu.ship.engr.discordbot.containers.Course;
import edu.ship.engr.discordbot.utils.CSVUtil;
import edu.ship.engr.discordbot.utils.GuildUtil;
import edu.ship.engr.discordbot.utils.Util;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Role;

import java.util.Calendar;
import java.util.List;

@BotCommand(
        name = "createcoursechannels",
        aliases = "createcourses|createclasses",
        type = CommandType.CLASSES,
        permissions = {Permission.MANAGE_SERVER, Permission.MANAGE_CHANNEL}
)
public class CreateCourseChannelsCommand extends Command {
    @Override
    public void onCommand(CommandEvent event) {
        createCurrentSemester();
    }

    /**
     * Creates a category for every offered course in the current semester
     */
    public static void createCurrentSemester() {
        Calendar date = Calendar.getInstance();
        String semesterCode = Util.getSemesterCode(date);
        List<Course> currentCourses = CSVUtil.getOfferedCourses(semesterCode);
        currentCourses.forEach(CreateCourseChannelsCommand::createCourseCategory);
    }

    /**
     * Creates a category and text channel for the provided course.
     * Also sets permissions so only people in that course can see and change it.
     *
     * @param course The course to create a category for
     */
    private static void createCourseCategory(Course course) {
        Category courseCategory = GuildUtil.getCategory(course.getCode());
        Role role = GuildUtil.getRole(course.getCode());
        courseCategory.putPermissionOverride(role).setAllow(Permission.MANAGE_CHANNEL, Permission.VIEW_CHANNEL).queue();
        courseCategory.putPermissionOverride(GuildUtil.getProfessorRole()).setAllow(Permission.MANAGE_CHANNEL, Permission.VIEW_CHANNEL).queue();
        courseCategory.putPermissionOverride(GuildUtil.getPublicRole()).setDeny(Permission.VIEW_CHANNEL).queue();
        String channelName = course.getCode().replace("-", "") + "-general";
        GuildUtil.getGuild()
                .createTextChannel(channelName).setParent(courseCategory)
                .setTopic(course.getTitle()).queue();
    }
}
