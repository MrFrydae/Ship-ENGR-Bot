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
        purgeChannels();
    }

    /**
     * Empty all categories from the server
     */
    public static void purgeChannels() {
        List<Course> courses = CSVUtil.getSingleton().getAllOfferedCourses();
        for (Course course : courses) {
            Category category = GuildUtil.getCategory(course.getCode());
            if (category != null) {
                category.getVoiceChannels().forEach(channel -> channel.delete().queueAfter(1, TimeUnit.SECONDS));
                category.getTextChannels().forEach(channel -> channel.delete().queueAfter(1, TimeUnit.SECONDS));
                category.delete().queueAfter(1, TimeUnit.SECONDS);
            }
        }

        List<Course> currentCourses = CSVUtil.getSingleton().getCurrentlyOfferedCourses();
        currentCourses.forEach(course -> {
            GuildUtil.createCategoryAction(course.getCode()).queueAfter(1, TimeUnit.SECONDS, category -> {
                Role role = GuildUtil.getRole(course.getCode());
                category.putPermissionOverride(role).setAllow(Permission.MANAGE_CHANNEL, Permission.VIEW_CHANNEL).queue();
                category.putPermissionOverride(GuildUtil.getProfessorRole()).setAllow(Permission.MANAGE_CHANNEL, Permission.VIEW_CHANNEL).queue();
                category.putPermissionOverride(GuildUtil.getPublicRole()).setDeny(Permission.VIEW_CHANNEL).queue();
                String channelName = course.getCode().replace("-", "") + "-general";
                category.createTextChannel(channelName).setTopic(course.getTitle()).queue();
            });
        });
    }
}
