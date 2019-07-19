package com.devonfrydae.ship.engrbot.containers;

import com.devonfrydae.ship.engrbot.Config;
import com.devonfrydae.ship.engrbot.utils.CSVUtil;
import com.devonfrydae.ship.engrbot.utils.Util;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;

/**
 * An object containing all relevant information about a student
 */
public class Student extends MappedUser {
    public Student(String email, String discordId) {
        super(email, discordId);
    }

    public String getName() {
        return CSVUtil.getStudentName(email);
    }

    public String getMajor() {
        return CSVUtil.getStudentMajor(email);
    }

    @Override
    public void sendUserInfo(User sendTo) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Config.getPrimaryEmbedColor());
        builder.setAuthor(user.getName() + "#" + user.getDiscriminator(), null, user.getAvatarUrl());
        builder.addField("Name", getName(), true);
        builder.addField("Email", getEmail(), true);
        builder.addField("Major", getMajor(), true);

        Util.sendPrivateMsg(sendTo, builder.build());
    }
}
