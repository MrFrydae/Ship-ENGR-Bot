package edu.ship.engr.discordbot.commands.user;

import java.util.List;

import com.google.common.collect.Lists;

import edu.ship.engr.discordbot.commands.BotCommand;
import edu.ship.engr.discordbot.commands.Command;
import edu.ship.engr.discordbot.commands.CommandEvent;
import edu.ship.engr.discordbot.commands.CommandType;
import edu.ship.engr.discordbot.containers.MappedUser;
import edu.ship.engr.discordbot.containers.Student;
import edu.ship.engr.discordbot.gateways.DiscordGateway;
import edu.ship.engr.discordbot.gateways.StudentMapper;
import edu.ship.engr.discordbot.utils.GuildUtil;
import edu.ship.engr.discordbot.utils.Patterns;
import edu.ship.engr.discordbot.utils.Util;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

@BotCommand(name = "identify", aliases = "identity", usage = "<email/@mention>",
        description = "If you don't use an argument, it will put you in a data entry state|"
        + "If you mention somebody, it will give you information about them", type = CommandType.USER)
public class IdentifyCommand extends Command {
    private static List<User> entryStates = Lists.newArrayList();

    @Override
    public void onCommand(CommandEvent event) {
        if (!event.hasArgs()) {
            enterEntryState(event.getAuthor());
        } else {
            MappedUser user = new StudentMapper().getMappedUser(event.getArg(0));
            if (user != null) {
                user.sendUserInfo(event.getAuthor());
            }
        }
    }

    public static boolean isInEntryState(User user) {
        return entryStates.contains(user);
    }

    /**
     * Put the user into an info entry state.
     *
     * @param user the user
     */
    public static void enterEntryState(User user) {
        Util.sendPrivateMsg(user, "Please enter your Shippensburg University email",
                "If you are not a student, please type ``skip``.");

        if (!isInEntryState(user)) {
            entryStates.add(user);
        }
    }

    public static void leaveEntryState(User user) {
        entryStates.remove(user);
    }

    /**
     * Handle every instance of a private message event.
     *
     * @param event the event to handle
     */
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
                DiscordGateway discordGateway = new DiscordGateway();
                discordGateway.storeDiscordId(member.getUser().getId(), message.toLowerCase());
                setupUser(message);
                Util.sendPrivateMsg(user, "Thank you for registering and have a nice day");
                leaveEntryState(user);
            }
        }
    }

    /**
     * Enroll the student with the provided email and
     * change their nickname to their preferred name.
     *
     * @param email the email to search for
     */
    public static void setupUser(String email) {
        StudentMapper studentMapper = new StudentMapper();
        Student student = studentMapper.getStudentByEmail(email);
        student.enrollStudent();
        student.setNickname();
    }
}