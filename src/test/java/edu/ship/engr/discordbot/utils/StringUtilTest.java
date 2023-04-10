package edu.ship.engr.discordbot.utils;

import edu.ship.engr.discordbot.testing.annotations.BotTest;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@BotTest
public class StringUtilTest {
    @Test
    public void isEmpty() {
        assertTrue(StringUtil.isEmpty(""));
        assertTrue(StringUtil.isEmpty(null));
        assertFalse(StringUtil.isEmpty("String"));
        assertFalse(StringUtil.isEmpty(" "));
    }

    @Test
    public void isNotEmpty() {
        assertFalse(StringUtil.isNotEmpty(""));
        assertFalse(StringUtil.isNotEmpty(null));
        assertTrue(StringUtil.isNotEmpty("String"));
        assertTrue(StringUtil.isNotEmpty(" "));
    }

    @Test
    public void getOrDefault() {
        assertEquals("String", StringUtil.getOrDefault("String", "Default"));
        assertEquals("Default", StringUtil.getOrDefault("", "Default"));
    }
}