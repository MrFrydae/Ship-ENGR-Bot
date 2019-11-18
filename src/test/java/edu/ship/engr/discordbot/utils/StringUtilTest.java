package edu.ship.engr.discordbot.utils;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

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