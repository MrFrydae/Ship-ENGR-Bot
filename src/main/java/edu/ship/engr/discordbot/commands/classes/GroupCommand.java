package edu.ship.engr.discordbot.commands.classes;

import com.google.common.collect.Lists;
import edu.ship.engr.discordbot.commands.BotCommand;
import edu.ship.engr.discordbot.commands.Command;
import edu.ship.engr.discordbot.commands.CommandEvent;
import edu.ship.engr.discordbot.commands.CommandType;
import edu.ship.engr.discordbot.tasks.TimeoutGroupTask;
import edu.ship.engr.discordbot.utils.GuildUtil;
import edu.ship.engr.discordbot.utils.Log;
import edu.ship.engr.discordbot.utils.Patterns;
import edu.ship.engr.discordbot.utils.TimeUtil;
import edu.ship.engr.discordbot.utils.Util;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.IPermissionHolder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.PermissionOverride;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@BotCommand(
        name = "group",
        type = CommandType.CLASSES
)
public class GroupCommand extends Command {
    public static long TIMEOUT_DELAY = TimeUtil.MINUTE.inSeconds(5);

    @Override
    public void onCommand(CommandEvent event) {
        if (event.hasArgs()) {
            switch (event.getArg(0).toLowerCase()) {
                case "create":
                    onCreate(event);
                    break;
                case "invite":
                    onInvite(event);
                    break;
                case "kick":
                    onKick(event);
                    break;
                case "delete":
                    onDelete(event);
                    break;
                case "rename":
                    onRename(event);
                    break;
            }
        }
    }

    private void onDelete(CommandEvent event) {
        if (!event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
            Util.sendMsg(event.getTextChannel(),
                    "You must be an admin to use this command.",
                    "Please try again once you've made enough friends");

            return;
        }

        TextChannel channel = GuildUtil.getGuild().getTextChannelById(Patterns.TAG_TEXT_CHANNEL.getGroup(event.getArg(1), 1));

        if (channel.getParent().getName().equals("Groups")) {
            processExpiredChannel(channel);
        }
    }

    // Usage: !group create <channelName> [numGroups]
    private void onCreate(CommandEvent event) {
        if (event.getArgs().length == 3) {
            if (!event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
                Util.sendMsg(event.getTextChannel(),
                        "You must be an admin to use this command.",
                        "Please try again without a number of groups");

                return;
            }

            int numGroups = Integer.parseInt(event.getArg(2));

            createGroupChannel(event, numGroups);
        } else {
            createGroupChannel(event, 1);
        }
    }

    private void createGroupChannel(CommandEvent event, int numGroups) {
        Category groups = GuildUtil.getCategory("Groups");

        if (numGroups == 0) {
            return;
        }

        int max = 0;

        for (TextChannel textChannel : groups.getTextChannels()) {
            if (textChannel.getName().startsWith("group-" + event.getArg(1))) {
                String[] split = textChannel.getName().split("-");

                int groupNum = Integer.parseInt(split[split.length - 1]);

                if (groupNum > max) {
                    max = groupNum;
                }
            }
        }

        groups.createTextChannel("group-" + event.getArg(1) + "-" + (max + 1)).queueAfter(1, TimeUnit.SECONDS, channel -> {
            channel.putPermissionOverride(event.getMember()).setAllow(Permission.VIEW_CHANNEL).queueAfter(1, TimeUnit.SECONDS);

            Util.sendMsg(channel, "Welcome to this group");

            channel.getManager().setTopic("This channel will expire after 1 month without activity").queueAfter(1, TimeUnit.SECONDS);

            createGroupChannel(event, numGroups - 1);
        });

        System.out.println("Created Groups");
    }

