package com.devonfrydae.ship.engrbot.commands.classes;

import com.devonfrydae.ship.engrbot.commands.BotCommand;
import com.devonfrydae.ship.engrbot.commands.Command;
import com.devonfrydae.ship.engrbot.commands.CommandEvent;
import com.devonfrydae.ship.engrbot.commands.CommandType;
import com.devonfrydae.ship.engrbot.containers.Course;
import com.devonfrydae.ship.engrbot.utils.CSVUtil;
import com.devonfrydae.ship.engrbot.utils.GuildUtil;
import com.devonfrydae.ship.engrbot.utils.Util;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Category;
import net.dv8tion.jda.core.entities.Role;

import java.util.Calendar;
import java.util.List;

@BotCommand(
        name = "createnewcourses",
        aliases = "newcourses",
        type = CommandType.CLASSES,
        permissions = {Permission.MANAGE_SERVER, Permission.MANAGE_CHANNEL}
)
public class CreateNewChannelsCommand extends Command {
    @Override
    public void onCommand(CommandEvent event) {
        createCurrentSemester();
    }


    // <editor-fold desc="Create Courses" defaultstate="collapsed">
    /**
     * Creates a category for every offered course in the current semester
     */
    private void createCurrentSemester() {
        Calendar date = Calendar.getInstance();
        String semesterCode = Util.getSemesterCode(date);
        List<Course> currentCourses = CSVUtil.getOfferedCourses(semesterCode);
        currentCourses.forEach(this::createCourseCategory);
    }

    /**
     * Creates a category and text channel for the provided course.
     * Also sets permissions so only people in that course can see and change it.
     *
     * @param course The course to create a category for
     */
    private void createCourseCategory(Course course) {
        Category courseCategory = GuildUtil.getCategory(course.getCode());
        Role role = GuildUtil.getRole(course.getCode());
        courseCategory.putPermissionOverride(role).setAllow(Permission.MANAGE_CHANNEL, Permission.VIEW_CHANNEL).queue();
        courseCategory.putPermissionOverride(GuildUtil.getProfessorRole()).setAllow(Permission.MANAGE_CHANNEL, Permission.VIEW_CHANNEL).queue();
        courseCategory.putPermissionOverride(GuildUtil.getPublicRole()).setDeny(Permission.VIEW_CHANNEL).queue();
        String channelName = course.getCode().replace("-", "") + "-general";
        GuildUtil.getGuild().getController()
                .createTextChannel(channelName).setParent(courseCategory)
                .setTopic(course.getTitle()).queue();
    }
    // </editor-fold>
}
