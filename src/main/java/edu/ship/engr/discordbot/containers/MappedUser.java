package edu.ship.engr.discordbot.containers;

import edu.ship.engr.discordbot.Config;
import edu.ship.engr.discordbot.utils.GuildUtil;
import edu.ship.engr.discordbot.utils.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

/**
 * Only used to link all types of {@link MappedUser} together
 */
public abstract class MappedUser {

    protected String name;
	protected String email;
	protected String discordId;
	protected Member member;
	protected String major;



	/**
	 * @return the user's name
	 */
	public String getName() {
	    return name;
	}


	/**
	 * @return the user's discord id
	 */
	public String getDiscordId() {
	    return discordId;
	}


	/**
	 * @return the user's email address
	 */
	public String getEmail() {
	    return email;
	}

	/**
	 * @return their discord member object
	 */
	public Member getMember() {
	    return member;
	}

	/**
	 * @return their user object within discord
	 */
	public User getUser() {
	    return getMember().getUser();
	}


	/**
	 * Gathers information about this person and sends it to the requestor
	 * @param sendTo the user the information is being sent to
	 */
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
	 * Changes the {@link Member member}'s nickname to their actual name.
	 */
	public void setNickname() {
	    GuildUtil.setNickname(getMember(), getName());
	}


	/**
	 * @return the student's major
	 */
	public String getMajor() {
	    return major;
	}
}
