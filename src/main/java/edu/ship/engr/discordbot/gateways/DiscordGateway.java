package edu.ship.engr.discordbot.gateways;

import edu.ship.engr.discordbot.utils.Exceptions.CSVException;
import edu.ship.engr.discordbot.utils.GuildUtil;
import edu.ship.engr.discordbot.utils.csv.CSVHandler;
import edu.ship.engr.discordbot.utils.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
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

    /**
     * Gets the discord id matching the provided email address.
     *
     * @param email
     *            the email to search for
     * @return the student's discord id
     */
    public String getDiscordIdByEmail(String email) {
        for (CSVRecord record : Objects.requireNonNull(userHandler).getRecords()) {
            String recordEmail = record.get(EMAIL_COLUMN_HEADER);

            if (!email.equalsIgnoreCase(recordEmail)) {
                continue;
            }

            return record.get(DISCORD_ID_COLUMN_HEADER);
        }
        return null;
    }

    /**
     * Gets the email matching the provided discord id.
     *
     * @param id the id to search for
     * @return the student's email
     */
    public String getEmailByDiscordId(String id) {
        for (CSVRecord record : Objects.requireNonNull(userHandler).getRecords()) {
            String recordId = record.get(DISCORD_ID_COLUMN_HEADER);

            if (!id.equals(recordId)) {
                continue;
            }

            return record.get(EMAIL_COLUMN_HEADER);
        }
        return null;
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
        for (CSVRecord record : Objects.requireNonNull(userHandler).getRecords()) {
            String recordEmail = record.get(EMAIL_COLUMN_HEADER);
            String recordID = record.get(DISCORD_ID_COLUMN_HEADER);

            if (recordEmail.equalsIgnoreCase(email) || recordID.equalsIgnoreCase(discordID)) {
                return true;
            }
        }
        return false;
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
                LinkedHashMap<String, String> entry = new LinkedHashMap<>();
                entry.put(EMAIL_COLUMN_HEADER, email);
                entry.put(DISCORD_ID_COLUMN_HEADER, discordID);
                userHandler.addEntry(entry);
            }
        } catch (CSVException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get a list of all of the emails in the data source.
     * @return all of the emails
     */
    public List<String> getAllEmails()
    {
        ArrayList<String> result = new ArrayList<>();
        userHandler.getRecords().forEach(record -> {
            String email = record.get("email").toLowerCase();
            result.add(email);
        });
        return result;
    }

    /**
     * Get a list of all of the ids in the data source.
     * @return all of the ids
     */
    public List<String> getAllIds() {
        // Collect a discord id
        // to see if that member is in the server,
        List<String> list = new ArrayList<>();
        for (CSVRecord record : userHandler.getRecords()) {
            String id = record.get("discord_id");
            if (GuildUtil.getMember(id) != null) {
                list.add(id);
            }
        }

        System.out.println(list.size());
        return list;                 // and add the id to the list.
    }

    private static void copyFileUsingStream(File source, File dest) throws IOException {
        try (InputStream is = new FileInputStream(source); OutputStream os = new FileOutputStream(dest)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }
    }

    /**
     * Save a copy of the csv file.
     */
    public void backUpTheData() {
        try {
            copyFileUsingStream(new File("stage/users.csv"), new File("stage/users.csv.bak"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Restore the saved copy of the file.
     */
    public void restoreTheData() {
        try {
            copyFileUsingStream(new File("stage/users.csv.bak"), new File("stage/users.csv"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
