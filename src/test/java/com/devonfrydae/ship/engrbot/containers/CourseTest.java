package com.devonfrydae.ship.engrbot.containers;

import org.junit.Test;

import static org.junit.Assert.*;

public class CourseTest {
    @Test
    public void testObject() {
        Course course = new Course("ENGR-120", "Programming for Engineers", "Every Spring");
        assertEquals("ENGR-120", course.getCode());
        assertEquals("Programming for Engineers", course.getTitle());
        assertEquals(Course.Frequency.EVERY_SPRING, course.getFrequency());
    }
}