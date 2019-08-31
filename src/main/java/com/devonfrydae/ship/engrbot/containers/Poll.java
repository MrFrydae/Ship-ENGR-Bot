package com.devonfrydae.ship.engrbot.containers;

import co.aikar.idb.DB;
import com.devonfrydae.ship.engrbot.utils.GuildUtil;
import com.devonfrydae.ship.engrbot.utils.Log;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class Poll {
    private String channelId;
    private String messageId;
    private String title;
    private String description;
    private String reactions;
    private String footer;
    private Color color;
    private String image;

    public Poll(String channelId, String messageId) {
        this.channelId = channelId;
        this.messageId = messageId;

        Message message = GuildUtil.getTextChannelByID(channelId).retrieveMessageById(messageId).complete();
        MessageEmbed embed = message.getEmbeds().get(0);
        this.title = embed.getTitle();
        this.description = embed.getDescription();
        this.footer = embed.getFooter() != null ? embed.getFooter().getText() : null;
        this.image = embed.getThumbnail() != null ? embed.getThumbnail().getUrl() : null;
        this.color = embed.getColor();
        this.reactions = message.getReactions().stream()
                .map(reaction -> reaction.getReactionEmote().getEmote().getName())
                .collect(Collectors.joining("+"));
    }

    public Poll setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Poll setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Poll setReactions(String reactions) {
        this.reactions = reactions;
        return this;
    }

    public String getReactions() {
        return reactions;
    }

    public Poll setFooter(String footer) {
        this.footer = footer;
        return this;
    }

    public String getFooter() {
        return footer;
    }

    public Poll setColor(Color color) {
        this.color = color;
        return this;
    }

    public Color getColor() {
        return color;
    }

    public Poll setImage(String image) {
        this.image = image;
        return this;
    }

    public String getImage() {
        return image;
    }

    public void update() {
        TextChannel channel = GuildUtil.getTextChannelByID(channelId);

        MessageEmbed embed = new EmbedBuilder()
                .setTitle(getTitle())
                .setThumbnail(getImage())
                .setDescription(getDescription())
                .setFooter(getFooter())
                .setColor(getColor())
                .build();
        MessageBuilder builder = new MessageBuilder();
        builder.setEmbed(embed);
        channel.editMessageById(messageId, builder.build()).complete();
    }

    public void endPoll() {
        // TODO: End poll
    }

    ////////////////////
    // STATIC METHODS //
    ////////////////////
    public static String getPollChannelFromMessage(String messageId) {
        try {
            return DB.getFirstColumn("SELECT channel_id FROM polls WHERE message_id = ?", messageId);
        } catch (SQLException e) {
            Log.exception("Exception in getPollChannelFromMessage: ", e);
            return null;
        }
    }
}
