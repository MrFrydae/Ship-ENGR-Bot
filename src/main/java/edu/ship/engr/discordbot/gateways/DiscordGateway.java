package edu.ship.engr.discordbot.gateways;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import edu.ship.engr.discordbot.utils.Exceptions.CSVException;
import edu.ship.engr.discordbot.utils.csv.CSVHandler;
import edu.ship.engr.discordbot.utils.csv.CSVRecord;

/**
 * Manages the mappings of discord ids to ship email addresses
 * 
 * @author merlin
 *
 */
public class DiscordGateway {

	private static final String DISCORD_ID_COLUMN_HEADER = "discord_id";
	private static final String EMAIL_COLUMN_HEADER = "email";
	private CSVHandler discordCSVHandler = new CSVHandler("users");

	/**
	 * Gets the discord id matching the provided email address
	 *
	 * @param email
	 *            the email to search for
	 * @return the student's discord id
	 */
	public String getDiscordIdByEmail(String email) {
		for (CSVRecord record : Objects.requireNonNull(discordCSVHandler).getRecords()) {
			String r_email = record.get(EMAIL_COLUMN_HEADER);

			if (!email.equalsIgnoreCase(r_email))
				continue;

			return record.get(DISCORD_ID_COLUMN_HEADER);
		}
		return null;
	}

	/**
	 * Checks if the Student's SU Email is matched to a Discord ID
	 *
	 * @param discordID
	 *            The Discord Member id
	 * @param email
	 *            The Student's SU Email
	 * @return true if their data exists
	 */
	public boolean isDiscordStored(String discordID, String email) {
		for (CSVRecord record : Objects.requireNonNull(discordCSVHandler).getRecords()) {
			String r_email = record.get(EMAIL_COLUMN_HEADER);
			String r_id = record.get(DISCORD_ID_COLUMN_HEADER);

			if (r_email.equalsIgnoreCase(email) || r_id.equalsIgnoreCase(discordID)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Stores the user's Discord ID and the Student's SU Email into a file for
	 * future usage
	 *
	 * @param discordID
	 *            The Discord ID for this user
	 * @param email
	 *            The Student's SU Email
	 */
	public void storeDiscordId(String discordID, String email) {
		try {
			if (!isDiscordStored(discordID, email)) {
				HashMap<String, String> entry = new HashMap<String, String>();
				entry.put(EMAIL_COLUMN_HEADER, email);
				entry.put(DISCORD_ID_COLUMN_HEADER, discordID);
				discordCSVHandler.addEntry(entry);
			}
		} catch (Exception ignored) {
		} catch (CSVException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get a list of all of the records in the data source.
	 * @return all of the records
	 */
	public Iterable<String> getAllEmails()
	{
		ArrayList<String> result = new ArrayList<String>();
		discordCSVHandler.getRecords().forEach(record -> {
            String email = record.get("email").toLowerCase();
            result.add(email);
        });
		return result;
	}

	private static void copyFileUsingStream(File source, File dest) throws IOException {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(source);
			os = new FileOutputStream(dest);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
		} finally {
			is.close();
			os.close();
		}
	}

	/**
	 * Save a copy of the csv file
	 */
	public void backUpTheData() {
		try {
			copyFileUsingStream(new File("stage/users.csv"), new File("stage/users.csv.bak"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Restore the saved copy of the file
	 */
	public void restoreTheData() {

		try {
			copyFileUsingStream(new File("stage/users.csv.bak"), new File("stage/users.csv"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
