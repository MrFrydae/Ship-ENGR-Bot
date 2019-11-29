package edu.ship.engr.discordbot.gateways;

import java.util.List;

import edu.ship.engr.discordbot.utils.csv.CSVHandler;
import edu.ship.engr.discordbot.utils.csv.CSVRecord;

/**
 * Gathers information about students.
 * @author merlin
 *
 */
public class StudentGateway {

	CSVHandler studentClassesHandler = new CSVHandler("students");

	/**
	 * get all of them
	 * @return a list of all of the records of students
	 */
	protected List<CSVRecord> getRecords()
	{
		return studentClassesHandler.getRecords();
	}
}
