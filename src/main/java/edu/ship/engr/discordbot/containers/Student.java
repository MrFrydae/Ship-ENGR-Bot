package edu.ship.engr.discordbot.containers;

import com.google.common.collect.Lists;
import edu.ship.engr.discordbot.Config;
import edu.ship.engr.discordbot.utils.CSVUtil;
import edu.ship.engr.discordbot.utils.GuildUtil;
import edu.ship.engr.discordbot.utils.Log;
import edu.ship.engr.discordbot.utils.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

/**
 * An object containing all relevant information about a student
 */
public class Student implements MappedUser {
    private String name;
    private String email;
    private String discordId;
    private String major;
    private String crew;
    private Member member;
    private List<Course> courses;

    public Student(String name, String email, String major, String crew, Member member, String discordId, List<Course> courses) {
        this.name = name;
        this.email = email;
        this.major = major;
        this.member = member;
        this.discordId = discordId;
        this.crew = crew;
        this.courses = courses;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDiscordId() {
        return discordId;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public String getMajor() {
        return major;
    }

    public String getCrew() {
        return crew;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public Member getMember() {
        return member;
    }

    public User getUser() {
        return getMember().getUser();
    }

    @Override
    public void sendUserInfo(User sendTo) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Config.getPrimaryEmbedColor());
        builder.setAuthor(getUser().getName() + "#" + getUser().getDiscriminator(), null, getUser().getAvatarUrl());
        builder.addField("Name", getName(), true);
        builder.addField("Email", getEmail(), true);
        builder.addField("Major", getMajor(), true);

        Util.sendPrivateMsg(sendTo, builder.build());
    }

    /**
     * Gets the role for this Student's crew
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
     * Gets a list of course roles that the Student should have
     *
     * @return A list of {@link Role courses} that the student is taking
     */
    public List<Role> getCourseRoles() {
        List<Role> roles = Lists.newArrayList();

        for (Course course : getCourses()) {
            if (course == null) continue;

            Role role = GuildUtil.getRole(course.getCode());
            roles.add(role);
        }

        return roles;
    }

    /**
     * Changes the {@link Member member}'s nickname to their actual name.
     */
    public void setNickname() {
        GuildUtil.setNickname(getMember(), getName());
    }

    /**
     * Enroll the Student in all of their {@link Course courses}.
     */
    public void enrollStudent() {
        List<Role> toAdd = Lists.newArrayList();
        if (getCrew() != null) toAdd.add(getCrewRole());
        toAdd.addAll(getCourseRoles());

        List<Role> toRemove = getRolesToRemoveOnEnroll();

        if (toAdd.isEmpty()) toAdd = null;
        if (toRemove.isEmpty()) toRemove = null;

        GuildUtil.modifyRoles(getMember(), toAdd, toRemove);
        Log.info("Enrolled " + getEmail());
    }

    /**
     * Finds all roles that the student shouldn't keep every semester
     *
     * @return A list of {@link Role roles} to remove from the Student
     */
    public List<Role> getRolesToRemoveOnEnroll() {
        List<Role> roles = Lists.newArrayList();

        for (Role role : member.getRoles()) {
            if (getCourseRoles().contains(role)) continue;

            String roleName = role.getName();
            //TODO should we be accessing CSVUtil from here?
            if (CSVUtil.isValidCourseName(roleName)) {
                roles.add(role);
            }
        }
        return roles;
    }
}
