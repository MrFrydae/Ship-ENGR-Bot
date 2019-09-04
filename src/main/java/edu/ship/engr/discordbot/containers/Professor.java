package edu.ship.engr.discordbot.containers;

import edu.ship.engr.discordbot.utils.StringUtil;

/**
 * An object containing all information about a professor
 */
public class Professor {
    private final String name;
    private final String title;
    private final String almaMater;
    private final String specialty;
    private final String officeNumber;
    private final String email;
    private final String phone;
    private final String website;
    private final String officeHours;

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
}