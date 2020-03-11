package edu.ship.engr.discordbot.commands.user;

import com.google.common.collect.Maps;
import edu.ship.engr.discordbot.commands.BotCommand;
import edu.ship.engr.discordbot.commands.Command;
import edu.ship.engr.discordbot.commands.CommandEvent;
import edu.ship.engr.discordbot.commands.CommandType;
import edu.ship.engr.discordbot.containers.Alumnus;
import edu.ship.engr.discordbot.containers.MappedUser;
import edu.ship.engr.discordbot.containers.Student;
import edu.ship.engr.discordbot.containers.entrystates.identify.EnterEmailState;
import edu.ship.engr.discordbot.containers.entrystates.identify.IdentifyEntryState;
import edu.ship.engr.discordbot.gateways.StudentMapper;
import edu.ship.engr.discordbot.utils.Util;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

import java.util.Map;

@BotCommand(name = "identify", aliases = "identity", usage = "<email/@mention>",
        description = "If you don't use an argument, it will put you in a data entry state|"
        + "If you mention somebody, it will give you information about them", type = CommandType.USER)
public class IdentifyCommand extends Command {
    private static Map<User, IdentifyEntryState> entryStates = Maps.newHashMap();

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
        return entryStates.containsKey(user);
    }

    /**
     * Changes the user's entry state.
     *
     * @param user the user
     * @param state the new state
     */
    public static void changeEntryState(User user, IdentifyEntryState state) {
        entryStates.replace(user, state);

        state.promptUser(user);
    }

    public static IdentifyEntryState getEntryState(User user) {
        return entryStates.get(user);
    }

    /**
     * Put the user into an info entry state.
     *
     * @param user the user
     */
    public static void enterEntryState(User user) {
        Util.sendPrivateMsg(user, "You are now in a data entry state.",
                "If you are either a student or an alumnus, please type ``start``, otherwise type ``skip``.");

        if (!isInEntryState(user)) {
            Alumnus.AlumnusBuilder builder = new Alumnus.AlumnusBuilder();
            entryStates.put(user, new EnterEmailState(builder.discordId(user.getId())));
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

        if (isInEntryState(user)) {
            getEntryState(user).receiveMessage(event);
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
