package edu.ship.engr.discordbot.gateways;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import dev.frydae.factories.Factory;
import dev.frydae.factories.annotations.InjectFactory;
import edu.ship.engr.discordbot.testing.annotations.BotTest;
import org.javatuples.Sextet;
import org.javatuples.Tuple;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@BotTest
public class StudentGatewayTest implements Factory<StudentGateway> {
    @InjectFactory private StudentGateway gateway;

    // region Factory
    @Override
    public StudentGateway factory(List<? extends Tuple> data) {
        StudentGateway gateway = mock(StudentGateway.class);

        when(gateway.getAllStudentTuplets()).thenReturn((List<Sextet<String, String, String, String, String, String>>) data);
        when(gateway.getUniqueStudentTuplets()).thenCallRealMethod();
        when(gateway.getStudentTupletsByEmail(anyString())).thenCallRealMethod();

        return gateway;
    }

    @Override
    public ListMultimap<Class<?>, ? extends Tuple> getFactoryData() {
        ListMultimap<Class<?>, Sextet<String, String, String, String, String, String>> data = ArrayListMultimap.create();

        data.put(StudentGateway.class, Sextet.with("Bob", "Billy", "student1@ship.edu", "Software Engineering", "BIO162", "202260"));
        data.put(StudentGateway.class, Sextet.with("Bob", "Billy", "student1@ship.edu", "Software Engineering", "ENGR100", "202260"));
        data.put(StudentGateway.class, Sextet.with("Bob", "Billy", "student1@ship.edu", "Software Engineering", "GEO103", "202260"));
        data.put(StudentGateway.class, Sextet.with("Bob", "Billy", "student1@ship.edu", "Software Engineering", "SWE100", "202260"));
        data.put(StudentGateway.class, Sextet.with("Bob", "Billy", "student1@ship.edu", "Software Engineering", "UNIV101", "202260"));
        data.put(StudentGateway.class, Sextet.with("Davis", "Kyle", "student2@ship.edu", "Software Engineering", "ENGR100", "202260"));
        data.put(StudentGateway.class, Sextet.with("Davis", "Kyle", "student2@ship.edu", "Software Engineering", "GEO101", "202260"));
        data.put(StudentGateway.class, Sextet.with("Davis", "Kyle", "student2@ship.edu", "Software Engineering", "HCS100", "202260"));
        data.put(StudentGateway.class, Sextet.with("Davis", "Kyle", "student2@ship.edu", "Software Engineering", "MAT117A", "202260"));
        data.put(StudentGateway.class, Sextet.with("Davis", "Kyle", "student2@ship.edu", "Software Engineering", "UNIV101", "202260"));
        data.put(StudentGateway.class, Sextet.with("Matthews", "Tony", "student3@ship.edu", "Computer Science", "BIO162", "202260"));
        data.put(StudentGateway.class, Sextet.with("Matthews", "Tony", "student3@ship.edu", "Computer Science", "FIN101", "202260"));
        data.put(StudentGateway.class, Sextet.with("Matthews", "Tony", "student3@ship.edu", "Computer Science", "HCS100", "202260"));
        data.put(StudentGateway.class, Sextet.with("Matthews", "Tony", "student3@ship.edu", "Computer Science", "MAT175", "202260"));

        return data;
    }
    // endregion

    @Test
    public void testGetUniqueStudents() {
        assertEquals(3, gateway.getUniqueStudentTuplets().size());
    }

    @Test
    public void testFindStudentByEmail() {
        List<Sextet<String, String, String, String, String, String>> records = gateway.getStudentTupletsByEmail("student1@ship.edu");

        assertEquals(5, records.size());
    }
}
