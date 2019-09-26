package edu.ship.engr.discordbot.gateways;

import java.util.Objects;

import org.apache.commons.csv.CSVRecord;

import edu.ship.engr.discordbot.utils.csv.CSVHandler;

public class DiscordGateway {

	private CSVHandler discordCSVHandler = new CSVHandler("users");
	
	 /**
     * Gets the discord id matching the provided email address
     *
     * @param email the email to search for
     * @return the student's discord id
     */
    public  String getDiscordIdByEmail(String email) {
        for (CSVRecord record : Objects.requireNonNull(discordCSVHandler).getRecords()) {
            String r_email = record.get("email");

            if (!email.equalsIgnoreCase(r_email)) continue;

            return record.get("discord_id");
        }
        return null;
    }
}
