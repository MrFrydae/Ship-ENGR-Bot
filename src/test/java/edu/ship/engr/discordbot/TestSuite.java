package edu.ship.engr.discordbot;

import edu.ship.engr.discordbot.containers.CourseTest;
import edu.ship.engr.discordbot.containers.StudentTest;
import edu.ship.engr.discordbot.gateways.CourseGatewayTest;
import edu.ship.engr.discordbot.gateways.DiscordGatewayTest;
import edu.ship.engr.discordbot.gateways.StudentMapperTest;
import edu.ship.engr.discordbot.utils.PatternsTest;
import edu.ship.engr.discordbot.utils.StringUtilTest;
import edu.ship.engr.discordbot.utils.UtilTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    CourseTest.class, StudentTest.class, CourseGatewayTest.class,
    DiscordGatewayTest.class, StudentMapperTest.class,
    PatternsTest.class, StringUtilTest.class, UtilTest.class
})
public class TestSuite {

}
