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

import java.util.List;
import java.util.concurrent.TimeUnit;

@BotCommand(
        name = "purgecoursechannels",
        aliases = "purgecourses|purgeclasses",
        description = "Purge all course related channels",
        type = CommandType.CLASSES,
        permissions = {Permission.MANAGE_SERVER, Permission.MANAGE_CHANNEL}
)
public class PurgeCourseChannelsCommand extends Command {

    @Override
    public void onCommand(CommandEvent event) {
        emptyCategories();
    }

    /**
     * Empty all categories from the server
     */
    public static void emptyCategories() {
        List<Course> courses = CSVUtil.getOfferedCourses();
        for (Course course : courses) {
            Category category = GuildUtil.getCategory(course.getCode());
            if (category != null) {
                category.getVoiceChannels().forEach(channel -> channel.delete().queueAfter(1, TimeUnit.SECONDS));
                category.getTextChannels().forEach(channel -> channel.delete().queueAfter(1, TimeUnit.SECONDS));
            }
        }
    }
}
