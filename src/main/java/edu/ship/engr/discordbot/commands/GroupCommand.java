package edu.ship.engr.discordbot.commands;

import dev.frydae.jda.commands.annotations.CommandAlias;
import dev.frydae.jda.commands.annotations.Condition;
import dev.frydae.jda.commands.annotations.Description;
import dev.frydae.jda.commands.annotations.Disabled;
import dev.frydae.jda.commands.annotations.Name;
import dev.frydae.jda.commands.annotations.Subcommand;
import dev.frydae.jda.commands.core.BaseCommand;
import edu.ship.engr.discordbot.containers.Group;
import edu.ship.engr.discordbot.systems.Groups;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.InteractionHook;

@CommandAlias("group")
@Disabled
@Description("Command for group channels")
public class GroupCommand extends BaseCommand {

    @Subcommand("invite")
    @Description("Invite a member to a group channel")
    public void onInvite(@Name("member") @Description("The member you'd like to invite") Member member,
                         @Name("channel") @Description("The group you want to invite someone to") Group group) {
        InteractionHook hook = getEvent().getInteraction().deferReply(true).complete();

        Groups.inviteMember(hook, group, member);

        hook.editOriginal("Will invite " + member.getAsMention() + " to " + group.getChannel().getAsMention()).queue();
    }

    @Subcommand("kick")
    @Description("Kick a member from a group channel")
    public void onKick(@Name("member") @Description("The member you'd like to kick") Member member,
                       @Name("channel") @Description("The group you want to invite someone to") Group group) {
        InteractionHook hook = getEvent().getInteraction().deferReply(true).complete();

        Groups.kickMember(group, member);

        hook.editOriginal("Will kick " + member.getAsMention() + " from " + group.getAsMention()).queue();
    }

    @Subcommand("delete")
    @Description("Delete a group channel")
    public void onDelete(@Name("channel") @Description("The channel you want to delete") Group group) {
        InteractionHook hook = getEvent().getInteraction().deferReply(true).complete();

        Groups.deleteGroup(group);

        hook.editOriginal("Will delete " + group.getAsMention()).queue();
    }

    @Subcommand("create")
    @Description("Create a new group channel")
    public void onCreate(@Name("name") @Description("The name of the group you'd like to create") String name) {
        InteractionHook hook = getEvent().getInteraction().deferReply(true).complete();

        Group group = Groups.createGroup(name);

        hook.editOriginal("Will create " + group.getName()).queue();
    }

    @Subcommand("rename")
    @Description("Rename a group channel")
    public void onRename(@Name("group") @Description("The channel you want to rename") @Condition("in") Group group,
                         @Name("name") @Description("The new name for the channel") String newName) {
        InteractionHook hook = getEvent().getInteraction().deferReply(true).complete();

        String oldName = group.getAsMention();

        Groups.renameGroup(hook, group, newName);

        hook.editOriginal("Renamed " + oldName + " to " + group.getAsMention()).queue();
    }
}
