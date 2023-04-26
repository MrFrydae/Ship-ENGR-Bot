package edu.ship.engr.discordbot.commands;

import dev.frydae.jda.commands.annotations.CommandAlias;
import dev.frydae.jda.commands.annotations.CommandPermission;
import dev.frydae.jda.commands.annotations.Condition;
import dev.frydae.jda.commands.annotations.Description;
import dev.frydae.jda.commands.annotations.GlobalCommand;
import dev.frydae.jda.commands.annotations.Name;
import dev.frydae.jda.commands.core.BaseCommand;
import edu.ship.engr.discordbot.utils.GuildUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.requests.RestAction;

import java.util.stream.Collectors;

public class MiscCommands extends BaseCommand {

    @CommandAlias("test")
    @Description("Test Command")
    public void onTestCommand() {
        if (!getEvent().getUser().getId().equals("246818302335254529")) {
            replyHidden("Only <@246818302335254529> can use this command").queue();
            return;
        }

        InteractionHook hook = getEvent().getInteraction().deferReply(true).complete();

        hook.editOriginal("beep").queue();
    }

    @CommandAlias("stop")
    @Description("Command to stop the Discord server")
    @CommandPermission(Permission.ADMINISTRATOR)
    public void onStop() {
        replyHidden("Stopping Server!").queue(success -> GuildUtil.shutdown());
    }

    @CommandAlias("ping")
    @Description("Pong!")
    @GlobalCommand
    public void onPing() {
        replyHidden(":ping_pong: Pong!").queue();
    }

    @CommandAlias("purge")
    @Description("Bulk delete chat messages")
    @CommandPermission(Permission.MESSAGE_MANAGE)
    public void onPurge(@Name("amount") @Description("The amount of messages to delete") @Condition("limits|min=1,max=100") Integer amount) {
        getEvent().getInteraction().deferReply(true).queue(hook -> {
            TextChannel channel = getEvent().getInteraction().getChannel().asTextChannel();

            channel.getHistory().retrievePast(amount).queue(messages -> {
                RestAction.allOf(messages.stream().map(Message::delete).collect(Collectors.toList())).queue(success -> {
                    hook.editOriginal(amount + " messages have been purged from chat").queue();
                });
            });
        });
    }

    @CommandAlias("mirror")
    @Description("Mirror a message into another channel")
    @CommandPermission(Permission.MESSAGE_MANAGE)
    public void onMirror(@Name("message") @Description("The ID of the message") String id,
                         @Name("channel") @Description("The channel you'd like to mirror to") TextChannel channel) {
        getEvent().getInteraction().deferReply(true).queue(hook -> {
            MessageChannel sentFrom = hook.getInteraction().getMessageChannel();

            sentFrom.retrieveMessageById(id)
                    .onErrorFlatMap((error) -> hook.editOriginal(error.getMessage()))
                    .onSuccess((message) -> {
                        String contentRaw = message.getContentRaw();

                        channel.sendMessage(contentRaw).queue();

                        hook.editOriginal("Message mirrored").queue();
                    })
                    .queue();
        });
    }
}
