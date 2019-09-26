package edu.ship.engr.discordbot.gateways;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ship.engr.discordbot.containers.Student;
import edu.ship.engr.discordbot.utils.OptionsManager;

public class StudentMapperTest {
	private StudentMapper studentMapper;

	@BeforeEach
	public void setup() {
		OptionsManager.getSingleton(true);
		studentMapper = new StudentMapper();
	}

	@Test
	public void testWeGetStudentFromEmail() {
		Student s = studentMapper.getStudentByEmail("sm5983@ship.edu");
		assertEquals("sm5983@ship.edu", s.getEmail());
		assertEquals("Mike Sissy", s.getName());
		assertEquals("offbyone", s.getCrew());
		assertEquals("Comp Sci & Engineering General", s.getMajor());
		assertEquals("344084000000002000", s.getDiscordId());

	}

	@Test
	public void testWeCanGetAllStudents() {
		List<Student> students = studentMapper.getAllStudentsWithDiscordIDs();
		assertEquals(3, students.size());
		for (Student s : students) {
			assertTrue("Weird email address " + s.getEmail(),
					s.getEmail().equals("jh2263@ship.edu") || s.getEmail().equals("hj4561@ship.edu")
					|| s.getEmail().equals("sm5983@ship.edu"));
		}
	}
}
