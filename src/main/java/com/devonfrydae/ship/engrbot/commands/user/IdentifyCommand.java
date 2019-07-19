package com.devonfrydae.ship.engrbot.commands.user;

import com.devonfrydae.ship.engrbot.commands.BotCommand;
import com.devonfrydae.ship.engrbot.commands.Command;
import com.devonfrydae.ship.engrbot.commands.CommandEvent;
import com.devonfrydae.ship.engrbot.commands.CommandType;
import com.devonfrydae.ship.engrbot.commands.classes.EnrollCommand;
import com.devonfrydae.ship.engrbot.containers.MappedUser;
import com.devonfrydae.ship.engrbot.utils.CSVUtil;
import com.devonfrydae.ship.engrbot.utils.GuildUtil;
import com.devonfrydae.ship.engrbot.utils.Patterns;
import com.devonfrydae.ship.engrbot.utils.Util;
import com.google.common.collect.Lists;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

import java.util.List;

@BotCommand(
        name = "identify",
        aliases = "identity",
        usage = "identify <email/@mention>",
        description = "If you don't use an argument, it will put you in a data entry state|" +
                "If you mention somebody, it will give you information about them",
        type = CommandType.USER
)
public class IdentifyCommand extends Command {
    private static List<User> entryStates = Lists.newArrayList();

    @Override
    public void onCommand(CommandEvent event) {
        if (!event.hasArgs()) {
            enterEntryState(event.getAuthor());
        } else {
            MappedUser user = CSVUtil.getMappedUser(event.getArg(0));
            if (user == null) return;

            user.sendUserInfo(event.getAuthor());
        }
    }

    public static boolean isInEntryState(User user) {
        return entryStates.contains(user);
    }

    public static void enterEntryState(User user) {
        Util.sendPrivateMsg(user, "Please enter your Shippensburg University email");
        entryStates.add(user);
    }

    public static void leaveEntryState(User user) {
        entryStates.remove(user);
    }

    public static void handlePrivateMessage(PrivateMessageReceivedEvent event) {
        User user = event.getAuthor();
        String message = event.getMessage().getContentRaw();

        if (isInEntryState(user)) {
            if (!Patterns.VALID_EMAIL_PATTERN.matcher(message).matches()) {
                Util.sendPrivateMsg(user, "Please enter a valid email");
            } else {
                Member member = GuildUtil.getMember(user);
                leaveEntryState(user);
                Util.sendPrivateMsg(user, "Thank you for registering and have a nice day");
                CSVUtil.storeDiscordId(member, message);
                EnrollCommand.enrollMember(member, message);
                GuildUtil.setNickname(member, CSVUtil.getStudentName(message));
            }
        }
    }
}