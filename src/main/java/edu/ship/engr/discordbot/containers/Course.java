package edu.ship.engr.discordbot.containers;

import com.google.common.collect.Lists;
import edu.ship.engr.discordbot.Config;
import edu.ship.engr.discordbot.utils.CSVUtil;
import edu.ship.engr.discordbot.utils.NumUtil;
import edu.ship.engr.discordbot.utils.Patterns;
import edu.ship.engr.discordbot.utils.StringUtil;
import edu.ship.engr.discordbot.utils.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.apache.commons.csv.CSVRecord;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

/**
 * An object containing all relevant information about a course
 */
public class Course {
    private String code;
    private String title;
    private Frequency frequency;

    public Course(String code, String title, String frequency) {
        this(code, title, Frequency.getFrequency(frequency));
    }
    public Course(String code, String title, Frequency frequency) {
        this.code = code;
        this.title = title;
        this.frequency = frequency;
    }

    /**
     * Finds the next semester when this course is offered
     *
     * @return the next Semester Code that this course is offered
     */
    public String getNextOffering() {
        Calendar date = Calendar.getInstance();

        for (CSVRecord record : Objects.requireNonNull(CSVUtil.getOfferedClasses()).getRecords()) {
            String r_className = record.get("Code");

            if (!r_className.equalsIgnoreCase(getCode().replace("-", ""))) {
                continue;
            }

            boolean found = false;
            String semesterCode = "";

            while (!found) {
                semesterCode = Util.getSemesterCode(date);

                if (!CSVUtil.getOfferedClasses().getHeaders().contains(semesterCode)) return null;

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
     * Gets a list of all semesters when this course is offered
     *
     * @return a list of strings, each of which being a semester code
     */
    public List<String> getAllOfferings() {
        List<String> semesters = CSVUtil.getOfferedClasses().getHeaders();
        semesters = semesters.subList(3, semesters.size());

        List<String> offerings = Lists.newArrayList();

        for (CSVRecord record : Objects.requireNonNull(CSVUtil.getOfferedClasses().getRecords())) {
            String r_courseCode = record.get("Code");

            if (!r_courseCode.equalsIgnoreCase(getCode().replace("-", ""))) {
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
     * Gets all information about this course
     *
     * @return an {@link MessageEmbed embedded message} with info for this course
     */
    public MessageEmbed getInfoEmbed() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Config.getPrimaryEmbedColor());
        builder.addField("Class Code", getCode(), true);
        builder.addField("Class Frequency", getFrequency().toString(), true);
        builder.addField("Class Name", getTitle(), true);
        if (getNextOffering() != null) builder.addField("Next Offering", getNextOffering(), true);

        return builder.build();
    }

    /**
     * Gets every offering for this course
     *
     * @return an {@link MessageEmbed embedded message} with when this course if offered
     */
    public MessageEmbed getOfferingsEmbed() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Config.getPrimaryEmbedColor());
        builder.setTitle("Offerings for \"" + getTitle() + "\"");

        for (String offering : getAllOfferings()) {
            String[] split = Patterns.COMMA.split(offering);
            String year = split[0];
            String spring = "Spring: " + split[1];
            String fall = "Fall: " + split[2];

            builder.addField(year, spring + "\n" + fall, true);
        }

        return builder.build();
    }

    /**
     * Formats a message telling the user when this course is offered next
     *
     * @return an pretty message
     */
    public String getNextOfferingString() {
        if (getNextOffering() != null) {
            return "The next time " + getCode() + " is offered is in " + getNextOffering() + ".";
        } else {
            return "Sorry, " + getCode() + " is not being offered at the moment.";
        }
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public enum Frequency {
        EVERY_SEMESTER, EVERY_SPRING, EVERY_FALL, SPRING_EVEN_AY,
        SPRING_ODD_AY, FALL_EVEN_AY, FALL_ODD_AY, ON_DEMAND;

        public static Frequency getFrequency(String frequency) {
            return Arrays
                    .stream(values())
                    .filter(value -> value.toString().equalsIgnoreCase(frequency))
                    .findFirst()
                    .orElse(ON_DEMAND);
        }

        @Override
        public String toString() {
            String name = name();
            name = name.replace("_", " ");
            name = Util.ucfirst(name);
            return name;
        }
    }
}
