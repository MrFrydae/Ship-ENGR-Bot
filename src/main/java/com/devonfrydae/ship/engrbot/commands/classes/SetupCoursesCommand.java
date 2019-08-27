package com.devonfrydae.ship.engrbot.commands.classes;

import com.devonfrydae.ship.engrbot.commands.BotCommand;
import com.devonfrydae.ship.engrbot.commands.Command;
import com.devonfrydae.ship.engrbot.commands.CommandEvent;
import com.devonfrydae.ship.engrbot.commands.CommandType;
import com.devonfrydae.ship.engrbot.containers.Course;
import com.devonfrydae.ship.engrbot.utils.CSVUtil;
import com.devonfrydae.ship.engrbot.utils.GuildUtil;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Category;
import net.dv8tion.jda.core.entities.Role;

import java.util.List;

@BotCommand(
        name = "setupcoursechannels",
        aliases = "setupcourses|setupclasses",
        description = "Purge all course related channels",
        type = CommandType.CLASSES,
        permissions = {Permission.MANAGE_SERVER, Permission.MANAGE_CHANNEL}
)
public class SetupCoursesCommand extends Command {

    @Override
    public void onCommand(CommandEvent event) {
        createCategories();
    }

    /**
     * Make sure that a {@link Category} exists for each course
     */
    public static void createCategories() {
        List<Course> courses = CSVUtil.getOfferedCourses();
        for (Course course : courses) {
            Category category = GuildUtil.getCategory(course.getCode());
            if (category == null) {
                GuildUtil.getGuild().getController().createCategory(course.getCode()).complete();
            }
            Role role = GuildUtil.getRole(course.getCode());
            if (role == null) {
                GuildUtil.getGuild().getController().createRole().setName(course.getCode()).complete();
            }
        }
    }
}
