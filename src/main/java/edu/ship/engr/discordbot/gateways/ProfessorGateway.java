package edu.ship.engr.discordbot.gateways;

import com.google.common.collect.Lists;
import edu.ship.engr.discordbot.containers.Professor;
import edu.ship.engr.discordbot.utils.Patterns;
import edu.ship.engr.discordbot.utils.csv.CSVHandler;
import edu.ship.engr.discordbot.utils.csv.CSVRecord;

import java.util.List;
import java.util.Objects;

/**
 * Gathers information about students.
 *
 * @author merlin
 */
public class ProfessorGateway {

    CSVHandler professorsHandler = new CSVHandler("professors");

    /**
     * Searches the list of professors for a match.
     *
     * @param search The String to search for
     * @return a list of professors if any are found
     */
    public List<Professor> getProfessorByNameOrEmail(String search) {
        List<Professor> professors = Lists.newArrayList();

        for (CSVRecord record : Objects.requireNonNull(professorsHandler).getRecords()) {
            Professor professor = getProfessor(record);

            // Match against name
            String recordName = record.get("professorName");

            // (Dr.)=0 (FirstName)=1 (LastName)=2
            String lastName = Patterns.SPACE.split(recordName)[2];
            if (lastName.equalsIgnoreCase(search)) {
                professors.add(professor);
            }

            // Match against email
            String recordEmail = record.get("email");
            String userName = Patterns.VALID_EMAIL_PATTERN.getGroup(recordEmail, 1);
            if (search.equalsIgnoreCase(recordEmail)) {
                professors.add(professor);
            }

            if (search.equalsIgnoreCase(userName)) {
                professors.add(professor);
            }
        }

        return professors;
    }

    private Professor getProfessor(CSVRecord record) {
        return Professor.builder()
                .name(record.get("professorName"))
                .title(record.get("title"))
                .almaMater(record.get("alma_mater"))
                .specialty(record.get("specialty"))
                .officeNumber(record.get("officeNumber"))
                .email(record.get("email"))
                .phone(record.get("phone"))
                .website(record.get("website"))
                .officeHours(record.get("office_hours"))
                .build();
    }
}
