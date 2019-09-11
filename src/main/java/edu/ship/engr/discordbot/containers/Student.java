package edu.ship.engr.discordbot.containers;

import com.google.common.collect.Lists;
import edu.ship.engr.discordbot.Config;
import edu.ship.engr.discordbot.utils.CSVUtil;
import edu.ship.engr.discordbot.utils.GuildUtil;
import edu.ship.engr.discordbot.utils.Log;
import edu.ship.engr.discordbot.utils.TimeUtil;
import edu.ship.engr.discordbot.utils.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import org.apache.commons.csv.CSVRecord;

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

    public Student(List<CSVRecord> records) {
        CSVRecord record = records.get(0);
        this.name = record.get("PREF_FIRST_NAME") + " " + record.get("PREF_LAST_NAME");
        this.email = record.get("EMAIL");
        this.major = record.get("MAJOR");
        this.discordId = CSVUtil.getDiscordIdByEmail(email);
        this.member = GuildUtil.getMember(discordId);
        this.crew = CSVUtil.getCrewByEmail(email);

        List<Course> courses = Lists.newArrayList();
        for (CSVRecord courseRecord : records) {
            String r_period = courseRecord.get("ACADEMIC_PERIOD");

            if (!r_period.equalsIgnoreCase(TimeUtil.getCurrentSemesterCode())) continue;

            String r_class = courseRecord.get("COURSE_IDENTIFICATION");
            Course course = CSVUtil.getCourse(r_class);
            courses.add(course);
        }
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

    public Role getCrewRole() {
        switch (getCrew().toLowerCase()) {
            case "outofbounds": return GuildUtil.getOutOfBounds();
            case "nullpointer": return GuildUtil.getNullPointer();
            case "offbyone": return GuildUtil.getOffByOne();
            default: return null;
        }
    }

    public List<Role> getCourseRoles() {
        List<Role> roles = Lists.newArrayList();

        for (Course course : getCourses()) {
            if (course == null) continue;

            Role role = GuildUtil.getRole(course.getCode());
            roles.add(role);
        }

        return roles;
    }

    public void setNickname() {
        GuildUtil.setNickname(getMember(), getName());
    }

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

    public List<Role> getRolesToRemoveOnEnroll() {
        List<Role> roles = Lists.newArrayList();
        // TODO: Put logic here
        return roles;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", discordId='" + discordId + '\'' +
                ", major='" + major + '\'' +
                ", crew='" + crew + '\'' +
                ", member=" + member +
                ", courses=" + courses +
                '}';
    }
}
