package edu.ship.engr.discordbot.containers;

import edu.ship.engr.discordbot.Config;
import edu.ship.engr.discordbot.utils.StringUtil;
import lombok.Builder;
import lombok.Data;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

/**
 * An object containing all information about a professor.
 */
@Data
@Builder
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

    /**
     * Gets all information about this professor.
     *
     * @return an {@link MessageEmbed embedded message} with all info about this professor
     */
    public MessageEmbed getInfoEmbed() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Config.getPrimaryEmbedColor());

        // Only add to builder if present
        if (StringUtil.isNotEmpty(name)) {
            builder.addField("Professor", getName(), true);
        }

        if (StringUtil.isNotEmpty(almaMater)) {
            builder.addField("Alma Mater", getAlmaMater(), true);
        }

        if (StringUtil.isNotEmpty(email)) {
            builder.addField("Email", getEmail(), true);
        }

        if (StringUtil.isNotEmpty(officeNumber)) {
            builder.addField("Office Number", getOfficeNumber(), true);
        }

        if (StringUtil.isNotEmpty(phone)) {
            builder.addField("Phone Number", getPhone(), true);
        }

        if (StringUtil.isNotEmpty(specialty)) {
            builder.addField("Specialty", getSpecialty(), true);
        }

        if (StringUtil.isNotEmpty(title)) {
            builder.addField("Title", getTitle(), true);
        }

        if (StringUtil.isNotEmpty(website)) {
            builder.addField("Website", getWebsite(), true);
        }

        if (StringUtil.isNotEmpty(officeHours)) {
            builder.addField("Office Hours", getOfficeHours(), true);
        }

        return builder.build();
    }
}