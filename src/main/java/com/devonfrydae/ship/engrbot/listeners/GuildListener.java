package com.devonfrydae.ship.engrbot.listeners;

import com.devonfrydae.ship.engrbot.commands.user.IdentifyCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildListener extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Member joined = event.getMember();
        IdentifyCommand.enterEntryState(joined.getUser());
    }
}
