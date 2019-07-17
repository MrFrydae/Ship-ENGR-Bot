package com.devonfrydae.ship.engrbot.utils;

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
import java.util.*;
import java.util.regex.Matcher;

public class CSVUtil {

    private static CSVParser getCSV(String fileName) {
        try {
            Reader reader = Files.newBufferedReader(Paths.get( fileName + ".csv"));
            return new CSVParser(reader, CSVFormat.EXCEL.withHeader());
        } catch (Exception e) {
            return null;
        }
    }

    private static CSVParser getCrews() {
        return getCSV("crews");
    }

    private static CSVParser getStudentClasses() {
        return getCSV("students");
    }

    private static CSVParser getDiscordIds() {
        return getCSV("users");
    }

    public static CSVParser getOfferedClasses() {
        return getCSV("offerings");
    }

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

    public static List<Role> getNewStudentClasses(String email) {
        return getStudentClasses(email, true);
    }

    public static List<Role> getOldStudentClasses(String email) {
        return getStudentClasses(email, false);
    }

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

    public static String getSemesterCode(Calendar date) {
        int month = date.get(Calendar.MONTH);
        int year = date.get(Calendar.YEAR);

        String mon;

        if (month < 6) mon = "20";
        else mon = "60";

        return year + mon;
    }

    public static Role getClass(String className) {
        className = formatClassName(className);
        return GuildUtil.getRole(className);
    }

    public static String formatClassName(String className) {
        Matcher matcher = Patterns.CLASS_NAME.matcher(className);

        String formatted = "";
        while (matcher.find()) {
            formatted = matcher.group(1) + "-" + matcher.group(2);
        }
        return formatted;
    }

    public static List<MappedUser> getMappedUsers() {
        List<MappedUser> users = Lists.newArrayList();

        users.addAll(getMappedUsers());

        return users;
    }

    public static List<Student> getMappedStudents() {
        List<Student> students = Lists.newArrayList();

        Objects.requireNonNull(getDiscordIds()).forEach(record -> {
            String email = record.get(0);
            String discordId = record.get(1);

            Student student = new Student(email, discordId);
            students.add(student);
        });

        return students;
    }

    private static boolean isDiscordStored(Member member, String email) {
        for (CSVRecord record : Objects.requireNonNull(getDiscordIds())) {
            String r_email = record.get(0);
            String r_id = record.get(1);

            if (r_email.equalsIgnoreCase(email) || r_id.equalsIgnoreCase(member.getUser().getId())) {
                return true;
            }
        }
        return false;
    }

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

    public static String formatSemesterCode(String semesterCode) {
        String year = semesterCode.substring(0, 4);
        String semester = semesterCode.substring(4);

        semester = (semester.equals("20")) ? "Spring" : "Fall";

        return semester + " " + year;
    }
}
