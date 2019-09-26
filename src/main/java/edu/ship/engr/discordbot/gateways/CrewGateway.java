package edu.ship.engr.discordbot.gateways;

import java.util.Objects;

import org.apache.commons.csv.CSVRecord;

import edu.ship.engr.discordbot.containers.Student;
import edu.ship.engr.discordbot.utils.csv.CSVHandler;

public class CrewGateway {

	private CSVHandler crewCSVHandler = new CSVHandler("crews");
	
    /**
     * Gets the {@link Student student}'s crew
     *
     * @param email The email to search for
     * @return the student's crew
     */
    public  String getCrewByEmail(String email) {
        for (CSVRecord record : Objects.requireNonNull(crewCSVHandler).getRecords()) {
            String r_email = record.get("EMAIL"); 

            if (!email.equalsIgnoreCase(r_email)) continue;

            return record.get("crew");
        }
        return null;
    }
}
