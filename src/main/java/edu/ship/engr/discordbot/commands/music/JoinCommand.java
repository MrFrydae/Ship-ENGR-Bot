package edu.ship.engr.discordbot.commands.music;

import edu.ship.engr.discordbot.commands.BotCommand;
import edu.ship.engr.discordbot.commands.Command;
import edu.ship.engr.discordbot.commands.CommandEvent;
import edu.ship.engr.discordbot.commands.CommandType;
import edu.ship.engr.discordbot.utils.GuildUtil;
import edu.ship.engr.discordbot.utils.Util;
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
        VoiceChannel channel = GuildUtil.getMemberVoiceChannel(event.getMember());
        boolean alreadyInVC = GuildUtil.isBotInVoiceChannel();
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

    /**
     * Joins the bot into a certain voice channel.
     *
     * @param channel the channel to join
     * @param deafened if the bot is deafened
     */
    public static void join(VoiceChannel channel, boolean deafened) {
        AudioManager manager = GuildUtil.getAudioManager();
        manager.setSelfDeafened(deafened);
        manager.openAudioConnection(channel);
    }
}
