package edu.ship.engr.discordbot;

import edu.ship.engr.discordbot.containers.CourseTest;
import edu.ship.engr.discordbot.containers.StudentTest;
import edu.ship.engr.discordbot.gateways.CourseGatewayTest;
import edu.ship.engr.discordbot.gateways.CrewGatewayTest;
import edu.ship.engr.discordbot.gateways.DiscordGatewayTest;
import edu.ship.engr.discordbot.gateways.StudentMapperTest;
import edu.ship.engr.discordbot.utils.OptionsManager;
import edu.ship.engr.discordbot.utils.PatternsTest;
import edu.ship.engr.discordbot.utils.StringUtilTest;
import edu.ship.engr.discordbot.utils.UtilTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CourseTest.class, StudentTest.class, CourseGatewayTest.class,
        CrewGatewayTest.class, DiscordGatewayTest.class, StudentMapperTest.class,
        PatternsTest.class, StringUtilTest.class, UtilTest.class
})
public class TestSuite {
    @BeforeAll
    static void setup() {
        OptionsManager.getSingleton(true);
    }
}
