package edu.ship.engr.discordbot.gateways;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ship.engr.discordbot.containers.MappedUser;
import edu.ship.engr.discordbot.utils.CSVUtil;
import edu.ship.engr.discordbot.utils.Exceptions.IdentifyException;
import edu.ship.engr.discordbot.utils.OptionsManager;
import net.dv8tion.jda.api.entities.Member;

/**
 * Test public methods in DiscordGateway.
 * 
 * @author merlin
 *
 */
public class DiscordGatewayTest {

	
	/**
	 * save the data so it can be restored after each test
	 */
	@BeforeAll
	public static void backupTheData()
	{
		OptionsManager.getSingleton(true);
		DiscordGateway gateway;
		gateway = new DiscordGateway();
		gateway.backUpTheData();
	}


	/**
	 * Restore the data 
	 */
	@AfterEach
	public void restore() {
		DiscordGateway gateway = new DiscordGateway();
		gateway.restoreTheData();
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

	/**
	 * Check to make sure that isDocordStored returns true if both params match in
	 * one record
	 */
	@Test
	public void matchesDiscordID() {
		DiscordGateway gateway = new DiscordGateway();
		assertTrue(gateway.isDiscordStored("247000000000000000", "hj4561@ship.edu"));
	}
	
	/**
	 * Test that we can store a new discord id/email pair
	 */
	@Test
	public void canStoreANewOne()
	{
		DiscordGateway gateway = new DiscordGateway();
		gateway.storeDiscordId("12345678912345", "silly@ship.edu");
		DiscordGateway testGateway = new DiscordGateway();
		assertTrue(testGateway.isDiscordStored("12345678912345", "silly@ship.edu"));
	}
}
