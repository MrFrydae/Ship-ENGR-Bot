package edu.ship.engr.discordbot.containers;

import java.util.List;

import com.google.common.collect.Lists;

import edu.ship.engr.discordbot.gateways.CourseGateway;
import edu.ship.engr.discordbot.utils.GuildUtil;
import edu.ship.engr.discordbot.utils.Log;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

/**
 * An object containing all relevant information about a student.
 */
public class Student extends MappedUser {
    @Getter private final List<Course> courses;

    /**
     * Rich constructor.
     *
     * @param name student's name
     * @param email email address
     * @param major student's major
     * @param discordId their discord id
     * @param courses the list of courses the student is in
     */
    public Student(
            String name, String email, String major,
            String discordId, List<Course> courses) {
        super(name, email, discordId, major);
        this.courses = courses;
    }
}
