package edu.ship.engr.discordbot.gateways;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import edu.ship.engr.discordbot.containers.Course;
import edu.ship.engr.discordbot.utils.csv.CSVHandler;
import edu.ship.engr.discordbot.utils.csv.CSVRecord;

/**
 * Gathers information about students.
 *
 * @author merlin
 */
public class StudentGateway {

    CSVHandler studentClassesHandler = new CSVHandler("students");

    // TODO: Move to Caches
    private static final Supplier<List<CSVRecord>> rowSupplier =
            Suppliers.memoizeWithExpiration(
                    () -> new StudentGateway().getHandler().getRecords(), 1, TimeUnit.MINUTES);

    /**
     * Get all fo the records in the handler.
     *
     * @return a list of all of the records of students
     */
    protected List<CSVRecord> getRecords() {
        return rowSupplier.get();
    }

    public CSVHandler getHandler() {
        return studentClassesHandler;
    }
}
