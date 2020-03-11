package edu.ship.engr.discordbot.gateways;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import edu.ship.engr.discordbot.containers.Alumnus;
import edu.ship.engr.discordbot.utils.Exceptions.CSVException;
import edu.ship.engr.discordbot.utils.Patterns;
import edu.ship.engr.discordbot.utils.csv.CSVHandler;
import edu.ship.engr.discordbot.utils.csv.CSVRecord;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

public class AlumnusGateway {
    private static final String DISCORD_ID_COLUMN_HEADER = "discord_id";
    private static final String EMAIL_COLUMN_HEADER = "email";
    private static final String NAME_COLUMN_HEADER = "name";
    private static final String GRAD_YEAR_COLUMN_HEADER = "grad_year";
    private static final String MAJORS_COLUMN_HEADER = "majors";
    private static final String MINORS_COLUMN_HEADER = "minors";
    private static final String MESSAGE_COLUMN_HEADER = "message";

    CSVHandler alumniHandler = new CSVHandler("alumni");

    /**
     * Get all of the records in the handler.
     *
     * @return a list of all of the records of alumni
     */
    protected List<CSVRecord> getRecords()
    {
        return alumniHandler.getRecords();
    }

    /**
     * Registers an alumnus into the file.
     *
     * @param discordId their discord id
     * @param email their email
     * @param name their name
     * @param gradYear their graduation year
     * @param majors their majors
     * @param minors their minors
     * @param message their message to students
     */
    public void register(String discordId, String email, String name, String gradYear, String majors, String minors, String message) {
        try {
            LinkedHashMap<String, String> entry = Maps.newLinkedHashMap();

            entry.put(DISCORD_ID_COLUMN_HEADER, discordId);
            entry.put(NAME_COLUMN_HEADER, name);
            entry.put(EMAIL_COLUMN_HEADER, email);
            entry.put(GRAD_YEAR_COLUMN_HEADER, gradYear);
            entry.put(MAJORS_COLUMN_HEADER, majors);
            entry.put(MINORS_COLUMN_HEADER, minors);
            entry.put(MESSAGE_COLUMN_HEADER, message);

            alumniHandler.addEntry(entry);
        } catch (CSVException e) {
            e.printStackTrace();
        }
    }

    /**
     * Searches the list of alumni for a match.
     *
     * @param search The String to search for
     * @return a list of alumni if any are found
     */
    public  List<Alumnus> getAlumnusByNameOrEmail(String search) {
        List<Alumnus> alumni = Lists.newArrayList();

        for (CSVRecord record : Objects.requireNonNull(alumniHandler).getRecords()) {
            Alumnus alumnus = getAlumnus(record);

            // Match against name
            String recordName = record.get("name");
            String lastName = Patterns.SPACE.split(recordName)[1]; // (FirstName)=0 (LastName)=1
            if (lastName.equalsIgnoreCase(search)) {
                alumni.add(alumnus);
            }

            // Match against email
            String recordEmail = record.get("email");
            String userName = Patterns.VALID_EMAIL_PATTERN.getGroup(recordEmail, 1);
            if (search.equalsIgnoreCase(recordEmail)) {
                alumni.add(alumnus);
            }
            if (search.equalsIgnoreCase(userName)) {
                alumni.add(alumnus);
            }

            if (Patterns.USER_MENTION.matches(search)) {
                if (Patterns.USER_MENTION.getGroup(search, 1).equals(record.get("discord_id"))) {
                    alumni.add(alumnus);
                }
            }
        }

        return alumni;
    }

    private Alumnus getAlumnus(CSVRecord record) {
        return new Alumnus(record.get("discord_id"), record.get("email"),
                record.get("name"), record.get("grad_year"),
                record.get("majors"), record.get("minors"), record.get("message"));
    }
}
