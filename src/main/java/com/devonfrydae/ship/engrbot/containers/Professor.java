package com.devonfrydae.ship.engrbot.containers;

/**
 * An object containing all information about a professor
 */

public class Professor {
    private final String professorName;
    private final String title;
    private final String almaMater;
    private final String specialty;
    private final String officeNumber;
    private final String email;
    private final String phone;
    private final String website;
    private final String officeHours;

    public Professor(String professorName, String title, String almaMater, String specialty, String officeNumber, String email, String phone, String website, String officeHours) {
        this.professorName = professorName;
        this.title = title;
        this.almaMater = almaMater;
        this.specialty = specialty;
        this.officeNumber = officeNumber;
        this.email = email;
        this.phone = phone;
        this.website = website;
        this.officeHours = officeHours;
    }

    public String getProfessorName() {
        return professorName;
    }

    public String getAlmaMater() {
        return almaMater;
    }

    public String getEmail() {
        return email;
    }

    public String getOfficeHours() {
        return officeHours;
    }

    public String getOfficeNumber() {
        return officeNumber;
    }

    public String getPhone() {
        return phone;
    }

    public String getSpecialty() {
        return specialty;
    }

    public String getTitle() {
        return title;
    }

    public String getWebsite() {
        return website;
    }

}
