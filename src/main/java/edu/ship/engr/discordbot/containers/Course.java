package edu.ship.engr.discordbot.containers;

import edu.ship.engr.discordbot.Config;
import edu.ship.engr.discordbot.utils.Patterns;
import edu.ship.engr.discordbot.utils.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.Arrays;
import java.util.List;

/**
 * An object containing all relevant information about a course
 */
public class Course {
    private String code;
    private String title;
    private Frequency frequency;
    private String nextOffering;
    private List<String> allOfferings;

    /**
     * Rich Constructor.
     * @param code Of the format SWE300
     * @param title the name of the course
     * @param frequency string as described in the Frequency enum
     * @param nextOffering <Fall or Spring>:year
     * @param allOfferings list of offerings
     */
    public Course(String code, String title, String frequency, String nextOffering, List<String> allOfferings) {
        this.code = code;
        this.title = title;
        this.frequency = Frequency.getFrequency(frequency);
        this.nextOffering = nextOffering;
        this.allOfferings = allOfferings;
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

    /**
     * @return the course's XXXnnn code
     */
    public String getCode() {
        return code;
    }

    /**
     * @return the name of the class
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the rule for when the course is offered
     */
    public Frequency getFrequency() {
        return frequency;
    }

    /**
     * @return the next time the course will be offered
     */
    public String getNextOffering() {
        return nextOffering;
    }

    /**
     * @return a list of the semesters the course will be offered
     */
    public List<String> getAllOfferings() {
        return allOfferings;
    }

    /**
     * The values for the rules for when a course will be offered
     *
     */
    public enum Frequency {
        /**
         * 
         */
        EVERY_SEMESTER, 
        /**
         * 
         */
        EVERY_SPRING, 
        /**
         * 
         */
        EVERY_FALL, 
        /**
         * 
         */
        SPRING_EVEN_AY,
        /**
         * 
         */
        SPRING_ODD_AY, 
        /**
         * 
         */
        FALL_EVEN_AY,
        /**
         * 
         */
        FALL_ODD_AY, 
        /**
         * 
         */
        ON_DEMAND;

        /**
         * @param frequency the frequency we are looking for
         * @return the instance of frequency that matches the parameter
         */
        public static Frequency getFrequency(String frequency) {
            return Arrays
                    .stream(values())
                    .filter(value -> value.toString().equalsIgnoreCase(frequency))
                    .findFirst()
                    .orElse(ON_DEMAND);
        }

        /**
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            String name = name();
            name = name.replace("_", " ");
            name = Util.ucfirst(name);
            return name;
        }
    }
}
