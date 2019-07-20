package com.devonfrydae.ship.engrbot.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class NumUtilTest {
    @Test
    public void parseInt() {
        assertEquals(12345, (int) NumUtil.parseInt("12345"));
    }

    @Test
    public void parseIntDefault() {
        assertEquals(12345, (int) NumUtil.parseInt("int", 12345));
    }

    @Test
    public void parseLong() {
        assertEquals(12345L, (long) NumUtil.parseLong("12345"));
    }

    @Test
    public void parseLongDefault() {
        assertEquals(12345L, (long) NumUtil.parseLong("long", 12345L));
    }

    @Test
    public void max() {
        assertEquals("12345", NumUtil.max("12345", "1234"));
    }
}