    // Usage: !group invite #text-channel @tagged-user
    private void onInvite(CommandEvent event) {
        TextChannel channel = GuildUtil.getGuild().getTextChannelById(Patterns.TAG_TEXT_CHANNEL.getGroup(event.getArg(1), 1));
        Member member = GuildUtil.getMember(Patterns.USER_MENTION.getGroup(event.getArg(2), 1));

        if (channel.getParent().getName().equalsIgnoreCase("Groups")) {
            if (member != null) {
                channel.putPermissionOverride(member).setAllow(Permission.VIEW_CHANNEL).queueAfter(1, TimeUnit.SECONDS);
            }
        }
    }

    // Usage: !group kick #text-channel @tagged-user
    private void onKick(CommandEvent event) {
        TextChannel channel = GuildUtil.getGuild().getTextChannelById(Patterns.TAG_TEXT_CHANNEL.getGroup(event.getArg(1), 1));
        Member member = GuildUtil.getMember(Patterns.USER_MENTION.getGroup(event.getArg(2), 1));

        if (channel.getParent().getName().equalsIgnoreCase("Groups")) {
            if (member != null) {
                channel.putPermissionOverride(member).setDeny(Permission.VIEW_CHANNEL).queueAfter(1, TimeUnit.SECONDS);
            }
        }

        if (member == event.getMember()) {
            Util.sendMsg(event.getTextChannel(), "You just kicked yourself... That must've hurt");
        }
    }

    public static List<TextChannel> getGroupChannels() {
        return GuildUtil.getCategory("Groups").getTextChannels();
    }

    public static void processExpiredChannel(TextChannel textChannel) {
        List<Member> members = getMembersInGroup(textChannel);

        for (Member member : members) {
            member.getUser().openPrivateChannel().queueAfter(1, TimeUnit.SECONDS, channel -> {
                channel.sendMessage("Log for " + textChannel.getName())
                        .addFile(getGroupLogFile(textChannel)).queue();
            });
        }

        archiveChannel(textChannel);
    }

    private static void archiveChannel(TextChannel textChannel) {
        // TODO: Actually archive the channel

        textChannel.delete().queue();
    }

    private static File getGroupLogFile(TextChannel channel) {
        File groups = Log.getTopLevelDirectory("Groups");

        return new File(groups, "group-" + channel.getId() + ".log");
    }

    public static List<Message> getMessagesInChannel(TextChannel channel) {
        List<Message> messages = Lists.newArrayList();

        String lastMessage = channel.getLatestMessageId();

        messages.add(channel.retrieveMessageById(lastMessage).complete());

        // While you can get messages before lastMessage, add them to the list, and update lastMessage
        while (true) {
            MessageHistory complete = channel.getHistoryBefore(lastMessage, 100).complete();

            if (complete.getRetrievedHistory().size() == 0) {
                break;
            }

            messages.addAll(complete.getRetrievedHistory());

            lastMessage = messages.get(messages.size() - 1).getId();
        }

        return messages;
    }

    private static List<Member> getMembersInGroup(TextChannel channel) {
        List<Member> members = Lists.newArrayList();

        for (PermissionOverride override : channel.getMemberPermissionOverrides()) {
            Member member = GuildUtil.getMember(override.getId());

            members.add(member);
        }

        return members;
    }

    private static void onRename(CommandEvent event) {
        Category groups = GuildUtil.getCategory("Groups");
        TextChannel channel = GuildUtil.getGuild().getTextChannelById(Patterns.TAG_TEXT_CHANNEL.getGroup(event.getArg(1), 1));
        int max = 0;

        if (channel.getName().startsWith("group-")) {
            for (TextChannel textChannel : groups.getTextChannels()) {
                if (textChannel.getName().startsWith("group-" + event.getArg(2))) {
                    String[] split = textChannel.getName().split("-");

                    int groupNum = Integer.parseInt(split[split.length - 1]);

                    if (groupNum > max) {
                        max = groupNum;
                    }
                }
            }
            channel.getManager().setName("group-" + event.getArg(2) + "-" + (max + 1)).queueAfter(1, TimeUnit.SECONDS);
        } else {
            Util.sendMsg(event.getTextChannel(), "Rename only applies to channels starting with the 'group-' prefix.");
        }
    }
}
