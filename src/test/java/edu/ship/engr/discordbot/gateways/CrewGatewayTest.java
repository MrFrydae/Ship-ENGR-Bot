package edu.ship.engr.discordbot.gateways;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ship.engr.discordbot.utils.OptionsManager;

public class CrewGatewayTest {

	@BeforeEach
	public void setup()
	{
		OptionsManager.getSingleton(true);
	}
	@Test
	public void canGetCrewFromEmail()
	{
		CrewGateway gateway = new CrewGateway();
		// a middle one
		assertEquals("offbyone", gateway.getCrewByEmail("aa3694@ship.edu"));
		//the first one
		assertEquals("nullpointer", gateway.getCrewByEmail("hj4561@ship.edu"));
		//the last one
		assertEquals("outofbounds", gateway.getCrewByEmail("ma6144@ship.edu"));

	}
}
