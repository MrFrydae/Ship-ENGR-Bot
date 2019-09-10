package edu.ship.engr.discordbot.utils;

import com.google.common.collect.Lists;
import edu.ship.engr.discordbot.containers.Course;
import edu.ship.engr.discordbot.containers.MappedUser;
import edu.ship.engr.discordbot.containers.Professor;
import edu.ship.engr.discordbot.containers.Student;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class CSVUtil {

    /**
     * Loads the file if it is found and returns all csv records
     *
     * @param fileName The name of the file without the extension
     * @return All records from the file
     */
    private static CSVParser getCSV(String fileName) {
        try {
            Reader reader = Files.newBufferedReader(Paths.get( fileName + ".csv"));
            return new CSVParser(reader, CSVFormat.EXCEL.withHeader());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @return All records from "crews.csv"
     */
    private static CSVParser getCrews() {
        return getCSV("crews");
    }

    /**
     * @return All records from "students.csv"
     */
    private static CSVParser getStudentClasses() {
        return getCSV("students");
    }

    /**
     * @return All records from "users.csv"
     */
    private static CSVParser getDiscordIds() {
        return getCSV("users");
    }

    /**
     * @return All records from "offerings.csv"
     */
    public static CSVParser getOfferedClasses() {
        return getCSV("offerings");
    }

    /**
     * @return All records from "professors.csv"
     */
    private static CSVParser getProfessorsInfo(){
        return getCSV( "professors");
    }

    /**
     * Gets all of the headers for the file
     *
     * @param parser The csv file to parse
     * @return The file headers in a list
     */
    private static List<String> getHeaders(CSVParser parser) {
        return Lists.newArrayList(parser.getHeaderMap().keySet());
    }

    /**
     * Gets the role matching the student's crew
     *
     * @param email The Student's SU Email
     * @return The {@link Role} matching the student's crew
     */
    public static Role getStudentCrew(String email) {
        for (CSVRecord record : Objects.requireNonNull(getCrews())) {
            String r_email = record.get("email");
            String r_crew = record.get("crew");

            if (email.equalsIgnoreCase(r_email)) {
                return GuildUtil.getCrew(r_crew);
            }
        }
        return null;
    }

    /**
     * Gets all classes for next semester
     *
     * @param email The Student's SU Email
     * @return A list of roles containing all classes found
     */
    public static List<Role> getNewStudentClasses(String email) {
        return getStudentClasses(email, true);
    }

    /**
     * Gets all classes from last semester
     *
     * @param email The Student's SU Email
     * @return A list of roles containing all classes found
     */
    public static List<Role> getOldStudentClasses(String email) {
        return getStudentClasses(email, false);
    }

    /**
     * Gets all classes to either add or remove from the Discord Member
     *
     * @param email The Student's SU Email
     * @param newClasses Should we check for new classes or last semester
     * @return A list of roles containing all classes found
     */
    private static List<Role> getStudentClasses(String email, boolean newClasses) {
        List<Role> classes = Lists.newArrayList();

        for (CSVRecord record : Objects.requireNonNull(getStudentClasses())) {
            String r_email = record.get("EMAIL");
            String r_sem_year = record.get("ACADEMIC_PERIOD");
            String r_class = record.get("COURSE_IDENTIFICATION");

            if (!email.equalsIgnoreCase(r_email)) continue;

            Calendar date = Calendar.getInstance();
            if (!newClasses) date.add(Calendar.MONTH, -6);

            String semCode = Util.getSemesterCode(date);

            if (!r_sem_year.equalsIgnoreCase(semCode)) continue;

            classes.add(Util.getClass(r_class));
        }
        return classes;
    }

    /**
     * Gets a {@link MappedUser} by either an email or mention
     *
     * @param search Either an email or a user mention
     * @return The {@link MappedUser} is one is found
     */
    public static MappedUser getMappedUser(String search) {
        for (MappedUser user : getMappedUsers()) {
            if (Patterns.VALID_EMAIL_PATTERN.matches(search)) {
                if (user.email.equalsIgnoreCase(search)) {
                    return user;
                }
            } else if (Patterns.USER_MENTION.matches(search)) {
                String discordId = Patterns.USER_MENTION.getGroup(search, 1);
                if (user.discordId.equalsIgnoreCase(discordId)) {
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
    public static List<MappedUser> getMappedUsers() {
        List<MappedUser> users = Lists.newArrayList();

        users.addAll(getMappedStudents());

        return users;
    }

    /**
     * Gets a list of all mapped students in file
     *
     * @return A list of all mapped students
     */
    public static List<Student> getMappedStudents() {
        List<Student> students = Lists.newArrayList();

        Objects.requireNonNull(getDiscordIds()).forEach(record -> {
            String email = record.get("email").toLowerCase();
            String discordId = record.get("discord_id").toLowerCase();

            Student student = new Student(email, discordId);
            students.add(student);
        });

        return students;
    }

    /**
     * Gets the student's major
     *
     * @param email The email to search for
     * @return The student's major
     */
    public static String getStudentMajor(String email) {
        for (CSVRecord record : Objects.requireNonNull(getStudentClasses())) {
            String r_email = record.get("EMAIL");

            if (!email.equalsIgnoreCase(r_email)) continue;

            return record.get("MAJOR");
        }
        return null;
    }

    /**
     * Gets the student's name
     *
     * @param email The email to search for
     * @return The student's full name
     */
    public static String getStudentName(String email) {
        for (CSVRecord record : Objects.requireNonNull(getStudentClasses())) {
            String r_email = record.get("EMAIL");

            if (!email.equalsIgnoreCase(r_email)) continue;

            // TODO: Fix this later
            String preferredLastName = record.get(0); // Hack until we know what went wrong
            String preferredFirstName = record.get(1); // Hack until we know what went wrong
            return preferredFirstName + " " + preferredLastName;
        }
        return null;
    }

    /**
     * Checks if the Student's SU Email is matched to a Discord ID
     *
     * @param member The Discord Member object
     * @param email The Student's SU Email
     * @return true if their data exists
     */
    private static boolean isDiscordStored(Member member, String email) {
        for (CSVRecord record : Objects.requireNonNull(getDiscordIds())) {
            String r_email = record.get("email");
            String r_id = record.get("discord_id");

            if (r_email.equalsIgnoreCase(email) || r_id.equalsIgnoreCase(member.getUser().getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Stores the {@link Member}'s Discord ID
     * and the Student's SU Email into a file for future usage
     *
     * @param member The Discord Member object
     * @param email The Student's SU Email
     */
    public static void storeDiscordId(Member member, String email) throws Exceptions.IdentifyException {
        try {
            if (!isDiscordStored(member, email)) {
                FileWriter fileWriter = new FileWriter("users.csv", true);
                PrintWriter printWriter = new PrintWriter(fileWriter);
                printWriter.println(email + "," + member.getUser().getId());
                Log.info("Stored id: " + member.getUser().getId() + ", email: " + email);
                printWriter.close();
            } else {
                throw new Exceptions.IdentifyException();
            }
        } catch (Exception ignored) {}
    }

    /**
     * Collects all course information from the file into a {@link Course}
     *
     * @param className The class to search for
     * @return The container containing relevant information for the class
     */
    public static Course getCourse(String className) {
        return getOfferedCourses()
                .stream()
                .filter(course -> StringUtil.equals(course.getCode(), Util.formatClassName(className), true))
                .findFirst().orElse(null);
    }

    /**
     * Get a list of all offered courses
     *
     * @return a list of courses
     */
    public static List<Course> getOfferedCourses() {
        return getOfferedCourses("");
    }

    /**
     * Gets a list of all offered courses in a given semester
     *
     * @param semesterCode The code to search for
     * @return a list of courses
     */
    public static List<Course> getOfferedCourses(String semesterCode) {
        List<Course> courses = Lists.newArrayList();
        for (CSVRecord record : Objects.requireNonNull(getOfferedClasses())) {
            String code = record.get("Code");
            String title = record.get("Title");
            String frequency = record.get("Frequency");

            Course course = new Course(Util.formatClassName(code), title, frequency);

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
     * Finds the next semester when the given class is offered
     *
     * @param className The class to search for
     * @return the next Semester Code that the class is offered
     */
    public static String getNextOffering(String className) {
        if (className.contains("-")) className = className.replace("-", "");
        Calendar date = Calendar.getInstance();

        for (CSVRecord record : Objects.requireNonNull(getOfferedClasses())) {
            String r_className = record.get("Code");

            if (!r_className.equalsIgnoreCase(className)) {
                continue;
            }

            boolean found = false;
            String semesterCode = "";

            while (!found) {
                semesterCode = Util.getSemesterCode(date);

                if (!getHeaders(getOfferedClasses()).contains(semesterCode)) return null;

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
     * Gets the title for the given course code
     *
     * @param courseCode The course to search for
     * @return The course's title
     */
    public static String getCourseTitle(String courseCode) {
        if (courseCode.contains("-")) courseCode = courseCode.replace("-", "");

        for (CSVRecord record : Objects.requireNonNull(getOfferedClasses())) {
            String r_courseCode = record.get("Code");

            if (!r_courseCode.equalsIgnoreCase(courseCode)) {
                continue;
            }

            String r_title = record.get("Title");
            return Util.ucfirst(r_title);
        }
        return null;
    }

    /**
     * Gets a list of all future offerings of the given course,
     * in the format of: year,springAmount,fallAmount
     *
     * @param courseCode The course to search for
     * @return A list of all future offerings
     */
    public static List<String> getAllOfferings(String courseCode) {
        if (courseCode.contains("-")) courseCode = courseCode.replace("-", "");

        List<String> semesters = getHeaders(getOfferedClasses());
        semesters = semesters.subList(3, semesters.size());
        //Collections.sort(semesters);

        List<String> offerings = Lists.newArrayList();

        for (CSVRecord record : Objects.requireNonNull(getOfferedClasses())) {
            String r_courseCode = record.get("Code");

            if (!r_courseCode.equalsIgnoreCase(courseCode)) {
                continue;
            }

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
     * Converts the provided CSVRecord to a Professor object
     *
     * @param record The {@link CSVRecord} containing the professor's info
     * @return The {@link Professor} object with their information
     */
    private static Professor getProfessor(CSVRecord record) {
        String name = record.get("professorName");
        String title = record.get("title");
        String almaMater = record.get("alma_mater");
        String specialty = record.get("specialty");
        String officeNumber = record.get("officeNumber");
        String email = record.get("email");
        String phone = record.get("phone");
        String website = record.get("website");
        String officeHours = record.get("office_hours");

        return new Professor(name, title, almaMater, specialty, officeNumber, email, phone, website, officeHours);
    }

    /**
     * Searches the list of professors for a match
     *
     * @param search The String to search for
     * @return a list of professors if any are found
     */
    public static List<Professor> getProfessorMatch(String search) {
        List<Professor> professors = Lists.newArrayList();

        for (CSVRecord record : Objects.requireNonNull(getProfessorsInfo())) {

            // Match against name
            String r_name = record.get("professorName");
            String lastName = Patterns.SPACE.split(r_name)[2]; // (Dr.)=0 (FirstName)=1 (LastName)=2
            if (lastName.equalsIgnoreCase(search)) professors.add(getProfessor(record));

            // Match against email
            String r_email = record.get("email");
            String userName = Patterns.VALID_EMAIL_PATTERN.getGroup(r_email, 1);
            if (search.equalsIgnoreCase(r_email)) professors.add(getProfessor(record));
            if (search.equalsIgnoreCase(userName)) professors.add(getProfessor(record));
        }

        return professors;
    }
}
