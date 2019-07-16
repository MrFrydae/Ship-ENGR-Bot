package com.devonfrydae.ship.engrbot.utils;

import com.devonfrydae.ship.engrbot.Config;
import com.devonfrydae.ship.engrbot.DiscordBot;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

import java.util.List;

public class GuildUtil {
    public static Guild getGuild() {
        return DiscordBot.getJDA().getGuildById(Config.getLong("bot.guild.id"));
    }

    public static Role getRole(String roleName) {
        return getRole(roleName, true);
    }
    public static Role getRole(String roleName, boolean ignoreCase) {
        return getGuild().getRolesByName(roleName, ignoreCase).get(0);
    }

    public static void addRolesToMember(Member member, Role... roles) {
        getGuild().getController().addRolesToMember(member, roles).queue();
    }

    public static void addRolesToMember(Member member, List<Role> roles) {
        getGuild().getController().addRolesToMember(member, roles).queue();
    }

    public static void removeRolesFromMember(Member member, Role... roles) {
        getGuild().getController().removeRolesFromMember(member, roles).queue();
    }

    public static void removeRolesFromMember(Member member, List<Role> roles) {
        getGuild().getController().removeRolesFromMember(member, roles).queue();
    }

    public static Role getOutOfBounds() {
        return GuildUtil.getRole("OutOfBounds");
    }

    public static Role getNullPointer() {
        return GuildUtil.getRole("NullPointer");
    }

    public static Role getOffByOne() {
        return GuildUtil.getRole("OffByOne");
    }

    public static Role getCrew(String crewName) {
        switch (crewName.toLowerCase()) {
            case "outofbounds": return getOutOfBounds();
            case "nullpointer": return getNullPointer();
            case "offbyone": return getOffByOne();
        }
        return null;
    }
}
