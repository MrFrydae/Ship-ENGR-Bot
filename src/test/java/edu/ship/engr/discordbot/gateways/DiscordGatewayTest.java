package edu.ship.engr.discordbot.gateways;

import edu.ship.engr.discordbot.utils.OptionsManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test public methods in DiscordGateway.
 *
 * @author merlin
 */
public class DiscordGatewayTest {

    private static final List<String> TEST_EMAILS = Arrays.asList("zb4403@ship.edu", "dg6744@ship.edu", "mp2159@ship.edu");

    /**
     * Save the data so it can be restored after each test.
     */
    @BeforeAll
    public static void backupTheData() {
        OptionsManager.getSingleton().setTestMode(true);
        DiscordGateway gateway;
        gateway = new DiscordGateway();
        gateway.backUpTheData();
    }


    /**
     * Restore the data.
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
        assertEquals("142386615942250496", gateway.getDiscordIdByEmail("zb4403@ship.edu"));
        // middle
        assertEquals("168125712404840449", gateway.getDiscordIdByEmail("dg6744@ship.edu"));
        // last
        assertEquals("353309962098704386", gateway.getDiscordIdByEmail("mp2159@ship.edu"));
    }

    /**
     * Check to make sure that isDiscordStored returns true if both params match in
     * one record.
     */
    @Test
    public void matchesDiscordID() {
        DiscordGateway gateway = new DiscordGateway();
        assertTrue(gateway.isDiscordStored("168125712404840449", "dg6744@ship.edu"));
    }

    /**
     * Test that we can store a new discord id/email pair.
     */
    @Test
    public void canStoreNewOne() {
        DiscordGateway gateway = new DiscordGateway();
        gateway.storeDiscordId("12345678912345", "silly@ship.edu");
        DiscordGateway testGateway = new DiscordGateway();
        assertTrue(testGateway.isDiscordStored("12345678912345", "silly@ship.edu"));
    }

    /**
     * Test valid calls on getAllEmails.
     */
    @Test
    public void canGetAllRecords() {
        DiscordGateway gateway = new DiscordGateway();
        List<String> records = gateway.getAllEmails();
        for (String x : TEST_EMAILS) {
            assertTrue(records.contains(x));
        }
    }
}
