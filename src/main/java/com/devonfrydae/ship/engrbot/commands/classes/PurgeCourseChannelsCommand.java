package com.devonfrydae.ship.engrbot.commands.classes;

import com.devonfrydae.ship.engrbot.commands.BotCommand;
import com.devonfrydae.ship.engrbot.commands.Command;
import com.devonfrydae.ship.engrbot.commands.CommandEvent;
import com.devonfrydae.ship.engrbot.commands.CommandType;
import com.devonfrydae.ship.engrbot.containers.Course;
import com.devonfrydae.ship.engrbot.utils.CSVUtil;
import com.devonfrydae.ship.engrbot.utils.GuildUtil;
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
