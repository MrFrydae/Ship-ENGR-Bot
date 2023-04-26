package edu.ship.engr.discordbot.commands;

import com.google.common.collect.Lists;
import dev.frydae.jda.commands.annotations.CommandAlias;
import dev.frydae.jda.commands.annotations.CommandPermission;
import dev.frydae.jda.commands.annotations.Description;
import dev.frydae.jda.commands.annotations.Subcommand;
import dev.frydae.jda.commands.core.BaseCommand;
import edu.ship.engr.discordbot.containers.Course;
import edu.ship.engr.discordbot.gateways.CourseGateway;
import edu.ship.engr.discordbot.utils.GuildUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.requests.RestAction;

import java.util.List;
import java.util.stream.Collectors;

@CommandAlias("maintenance")
@Description("Command for maintenance")
@CommandPermission({Permission.MANAGE_SERVER, Permission.MANAGE_ROLES, Permission.MANAGE_CHANNEL})
public class MaintenanceCommand extends BaseCommand {

    @CommandAlias("purgecoursechannels")
    @Subcommand("purge")
    @Description("Purge all course related channels")
    public void onPurgeCourseChannels() {
        InteractionHook hook = getEvent().getInteraction().deferReply(true).complete();

        List<RestAction<?>> actions = Lists.newArrayList();
        for (Course course : new CourseGateway().getAllOfferedCourses()) {
            Category category = GuildUtil.getCategory(course.getCode());

            List<RestAction<Void>> deleteActions = category.getChannels().stream().map(Channel::delete).collect(Collectors.toList());

            actions.add(RestAction.allOf(deleteActions).onSuccess(success -> category.delete().queue()));
        }

        RestAction.allOf(actions).queue(success -> hook.editOriginal("Courses purged").queue());
    }

    @CommandAlias("createcourses")
    @Subcommand("create")
    @Description("Create new course channels for this semester")
    public void onCreateNewCourses() {
        InteractionHook hook = getEvent().getInteraction().deferReply(true).complete();

        List<RestAction<?>> actions = Lists.newArrayList();
        for (Course course : new CourseGateway().getAllOfferedCourses()) {
            RestAction<Category> categoryRestAction = GuildUtil.createCourseCategoryAction(course);

            actions.add(categoryRestAction);
        }

        RestAction.allOf(actions).queue(success -> hook.editOriginal("Courses created").queue());
    }

}
