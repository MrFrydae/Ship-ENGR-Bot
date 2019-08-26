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
        name = "purgecoursechannels",
        aliases = "cleancourses|cleanclasses",
        description = "Purge all course related channels",
        type = CommandType.CLASSES,
        permissions = {Permission.MANAGE_SERVER, Permission.MANAGE_CHANNEL}
)
public class PurgeCourseChannelsCommand extends Command {

    @Override
    public void onCommand(CommandEvent event) {
        wipeCategories();
    }

    /**
     * Wipe all categories from the current semester
     */
    private void wipeCategories() {
        List<Course> courses = CSVUtil.getOfferedCourses();
        for (Course course : courses) {
            Category category = GuildUtil.getCategory(course.getCode());
            if (category != null) {
                category.getVoiceChannels().forEach(channel -> channel.delete().queue());
                category.getTextChannels().forEach(channel -> channel.delete().queue());
                category.delete().queue();
            }
            GuildUtil.getGuild().getController().createCategory(course.getCode()).queue();
            Role role = GuildUtil.getRole(course.getCode());
            if (role != null) {
                role.delete().queue();
            }
            GuildUtil.getGuild().getController().createRole().setName(course.getCode()).queue();;
        }
    }
}
