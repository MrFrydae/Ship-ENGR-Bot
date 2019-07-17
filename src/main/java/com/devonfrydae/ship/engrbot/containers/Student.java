package com.devonfrydae.ship.engrbot.containers;

public class Student implements MappedUser {
    private String email;
    private String discordId;
    private String major;

    public Student(String email, String discordId) {
        this.email = email;
        this.discordId = discordId;
    }

    public String getEmail() {
        return email;
    }

    public String getDiscordId() {
        return discordId;
    }

    public Student setMajor(String major) {
        this.major = major;

        return this;
    }
}
