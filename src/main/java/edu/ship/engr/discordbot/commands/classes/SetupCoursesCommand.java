package edu.ship.engr.discordbot.commands.classes;

import edu.ship.engr.discordbot.commands.BotCommand;
import edu.ship.engr.discordbot.commands.Command;
import edu.ship.engr.discordbot.commands.CommandEvent;
import edu.ship.engr.discordbot.commands.CommandType;
import edu.ship.engr.discordbot.containers.Course;
import edu.ship.engr.discordbot.utils.CSVUtil;
import edu.ship.engr.discordbot.utils.GuildUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Role;

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
                GuildUtil.getGuild().createCategory(course.getCode()).complete();
            }
            Role role = GuildUtil.getRole(course.getCode());
            if (role == null) {
                GuildUtil.getGuild().createRole().setName(course.getCode()).complete();
            }
        }
    }
}
