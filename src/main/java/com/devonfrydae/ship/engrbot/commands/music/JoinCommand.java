package com.devonfrydae.ship.engrbot.commands.music;

import com.devonfrydae.ship.engrbot.commands.BotCommand;
import com.devonfrydae.ship.engrbot.commands.Command;
import com.devonfrydae.ship.engrbot.commands.CommandEvent;
import com.devonfrydae.ship.engrbot.commands.CommandType;
import com.devonfrydae.ship.engrbot.utils.GuildUtil;
import com.devonfrydae.ship.engrbot.utils.Util;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

@BotCommand(
        name = "join",
        description = "Make the bot join your voice channel",
        type = CommandType.MUSIC
)
public class JoinCommand extends Command {
    @Override
    public void onCommand(CommandEvent event) {
        VoiceChannel channel = event.getMember().getVoiceState().getChannel();
        boolean alreadyInVC = GuildUtil.getGuild().getSelfMember().getVoiceState().getChannel() != null;
        if (!alreadyInVC) {
            if (channel != null) {
                join(channel, true);
                Util.sendMsg(event.getTextChannel(), ":mailbox_with_no_mail: **Successfully connected**");
            } else {
                Util.sendMsg(event.getTextChannel(), ":x: **You must be in a voice channel to use this command**");
            }
        } else {
            Util.sendMsg(event.getTextChannel(), ":x: **Already connected to a channel**");
        }
    }

    public static void join(VoiceChannel channel, boolean deafened) {
        AudioManager manager = channel.getGuild().getAudioManager();
        manager.setSelfDeafened(deafened);
        manager.openAudioConnection(channel);
    }
}
