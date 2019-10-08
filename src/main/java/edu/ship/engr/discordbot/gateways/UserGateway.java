package edu.ship.engr.discordbot.gateways;

import java.util.ArrayList;

import edu.ship.engr.discordbot.utils.csv.CSVHandler;

public class UserGateway
{

	CSVHandler userCSV = new CSVHandler("users");
   
	public Iterable<String> getRecords()
	{
		ArrayList<String> result = new ArrayList<String>();
		userCSV.getRecords().forEach(record -> {
            String email = record.get("email").toLowerCase();
            result.add(email);
        });
		return result;
	}
}
