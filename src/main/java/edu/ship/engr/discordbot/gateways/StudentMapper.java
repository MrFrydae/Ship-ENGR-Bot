package edu.ship.engr.discordbot.gateways;

import java.util.List;

import org.apache.commons.csv.CSVRecord;

import edu.ship.engr.discordbot.containers.Course;
import edu.ship.engr.discordbot.containers.Student;
import edu.ship.engr.discordbot.utils.CSVUtil;
import edu.ship.engr.discordbot.utils.GuildUtil;
import edu.ship.engr.discordbot.utils.Util;
import net.dv8tion.jda.api.entities.Member;

public class StudentMapper {

	
	private static StudentMapper singleton;
	
	StudentGateway studentGateway = new StudentGateway();
	
	private StudentMapper()
	{
		
	}
	public static StudentMapper getSingleton()
	{
		if (singleton == null)
		{
			singleton = new StudentMapper();
		}
		return singleton;
	}
 
    /**
     * Gets the {@link Student student} with the provided email
     *
     * @param email The email to search for
     * @return A student object
     */
    public Student getStudentByEmail(String email) {
        for (CSVRecord record : studentGateway.getRecords()) {
            String r_email = record.get("EMAIL_PREFERRED_ADDRESS");

            if (email.equalsIgnoreCase(r_email))
            {
            	return getStudentFromRecord(record);
            }
        }
        return null;
    }

    private Student getStudentFromRecord(CSVRecord record) {
       String name = record.get("PREF_FIRST_NAME") + " " + record.get("PREF_LAST_NAME");
       name = Util.ucfirst(name);
       String email = record.get("EMAIL_PREFERRED_ADDRESS");
       String major = record.get("MAJOR");
       String crew = CSVUtil.getSingleton().getCrewByEmail(email);
       String discordId = CSVUtil.getSingleton().getDiscordIdByEmail(email);
       Member member = GuildUtil.getMember(discordId);
       List<Course> courses = CSVUtil.getSingleton().getCoursesByEmail(email);
       return new Student(name, email, major, crew, member, discordId, courses);
   }
}
