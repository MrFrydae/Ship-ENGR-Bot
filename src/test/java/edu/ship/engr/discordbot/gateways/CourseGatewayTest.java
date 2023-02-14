package edu.ship.engr.discordbot.gateways;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ship.engr.discordbot.utils.OptionsManager;

/**
 * Tests the public methods in CourseGateway.
 *
 * @author merlin
 */
public class CourseGatewayTest {
    /**
     * Make sure we are in testing mode.
     */
    @BeforeEach
    public void setup() {
        OptionsManager.getSingleton().setTestMode(true);
    }

    /**
     * Valid course names have an alphabetic prefix, a number suffix, an optional
     * dash between them and must be in the offerings data source.
     */
    @Test
    public void validCourseName() {
        CourseGateway gateway = new CourseGateway();
        assertTrue(gateway.isValidCourseName("SWE200"));
        assertTrue(gateway.isValidCourseName("SWE-200"));
        assertFalse(gateway.isValidCourseName("SWE 200"));
        assertFalse(gateway.isValidCourseName("SWE 107"));
        assertFalse(gateway.isValidCourseName("Merlin"));
    }
}
