package edu.ship.engr.discordbot.utils;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

public class PatternsTest {

    @Test
    public void testSpaceSplit() {
        assertArrayEquals(new String[]{"First", "Second", "Third"}, Patterns.SPACE.split("First Second Third"));
    }

    @Test
    public void testPipeSplit() {
        assertArrayEquals(new String[]{"First", "Second", "Third"}, Patterns.PIPE.split("First|Second|Third"));
    }

    @Test
    public void testCommaSplit() {
        assertArrayEquals(new String[]{"First", "Second", "Third"}, Patterns.COMMA.split("First,Second,Third"));
    }

    @Test
    public void testEmailMatch() {
        assertTrue(Patterns.VALID_EMAIL_PATTERN.matches("admin@ship.edu"));
    }

    @Test
    public void testClassNameMatch() {
        assertTrue(Patterns.CLASS_NAME.matches("ENGR-120"));
    }

    @Test
    public void testUserMentionMatch() {
        assertTrue(Patterns.USER_MENTION.matches("<@!000000000000000000>"));
        assertTrue(Patterns.USER_MENTION.matches("000000000000000000"));
    }
}