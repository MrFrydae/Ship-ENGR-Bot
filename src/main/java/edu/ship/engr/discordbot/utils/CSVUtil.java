package edu.ship.engr.discordbot.utils;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import org.apache.commons.csv.CSVRecord;

import com.google.common.collect.Lists;

import edu.ship.engr.discordbot.containers.Course;
import edu.ship.engr.discordbot.containers.MappedUser;
import edu.ship.engr.discordbot.containers.Professor;
import edu.ship.engr.discordbot.gateways.DiscordGateway;
import edu.ship.engr.discordbot.gateways.StudentMapper;
import edu.ship.engr.discordbot.utils.csv.CSVHandler;

public class CSVUtil {

	private static CSVUtil singleton;
	
	public static CSVUtil getSingleton()
	{
		if (singleton == null)
		{
			singleton = new CSVUtil();
		}
		return singleton;
	}
	
    private StudentMapper studentMapper;
	private DiscordGateway discordGateway;

    private CSVUtil()
    {
    	discordGateway = new DiscordGateway();
    }
	/**
     * @return All records from "crews.csv"
     */
    private  CSVHandler getCrews() {
        return new CSVHandler("crews");
    }

    /**
     * @return All records from "users.csv"
     */
    private  CSVHandler getDiscordIds() {
        return new CSVHandler("users");
    }

 

    /**
     * @return All records from "professors.csv"
     */
    private  CSVHandler getProfessorsInfo(){
        return new CSVHandler( "professors");
    }



    /**
     * Gets a {@link MappedUser} by either an email or mention
     *
     * @param search Either an email or a user mention
     * @return The {@link MappedUser} is one is found
     */
    public  MappedUser getMappedUser(String search) {
        for (MappedUser user : getMappedUsers()) {
            if (Patterns.VALID_EMAIL_PATTERN.matches(search)) {
                if (user.getEmail().equalsIgnoreCase(search)) {
                    return user;
                }
            } else if (Patterns.USER_MENTION.matches(search)) {
                String discordId = Patterns.USER_MENTION.getGroup(search, 1);
                if (user.getDiscordId().equalsIgnoreCase(discordId)) {
                    return user;
                }
            }
        }
        return null;
    }

    /**
     * Gets a list of all mapped users regardless of type
     *
     * @return A list of all mapped users
     */
    public  List<MappedUser> getMappedUsers() {
        List<MappedUser> users = Lists.newArrayList();

        users.addAll(studentMapper.getAllStudentsWithDiscordIDs());

        return users;
    }




   

  
    /**
     * Searches the list of professors for a match
     *
     * @param search The String to search for
     * @return a list of professors if any are found
     */
    public  List<Professor> getProfessorByNameOrEmail(String search) {
        List<Professor> professors = Lists.newArrayList();

        for (CSVRecord record : Objects.requireNonNull(getProfessorsInfo()).getRecords()) {
            Professor professor = getProfessor(record);

            // Match against name
            String r_name = record.get("professorName");
            String lastName = Patterns.SPACE.split(r_name)[2]; // (Dr.)=0 (FirstName)=1 (LastName)=2
            if (lastName.equalsIgnoreCase(search)) professors.add(professor);

            // Match against email
            String r_email = record.get("email");
            String userName = Patterns.VALID_EMAIL_PATTERN.getGroup(r_email, 1);
            if (search.equalsIgnoreCase(r_email)) professors.add(professor);
            if (search.equalsIgnoreCase(userName)) professors.add(professor);
        }

        return professors;
    }

    private  Professor getProfessor(CSVRecord record) {
        return new Professor(record.get("professorName"), record.get("title"), record.get("alma_mater"), record.get("specialty"), record.get("officeNumber"), record.get("email"), record.get("phone"), record.get("website"), record.get("office_hours"));
    }



}
