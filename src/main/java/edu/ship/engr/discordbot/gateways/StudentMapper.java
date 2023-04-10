package edu.ship.engr.discordbot.gateways;

import com.google.common.collect.Lists;
import edu.ship.engr.discordbot.containers.Course;
import edu.ship.engr.discordbot.containers.Student;
import edu.ship.engr.discordbot.utils.TimeUtil;
import edu.ship.engr.discordbot.utils.Util;
import lombok.Getter;
import lombok.SneakyThrows;
import org.javatuples.Sextet;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * This is the class we should use to get information about students.  
 * It manages a number of different gateways that hold information
 * about students.
 *
 * @author merlin
 *
 */
public class StudentMapper {
    @Getter private StudentGateway studentGateway = new StudentGateway();
    @Getter private DiscordGateway discordGateway = new DiscordGateway();
    @Getter private CourseGateway courseGateway = new CourseGateway();

    /**
     * Gets the {@link Student student} with the provided email.
     *
     * @param email The email to search for
     * @return A student object
     */
    @Contract("null -> null")
    public Student getStudentByEmail(String email) {
        if (email == null) {
            return null;
        }

        return getStudentFromData(studentGateway.getStudentTupletsByEmail(email));
    }
    
    /**
     * Gets a list of all mapped students in file.
     *
     * @return A list of all mapped students
     */
    @SneakyThrows
    public List<Student> getAllMappedStudents() {
        List<Student> students = Lists.newArrayList();

        ExecutorService service = Executors.newFixedThreadPool(10);
        Objects.requireNonNull(discordGateway.getAllEmails()).forEach(email -> {
            service.submit(() -> {
                Student student = getStudentByEmail(email);

                if (student != null) {
                    students.add(student);
                }
            });
        });

        service.shutdown();
        service.awaitTermination(15, TimeUnit.SECONDS);

        return students;
    }

    @Contract("_ -> new")
    private Student getStudentFromData(@NotNull List<Sextet<String, String, String, String, String, String>> data) {
        Sextet<String, String, String, String, String, String> row = data.get(0);

        String name = Util.ucfirst(String.format("%s %s", row.getValue1(), row.getValue0()));
        String email = row.getValue2();
        String discordId = discordGateway.getDiscordIdByEmail(email);
        String major = row.getValue3();
        List<Course> courses = getCoursesByEmail(email);

        return new Student(name, email, major, discordId, courses);
    }

    /**
     * Get a list of courses by email.
     *
     * @param email the email to search for
     * @return a list of courses
     */
    @Contract("null -> null")
    public List<Course> getCoursesByEmail(String email) {
        if (email == null) {
            return null;
        }

        return studentGateway.getStudentTupletsByEmail(email)
                .stream()
                .filter(t -> t.getValue5().equalsIgnoreCase(TimeUtil.getCurrentSemesterCode()))
                .map(t -> courseGateway.getCourse(t.getValue4()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}
