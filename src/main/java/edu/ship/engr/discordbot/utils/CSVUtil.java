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
     * @return All records from "offerings.csv"
     */
    public  CSVHandler getOfferedClasses() {
        return new CSVHandler("offerings");
    }

    /**
     * @return All records from "professors.csv"
     */
    private  CSVHandler getProfessorsInfo(){
        return new CSVHandler( "professors");
    }

    /**
     * Checks the entries in offerings.csv for matching courses
     *
     * @param courseName The course to search for
     * @return true if it is a valid course name
     */
    public  boolean isValidCourseName(String courseName) {
        courseName = Util.formatClassName(courseName);

        for (Course course : getAllOfferedCourses()) {
            if (course.getCode().equalsIgnoreCase(courseName)) {
                return true;
            }
        }
        return false;
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
     * Collects all course information from the file into a {@link Course}
     *
     * @param className The class to search for
     * @return The container containing relevant information for the class
     */
    public  Course getCourse(String className) {
        return getAllOfferedCourses()
                .stream()
                .filter(course -> StringUtil.equals(course.getCode(), Util.formatClassName(className), true))
                .findFirst().orElse(null);
    }

    /**
     * Get a list of all offered courses
     *
     * @return a list of courses
     */
    public  List<Course> getAllOfferedCourses() {
        return getOfferedCourses("");
    }

    public  List<Course> getCurrentlyOfferedCourses() {
        return getOfferedCourses(Util.getSemesterCode(Calendar.getInstance()));
    }

    /**
     * Gets a list of all offered courses in a given semester
     *
     * @param semesterCode
     *          The code to search for.
     *          Make this blank to get all offered courses regardless of semester
     * @return a list of courses
     */
    public  List<Course> getOfferedCourses(String semesterCode) {
        List<Course> courses = Lists.newArrayList();
        for (CSVRecord record : Objects.requireNonNull(getOfferedClasses()).getRecords()) {
            String code = record.get("Code");
            String title = record.get("Title");
            String frequency = record.get("Frequency");

            code = Util.formatClassName(code);

            Course course = new Course(code, title, frequency, getNextCourseOffering(code), getAllCourseOfferings(code));

            if (!semesterCode.isEmpty()) {
                String offerings = record.get(semesterCode);
                if (offerings.isEmpty()) {
                    continue;
                }
            }
            courses.add(course);
        }
        return courses;
    }

    /**
     * Gets a list of all semesters when this course is offered
     *
     * @return a list of strings, each of which being a semester code
     */
     List<String> getAllCourseOfferings(String code) {
        List<String> semesters = CSVUtil.getSingleton().getOfferedClasses().getHeaders();
        semesters = semesters.subList(3, semesters.size());

        List<String> offerings = Lists.newArrayList();

        for (CSVRecord record : Objects.requireNonNull(CSVUtil.getSingleton().getOfferedClasses().getRecords())) {
            String r_courseCode = record.get("Code");

            if (!r_courseCode.equalsIgnoreCase(code)) continue;

            for (int i = 0; i < semesters.size() - 1; i += 2) {
                String year = semesters.get(i).substring(0, 4);
                int numYear = NumUtil.parseInt(year);
                if (numYear < Calendar.getInstance().get(Calendar.YEAR)) continue;

                String spring = StringUtil.getOrDefault(record.get((numYear + 1) + "20"), "0");
                String fall = StringUtil.getOrDefault(record.get(numYear + "60"), "0");
                offerings.add(year + "," + spring + "," + fall);
            }
        }

        return offerings;
    }

    /**
     * Finds the next semester when this course is offered
     *
     * @return the next Semester Code that this course is offered
     */
     String getNextCourseOffering(String code) {
        Calendar date = TimeUtil.getCurrentDate();

        for (CSVRecord record : Objects.requireNonNull(CSVUtil.getSingleton().getOfferedClasses()).getRecords()) {
            String r_className = record.get("Code");

            if (!r_className.equalsIgnoreCase(code)) {
                continue;
            }

            boolean found = false;
            String semesterCode = "";

            while (!found) {
                semesterCode = Util.getSemesterCode(date);

                if (!CSVUtil.getSingleton().getOfferedClasses().getHeaders().contains(semesterCode)) return null;

                if (record.get(semesterCode).isEmpty()) {
                    date.add(Calendar.MONTH, 6);
                } else {
                    found = true;
                }
            }

            if (semesterCode.isEmpty()) return null;

            return Util.formatSemesterCode(semesterCode);
        }
        return null;
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
