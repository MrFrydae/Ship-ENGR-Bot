package edu.ship.engr.discordbot.gateways;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import dev.frydae.factories.Factory;
import dev.frydae.factories.annotations.InjectFactory;
import edu.ship.engr.discordbot.containers.Course;
import edu.ship.engr.discordbot.testing.annotations.BotTest;
import org.javatuples.Quintet;
import org.javatuples.Tuple;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests the public methods in CourseGateway.
 *
 * @author merlin
 */
@BotTest
public class CourseGatewayTest implements Factory<CourseGateway> {
    @InjectFactory private CourseGateway gateway;

    // region Factory
    @Override
    public ListMultimap<Class<?>, ? extends Tuple> getFactoryData() {
        ListMultimap<Class<?>, Quintet<String, String, String, String, List<String>>> data = ArrayListMultimap.create();

        data.put(CourseGateway.class, Quintet.with("CIVE-110", "Measurements and CAD", "Every Fall", "Fall:2022", Lists.newArrayList()));
        data.put(CourseGateway.class, Quintet.with("SWE-200", "Design Patterns", "Every Fall", "Fall:2023", Lists.newArrayList()));
        data.put(CourseGateway.class, Quintet.with("ENGR-120", "Programming for Engineers", "Every Fall", "Fall:2022", Lists.newArrayList()));

        return data;
    }

    @Override
    public CourseGateway factory(List<? extends Tuple> data) {
        CourseGateway gateway = mock(CourseGateway.class);

        System.out.printf("This is our mocked gateway: %s\n", gateway);

        when(gateway.getAllOfferedCourses()).thenReturn(getAllOfferedCourses((List<Quintet<String, String, String, String, List<String>>>) data));
        when(gateway.getCourse(anyString())).thenCallRealMethod();
        when(gateway.isValidCourseName(anyString())).thenCallRealMethod();

        return gateway;
    }

    private List<Course> getAllOfferedCourses(List<Quintet<String, String, String, String, List<String>>> data) {
        return data.stream()
                .map(d -> Course.builder()
                        .code(d.getValue0())
                        .title(d.getValue1())
                        .frequency(d.getValue2())
                        .nextOffering(d.getValue3())
                        .allOfferings(d.getValue4())
                        .build()
                )
                .collect(Collectors.toList());
    }
    // endregion

    /**
     * Valid course names have an alphabetic prefix, a number suffix, an optional
     * dash between them and must be in the offerings data source.
     */
    @Test
    public void validCourseName() {
        System.out.printf("This is our gateway: %s\n", gateway);

        assertTrue(gateway.isValidCourseName("SWE200"));
        assertTrue(gateway.isValidCourseName("SWE-200"));
        assertFalse(gateway.isValidCourseName("SWE 200"));
        assertFalse(gateway.isValidCourseName("SWE 107"));
        assertFalse(gateway.isValidCourseName("Merlin"));
    }

    @Test
    public void testGetCourse() {
        Course course = gateway.getCourse("ENGR-120");
        assertNotNull(course);
        assertEquals("Programming for Engineers", course.getTitle());
    }
}
