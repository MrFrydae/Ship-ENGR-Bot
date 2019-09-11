package edu.ship.engr.discordbot.commands.user;

import com.google.common.collect.Lists;
import edu.ship.engr.discordbot.commands.BotCommand;
import edu.ship.engr.discordbot.commands.Command;
import edu.ship.engr.discordbot.commands.CommandEvent;
import edu.ship.engr.discordbot.commands.CommandType;
import edu.ship.engr.discordbot.containers.MappedUser;
import edu.ship.engr.discordbot.containers.Student;
import edu.ship.engr.discordbot.utils.CSVUtil;
import edu.ship.engr.discordbot.utils.Exceptions;
import edu.ship.engr.discordbot.utils.GuildUtil;
import edu.ship.engr.discordbot.utils.Log;
import edu.ship.engr.discordbot.utils.Patterns;
import edu.ship.engr.discordbot.utils.Util;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

import java.util.List;

@BotCommand(
        name = "identify",
        aliases = "identity",
        usage = "<email/@mention>",
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
        Util.sendPrivateMsg(user,
                "Please enter your Shippensburg University email",
                "If you are not a student, please type ``skip``.");
        entryStates.add(user);
    }

    public static void leaveEntryState(User user) {
        entryStates.remove(user);
    }

    public static void handlePrivateMessage(PrivateMessageReceivedEvent event) {
        User user = event.getAuthor();
        String message = event.getMessage().getContentRaw();

        if (isInEntryState(user)) {
            if (message.equalsIgnoreCase("skip")) {
                leaveEntryState(user);
            } else if (!Patterns.VALID_EMAIL_PATTERN.matches(message)) {
                Util.sendPrivateMsg(user, "Please enter a valid Shippensburg University email");
            } else if (!Patterns.VALID_SHIP_EMAIL_PATTERN.matches(message)
                    && Patterns.VALID_EMAIL_PATTERN.matches(message)) {
                Util.sendPrivateMsg(user, "Please enter a valid Shippensburg University email");
            } else {
                Member member = GuildUtil.getMember(user);
                try {
                    CSVUtil.storeDiscordId(member, message.toLowerCase());
                    setupUser(message);
                    Util.sendPrivateMsg(user, "Thank you for registering and have a nice day");
                    leaveEntryState(user);
                } catch (Exceptions.IdentifyException e) {
                    Util.sendPrivateMsg(user, "Sorry, something bad happened on our end.", "Please try again with a valid Shippensburg University email");
                    Log.exception("There was an error setting up user. Nickname: " + member.getEffectiveName() + " Email: " + message);
                }
            }
        }
    }

    public static void setupUser(String email) {
        Student student = CSVUtil.getStudentByEmail(email);
        student.enrollStudent();
        student.setNickname();
    }
}
