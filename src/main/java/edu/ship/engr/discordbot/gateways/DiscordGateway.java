package edu.ship.engr.discordbot.gateways;

import com.google.common.collect.Lists;
import edu.ship.engr.discordbot.utils.Exceptions.CSVException;
import edu.ship.engr.discordbot.utils.csv.CSVHandler;
import edu.ship.engr.discordbot.utils.csv.CSVRecord;
import lombok.Cleanup;
import org.javatuples.Pair;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages the mappings of discord ids to ship email addresses.
 *
 * @author merlin
 */
public class DiscordGateway {

    private static final String DISCORD_ID_COLUMN_HEADER = "discord_id";
    private static final String EMAIL_COLUMN_HEADER = "email";
    private CSVHandler userHandler = new CSVHandler("users");

    public CSVHandler getHandler() {
        return userHandler;
    }

    /**
     * Gets the discord id matching the provided email address.
     *
     * @param email
     *            the email to search for
     * @return the student's discord id
     */
    @Nullable
    public String getDiscordIdByEmail(String email) {
        return getAllDiscordUsers().stream().filter(user -> user.getValue0().equalsIgnoreCase(email)).findFirst().map(Pair::getValue1).orElse(null);
    }

    /**
     * Gets the email matching the provided discord id.
     *
     * @param id the id to search for
     * @return the student's email
     */
    @Nullable
    public String getEmailByDiscordId(String id) {
        return getAllDiscordUsers().stream().filter(user -> user.getValue1().equalsIgnoreCase(id)).findFirst().map(Pair::getValue0).orElse(null);
    }

    /**
     * Checks if the Student's SU Email is matched to a Discord ID.
     *
     * @param discordID
     *            The Discord Member id
     * @param email
     *            The Student's SU Email
     * @return true if their data exists
     */
    public boolean isDiscordStored(String discordID, String email) {
        return getAllDiscordUsers().stream().anyMatch(user -> user.getValue0().equalsIgnoreCase(email) || user.getValue1().equalsIgnoreCase(discordID));
    }

    /**
     * Stores the user's Discord ID and the Student's SU Email into a file for
     * future usage.
     *
     * @param discordID
     *            The Discord ID for this user
     * @param email
     *            The Student's SU Email
     */
    public void storeDiscordId(String discordID, String email) {
        try {
            if (!isDiscordStored(discordID, email)) {
                userHandler.addEntry(Pair.with(email, discordID));
            }
        } catch (CSVException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get a list of all of the emails in the data source.
     *
     * @return all of the emails
     */
    public List<String> getAllEmails() {
        return getAllDiscordUsers().stream().map(Pair::getValue0).collect(Collectors.toList());
    }

    /**
     * Get a list of all of the ids in the data source.
     *
     * @return all of the ids
     */
    public List<String> getAllIds() {
        return getAllDiscordUsers().stream().map(Pair::getValue1).collect(Collectors.toList());
    }

    /**
     * Gets all mapped Discord users.
     *
     * @return all mapped Discord users
     */
    public List<Pair<String, String>> getAllDiscordUsers() {
        List<Pair<String, String>> users = Lists.newArrayList();

        for (CSVRecord record : getHandler().getRecords()) {
            String email = record.get(EMAIL_COLUMN_HEADER).toLowerCase().trim();
            String discordID = record.get(DISCORD_ID_COLUMN_HEADER).toLowerCase().trim();

            users.add(Pair.with(email, discordID));
        }

        return users;
    }

    private static void copyFileUsingStream(File source, File dest) throws IOException {
        @Cleanup InputStream is = new FileInputStream(source);
        @Cleanup OutputStream os = new FileOutputStream(dest);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
    }

    /**
     * Save a copy of the csv file.
     */
    @Deprecated(forRemoval = true)
    public void backUpTheData() {
        try {
            File source = new File("stage/users.csv");
            File dest = new File("stage/users.csv.bak");
            copyFileUsingStream(source, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Restore the saved copy of the file.
     */
    @Deprecated(forRemoval = true)
    public void restoreTheData() {
        try {
            File source = new File("stage/users.csv.bak");
            File dest = new File("stage/users.csv");

            copyFileUsingStream(source, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
