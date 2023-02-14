package edu.ship.engr.discordbot.containers;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public final class Group {
    private final TextChannel channel;

    public Group(TextChannel channel) {
        this.channel = channel;
    }

    public TextChannel getChannel() {
        return channel;
    }

    public String getAsMention() {
        return channel.getAsMention();
    }

    public boolean contains(Member member) {
        return getChannel().getMembers().contains(member);
    }

    public String getName() {
        return getChannel().getName();
    }
}
