package edu.ship.engr.discordbot.commands.misc;

import com.google.common.collect.Lists;
import edu.ship.engr.discordbot.commands.BotCommand;
import edu.ship.engr.discordbot.commands.Command;
import edu.ship.engr.discordbot.commands.CommandEvent;
import edu.ship.engr.discordbot.commands.CommandType;
import edu.ship.engr.discordbot.utils.Util;

import java.util.List;
import java.util.Random;

@BotCommand(
        name = "coronavirus",
        description = "Stay safe now y'all...",
        type = CommandType.MISC
)
public class CoronavirusCommand extends Command {
    private List<String> gifLinks = Lists.newArrayList("https://tenor.com/view/gas-mask-yow-cool-peace-peace-out-gif-16433328",
            "https://giphy.com/gifs/justin-g-stinky-stink-stench-3o7ZezSB4ckAYlcCRy",
            "https://giphy.com/gifs/Ludo-Studio-thestrangechores-the-strange-chores-JUSBKSSF9Hrmd7NIU1",
            "https://giphy.com/gifs/corona-camdelafu-coronavirus-IbmS6XKR5fTVchlxcN",
            "https://giphy.com/gifs/teamcoco-conan-obrien-olivia-munn-3oeHLt0kqVDcHyX9Pq",
            "https://giphy.com/gifs/pollution-flu-loof-YP8LQelffqJq4ncp3M",
            "https://giphy.com/gifs/leroypatterson-germs-sadie-wash-your-hands-cj2Jay0wKyqFgprCUM",
            "https://giphy.com/gifs/potato-coronavirus-cornona-h45pDBwEdnmtThwroC",
            "https://giphy.com/gifs/colbertlateshow-stephen-colbert-late-show-plague-kHl4TKQMGSgISlozZb",
            "https://giphy.com/gifs/drink-virus-corona-JRsY1oIVA7IetTkKVO",
            "https://giphy.com/gifs/colbertlateshow-stephen-colbert-late-show-hand-sanitizer-3o7TKr03EgSbQCyT6w",
            "https://giphy.com/gifs/spongebob-spongebob-squarepants-episode-3-xUPJPhgFOByeJOxOGk",
            "https://giphy.com/gifs/spongebob-spongebob-squarepants-season-5-3ogwG4n0qEo9TEAMak",
            "https://giphy.com/gifs/fallontonight-jimmy-fallon-tonight-show-kim-kardashian-1dKPBBmBoOjjTkei4t");


    @Override
    public void onCommand(CommandEvent event) {
        String gif = gifLinks.get(new Random().nextInt(gifLinks.size()));
        Util.sendMsg(event.getTextChannel(), gif);
    }
}