package edu.ship.engr.discordbot.containers;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;


/**
 * Test course DTO
 * 
 * @author merlin
 *
 */
public class CourseTest {
    /**
     * The constructor needs to store a lot of stuff
     */
    @Test
    public void testObject() {
        Course course = new Course("ENGR-120", "Programming for Engineers", "Every Spring", "202020", Lists.newArrayList("201820", "201860", "201920", "202020"));
        assertEquals("ENGR-120", course.getCode());
        assertEquals("Programming for Engineers", course.getTitle());
        assertEquals(Course.Frequency.EVERY_SPRING, course.getFrequency());
        String nextOffering = course.getNextOffering();
        assertEquals("202020", nextOffering);
        List<String> allOfferings = course.getAllOfferings();
        assertEquals("201920", allOfferings.get(2));
        assertTrue(allOfferings.contains(nextOffering));
    }
}