package edu.ship.engr.discordbot.gateways;

import edu.ship.engr.discordbot.containers.Student;
import edu.ship.engr.discordbot.utils.GuildUtil;
import edu.ship.engr.discordbot.utils.csv.CSVHandler;
import edu.ship.engr.discordbot.utils.csv.CSVRecord;
import net.dv8tion.jda.api.entities.Role;

import java.util.Objects;

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
            String recordEmail = record.get("email");

            if (!email.equalsIgnoreCase(recordEmail)) {
                continue;
            }

            return record.get("crew");
        }
        return null;
    }

    /**
     * Gets the {@link Student student}'s crew role.
     *
     * @param email The email to search for
     * @return the student's crew role
     */
    public Role getCrewRoleByEmail(String email) {
        String roleName = getCrewByEmail(email);

        switch (roleName.toLowerCase()) {
            case "outofbounds": return GuildUtil.getOutOfBounds();
            case "nullpointer": return GuildUtil.getNullPointer();
            case "offbyone": return GuildUtil.getOffByOne();
            default: return null;
        }
    }
}
