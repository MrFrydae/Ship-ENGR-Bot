package edu.ship.engr.discordbot.gateways;

import java.util.Objects;

import edu.ship.engr.discordbot.containers.Student;
import edu.ship.engr.discordbot.utils.csv.CSVHandler;
import edu.ship.engr.discordbot.utils.csv.CSVRecord;

/**
 * Gathers information about student's association with crews.
 *
 * @author merlin
 */
public class CrewGateway {
    private CSVHandler crewHandler = new CSVHandler("crews");

    /**
     * Gets the {@link Student student}'s crew.
     *
     * @param email The email to search for
     * @return the student's crew
     */
    public  String getCrewByEmail(String email) {
        for (CSVRecord record : Objects.requireNonNull(crewHandler).getRecords()) {
            String recordEmail = record.get("EMAIL");

            if (!email.equalsIgnoreCase(recordEmail)) {
                continue;
            }

            return record.get("crew");
        }
        return null;
    }
}
