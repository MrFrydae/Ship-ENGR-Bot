package edu.ship.engr.discordbot.gateways;

import edu.ship.engr.discordbot.utils.OptionsManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests the public methods in CrewGateway.
 * 
 * @author merlin
 */
public class CrewGatewayTest {
    /**
     * Make sure we are in testing mode.
     */
    @BeforeEach
    public void setup() {
        OptionsManager.getSingleton().setTestMode(true);
    }

    /**
     * Test valid calls on getCrewByEmail.
     */
    @Test
    public void canGetCrewFromEmail() {
        CrewGateway gateway = new CrewGateway();
        // a middle one
        assertEquals("nullpointer", gateway.getCrewByEmail("as3817@ship.edu"));
        // the first one
        assertEquals("offbyone", gateway.getCrewByEmail("dj0327@ship.edu"));
        // the last one
        assertEquals("outofbounds", gateway.getCrewByEmail("ep7481@ship.edu"));
    }
}
