package edu.ship.engr.discordbot.containers;

import edu.ship.engr.discordbot.Config;
import edu.ship.engr.discordbot.utils.StringUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

/**
 * An object containing all information about a professor
 */
public class Professor {
    private String name;
    private String title;
    private String almaMater;
    private String specialty;
    private String officeNumber;
    private String email;
    private String phone;
    private String website;
    private String officeHours;

    public Professor(String name, String title, String almaMater, String specialty, String officeNumber, String email, String phone, String website, String officeHours) {
        this.name = name;
        this.title = title;
        this.almaMater = almaMater;
        this.specialty = specialty;
        this.officeNumber = officeNumber;
        this.email = email;
        this.phone = phone;
        this.website = website;
        this.officeHours = officeHours;
    }

    public String getName() {
        return name;
    }

    public boolean hasName() {
        return StringUtil.isNotEmpty(name);
    }

    public String getAlmaMater() {
        return almaMater;
    }

    public boolean hasAlmaMater() {
        return StringUtil.isNotEmpty(almaMater);
    }

    public String getEmail() {
        return email;
    }

    public boolean hasEmail() {
        return StringUtil.isNotEmpty(email);
    }

    public String getOfficeHours() {
        return officeHours;
    }

    public boolean hasOfficeHours() {
        return StringUtil.isNotEmpty(officeHours);
    }

    public String getOfficeNumber() {
        return officeNumber;
    }

    public boolean hasOfficeNumber() {
        return StringUtil.isNotEmpty(officeNumber);
    }

    public String getPhone() {
        return phone;
    }

    public boolean hasPhone() {
        return StringUtil.isNotEmpty(phone);
    }

    public String getSpecialty() {
        return specialty;
    }

    public boolean hasSpecialty() {
        return StringUtil.isNotEmpty(specialty);
    }

    public String getTitle() {
        return title;
    }

    public boolean hasTitle() {
        return StringUtil.isNotEmpty(title);
    }

    public String getWebsite() {
        return website;
    }

    public boolean hasWebsite() {
        return StringUtil.isNotEmpty(website);
    }

    /**
     * Gets all information about this professor
     *
     * @return an {@link MessageEmbed embedded message} with all info about this professor
     */
    public MessageEmbed getInfoEmbed() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Config.getPrimaryEmbedColor());

        // Only add to builder if present
        if (hasName()) builder.addField("Professor", getName(), true);
        if (hasAlmaMater()) builder.addField("Alma Mater", getAlmaMater(), true);
        if (hasEmail()) builder.addField("Email", getEmail(), true);
        if (hasOfficeNumber()) builder.addField("Office Number", getOfficeNumber(), true);
        if (hasPhone()) builder.addField("Phone Number", getPhone(), true);
        if (hasSpecialty()) builder.addField("Specialty", getSpecialty(), true);
        if (hasTitle()) builder.addField("Title", getTitle(), true);
        if (hasWebsite()) builder.addField("Website", getWebsite(), true);
        if (hasOfficeHours()) builder.addField("Office Hours", getOfficeHours(), true);

        return builder.build();
    }
}