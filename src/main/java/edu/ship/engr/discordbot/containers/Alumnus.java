package edu.ship.engr.discordbot.containers;

import edu.ship.engr.discordbot.Config;
import edu.ship.engr.discordbot.gateways.AlumnusGateway;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class Alumnus {
    private String discordId;
    private String name;
    private String email;
    private String gradYear;
    private String majors;
    private String minors;
    private String message;

    /**
     * Constructor for alumnus.
     */
    public Alumnus(String discordId, String email, String name, String gradYear, String majors, String minors, String message) {
        this.discordId = discordId;
        this.email = email;
        this.name = name;
        this.gradYear = gradYear;
        this.majors = majors;
        this.minors = minors;
        this.message = message;
    }

    public String getDiscordId() {
        return discordId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getGradYear() {
        return gradYear;
    }

    public String getMajors() {
        return majors;
    }

    public String getMinors() {
        return minors;
    }

    public boolean hasMinors() {
        return minors != null;
    }

    public String getMessage() {
        return message;
    }

    public boolean hasMessage() {
        return message != null;
    }

    /**
     * Registers this alumnus into a file.
     */
    public void register() {
        AlumnusGateway gateway = new AlumnusGateway();

        gateway.register(discordId, email, name, gradYear, majors, minors, message);
    }

    /**
     * Gets all information about this professor.
     *
     * @return an {@link MessageEmbed embedded message} with all info about this professor
     */
    public MessageEmbed getInfoEmbed() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Config.getPrimaryEmbedColor());

        builder.addField("Name", getName(), true);
        builder.addField("Email", getEmail(), true);
        builder.addField("Graduation Year", getGradYear(), true);
        builder.addField("Majors", getMajors().replace("||", ", "), true);
        if (hasMinors()) {
            builder.addField("Minors", getMinors().replace("||", ", "), true);
        }
        builder.addField("Message", getMessage(), true);

        return builder.build();
    }

    public static class AlumnusBuilder {
        private String discordId;
        private String email;
        private String name;
        private String gradYear;
        private String majors;
        private String minors;
        private String message;

        public AlumnusBuilder discordId(String discordId) {
            this.discordId = discordId;
            return this;
        }

        public String getDiscordId() {
            return discordId;
        }

        public AlumnusBuilder email(String email) {
            this.email = email;
            return this;
        }

        public String getEmail() {
            return email;
        }

        public AlumnusBuilder name(String name) {
            this.name = name;
            return this;
        }

        public String getName() {
            return name;
        }

        public AlumnusBuilder gradYear(String gradYear) {
            this.gradYear = gradYear;
            return this;
        }

        public String getGradYear() {
            return gradYear;
        }

        public AlumnusBuilder majors(String majors) {
            this.majors = majors;
            return this;
        }

        public String getMajors() {
            return majors;
        }

        public AlumnusBuilder minors(String minors) {
            this.minors = minors;
            return this;
        }

        public String getMinors() {
            return minors;
        }

        public AlumnusBuilder message(String message) {
            this.message = message;
            return this;
        }

        public String getMessage() {
            return message;
        }

        public Alumnus build() {
            return new Alumnus(discordId, email, name, gradYear, majors, minors, message);
        }
    }
}