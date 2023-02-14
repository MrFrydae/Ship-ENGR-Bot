package edu.ship.engr.discordbot.systems;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import edu.ship.engr.discordbot.DiscordBot;
import edu.ship.engr.discordbot.containers.Group;
import edu.ship.engr.discordbot.utils.GuildUtil;
import edu.ship.engr.discordbot.utils.Util;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.InteractionHook;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public final class Groups {
    private static final Supplier<List<Group>> allGroups = Suppliers.memoizeWithExpiration(() -> {
        Category groups = GuildUtil.getCategory("Groups");

        return groups.getTextChannels().stream()
                .filter(c -> c.getName().startsWith("group-"))
                .map(Groups::getGroup)
                .collect(Collectors.toList());
    }, 10, TimeUnit.SECONDS);

    public static void renameGroup(InteractionHook hook, Group channel, String newName) {

    }

    public static Group getGroup(TextChannel channel) {
        return new Group(channel);
    }

    public static List<Group> getAllGroups() {
        return allGroups.get();
    }

    @NotNull
    public static Group createGroup(String name) {
        return null;
    }

    public static void inviteMember(InteractionHook hook, Group group, Member member) {

    }

    public static void kickMember(Group group, Member member) {

    }

    public static void deleteGroup(Group group) {

    }
}
