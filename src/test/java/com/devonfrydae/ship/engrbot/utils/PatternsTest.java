package com.devonfrydae.ship.engrbot.utils;

import org.junit.Test;

import static org.junit.Assert.*;

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
    public void testEmailMatch() {
        assertTrue(Patterns.VALID_EMAIL_PATTERN.matcher("admin@ship.edu").matches());
    }

    @Test
    public void testClassNameMatch() {
        assertTrue(Patterns.CLASS_NAME.matcher("ENGR-120").matches());
    }

    @Test
    public void testUserMentionMatch() {
        assertTrue(Patterns.USER_MENTION.matcher("<@!000000000000000000>").matches());
        assertTrue(Patterns.USER_MENTION.matcher("000000000000000000").matches());
    }
}