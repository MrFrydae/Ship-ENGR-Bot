package edu.ship.engr.discordbot.containers;

import java.util.List;

import com.google.common.collect.Lists;

import edu.ship.engr.discordbot.gateways.CourseGateway;
import edu.ship.engr.discordbot.utils.GuildUtil;
import edu.ship.engr.discordbot.utils.Log;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

/**
 * An object containing all relevant information about a student.
 */
public class Student extends MappedUser {
    private String crew;
    private List<Course> courses;

    /**
     * Rich constructor.
     *
     * @param name student's name
     * @param email email address
     * @param major student's major
     * @param crew the crew the student belongs to
     * @param member member connection to discord
     * @param discordId their discord id
     * @param courses the list of courses the student is in
     */
    public Student(String name, String email, String major, String crew, Member member, String discordId, List<Course> courses) {
        this.name = name;
        this.email = email;
        this.major = major;
        this.member = member;
        this.discordId = discordId;
        this.crew = crew;
        this.courses = courses;
    }

    /**
     * Gets the student's crew.
     *
     * @return the crew the student belongs to
     */
    public String getCrew() {
        return crew;
    }

    /**
     * Gets the student's courses.
     *
     * @return a list of courses the student is enrolled in
     */
    public List<Course> getCourses() {
        return courses;
    }

    /**
     * Gets the role for this Student's crew.
     *
     * @return The {@link Role role} belonging to this Student's crew
     */
    public Role getCrewRole() {
        switch (getCrew().toLowerCase()) {
            case "outofbounds": return GuildUtil.getOutOfBounds();
            case "nullpointer": return GuildUtil.getNullPointer();
            case "offbyone": return GuildUtil.getOffByOne();
            default: return null;
        }
    }

    /**
     * Gets a list of course roles that the Student should have.
     *
     * @return A list of {@link Role courses} that the student is taking
     */
    public List<Role> getCourseRoles() {
        List<Role> roles = Lists.newArrayList();

        for (Course course : getCourses()) {
            if (course == null) {
                continue;
            }

            Role role = GuildUtil.getRole(course.getCode());
            roles.add(role);
        }

        return roles;
    }

    /**
     * Enroll the Student in all of their {@link Course courses}.
     */
    public void enrollStudent() {
        List<Role> toAdd = Lists.newArrayList();
        if (getCrew() != null) {
            toAdd.add(getCrewRole());
        }

        toAdd.addAll(getCourseRoles());

        List<Role> toRemove = getRolesToRemoveOnEnroll();

        if (toAdd.isEmpty()) {
            toAdd = null;
        }

        if (toRemove.isEmpty()) {
            toRemove = null;
        }

        GuildUtil.modifyRoles(getMember(), toAdd, toRemove);
        Log.info("Enrolled " + getEmail());
    }

    /**
     * Finds all roles that the student shouldn't keep every semester.
     *
     * @return A list of {@link Role roles} to remove from the Student
     */
    public List<Role> getRolesToRemoveOnEnroll() {
        List<Role> roles = Lists.newArrayList();
        CourseGateway courseGateway = new CourseGateway();
        
        for (Role role : member.getRoles()) {
            if (getCourseRoles().contains(role)) {
                continue;
            }

            String roleName = role.getName();
            if (courseGateway.isValidCourseName(roleName)) {
                roles.add(role);
            }
        }
        return roles;
    }
}
