package edu.ship.engr.discordbot.containers;

import com.google.common.collect.Lists;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test the Student DTO
 * @author merlin
 *
 */
public class StudentTest {
    /**
     * the constructor needs to store a bunch of stuff.
     */
    @Test
    public void testInitialization() {
        Course course = new Course("ENGR-120", "Programming for Engineers", "Every Spring", "202020", Lists.newArrayList("201820", "201860", "201920", "202020"));
        Student s = new Student("Derek Williams", "dw1738@ship.edu", "Software Engineering", "outofbounds", null, null, Lists.newArrayList(course));
        assertEquals("Derek Williams", s.getName());
        assertEquals("dw1738@ship.edu", s.getEmail());
        assertEquals("Software Engineering", s.getMajor());
        assertEquals("outofbounds", s.getCrew());
        assertEquals("ENGR-120", s.getCourses().get(0).getCode());
    }
}