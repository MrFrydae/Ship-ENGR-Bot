package edu.ship.engr.discordbot.gateways;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import com.google.common.collect.Lists;

import edu.ship.engr.discordbot.containers.Course;
import edu.ship.engr.discordbot.utils.StringUtil;
import edu.ship.engr.discordbot.utils.TimeUtil;
import edu.ship.engr.discordbot.utils.Util;
import edu.ship.engr.discordbot.utils.csv.CSVHandler;
import edu.ship.engr.discordbot.utils.csv.CSVRecord;

/**
 * Connection to the datasource that has information about when courses are being offered.
 *
 * @author merlin
 */
public class CourseGateway {
    /**
     * Loads a {@link CSVHandler handler} for the "offerings" file.
     *
     * @return All records from "offerings.csv"
     */
    public CSVHandler getOfferedClasses() {
        return new CSVHandler("offerings");
    }

    /**
     * Checks the entries in offerings.csv for matching courses.
     *
     * @param courseName The course to search for
     * @return true if it is a valid course name
     */
    public boolean isValidCourseName(String courseName) {
        courseName = Util.formatClassName(courseName);

        for (Course course : getAllOfferedCourses()) {
            if (course.getCode().equalsIgnoreCase(courseName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get a list of all offered courses.
     *
     * @return a list of courses
     */
    public List<Course> getAllOfferedCourses() {
        return getOfferedCourses("");
    }

    /**
     * Gets a list of all offered courses in a given semester.
     *
     * @param semesterCode The code to search for.
     *                     Make this blank to get all offered courses regardless of semester
     * @return a list of courses
     */
    protected List<Course> getOfferedCourses(String semesterCode) {
        List<Course> courses = Lists.newArrayList();
        for (CSVRecord record : Objects.requireNonNull(getOfferedClasses()).getRecords()) {
            String code = record.get("Code");
            String title = record.get("Title");
            String frequency = record.get("Frequency");

            code = Util.formatClassName(code);

            Course course = new Course(code, title, frequency, getNextCourseOffering(code), getAllCourseOfferings(code));

            if (!semesterCode.isEmpty()) {
                String offerings = record.get(semesterCode);
                if (offerings.isEmpty()) {
                    continue;
                }
            }
            courses.add(course);
        }
        return courses;
    }

    /**
     * Collects all course information from the file into a {@link Course course}.
     *
     * @param className The class to search for
     * @return The container containing relevant information for the class
     */
    public Course getCourse(String className) {
        return getAllOfferedCourses()
                .stream()
                .filter(course -> StringUtil.equals(course.getCode(), Util.formatClassName(className), true))
                .findFirst().orElse(null);
    }

    /**
     * Collects a list of courses that are offered in the current semester.
     *
     * @return the courses offered in the current semester
     */
    public List<Course> getCurrentlyOfferedCourses() {
        return getOfferedCourses(Util.getSemesterCode(Calendar.getInstance()));
    }

    /**
     * Gets a list of all semesters when this course is offered.
     *
     * @return a list of strings, each of which being a semester code
     */
    List<String> getAllCourseOfferings(String code) {
        List<String> offerings = Lists.newArrayList();

        Objects.requireNonNull(getOfferedClasses().getRecords()).stream()
                .filter(record -> record.get("Code").equalsIgnoreCase(code.replace("-", "")))
                .forEach(record -> {
                    for (int year : collectAcademicYears()) {
                        String spring = StringUtil.getOrDefault(record.get(year + "20"), "0");
                        String fall = StringUtil.getOrDefault(record.get(year + "60"), "0");
                        offerings.add(year + "," + spring + "," + fall);
                    }
                });

        return offerings;
    }

    /**
     * Finds the next semester when this course is offered.
     *
     * @return the next Semester Code that this course is offered
     */
    String getNextCourseOffering(String code) {
        Calendar date = TimeUtil.getCurrentDate();

        for (CSVRecord record : Objects.requireNonNull(getOfferedClasses()).getRecords()) {
            String recordCode = record.get("Code");

            if (!recordCode.equalsIgnoreCase(code)) {
                continue;
            }

            boolean found = false;
            String semesterCode = "";

            while (!found) {
                semesterCode = Util.getSemesterCode(date);

                if (!getOfferedClasses().getHeaders().contains(semesterCode)) {
                    return null;
                }

                if (record.get(semesterCode).isEmpty()) {
                    date.add(Calendar.MONTH, 6);
                } else {
                    found = true;
                }
            }

            if (semesterCode.isEmpty()) {
                return null;
            }

            return Util.formatSemesterCode(semesterCode);
        }
        return null;
    }

    List<Integer> collectAcademicYears() {
        List<Integer> years = Lists.newArrayList();

        List<String> headers = getOfferedClasses().getHeaders();
        headers = headers.subList(3, headers.size());

        headers.stream()
                .map(header -> header.substring(0, 4))
                .mapToInt(Integer::parseInt)
                .filter(year -> year >= Calendar.getInstance().get(Calendar.YEAR))
                .filter(year -> !years.contains(year))
                .forEach(years::add);

        return years;
    }

}
