package edu.ship.engr.discordbot.commands.music;

import edu.ship.engr.discordbot.commands.BotCommand;
import edu.ship.engr.discordbot.commands.Command;
import edu.ship.engr.discordbot.commands.CommandEvent;
import edu.ship.engr.discordbot.commands.CommandType;
import edu.ship.engr.discordbot.utils.GuildUtil;
import edu.ship.engr.discordbot.utils.Util;

@BotCommand(
        name = "leave",
        description = "Make the bot leave it's current voice channel",
        type = CommandType.MUSIC
)
public class LeaveCommand extends Command {
    @Override
    public void onCommand(CommandEvent event) {
        boolean inVoiceChannel = GuildUtil.isBotInVoiceChannel();
        if (inVoiceChannel) {
            GuildUtil.getAudioManager().closeAudioConnection();
            Util.sendMsg(event.getTextChannel(), ":mailbox_with_no_mail: **Successfully disconnected**");
        } else {
            Util.sendMsg(event.getTextChannel(), ":x: **I'm not connected to a channel**");
        }
    }
}
