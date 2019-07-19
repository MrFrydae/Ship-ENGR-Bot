package com.devonfrydae.ship.engrbot.utils;

import com.devonfrydae.ship.engrbot.containers.Course;
import com.devonfrydae.ship.engrbot.containers.MappedUser;
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
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

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

            String semCode = getSemesterCode(date);

            if (!r_sem_year.equalsIgnoreCase(semCode)) continue;

            classes.add(getClass(r_class));
        }
        return classes;
    }

    /**
     * Converts the date to a Semester Code
     * EX: "November 2019" -> "201960"
     * EX: "February 2021" -> "202120"
     *
     * @param date The date to convert
     * @return The formatted Semester Code
     */
    public static String getSemesterCode(Calendar date) {
        int month = date.get(Calendar.MONTH);
        int year = date.get(Calendar.YEAR);

        String mon;

        if (month < 6) mon = "20";
        else mon = "60";

        return year + mon;
    }

    /**
     * Searches all roles for the matching class role
     *
     * @param className The class code to search for
     * @return The role belonging to the provided course
     */
    public static Role getClass(String className) {
        className = formatClassName(className);
        return GuildUtil.getRole(className);
    }

    /**
     * Gets the proper class code with a dash
     *
     * @param className The raw class code
     * @return The class code with a dash
     */
    public static String formatClassName(String className) {
        Matcher matcher = Patterns.CLASS_NAME.matcher(className);

        String formatted = "";
        while (matcher.find()) {
            formatted = matcher.group(1).toUpperCase() + "-" + matcher.group(2);
        }
        return formatted;
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

            if (className.equalsIgnoreCase(formatClassName(code))) {
                return new Course(formatClassName(code), title, frequency);
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
                semesterCode = getSemesterCode(date);

                if (!getOfferedClasses().getHeaderMap().containsKey(semesterCode)) return null;

                if (record.get(semesterCode).isEmpty()) {
                    date.add(Calendar.MONTH, 6);
                } else {
                    found = true;
                }
            }

            if (semesterCode.isEmpty()) return null;

            return formatSemesterCode(semesterCode);
        }
        return null;
    }

    /**
     * Formats the given code to a prettier format
     * EX: "201960" -> "Fall 2019"
     * EX: "202120" -> "Spring 2021"
     *
     * @param semesterCode Semester code from file
     * @return Semester code in readable format
     */
    public static String formatSemesterCode(String semesterCode) {
        String year = semesterCode.substring(0, 4);
        String semester = semesterCode.substring(4);

        semester = (semester.equals("20")) ? "Spring" : "Fall";

        return semester + " " + year;
    }
}
