package edu.ship.engr.discordbot.containers;

import edu.ship.engr.discordbot.utils.Util;

import java.util.Arrays;

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
