package edu.ship.engr.discordbot.gateways;

import java.util.List;

import edu.ship.engr.discordbot.utils.csv.CSVHandler;
import edu.ship.engr.discordbot.utils.csv.CSVRecord;

/**
 * Gathers information about students.
 *
 * @author merlin
 */
public class StudentGateway {

    CSVHandler studentClassesHandler = new CSVHandler("students");

    /**
     * Get all fo the records in the handler.
     *
     * @return a list of all of the records of students
     */
    protected List<CSVRecord> getRecords()
    {
        System.out.println("# of records: " + studentClassesHandler.getRecords().size());
        return studentClassesHandler.getRecords();
    }
}
