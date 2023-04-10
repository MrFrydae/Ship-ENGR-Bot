package edu.ship.engr.discordbot.commands;

import edu.ship.engr.discordbot.commands.annotations.AutoCompletion;
import edu.ship.engr.discordbot.commands.annotations.CommandAlias;
import edu.ship.engr.discordbot.commands.annotations.Description;
import edu.ship.engr.discordbot.commands.annotations.Name;
import edu.ship.engr.discordbot.commands.core.BaseCommand;
import edu.ship.engr.discordbot.containers.Course;

public class CourseCommands extends BaseCommand {
    @CommandAlias("classinfo")
    @Description("View information about a course")
    public void onClassInfo(@Name("course") @Description("The course you wish to view") @AutoCompletion("courses") Course course) {
        replyHidden(course.getInfoEmbed()).queue();
    }

    @CommandAlias("offerings")
    @Description("Shows when this class is offered")
    public void onOfferings(@Name("course") @Description("The course you wish to view") @AutoCompletion("courses") Course course) {
        replyHidden(course.getOfferingsEmbed()).queue();
    }

    @CommandAlias("nextoffering")
    @Description("Shows when this class is offered")
    public void onNextOffering(@Name("course") @Description("The course you wish to view") @AutoCompletion("courses") Course course) {
        replyHidden(course.getNextOfferingString()).queue();
    }
}
