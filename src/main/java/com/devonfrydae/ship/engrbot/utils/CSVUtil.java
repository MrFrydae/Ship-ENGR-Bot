package com.devonfrydae.ship.engrbot.utils;

import com.devonfrydae.ship.engrbot.containers.Course;
import com.devonfrydae.ship.engrbot.containers.MappedUser;
import com.devonfrydae.ship.engrbot.containers.Professor;
import com.devonfrydae.ship.engrbot.containers.Student;
import com.google.common.collect.Lists;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Collections;
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
            String email = record.get("email");
            String discordId = record.get("discord_id");

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

            return record.get("PREF_FIRST_NAME") + " " + record.get("PREF_LAST_NAME");
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
    public static void storeDiscordId(Member member, String email) {
        try {
            FileWriter fileWriter = new FileWriter("users.csv", true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            if (!isDiscordStored(member, email)) {
                printWriter.println(email + "," + member.getUser().getId());
            }
            printWriter.close();
        } catch (Exception ignored) {}
    }

    /**
     * Collects all course information from the file into a {@link Course}
     *
     * @param className The class to search for
     * @return The container containing relevant information for the class
     */
    public static Course getCourse(String className) {
        for (CSVRecord record : Objects.requireNonNull(getOfferedClasses())) {
            String code = record.get("Code");
            String title = record.get("Title");
            String frequency = record.get("Frequency");

            if (className.equalsIgnoreCase(Util.formatClassName(code))) {
                return new Course(Util.formatClassName(code), title, frequency);
            }
        }
        return null;
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
        Collections.sort(semesters);

        List<String> offerings = Lists.newArrayList();

        for (CSVRecord record : Objects.requireNonNull(getOfferedClasses())) {
            String r_courseCode = record.get("Code");

            if (!r_courseCode.equalsIgnoreCase(courseCode)) {
                continue;
            }

            for (int i = 0; i < semesters.size() - 1; i += 2) {
                String year = semesters.get(i).substring(0, 4);
                if (NumUtil.parseInt(year) < Calendar.getInstance().get(Calendar.YEAR)) continue;

                String spring = StringUtil.getOrDefault(record.get(semesters.get(i)), "0");
                String fall = StringUtil.getOrDefault(record.get(semesters.get(i + 1)), "0");
                offerings.add(year + "," + spring + "," + fall);
            }
        }

        return offerings;
    }

    public static Professor getProfessor(String search) {
        for (CSVRecord record : Objects.requireNonNull(getProfessorsInfo())) {
            search = search.toLowerCase();
            String professor = record.get("professorName");
            String profEmail = record.get("email");
            professor = professor.toLowerCase();

            if (!professor.contains(search)) {

                String title = record.get("title");
                String almaMater = record.get("alma_mater");
                String specialty = record.get("specialty");
                String officeNumber = record.get("officeNumber");
                String email = record.get("email");
                String phone = record.get("phone");
                String website = record.get("website");
                String office_hours = record.get("office_hours");

                return new Professor(professor, title, almaMater, specialty, officeNumber, email, phone, website, office_hours);
            } else if (!profEmail.contains(search)) continue;

            String title = record.get("title");
            String almaMater = record.get("alma_mater");
            String specialty = record.get("specialty");
            String officeNumber = record.get("officeNumber");
            String email = record.get("email");
            String phone = record.get("phone");
            String website = record.get("website");
            String office_hours = record.get("office_hours");

            return new Professor(professor, title, almaMater, specialty, officeNumber, email, phone, website, office_hours);
        }
            return null;
    }
}
