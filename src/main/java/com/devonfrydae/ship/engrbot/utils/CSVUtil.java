package com.devonfrydae.ship.engrbot.utils;

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

    private static CSVParser getCrews() {
        try {
            Reader reader = Files.newBufferedReader(Paths.get("crews.csv"));
            return new CSVParser(reader, CSVFormat.DEFAULT);
        } catch (Exception e) {
            return null;
        }
    }

    public static Role getStudentCrew(String email) {
        for (CSVRecord record : Objects.requireNonNull(getCrews())) {
            String r_email = record.get(0);
            String r_crew = record.get(1);

            if (email.equalsIgnoreCase(r_email)) {
                return GuildUtil.getCrew(r_crew);
            }
        }
        return null;
    }

    private static CSVParser getStudentClasses() {
        try {
            Reader reader = Files.newBufferedReader(Paths.get("students.csv"));
            return new CSVParser(reader, CSVFormat.DEFAULT);
        } catch (Exception e) {
            return null;
        }
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
            String r_email = record.get(7);
            String r_sem_year = record.get(19);
            String r_class = record.get(20);

            if (!email.equalsIgnoreCase(r_email)) continue;

            Calendar date = Calendar.getInstance();
            if (!newClasses) date.add(Calendar.MONTH, -6);

            int month = date.get(Calendar.MONTH);
            int year = date.get(Calendar.YEAR);

            String mon;

            if (month < 6) mon = "20";
            else mon = "60";

            if (!r_sem_year.equalsIgnoreCase(year + mon)) continue;

            classes.add(getClass(r_class));
        }
        return classes;
    }

    public static Role getClass(String className) {
        Matcher matcher = Patterns.CLASS_NAME.matcher(className);

        String roleName = "";
        while (matcher.find()) {
            roleName = matcher.group(1) + "-" + matcher.group(2);
        }
        return GuildUtil.getRole(roleName);
    }

    private static CSVParser getDiscordIds() {
        try {
            Reader reader = Files.newBufferedReader(Paths.get("users.csv"));
            return new CSVParser(reader, CSVFormat.DEFAULT.withHeader("email", "discord_id"));
        } catch (Exception e) {
            return null;
        }
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
}
