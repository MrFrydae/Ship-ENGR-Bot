package edu.ship.engr.discordbot.gateways;

import java.util.List;
import java.util.Objects;

import com.google.common.collect.Lists;

import edu.ship.engr.discordbot.containers.Professor;
import edu.ship.engr.discordbot.utils.Patterns;
import edu.ship.engr.discordbot.utils.csv.CSVHandler;
import edu.ship.engr.discordbot.utils.csv.CSVRecord;

/**
 * Gathers information about students.
 *
 * @author merlin
 */
public class ProfessorGateway {

    CSVHandler studentClassesHandler = new CSVHandler("students");

    /**
     * Searches the list of professors for a match.
     *
     * @param search The String to search for
     * @return a list of professors if any are found
     */
    public  List<Professor> getProfessorByNameOrEmail(String search) {
        List<Professor> professors = Lists.newArrayList();

        for (CSVRecord record : Objects.requireNonNull(getProfessorsInfo()).getRecords()) {
            Professor professor = getProfessor(record);

            // Match against name
            String recordName = record.get("professorName");
            String lastName = Patterns.SPACE.split(recordName)[2]; // (Dr.)=0 (FirstName)=1 (LastName)=2
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
    
    /**
     * Gets a CSVHandler for the professors file.
     *
     * @return All records from "professors.csv"
     */
    private  CSVHandler getProfessorsInfo() {
        return new CSVHandler("professors");
    }

    private  Professor getProfessor(CSVRecord record) {
        return new Professor(record.get("professorName"), record.get("title"), record.get("alma_mater"),
                record.get("specialty"), record.get("officeNumber"), record.get("email"), record.get("phone"),
                record.get("website"), record.get("office_hours"));
    }
}
