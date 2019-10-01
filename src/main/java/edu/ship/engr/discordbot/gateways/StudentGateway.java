package edu.ship.engr.discordbot.gateways;

import java.util.List;

import org.apache.commons.csv.CSVRecord;

import edu.ship.engr.discordbot.utils.csv.CSVHandler;

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
