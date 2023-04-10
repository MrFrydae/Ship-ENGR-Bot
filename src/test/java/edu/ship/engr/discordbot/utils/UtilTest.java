package edu.ship.engr.discordbot.utils;

import edu.ship.engr.discordbot.testing.annotations.BotTest;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;

@BotTest
public class UtilTest {

    @Test
    public void ucfirst() {
        assertEquals("Test String", Util.ucfirst("TEST STRING"));
    }

    @Test
    public void getSemesterCode() {
        Calendar date = Calendar.getInstance();
        date.set(2019, 11, 1);
        assertEquals("201960", Util.getSemesterCode(date));
        date.set(2020, 3, 1);
        assertEquals("202020", Util.getSemesterCode(date));
    }

    @Test
    public void formatClassName() {
        assertEquals("ENGR-120", Util.formatClassName("engr120"));
    }

    @Test
    public void formatSemesterCode() {
        assertEquals("Fall 2019", Util.formatSemesterCode("201960"));
        assertEquals("Spring 2020", Util.formatSemesterCode("202020"));
    }
}