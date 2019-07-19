package com.devonfrydae.ship.engrbot.commands.music;

import com.devonfrydae.ship.engrbot.commands.BotCommand;
import com.devonfrydae.ship.engrbot.commands.Command;
import com.devonfrydae.ship.engrbot.commands.CommandEvent;
import com.devonfrydae.ship.engrbot.commands.CommandType;
import com.devonfrydae.ship.engrbot.utils.GuildUtil;
import com.devonfrydae.ship.engrbot.utils.Util;

@BotCommand(
        name = "leave",
        usage = "leave",
        description = "Make the bot leave it's current voice channel",
        type = CommandType.MUSIC
)
public class LeaveCommand extends Command {
    @Override
    public void onCommand(CommandEvent event) {
        boolean inVoiceChannel = GuildUtil.getGuild().getSelfMember().getVoiceState().inVoiceChannel();
        if (inVoiceChannel) {
            GuildUtil.getGuild().getAudioManager().closeAudioConnection();
            Util.sendMsg(event.getTextChannel(), ":mailbox_with_no_mail: **Successfully disconnected**");
        } else {
            Util.sendMsg(event.getTextChannel(), ":x: **I'm not connected to a channel**");
        }
    }
}
