package edu.ship.engr.discordbot.gateways;

import com.google.common.collect.Lists;
import edu.ship.engr.discordbot.containers.Course;
import edu.ship.engr.discordbot.containers.Student;
import edu.ship.engr.discordbot.utils.Log;
import edu.ship.engr.discordbot.utils.TimeUtil;
import edu.ship.engr.discordbot.utils.Util;
import edu.ship.engr.discordbot.utils.csv.CSVRecord;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * This is the class we should use to get information about students.  
 * It manages a number of different gateways that hold information
 * about students.
 *
 * @author merlin
 *
 */
public class StudentMapper {
    private final StudentGateway studentGateway = new StudentGateway();
    private final DiscordGateway discordGateway = new DiscordGateway();
    private final CourseGateway courseGateway = new CourseGateway();


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

        for (CSVRecord record : studentGateway.getRecords()) {
            String recordEmail = record.get("EMAIL");

            if (email.equalsIgnoreCase(recordEmail)) {
                return getStudentFromRecord(record);
            }
        }

        return null;
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

        Log.info("Gathered %s students", students.size());

        return students;
    }

    /**
     * Convert a {@link CSVRecord} to a {@link Student}.
     *
     * @param record the record to parse
     * @return a new Student object
     */
    @NotNull
    @Contract("_ -> new")
    private Student getStudentFromRecord(@NotNull CSVRecord record) {
        String name = record.get("PREF_FIRST_NAME") + " " + record.get("PREF_LAST_NAME");
        name = Util.ucfirst(name);
        String email = record.get("EMAIL");
        String discordId = discordGateway.getDiscordIdByEmail(email);

        String major = record.get("MAJOR_DESC");
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

        List<Course> courses = Lists.newArrayList();
        for (CSVRecord record : studentGateway.getRecords()) {
            String recordEmail = record.get("EMAIL");

            if (!recordEmail.equalsIgnoreCase(email)) {
                continue;
            }

            String recordPeriod = record.get("ACADEMIC_PERIOD");

            if (!recordPeriod.equalsIgnoreCase(TimeUtil.getCurrentSemesterCode())) {
                continue;
            }

            String recordCourse = record.get("COURSE_IDENTIFICATION");
            Course course = courseGateway.getCourse(recordCourse);

            if (course != null) {
                courses.add(course);
            }
        }

        return courses;
    }
}
