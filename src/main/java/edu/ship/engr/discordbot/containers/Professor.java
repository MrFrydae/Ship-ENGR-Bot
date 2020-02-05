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

    /**
     * @param name professor's name
     * @param title academic title
     * @param almaMater school their PhD is from
     * @param specialty area of expertise
     * @param officeNumber office location
     * @param email email
     * @param phone office phone number
     * @param website website url
     * @param officeHours office hours
     */
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

    /**
     * @return faculty name
     */
    public String getName() {
        return name;
    }

    /**
     * @return true if the faculty has a name
     */
    public boolean hasName() {
        return StringUtil.isNotEmpty(name);
    }

    /**
     * @return where they got their PhD from
     */
    public String getAlmaMater() {
        return almaMater;
    }

    /**
     * @return true if we know their alma mater
     */
    public boolean hasAlmaMater() {
        return StringUtil.isNotEmpty(almaMater);
    }

    /**
     * @return their email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return true if we know their email
     */
    public boolean hasEmail() {
        return StringUtil.isNotEmpty(email);
    }

    /**
     * @return office hours description
     */
    public String getOfficeHours() {
        return officeHours;
    }

    /**
     * @return true if we know the professor's office hours
     */
    public boolean hasOfficeHours() {
        return StringUtil.isNotEmpty(officeHours);
    }

    /**
     * @return the office location
     */
    public String getOfficeNumber() {
        return officeNumber;
    }

    /**
     * @return true if we have an office location
     */
    public boolean hasOfficeNumber() {
        return StringUtil.isNotEmpty(officeNumber);
    }

    /**
     * @return phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @return true if we have a phone number
     */
    public boolean hasPhone() {
        return StringUtil.isNotEmpty(phone);
    }

    /**
     * @return the specialty
     */
    public String getSpecialty() {
        return specialty;
    }

    /**
     * @return true if we have a specialty
     */
    public boolean hasSpecialty() {
        return StringUtil.isNotEmpty(specialty);
    }

    /**
     * @return academic title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return true if we have a title
     */
    public boolean hasTitle() {
        return StringUtil.isNotEmpty(title);
    }

    /**
     * @return the url of the professor's web site
     */
    public String getWebsite() {
        return website;
    }

    /**
     * @return true if we know where the web site is
     */
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