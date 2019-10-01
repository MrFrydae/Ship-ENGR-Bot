package edu.ship.engr.discordbot.gateways;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ship.engr.discordbot.utils.OptionsManager;

/**
 * Test public methods in DiscordGateway.
 * @author merlin
 *
 */
public class DiscordGatewayTest {

	/**
	 * Make sure we are in testing mode
	 */
	@BeforeEach
	public void setup() {
		OptionsManager.getSingleton(true);
	}

	/**
	 * Make sure valid calls on getDiscordIDByEmail work.
	 */
	@Test
	public void getGetExistingID() {
		DiscordGateway gateway = new DiscordGateway();
		// first
		assertEquals("342000000000000000", gateway.getDiscordIdByEmail("jh2263@ship.edu"));
		// middle
		assertEquals("247000000000000000", gateway.getDiscordIdByEmail("hj4561@ship.edu"));
		// last
		assertEquals("344084000000002000", gateway.getDiscordIdByEmail("sm5983@ship.edu"));

	}
}
