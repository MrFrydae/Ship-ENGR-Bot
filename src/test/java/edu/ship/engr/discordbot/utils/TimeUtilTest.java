package edu.ship.engr.discordbot.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimeUtilTest {

    @ParameterizedTest
    @MethodSource
    public void testFormatTime(Integer seconds, String expectedDisplay, int depth) {
        assertEquals(expectedDisplay, TimeUtil.formatTime(seconds, depth));
    }

    private static Stream<Arguments> testFormatTime() {
        return Stream.of(
                Arguments.of(null, "moments", 10),
                Arguments.of(1, "moments", 10),
                Arguments.of(5, "5 seconds", 10),
                Arguments.of(59, "59 seconds", 10),
                Arguments.of(60, "1 minute", 10),
                Arguments.of(65, "1 minute, 5 seconds", 10),
                Arguments.of(2 * 60, "2 minutes", 10),
                Arguments.of((59 * 60) + 59, "59 minutes, 59 seconds", 10),
                Arguments.of(3600, "1 hour", 10),
                Arguments.of(3600 + 5, "1 hour, 5 seconds", 10),
                Arguments.of(3600 + (60 * 2) + 12, "1 hour, 2 minutes, 12 seconds", 10),
                Arguments.of((3600 * 2), "2 hours", 10),
                Arguments.of((3600 * 2) + (60 * 2) + 12, "2 hours, 2 minutes, 12 seconds", 10),
                Arguments.of((3600 * 23) + (60 * 59) + 59, "23 hours, 59 minutes, 59 seconds", 10),
                Arguments.of(86400, "1 day", 10),
                Arguments.of(86400 + 5, "1 day, 5 seconds", 10),
                Arguments.of(86400 + (3600 * 2) + (60 * 2) + 12, "1 day, 2 hours, 2 minutes, 12 seconds", 10),
                Arguments.of(86400 + (3600 * 23) + (60 * 59) + 59, "1 day, 23 hours, 59 minutes, 59 seconds", 10),
                Arguments.of((86400 * 2), "2 days", 10)
        );
    }
}
