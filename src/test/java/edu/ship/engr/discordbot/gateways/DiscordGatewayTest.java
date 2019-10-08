package edu.ship.engr.discordbot.gateways;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import edu.ship.engr.discordbot.utils.OptionsManager;

/**
 * Test public methods in DiscordGateway.
 * 
 * @author merlin
 *
 */
public class DiscordGatewayTest {

	private static final List<String> TEST_EMAILS = Arrays.asList(new String[] {"jh2263@ship.edu","hj4561@ship.edu","sm5983@ship.edu"});

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
	
	/**
	 * Test valid calls on getCrewByEmail.
	 */
	@Test
	public void canGetAllRecords() {
		DiscordGateway gateway = new DiscordGateway();
		Iterable<String> records = gateway.getAllEmails();
		for(String x:records)
		{
			assertTrue(TEST_EMAILS.contains(x));
		}
		
	}
}
