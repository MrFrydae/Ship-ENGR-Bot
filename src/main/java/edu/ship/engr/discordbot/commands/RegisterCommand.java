package edu.ship.engr.discordbot.commands;

import dev.frydae.jda.commands.annotations.CommandAlias;
import dev.frydae.jda.commands.annotations.Description;
import dev.frydae.jda.commands.annotations.Name;
import dev.frydae.jda.commands.annotations.Subcommand;
import dev.frydae.jda.commands.core.BaseCommand;
import edu.ship.engr.discordbot.systems.Registration;
import net.dv8tion.jda.api.interactions.InteractionHook;

@CommandAlias("register")
@Description("Command to register yourself")
public class RegisterCommand extends BaseCommand {

    @Subcommand("student")
    @Description("Register command for students")
    public void onStudent(@Name("email") @Description("Your Shippensburg University email") String email) {
        InteractionHook hook = getEvent().getInteraction().deferReply(true).complete();

        Registration.registerStudent(hook, getSender(), email);
    }

    @Subcommand("alumni")
    @Description("Register command for alumni")
    public void onAlumni(@Name("name") @Description("Your name") String name) {
        InteractionHook hook = getEvent().getInteraction().deferReply(true).complete();

        Registration.registerAlumnus(hook, getEvent(), name);

        hook.editOriginal("A request for registration has been sent for review.").queue();
    }

    @Subcommand("professor")
    @Description("Register command for professors")
    public void onProfessor(@Name("name") @Description("Your name") String name) {
        InteractionHook hook = getEvent().getInteraction().deferReply(true).complete();

        Registration.registerProfessor(hook, getEvent(), name);

        hook.editOriginal("A request for registration has been sent for review.").queue();
    }
}
