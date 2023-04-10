package edu.ship.engr.discordbot.gateways;

import dev.frydae.factories.Factory;
import dev.frydae.factories.annotations.InjectFactory;
import edu.ship.engr.discordbot.containers.Student;
import edu.ship.engr.discordbot.testing.annotations.BotTest;
import org.javatuples.Tuple;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test public methods of StudentMapper.
 *
 * @author merlin
 */
@BotTest
public class StudentMapperTest implements Factory<StudentMapper> {
    @InjectFactory private StudentMapper studentMapper;

    @Override
    public StudentMapper factory(List<? extends Tuple> data) {
        StudentMapper mapper = mock(StudentMapper.class);

        when(mapper.getCourseGateway()).thenCallRealMethod();
        when(mapper.getDiscordGateway()).thenCallRealMethod();
        when(mapper.getStudentGateway()).thenCallRealMethod();

        when(mapper.getStudentByEmail(anyString())).thenCallRealMethod();
        when(mapper.getAllMappedStudents()).thenCallRealMethod();

        return mapper;
    }

    /**
     * Check all data returned by a valid call on getStudentByEmail.
     */
    @Test
    public void testWeGetStudentFromEmail() {
        Student s = studentMapper.getStudentByEmail("student1@ship.edu");

        assertEquals("student1@ship.edu", s.getEmail());
        assertEquals("Billy Bob", s.getName());
        assertEquals("Software Engineering", s.getMajor());
        assertEquals("127389127389217837", s.getDiscordId());

    }

    /**
     * Check that we get all three students when we call
     * getAllStudentsWithDiscordIDs.
     */
    @Test
    public void testWeCanGetAllStudents() {
        List<Student> students = studentMapper.getAllMappedStudents();

        assertEquals(3, students.size());

        assertNotNull(studentMapper.getStudentByEmail("student1@ship.edu"));
        assertNotNull(studentMapper.getStudentByEmail("student2@ship.edu"));
        assertNotNull(studentMapper.getStudentByEmail("student3@ship.edu"));
    }
}
