package edu.ship.engr.discordbot.gateways;

import java.util.List;
import java.util.Objects;

import com.google.common.collect.Lists;

import edu.ship.engr.discordbot.containers.Course;
import edu.ship.engr.discordbot.containers.MappedUser;
import edu.ship.engr.discordbot.containers.Student;
import edu.ship.engr.discordbot.utils.GuildUtil;
import edu.ship.engr.discordbot.utils.Patterns;
import edu.ship.engr.discordbot.utils.TimeUtil;
import edu.ship.engr.discordbot.utils.Util;
import edu.ship.engr.discordbot.utils.csv.CSVRecord;
import net.dv8tion.jda.api.entities.Member;

/**
 * This is the class we should use to get information about students.  
 * It manages a number of different gateways that hold information
 * about students.
 * 
 * @author merlin
 *
 */
public class StudentMapper {

	private StudentGateway studentGateway = new StudentGateway();
	private CrewGateway crewGateway = new CrewGateway();
	private DiscordGateway discordGateway = new DiscordGateway();
	private CourseGateway courseGateway = new CourseGateway();
	

    /**
     * Gets the {@link Student student} with the provided email
     *
     * @param email The email to search for
     * @return A student object
     */
    public Student getStudentByEmail(String email) {
        for (CSVRecord record : studentGateway.getRecords()) {
            String r_email = record.get("EMAIL");

            if (email.equalsIgnoreCase(r_email))
            {
            	return getStudentFromRecord(record);
            }
        }
        return null;
    }
    
    /**
     * Gets a list of all mapped students in file
     *
     * @return A list of all mapped students
     */
    public  List<Student> getAllStudentsWithDiscordIDs() {
        List<Student> students = Lists.newArrayList();

        Objects.requireNonNull(discordGateway.getAllEmails()).forEach(email -> {
            Student student = getStudentByEmail(email);

            if (student != null) {
                students.add(student);
            }
        });

        return students;
    }

    /**
     * Gets a list of all mapped students
     *
     * @return A list of all mapped students
     */
    public  List<MappedUser> getMappedStudents() {
        List<MappedUser> users = Lists.newArrayList();

        users.addAll(getAllStudentsWithDiscordIDs());

        return users;
    }
    

    /**
     * Gets a {@link MappedUser} by either an email or mention
     *
     * @param search Either an email or a user mention
     * @return The {@link MappedUser} is one is found
     */
    public  MappedUser getMappedUser(String search) {
        for (MappedUser user : getMappedStudents()) {
            if (user == null) {
                continue;
            }

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
    private Student getStudentFromRecord(CSVRecord record) {
       String name = record.get("PREF_FIRST_NAME") + " " + record.get("PREF_LAST_NAME");
       name = Util.ucfirst(name);
       String email = record.get("EMAIL");
       String discordId = discordGateway.getDiscordIdByEmail(email);
       Member member = GuildUtil.getMember(discordId);

       if (member == null) {
           return null;
       }

       String major = record.get("MAJOR_DESC");
       String crew = crewGateway.getCrewByEmail(email);
       List<Course> courses = getCoursesByEmail(email);
       return new Student(name, email, major, crew, member, discordId, courses);
   }
    
    private List<Course> getCoursesByEmail(String email) {
        List<Course> courses = Lists.newArrayList();
        for (CSVRecord record : studentGateway.getRecords()) {
            String r_email = record.get("EMAIL");

            if (!r_email.equalsIgnoreCase(email)) continue;

            String r_period = record.get("ACADEMIC_PERIOD");

            if (!r_period.equalsIgnoreCase(TimeUtil.getCurrentSemesterCode())) continue;

            String r_class = record.get("COURSE_IDENTIFICATION");
            Course course = courseGateway.getCourse(r_class);
            courses.add(course);
        }

        return courses;
    }
}
