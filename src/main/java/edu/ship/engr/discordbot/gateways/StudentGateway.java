package edu.ship.engr.discordbot.gateways;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import edu.ship.engr.discordbot.utils.csv.CSVHandler;
import edu.ship.engr.discordbot.utils.csv.CSVRecord;
import lombok.Getter;
import org.javatuples.Sextet;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static edu.ship.engr.discordbot.utils.Util.distinctByKey;

/**
 * Gathers information about students.
 *
 * @author merlin
 */
public class StudentGateway {

    @Getter private CSVHandler handler = new CSVHandler("students");

    // TODO: Move to Caches
    private static final Supplier<List<CSVRecord>> rowSupplier =
            Suppliers.memoizeWithExpiration(
                    () -> new StudentGateway().getHandler().getRecords(), 1, TimeUnit.MINUTES);

    /**
     * Get all of the records in the handler.
     *
     * @return a list of all of the records of students
     */
    protected List<CSVRecord> getRecords() {
        return rowSupplier.get();
    }

    @Contract("null -> null; !null -> new")
    private static Sextet<String, String, String, String, String, String> getStudentTuplet(CSVRecord record) {
        if (record == null) {
            return null;
        }

        String lastName = record.get("PREF_LAST_NAME");
        String firstName = record.get("PREF_FIRST_NAME");
        String email = record.get("EMAIL");
        String major = record.get("MAJOR_DESC");
        String courseCode = record.get("COURSE_IDENTIFICATION");
        String semesterCode = record.get("ACADEMIC_PERIOD");

        return Sextet.with(lastName, firstName, email, major, courseCode, semesterCode);
    }

    /**
     * Gathers a list of records in the file.
     *
     * @return all {@link Sextet tuplets} for every {@link CSVRecord} in the file
     */
    @NotNull
    @Contract(pure = true)
    public List<Sextet<String, String, String, String, String, String>> getAllStudentTuplets() {
        return getRecords().stream().map(StudentGateway::getStudentTuplet).collect(Collectors.toList());
    }

    /**
     * Gathers a list of records in the file, distinct by email.
     *
     * @return all unique {@link Sextet tuplets} for every {@link CSVRecord} in the file
     */
    @NotNull
    @Contract(pure = true)
    public List<Sextet<String, String, String, String, String, String>> getUniqueStudentTuplets() {
        return getAllStudentTuplets()
                .stream()
                .filter(distinctByKey(Sextet::getValue2))
                .collect(Collectors.toList());
    }

    /**
     * Gathers a list of records in the file matching the given email.
     *
     * @return all {@link Sextet tuplets} for every matching {@link CSVRecord} in the file
     */
    @NotNull
    @Contract(pure = true)
    public List<Sextet<String, String, String, String, String, String>> getStudentTupletsByEmail(@NotNull String email) {
        return getAllStudentTuplets()
                .stream()
                .filter(t -> t.getValue2().equalsIgnoreCase(email))
                .collect(Collectors.toList());
    }
}
