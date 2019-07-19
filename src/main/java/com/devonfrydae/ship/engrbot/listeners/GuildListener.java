package com.devonfrydae.ship.engrbot.listeners;

import com.devonfrydae.ship.engrbot.commands.user.IdentifyCommand;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class GuildListener extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Member joined = event.getMember();
        IdentifyCommand.enterEntryState(joined.getUser());
    }
}
