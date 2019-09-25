package edu.ship.engr.discordbot.gateways;

import java.util.List;

import org.apache.commons.csv.CSVRecord;

import edu.ship.engr.discordbot.utils.csv.CSVHandler;

public class StudentGateway {

	CSVHandler studentClassesHandler = new CSVHandler("students");

	public List<CSVRecord> getRecords()
	{
		return studentClassesHandler.getRecords();
	}
}
